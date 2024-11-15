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

## Setting Up the Environment

## IaC Jenkins Job Description
### `restaurant-terraform-build.groovy`
- **Functionality**: Sets up Azure resources.
- **Actions**:
  - Provisions an Azure Function App.
  - Creates an Azure SQL Server and the `restaurant_info` table.
![image](https://github.com/user-attachments/assets/48cd03d6-6285-40f0-a32c-8f1985966a53)
<img width="844" alt="image" src="https://github.com/user-attachments/assets/0d9fb113-f3b6-4fb2-8577-d4f219821dbe">


### Run Terraform Build Job
- Execute the **`restaurant-terraform-build.groovy`** Jenkins job to create the Azure Function App and SQL Server.


## Deploy the Function App

To deploy the Azure Function App, follow these steps:

1. Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   cd <repository-name>
2. Navigate to the `azure-function/function-rest` directory in your local repository.
3. From terraform outputs copy the **FQDN** of the Azure SQL Server and export it as environment variables:
   ```bash
      export DB_HOST=<SQL_SERVER_FQDN>
      export DB_USER=<SQL_SERVER_USERNAME>
      export DB_PASSWORD=<SQL_SERVER_PASSWORD>
   ```   

4. Function app python file will get the environment parameters above with:
   ```
        db_config = {
            'user': os.getenv('DB_USER'),
            'password': os.getenv('DB_PASSWORD'),
            'host': os.getenv('DB_HOST'),
            'database': 'restaurants',
            'port': 3306
        }
   ```

5. Use the Azure CLI to deploy the Function App:
   ```bash
   func azure functionapp publish func-restaurant
   ```

## Monitor the Function App

To monitor and validate the Azure Function App, follow these steps:

1. **View Logs in the Azure Portal**:
   - Navigate to the **Function App** in the Azure Portal.
   - Go to the **Logs** section under **Monitor** to see detailed logs of all incoming and outgoing HTTP messages.

2. **Check Live Metrics**:
   - In the Azure Portal, access the **Live Metrics** tab under **Monitor**.
   - Review performance metrics, such as execution times, throughput, and error rates.

![image](https://github.com/user-attachments/assets/5787ca47-09b0-4548-98b6-3ab0ff6647c0)


## Application Jenkins Jobs Descriptions

Prior to running (1,2) Jenkins jobs below,  
```
Create **azure-function-url** and **azure-function-host-key** credentials in Jenkins.  
```
These credentials are taken from Azure portal:  
< function app > --> < function > --> Get Function URL --> _master (Host key)  
![image](https://github.com/user-attachments/assets/223ff77e-21dc-4531-8c9d-ca561509217e)


```
Example:  

_master (Host key) =  http://func-restaurant.azurewebsites.net/api/function-rest/\?code\=< key >   
azure-function-url = https://func-restaurant.azurewebsites.net/api/function-rest   
azure-function-host-key = < key >
```
Jenkins jobs (1,2) will need to use these credentials to reach and access the function app:
```
    environment {
        FUNCTION_URL = credentials('azure-function-url')
        HOST_KEY = credentials('azure-function-host-key')
    }
```

### 1. `restaurant-recommend.groovy`
- **Functionality**: Retrieves restaurant recommendations.
- **Inputs**:
  - **Restaurant Style**: Dropdown menu for styles like Italian, Chinese, etc.
  - **Vegetarian Option**: Yes/No.
- **Workflow**:
  - Sends HTTP requests to the Azure Function App.
  - Function App queries the SQL database (`restaurant_info` table).
  - Retrieves restaurants currently open (`open_hour` and `close_hour`).
![image](https://github.com/user-attachments/assets/bda86bc8-4de1-4538-b442-3761d150f84b)


---

### 2. `restaurant-sql-table.groovy`
- **Functionality**: Manages the `restaurant_info` table.
- **Options**:
  - **ADD**: Adds a new restaurant with details: name, style, open_hour, close_hour, address, and vegetarian status.
  - **UPDATE**: Updates the `open_hour` and `close_hour` of an existing restaurant.
  - **DELETE**: Deletes a restaurant from the database.

---
![image](https://github.com/user-attachments/assets/82299160-d5dc-4119-ac85-3f57009a704b)



    
