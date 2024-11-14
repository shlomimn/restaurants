import azure.functions as func
import datetime
from datetime import datetime, timedelta
import logging
import pymysql
import json


app = func.FunctionApp()

# Database configuration - update with your MySQL database credentials
db_config = {
    'user': 'root',
    'password': 'admin',
    'host': '192.168.1.221',
    'database': 'restaurants',
    'port': 3306
}

def get_recommendations(style, vegetarian):
    # conn = mysql.connector.connect(**db_config)
    conn = pymysql.connect(**db_config, cursorclass=pymysql.cursors.DictCursor)
    # cursor = conn.cursor(dictionary=True)
    cursor = conn.cursor()

    # SQL query to retrieve restaurant details based on style and vegetarian options
    query = """
        SELECT name, style, vegetarian, open_hour, close_hour, address
        FROM restaurants_info
        WHERE style = %s AND vegetarian = %s
    """
    cursor.execute(query, (style, vegetarian))
    results = cursor.fetchall()

    cursor.close()
    conn.close()

    recommendations = []
    current_time = datetime.now().time()

    for result in results:
        # Check if the restaurant is currently open
        open_time = (datetime.min + result['open_hour']).time() if isinstance(result['open_hour'], timedelta) else \
        result['open_hour']
        close_time = (datetime.min + result['close_hour']).time() if isinstance(result['close_hour'], timedelta) else \
        result['close_hour']

        if open_time <= current_time <= close_time:
            # Restaurant is open, add to recommendations
            recommendations.append({
                "name": result['name'],
                "style": result['style'],
                "vegetarian": result['vegetarian'],
                "openHour": str(open_time),
                "closeHour": str(close_time),
                "address": result['address']
            })

    if recommendations:
        return {"restaurantRecommendations": recommendations}

    return None

@app.route('recommend', methods=['GET'], auth_level=func.AuthLevel.ANONYMOUS)
def recommend_restaurant(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Python HTTP trigger function processed a request.')
    # Parse JSON from the HTTP body
    data = req.get_json()

    if not data:
        return func.HttpResponse("error: No JSON data provided", status_code=400 )

    # Extract style and vegetarian parameters
    style = data.get("style")
    print(style)
    vegetarian = data.get("vegetarian")
    print(vegetarian)

    if not style or not vegetarian:
        return func.HttpResponse("error: Missing style or vegetarian field", status_code=400 )

    # Convert vegetarian to boolean if it's in string format like 'yes'/'no'
    vegetarian = True if vegetarian.lower() == 'yes' else False

    # Fetch recommendations from the database
    recommendations = get_recommendations(style, vegetarian)

    if recommendations:
        # Return the recommendations in a nicely formatted JSON response, with each restaurant as a separate JSON object
        return func.HttpResponse(json.dumps(recommendations.get("restaurantRecommendations", [])), status_code=200, mimetype="application/json")
    else:
        return func.HttpResponse("error: No matching restaurant found or it's currently closed", status_code=404)


@app.route('restaurant', methods=['POST'], auth_level=func.AuthLevel.ANONYMOUS)
def add_restaurant(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Python HTTP trigger function processed a request.')

    data = req.get_json()
    name = data.get('name')
    style = data.get('style')
    vegetarian = data.get('vegetarian')
    open_hour = data.get('open_hour')
    close_hour = data.get('close_hour')
    address = data.get('address')

    try:
        connection = pymysql.connect(
            host='192.168.1.221',
            user='root',
            password='admin',
            database='restaurants'
        )
        with connection.cursor() as cursor:
            query = """INSERT INTO restaurants_info (name, style, vegetarian, open_hour, close_hour, address) 
                       VALUES (%s, %s, %s, %s, %s, %s)"""
            cursor.execute(query, (name, style, vegetarian, open_hour, close_hour, address))
            connection.commit()
        return func.HttpResponse("message: Restaurant added successfully", status_code=201)
    except Exception as e:
        return func.HttpResponse(f"error: {str(e)}", status_code=500)
    finally:
        if connection:
            connection.close()

@app.route('restaurant', methods=['DELETE'], auth_level=func.AuthLevel.ANONYMOUS)
def delete_restaurant(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Python HTTP trigger function processed a request.')

    data = req.get_json()
    name = data.get('name')

    try:
        connection = pymysql.connect(
            host='192.168.1.221',
            user='root',
            password='admin',
            database='restaurants'
        )
        with connection.cursor() as cursor:
            query = """DELETE FROM restaurants_info WHERE name = %s"""
            cursor.execute(query, (name,))
            connection.commit()
        return func.HttpResponse("message: Restaurant deleted successfully", status_code=200)
    except Exception as e:
        return func.HttpResponse(f"error: {str(e)}", status_code=500)
    finally:
        if connection:
            connection.close()

@app.route('restaurant', methods=['PUT'], auth_level=func.AuthLevel.ANONYMOUS)
def update_restaurant(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Python HTTP trigger function processed a request.')

    data = req.get_json()
    name = data.get('name')
    open_hour = data.get('open_hour')
    close_hour = data.get('close_hour')

    try:
        connection = pymysql.connect(
            host='192.168.1.221',
            user='root',
            password='admin',
            database='restaurants'
        )
        with connection.cursor() as cursor:
            query = """UPDATE restaurants_info 
                       SET open_hour = %s, close_hour = %s 
                       WHERE name = %s"""
            cursor.execute(query, (open_hour, close_hour, name))
            connection.commit()
        return func.HttpResponse("message: Restaurant updated successfully", status_code=200)
    except Exception as e:
        return func.HttpResponse(f"error: {str(e)}", status_code=500)
    finally:
        if connection:
            connection.close()