# From location: ../restaurants/terraform-code
# Run CLI commands:
#   terraform plan -var-file=tfvars/restaurants.tfvars -var="mysql_pw=your_secure_password"
#   terraform apply -var-file=tfvars/restaurants.tfvars -var="mysql_pw=your_secure_password"
#   terraform destroy -var-file=tfvars/restaurants.tfvars -var="mysql_pw=your_secure_password"

# The mysql_pw can also be set instead by export TF_VAR_mysql_pw="your_secure_password"

# general
location = "West Europe"

# mysql_server
mysql_server_name = "mysql-restaurant-flexible-server"

# mysql_database
mysql_database_name = "restaurants"
