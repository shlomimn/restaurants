pipeline {
    agent any

    parameters {
        string(name: 'IP_ADDRESS', defaultValue: '127.0.0.1', description: 'Enter the API server IP address')
        choice(name: 'STYLE', choices: ['Italian', 'French', 'Korean'], description: 'Choose a style')
        choice(name: 'VEGETARIAN', choices: ['yes', 'no'], description: 'Vegetarian option')
    }

    environment {
        PORT = '5000'
        ENDPOINT = '/recommend'
    }

    stages {
        stage('Load Parameters') {
            steps {
                echo "Selected IP Address: ${params.IP_ADDRESS}"
                echo "Selected Style: ${params.STYLE}"
                echo "Vegetarian Option: ${params.VEGETARIAN}"
            }
        }

        stage('Trigger Recommendations') {
            steps {
                script {
                    def apiUrl = "http://${params.IP_ADDRESS}:${env.PORT}${env.ENDPOINT}"
                    echo "Sending request to API URL: ${apiUrl} with style: ${params.STYLE} and vegetarian: ${params.VEGETARIAN}"

                    // Send the curl request with selected parameters
                    sh """
                        curl -X GET "${apiUrl}" \
                             -H "Content-Type: application/json" \
                             -d '{\"style\": \"${params.STYLE}\", \"vegetarian\": \"${params.VEGETARIAN}\"}'
                    """
                }
            }
        }
    }
}