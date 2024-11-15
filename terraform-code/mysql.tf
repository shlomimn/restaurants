# in case that the mysql login password is not supplied, I will generate it.
resource "random_password" "mysql_admin_password" {
  count       = var.mysql_pw == null ? 1 : 0
  length      = 20
  special     = true
  min_numeric = 1
  min_upper   = 1
  min_lower   = 1
  min_special = 1
}

locals {
  mysql_pw = try(random_password.mysql_admin_password[0].result, var.mysql_pw)
}

resource "azurerm_mysql_flexible_server" "this" {
  name                   = var.mysql_server_name
  resource_group_name    = azurerm_resource_group.this.name
  location               = azurerm_resource_group.this.location
  administrator_login    = var.mysql_login
  administrator_password = local.mysql_pw
  sku_name               = var.mysql_sku_name
}

resource "azurerm_mysql_flexible_database" "this" {
  name                = var.mysql_database_name
  resource_group_name = azurerm_resource_group.this.name
  server_name         = azurerm_mysql_flexible_server.this.name
  charset             = "utf8"
  collation           = "utf8_unicode_ci"
  depends_on = [azurerm_mysql_flexible_server.this]
}

resource "null_resource" "create_database_table" {
  provisioner "local-exec" {
    command = <<EOT
      mysql -h ${azurerm_mysql_flexible_server.this.fqdn} \
            -u ${azurerm_mysql_flexible_server.this.administrator_login} \
            -p ${azurerm_mysql_flexible_server.this.administrator_password} \
            -e "
      CREATE DATABASE IF NOT EXISTS restaurants;
      USE restaurants;
      CREATE TABLE IF NOT EXISTS restaurants_info (
          name VARCHAR(255) NOT NULL,
          style VARCHAR(50) NOT NULL,
          vegetarian TINYINT(1) NOT NULL,
          open_hour TIME NOT NULL,
          close_hour TIME NOT NULL,
          address VARCHAR(255) NOT NULL,
        PRIMARY KEY (name)
      );
      "
    EOT
  }

  # Ensure the MySQL server and database are created first
  depends_on = [azurerm_mysql_flexible_server.this, azurerm_mysql_flexible_database.this]
}
