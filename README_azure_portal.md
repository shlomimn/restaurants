# restaurants
Which restaurant is opened now?

# Azure Resources
Create a resource group.

Create a function app.
1. Choose: Consumption
2. Basic: Runtime Stack=python , version=3.9
3. Networking: Enable public access=On
4. Monitoring: Enable Application Insights=Yes

Create a storage account.
And get it's access keys connection string.

Back to the function app
Add an "application setting" in Environment variable:
AzureWebJobsStorage=<storage account's access keys connection string>

In your SDK,
1. Verify you have az.
2. Verify you have func.
```
func azure functionapp publish func-restaurant 
```
Verify successful deployment with the following CLI command:
```
func azure functionapp list-functions func-restaurant --show-keys

```
