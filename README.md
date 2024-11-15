# Which restaurant is opened with Jenkins and Azure

This project automates restaurant management using Jenkins and Azure services. It includes three key Jenkins jobs: 

1. **`restaurant-recommend.groovy`**: Queries restaurants based on style, vegetarian option, and current availability.
2. **`restaurant-sql-table.groovy`**: Manages the SQL database with Add, Update, and Delete operations for restaurant entries.
3. **`restaurant-terraform-build.groovy`**: Sets up the Azure infrastructure, including a Function App and SQL Server.

## Architecture Diagram

```plaintext
+-----------------------------+
|        Jenkins Server       |
+-----------------------------+
        |               |
        v               v
+------------------+ +-------------------+
| `restaurant-     | | `restaurant-sql-  |
| recommend.groovy`| | table.groovy`     |
+----------------=-+ +-------------------+
        |                |
        v                v
+---------------------------+   
| Azure Function App        |   
| `func-restaurant`         |   
+---------------------------+   
        |                       
        v                       
+-------------------------------+
| Azure SQL Server             |
| `restaurant_info` table      |
+-------------------------------+
```

## Jenkins Job Descriptions

### 1. `restaurant-recommend.groovy`
- **Functionality**: Retrieves restaurant recommendations.
- **Inputs**:
  - **Restaurant Style**: Dropdown menu for styles like Italian, Chinese, etc.
  - **Vegetarian Option**: Yes/No.
- **Workflow**:
  - Sends HTTP requests to the Azure Function App.
  - Function App queries the SQL database (`restaurant_info` table).
  - Retrieves restaurants currently open (`open_hour` and `close_hour`).

---

### 2. `restaurant-sql-table.groovy`
- **Functionality**: Manages the `restaurant_info` table.
- **Options**:
  - **ADD**: Adds a new restaurant with details: name, style, open_hour, close_hour, address, and vegetarian status.
  - **UPDATE**: Updates the `open_hour` and `close_hour` of an existing restaurant.
  - **DELETE**: Deletes a restaurant from the database.

---

### 3. `restaurant-terraform-build.groovy`
- **Functionality**: Sets up Azure resources.
- **Actions**:
  - Provisions an Azure Function App.
  - Creates an Azure SQL Server and the `restaurant_info` table.


## Setting Up the Environment

### Step 1: Run Terraform Build Job
- Execute the **`restaurant-terraform-build.groovy`** Jenkins job to create the Azure Function App and SQL Server.

### Step 2: Export Environment Variables
- From terraform outputs copy the **FQDN** of the Azure SQL Server and export it as environment variables:
  ```bash
  export DB_HOST=<SQL_SERVER_FQDN>
  export DB_USER=<SQL_SERVER_USERNAME>
  export DB_PASSWORD=<SQL_SERVER_PASSWORD>

## Deploy the Function App

To deploy the Azure Function App, follow these steps:

1. Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   cd <repository-name>
2. Navigate to the `azure-function/function-rest` directory in your local repository.
3. Use the Azure CLI to deploy the Function App:
   ```bash
   func azure functionapp publish func-restaurant


## Monitor the Function App

To monitor and validate the Azure Function App, follow these steps:

1. **View Logs in the Azure Portal**:
   - Navigate to the **Function App** in the Azure Portal.
   - Go to the **Logs** section under **Monitor** to see detailed logs of all incoming and outgoing HTTP messages.

2. **Check Live Metrics**:
   - In the Azure Portal, access the **Live Metrics** tab under **Monitor**.
   - Review performance metrics, such as execution times, throughput, and error rates.
