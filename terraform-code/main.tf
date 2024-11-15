terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.108.0"
    }
  }
}

provider "azurerm" {
  features {}
}

data "azurerm_subscription" "this" {}

resource "azurerm_resource_group" "this" {
  location = var.location
  name     = var.rg_name
}

