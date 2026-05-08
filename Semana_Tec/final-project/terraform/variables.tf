# ── Proxmox connection ────────────────────────────────────────────────────────
variable "proxmox_api_url" {
  description = "Proxmox API endpoint (e.g. https://pve.example.com)"
  type        = string
}

variable "proxmox_api_token_id" {
  description = "Proxmox API token in the form user@realm!tokenname=secret"
  type        = string
  sensitive   = true
}

variable "proxmox_ssh_private_key" {
  description = "PEM private key for SSH access to the Proxmox host (leave empty to use the local SSH agent)"
  type        = string
  sensitive   = true
  default     = ""
}

variable "proxmox_tls_insecure" {
  description = "Skip TLS verification for the Proxmox API"
  type        = bool
  default     = false
}

variable "proxmox_node" {
  description = "Target Proxmox node name"
  type        = string
  default     = "andromeda"
}

variable "proxmox_ssh_user" {
  description = "SSH username used by the provider to upload cloud-init snippets"
  type        = string
  default     = "root"
}

# ── VM settings ───────────────────────────────────────────────────────────────
variable "vm_id" {
  description = "Proxmox VM ID (0 = next available)"
  type        = number
  default     = 0
}

variable "vm_name" {
  description = "Name of the n8n VM"
  type        = string
  default     = "n8n"
}

variable "vm_template" {
  description = "Name of the cloud-init-ready template to clone"
  type        = string
}

variable "vm_cores" {
  description = "Number of CPU cores"
  type        = number
  default     = 2
}

variable "vm_memory" {
  description = "RAM in MiB"
  type        = number
  default     = 8192
}

variable "vm_disk_size" {
  description = "Boot disk size in GiB"
  type        = number
  default     = 32
}

variable "vm_storage" {
  description = "Proxmox storage pool for the disk"
  type        = string
  default     = "local-zfs"
}

variable "vm_bridge" {
  description = "Network bridge"
  type        = string
  default     = "vmbr0"
}

variable "vm_ip_address" {
  description = "Cloud-init IPv4 address with prefix (e.g. '10.0.0.60/24') or 'dhcp'"
  type        = string
  default     = "dhcp"
}

variable "vm_gateway" {
  description = "Default gateway IP (leave empty for DHCP)"
  type        = string
  default     = ""
}

variable "vm_dns_servers" {
  description = "List of DNS server IPs injected via cloud-init"
  type        = list(string)
  default     = []
}

variable "vm_ssh_keys" {
  description = "Public SSH keys injected via cloud-init (one per line)"
  type        = string
  default     = ""
}

variable "vm_user" {
  description = "Default user created by cloud-init"
  type        = string
  default     = "n8n"
}

variable "vm_user_password" {
  description = "Console password for the VM user"
  type        = string
  sensitive   = true
  default     = ""
}

# ── n8n settings ──────────────────────────────────────────────────────────────
variable "n8n_encryption_key" {
  description = "Encryption key for n8n credentials (generate with: openssl rand -hex 32)"
  type        = string
  sensitive   = true
}

variable "openrouter_api_key" {
  description = "OpenRouter API key used by the workflow (https://openrouter.ai/keys)"
  type        = string
  sensitive   = true
}

variable "postgres_user" {
  description = "PostgreSQL username"
  type        = string
  default     = "n8n"
}

variable "postgres_password" {
  description = "PostgreSQL password"
  type        = string
  sensitive   = true
}

variable "postgres_db" {
  description = "PostgreSQL database name"
  type        = string
  default     = "n8n"
}

variable "webhook_url" {
  description = "Public base URL for n8n webhooks — set to your Cloudflare tunnel domain (e.g. https://n8n.example.com)"
  type        = string
  default     = ""
}
