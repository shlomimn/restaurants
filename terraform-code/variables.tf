# general
variable "location" {
  type    = string
  default = "West Europe"
}
variable "rg_name" {
  type    = string
  default = "rg-func-app"
}

# mysql_server
variable "mysql_server_name" {
  type = string
}
variable "mysql_login" {
  type    = string
  default = "mysqladmin"
}
variable "mysql_pw" {
  type      = string
  sensitive = true
  default   = null
}
variable "mysql_sku_name" {
  type    = string
  default = "B_Standard_B1s"
}

# mysql_database
variable "mysql_database_name" {
  type = string
}

# function_app
variable "storage_account_name" {
  type    = string
  default = "funcapprestaurant2"
}
variable "azurerm_app_service_plan_sku" {
  type = object({
    tier = string
    size = string
  })
  default = {
    tier = "Dynamic"
    size = "Y1"
  }
}
