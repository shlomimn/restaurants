# mysql_server
output "mysql_server_endpoint" {
  value = azurerm_mysql_flexible_server.this.fqdn
}
