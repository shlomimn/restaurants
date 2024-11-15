# This resource-group, storage-account, container must be created manually in azure portal prior to terraform init
# From location: ../restaurants/terraform-code
# Run CLI command: terraform init -backend-config=backend-config/backend_restaurants.tf

resource_group_name  = "rg-terraform-state-storage"
storage_account_name = "terraformstaterestaurant"
container_name       = "tf-state"
key                  = "restaurant/terrastate"