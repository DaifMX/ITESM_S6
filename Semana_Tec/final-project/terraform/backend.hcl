bucket = "terraform-state"
key    = "cancha-viva-n8n/terraform.tfstate"

# Garage ignores the region value but the S3 backend requires one.
region = "garage"

skip_credentials_validation = true
skip_metadata_api_check     = true
skip_region_validation      = true
skip_requesting_account_id  = true
use_path_style              = true
