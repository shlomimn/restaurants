pipeline {
    agent any

    parameters {
        choice(
            name: 'ACTION',
            choices: ['ADD', 'UPDATE', 'REMOVE'],
            description: '''Choose an ACTION method to interact with the restaurant SQL table.
            - For ADD, fill all the fields.
            - For UPDATE, just fill the NAME, OPEN_HOUR, and CLOSE_HOUR fields.
            - For REMOVE, just fill the NAME field.'''
        )
        string(name: 'API_URL', defaultValue: 'http://192.168.1.221:5000/restaurant', description: 'Enter the API endpoint URL')
        string(name: 'NAME', defaultValue: '', description: 'Name of the restaurant')
        choice(name: 'STYLE', choices: ['Italian', 'French', 'Japanese', 'Korean', 'American', 'Asian'], description: 'Style of the restaurant (only for ADD)')
        choice(name: 'VEGETARIAN', choices: ['yes', 'no'], description: 'Vegetarian option (only for ADD)')
        string(name: 'OPEN_HOUR', defaultValue: '', description: 'Opening hour (for ADD and UPDATE)')
        string(name: 'CLOSE_HOUR', defaultValue: '', description: 'Closing hour (for ADD and UPDATE)')
        string(name: 'ADDRESS', defaultValue: 'somewhere', description: 'Street, Country (for ADD and UPDATE)')
    }

    stages {
        stage('Prepare Parameters') {
            steps {
                script {
                    jsonData = ''
                    def vegetarianValue = (params.VEGETARIAN == 'yes') ? 1 : 0
                    if (params.ACTION == 'ADD') {
                        jsonData = """{
                            "name": "${params.NAME}",
                            "style": "${params.STYLE}",
                            "vegetarian": ${vegetarianValue},
                            "open_hour": "${params.OPEN_HOUR}",
                            "close_hour": "${params.CLOSE_HOUR}",
                            "address": "${params.ADDRESS}"
                        }"""
                    } else if (params.ACTION == 'UPDATE') {
                        jsonData = """{
                            "name": "${params.NAME}",
                            "open_hour": "${params.OPEN_HOUR}",
                            "close_hour": "${params.CLOSE_HOUR}"
                        }"""
                    } else if (params.ACTION == 'REMOVE') {
                        jsonData = """{
                            "name": "${params.NAME}"
                        }"""
                    }

                    echo "ACTION Method: ${params.ACTION}"
                    echo "API URL: ${params.API_URL}"
                    echo "JSON Data: ${jsonData}"
                }
            }
        }

        stage('Send API Request') {
            steps {
                script {
                    // Define a map to convert ACTION to HTTP method
                    def httpMethods = [
                        'ADD'    : 'POST',
                        'UPDATE' : 'PUT',
                        'REMOVE' : 'DELETE'
                    ]

                    // Get the appropriate HTTP method based on the ACTION
                    def httpMethod = httpMethods[params.ACTION]

                    sh """
                        curl -X ${httpMethod} ${params.API_URL} \
                             -H "Content-Type: application/json" \
                             -d '${jsonData}'
                    """
                }
            }
        }
    }
}
