
# resource "azurerm_storage_account" "this" {
#   name                     = var.storage_account_name
#   resource_group_name      = azurerm_resource_group.this.name
#   location                 = var.location
#   account_tier             = "Standard"
#   account_replication_type = "LRS"
# }
#
# resource "azurerm_app_service_plan" "this" {
#   name                = "azure-functions-consumption-sp"
#   location            = var.location
#   resource_group_name = azurerm_resource_group.this.name
#   kind                = "Linux"
#   reserved            = true
#   sku                 = var.azurerm_app_service_plan_sku
# }
#
# resource "azurerm_function_app" "this" {
#   name                       = "func-restaurant"
#   location                   = var.location
#   resource_group_name        = azurerm_resource_group.this.name
#   app_service_plan_id        = azurerm_app_service_plan.this.id
#   storage_account_name       = azurerm_storage_account.this.name
#   storage_account_access_key = azurerm_storage_account.this.primary_access_key
#   os_type                    = "linux"
#   version                    = "~4"
#
#   app_settings {
#     FUNCTIONS_WORKER_RUNTIME = "python"
#   }
#
#   site_config {
#     linux_fx_version = "python|3.9"
#   }
# }

