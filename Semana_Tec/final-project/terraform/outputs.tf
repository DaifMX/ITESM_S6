output "vm_id" {
  description = "Proxmox VM ID of the n8n instance"
  value       = proxmox_virtual_environment_vm.n8n.vm_id
}

output "vm_name" {
  description = "VM name"
  value       = proxmox_virtual_environment_vm.n8n.name
}

output "vm_ipv4_addresses" {
  description = "IPv4 addresses reported by the QEMU guest agent"
  value       = proxmox_virtual_environment_vm.n8n.ipv4_addresses
}
