
HashMap endpoint = [
    "azure-function": "/api/recommend"
    "flask": "/recommend"
]

HashMap ipaddress = [
    "azure-function": "localhost"
    "flask": "192.168.1.221"
]

HashMap port = [
    "azure-function": "7071"
    "flask": "5000"
]

pipeline {
    agent any

    parameters {
        choice(name: 'API_TYPE', choices: ['azure-function', 'flask'], description: 'Choose the API type')
        choice(name: 'STYLE', choices: ['Italian', 'French', 'Japanese', 'Korean', 'American', 'Asian'], description: 'Choose a style')
        choice(name: 'VEGETARIAN', choices: ['yes', 'no'], description: 'Vegetarian option')
    }

    stages {
        stage('Load Parameters') {
            steps {
                script {
                    echo "Selected Style: ${params.STYLE}"
                    echo "Vegetarian Option: ${params.VEGETARIAN}"
                    echo "API Type: ${params.API_TYPE}"
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
                    def apiUrl = "http://${ipaddress[params.API_TYPE]}:${port[params.API_TYPE]}${endpoint[params.API_TYPE]}"
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
