pipeline {
    agent any

    parameters {
        choice(name: 'STYLE', choices: ['Italian', 'French', 'Korean'], description: 'Choose a style')
        choice(name: 'VEGETARIAN', choices: ['yes', 'no'], description: 'Vegetarian option')
    }

    environment {
        API_URL = 'http://127.0.0.1:5000/recommend'
    }

    stages {
        stage('Load Parameters') {
            steps {
                echo "Selected Style: ${params.STYLE}"
                echo "Vegetarian Option: ${params.VEGETARIAN}"
            }
        }

        stage('Trigger Recommendations') {
            steps {
                script {
                    echo "Sending request for style: ${params.STYLE}, vegetarian: ${params.VEGETARIAN}"

                    // Send the curl request with selected parameters
                    sh """
                        curl -X GET $API_URL \
                             -H "Content-Type: application/json" \
                             -d '{\"style\": \"${params.STYLE}\", \"vegetarian\": \"${params.VEGETARIAN}\"}'
                    """
                }
            }
        }
    }
}
