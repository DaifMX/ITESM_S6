terraform {
  required_version = ">= 1.5.0"

  required_providers {
    proxmox = {
      source  = "bpg/proxmox"
      version = ">= 0.66.0"
    }
    archive = {
      source  = "hashicorp/archive"
      version = ">= 2.4.0"
    }
  }

  backend "local" {}
}

provider "proxmox" {
  endpoint  = var.proxmox_api_url
  api_token = var.proxmox_api_token_id
  insecure  = var.proxmox_tls_insecure

  ssh {
    username    = var.proxmox_ssh_user
    agent       = var.proxmox_ssh_private_key == "" ? true : false
    private_key = var.proxmox_ssh_private_key != "" ? var.proxmox_ssh_private_key : null
  }
}
