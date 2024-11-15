pipeline {
    agent any

    parameters {
        string(name: 'STYLE', choices: ['Italian', 'French', 'Japanese', 'Korean', 'American', 'Asian'], description: 'Choose a style')
        choice(name: 'VEGETARIAN', choices: ['yes', 'no'], description: 'Vegetarian option')
    }

    environment {
        // Set the Azure Function URL and use Jenkins credentials for the host key
        FUNCTION_URL = credentials('azure-function-url') // Replace 'azure-function-url' with your Jenkins credential ID
        HOST_KEY = credentials('azure-function-host-key') // Replace 'azure-function-host-key' with your Jenkins credential ID
    }

    stages {
        stage('Load Parameters') {
            steps {
                script {
                    echo "Selected Style: ${params.STYLE}"
                    echo "Vegetarian Option: ${params.VEGETARIAN}"
                    echo "Using Azure Function URL: ${env.FUNCTION_URL}"
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
                    echo "Sending request to Azure Function URL: ${env.FUNCTION_URL} with style: ${params.STYLE} and vegetarian: ${params.VEGETARIAN}"

                    // Send the curl request with selected parameters, including the host key
                    sh """
                        curl -X GET "${env.FUNCTION_URL}" \
                             -H "Content-Type: application/json" \
                             -H "x-functions-key: ${env.HOST_KEY}" \
                             -d '{"style": "${params.STYLE}", "vegetarian": "${params.VEGETARIAN}"}' | ./jq '.[]'
                    """
                }
            }
        }
    }
}