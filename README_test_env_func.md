# restaurants
Which restaurant is opened now?


# pre-requests
MySQL on docker for testing   - like in README_test_env_flask.  
Jenkins on Docker for testing - like in README_test_env_flask.

# cli local function config
func init azure-func --worker-runtime python --model V2  
cd azure-func  
func new --template "Http Trigger" --name routeName
update function_app.py code  
func start  

# curl-s examples to test with
GET (Recommend on opened restaurants now according to SQL table)
```
curl -X GET http://localhost:7071/api/recommend \
     -H "Content-Type: application/json" \
     -d '{"style": "American", "vegetarian": "no"}'
```

POST (Add Restaurant to SQL table)
```
curl -X POST http://localhost:7071/api/restaurant \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Temporary Silver",
           "style": "Asian",
           "vegetarian": true,
           "open_hour": "10:00:00",
           "close_hour": "11:00:00",
           "address": "222 AIG St, Los Deleted Soon, CA"
         }'
```

PUT (Update opening hours of a restaurant in SQL table)
```
curl -X PUT http://localhost:7071/api/restaurant \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Temporary Silver",
           "open_hour": "06:06:00",
           "close_hour": "10:10:00"
         }'
```

DELETE (Remove restaurant from SQL table)
```
curl -X DELETE http://localhost:7071/api/restaurant \                                                                                          (eastus-dev-aks/default)
     -H "Content-Type: application/json" \
     -d '{"name": "Temporary Silver"}
```