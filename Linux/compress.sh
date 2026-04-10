#!/bin/bash

set -euo pipefail

usage() {
    cat <<EOF
Usage: $(basename "$0") -f <folder> -i <tarignore> [-o <output>]

Options:
  -f, --folder <path>      Source folder to tar (required)
  -i, --ignore <path>      .tarignore file (required)
  -o, --output <path>      Output tar.gz file (default: DIRNAME_TIMESTAMP.tar.gz)
  -h, --help              Show this help message

Examples:
  $(basename "$0") -f ./myproject -i .tarignore
  $(basename "$0") -f /path/to/src -i .tarignore -o backup.tar.gz
EOF
    exit "${1:-0}"
}

folder=""
ignore_file=""
output=""

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -f|--folder)
            folder="$2"
            shift 2
            ;;
        -i|--ignore)
            ignore_file="$2"
            shift 2
            ;;
        -o|--output)
            output="$2"
            shift 2
            ;;
        -h|--help)
            usage 0
            ;;
        *)
            echo "Error: Unknown option $1"
            usage 1
            ;;
    esac
done

# Validate required arguments
if [[ -z "$folder" ]]; then
    echo "Error: --folder is required"
    usage 1
fi

if [[ -z "$ignore_file" ]]; then
    echo "Error: --ignore is required"
    usage 1
fi

# Validate files exist
if [[ ! -d "$folder" ]]; then
    echo "Error: Folder '$folder' does not exist"
    exit 1
fi

if [[ ! -f "$ignore_file" ]]; then
    echo "Error: Ignore file '$ignore_file' does not exist"
    exit 1
fi

# Set default output if not provided
if [[ -z "$output" ]]; then
    timestamp=$(date +"%Y%m%d_%H%M%S")
    output="$(basename "$folder")_${timestamp}.tar.gz"
fi

# Check if output file already exists
if [[ -f "$output" ]]; then
    echo "Warning: Output file '$output' already exists and will be overwritten"
fi

# Build tar exclude options from .tarignore
exclude_opts=()
while IFS= read -r line; do
    # Skip empty lines and comments
    [[ -z "$line" || "$line" =~ ^# ]] && continue
    exclude_opts+=("--exclude=$line")
done < "$ignore_file"

# Create tar.gz
echo "Creating $output from $folder..."
tar -czf "$output" "${exclude_opts[@]}" -C "$(dirname "$folder")" "$(basename "$folder")"

if [[ $? -eq 0 ]]; then
    size=$(du -h "$output" | cut -f1)
    echo "✓ Successfully created $output (size: $size)"
else
    echo "✗ Failed to create tar.gz"
    exit 1
fi
