# ── Bundle the pre-built frontend dist as a zip ───────────────────────────────
# Run `npm run build` locally first with VITE_N8N_WEBHOOK_URL set.
data "archive_file" "frontend_dist" {
  type        = "zip"
  source_dir  = "${path.module}/../dist"
  output_path = "${path.module}/.terraform/frontend-dist.zip"
}

# ── Cloud-init user-data ──────────────────────────────────────────────────────
locals {
  cloud_init = templatefile("${path.module}/templates/cloud-init-n8n.yaml.tftpl", {
    vm_user          = var.vm_user
    vm_user_password = var.vm_user_password

    # Embed files as base64 to avoid YAML/shell escaping issues
    compose_b64       = base64encode(file("${path.module}/../compose.yml"))
    nginx_conf_b64    = base64encode(file("${path.module}/../nginx.conf"))
    workflow_b64      = base64encode(file("${path.module}/../n8n.json"))
    frontend_dist_b64 = filebase64(data.archive_file.frontend_dist.output_path)

    n8n_encryption_key = var.n8n_encryption_key
    openrouter_api_key = var.openrouter_api_key
    postgres_user      = var.postgres_user
    postgres_password  = var.postgres_password
    postgres_db        = var.postgres_db
    webhook_url        = var.webhook_url
  })
}

resource "proxmox_virtual_environment_file" "cloud_init" {
  content_type = "snippets"
  datastore_id = "local"
  node_name    = var.proxmox_node

  source_raw {
    data      = local.cloud_init
    file_name = "${var.vm_name}-ci.yaml"
  }
}

# ── Proxmox VM ────────────────────────────────────────────────────────────────
resource "proxmox_virtual_environment_vm" "n8n" {
  name      = var.vm_name
  node_name = var.proxmox_node
  vm_id     = var.vm_id != 0 ? var.vm_id : null
  on_boot   = true

  clone {
    vm_id        = data.proxmox_virtual_environment_vms.template.vms[0].vm_id
    datastore_id = var.vm_storage
  }

  agent {
    enabled = true
  }

  cpu {
    cores = var.vm_cores
    type  = "x86-64-v2-AES"
  }

  memory {
    dedicated = var.vm_memory
  }

  disk {
    datastore_id = var.vm_storage
    interface    = "scsi0"
    size         = var.vm_disk_size
  }

  network_device {
    bridge = var.vm_bridge
  }

  initialization {
    datastore_id = var.vm_storage

    user_account {
      username = var.vm_user
      password = var.vm_user_password
      keys     = var.vm_ssh_keys != "" ? split("\n", var.vm_ssh_keys) : []
    }

    ip_config {
      ipv4 {
        address = var.vm_ip_address
        gateway = var.vm_gateway != "" ? var.vm_gateway : null
      }
    }

    dns {
      servers = var.vm_dns_servers
    }

    user_data_file_id = proxmox_virtual_environment_file.cloud_init.id
  }

  lifecycle {
    ignore_changes = [
      initialization[0].user_account[0].keys,
    ]
  }
}

# ── Data source: find the template VM by name ─────────────────────────────────
data "proxmox_virtual_environment_vms" "template" {
  node_name = var.proxmox_node

  filter {
    name   = "name"
    values = [var.vm_template]
  }

  filter {
    name   = "template"
    values = ["true"]
  }
}
