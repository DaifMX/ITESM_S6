# compress

Bash utility that creates a `.tar.gz` archive from a folder, respecting a `.tarignore` exclusion file and auto-generating a timestamped output name.

## Files

| File | Description |
|------|-------------|
| [compress.sh](compress.sh) | Main archiving script |
| [.tarignore](.tarignore) | Default exclusion patterns |

## Usage

```bash
./compress.sh -f <folder> -i <tarignore> [-o <output>]
```

| Flag | Required | Description |
|------|----------|-------------|
| `-f`, `--folder` | Yes | Source folder to archive |
| `-i`, `--ignore` | Yes | Path to `.tarignore` exclusion file |
| `-o`, `--output` | No | Output filename (default: `DIRNAME_YYYYMMDD_HHMMSS.tar.gz`) |

## Examples

```bash
# Archive current project, exclude files listed in .tarignore
./compress.sh -f ./myproject -i .tarignore

# Archive with a custom output name
./compress.sh -f /path/to/src -i .tarignore -o backup.tar.gz
```

## .tarignore Format

One pattern per line. Lines starting with `#` and blank lines are ignored — same convention as `.gitignore`.

```
node_modules/
*.log
.env
target/
```

## Topics Covered

- Bash argument parsing with `while/case`
- `set -euo pipefail` for strict error handling
- Dynamic `tar` exclude option construction from a file
- `du -h` for human-readable archive size reporting
