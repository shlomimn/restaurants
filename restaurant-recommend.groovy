pipeline {
    agent any

    parameters {
        string(name: 'IP_ADDRESS', defaultValue: '192.168.1.221', description: 'Enter the API server IP address')
        choice(name: 'API_TYPE', choices: ['flask', 'azure-function'], description: 'Choose the API type', defaultValue: 'azure-function')
        choice(name: 'STYLE', choices: ['Italian', 'French', 'Japanese', 'Korean', 'American', 'Asian'], description: 'Choose a style')
        choice(name: 'VEGETARIAN', choices: ['yes', 'no'], description: 'Vegetarian option')
    }

    environment {
        ENDPOINT = '/recommend'
    }

    stages {
        stage('Load Parameters') {
            steps {
                script {
                    if (params.API_TYPE == 'flask') {
                        env.PORT = '5000'
                    } else if (params.API_TYPE == 'azure-function') {
                        env.PORT = '7071'
                    }
                    echo "Selected IP Address: ${params.IP_ADDRESS}"
                    echo "Selected Style: ${params.STYLE}"
                    echo "Vegetarian Option: ${params.VEGETARIAN}"
                    echo "API Type: ${params.API_TYPE}"
                    echo "Using Port: ${env.PORT}"
                }
            }
        }

        stage('Download jq if needed') {
            steps {
                script {
                    // Download jq locally if it's not already downloaded
                    sh """
                        if ! [ -x "./jq" ]; then
                            echo 'jq not found, downloading...'
                            curl -L -o jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64
                            chmod +x jq
                        fi
                    """
                }
            }
        }

        stage('Trigger Recommendations') {
            steps {
                script {
                    def apiUrl = "http://${params.IP_ADDRESS}:${env.PORT}${env.ENDPOINT}"
                    echo "Sending request to API URL: ${apiUrl} with style: ${params.STYLE} and vegetarian: ${params.VEGETARIAN}"

                    // Send the curl request with selected parameters and format the result with jq
                    sh """
                        curl -X GET "${apiUrl}" \
                             -H "Content-Type: application/json" \
                             -d '{"style": "${params.STYLE}", "vegetarian": "${params.VEGETARIAN}"}' | ./jq '.[]'
                    """
                }
            }
        }
    }
}
