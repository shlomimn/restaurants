pipeline {
    agent any

    environment {
        TF_BACKEND_CONFIG = "terraform-code/terraform-config/backend-restaurant.tf"
        TF_VAR_FILE       = "terraform-code/tfvars/restaurant.tfvars"
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    // Retrieve the database password from Jenkins credentials
                    def dbPassword = credentials('mysql_pw_id') // Use the credentials ID configured in Jenkins

                    // Initialize Terraform
                    sh """
                        terraform init \
                        -backend-config=${TF_BACKEND_CONFIG}
                    """

                    // Set the database password as an environment variable for Terraform
                    env.TF_VAR_mysql_pw = dbPassword
                }
            }
        }

        stage('Validate') {
            steps {
                script {
                    // Validate Terraform code
                    sh "terraform validate"
                }
            }
        }

        stage('Plan') {
            steps {
                script {
                    // Run Terraform plan with the var-file and db password
                    sh """
                        terraform plan \
                        -var-file=${TF_VAR_FILE} \
                        -var="mysql_pw=$TF_VAR_mysql_pw"
                    """
                }
            }
        }

        stage('Apply') {
            steps {
                input message: "Approve Terraform Apply?"
                script {
                    // Apply Terraform configuration
                    sh """
                        terraform apply -auto-approve \
                        -var-file=${TF_VAR_FILE} \
                        -var="mysql_pw=$TF_VAR_mysql_pw"
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                // Clean up sensitive environment variables
                env.TF_VAR_mysql_pw = null
            }
        }
        failure {
            echo 'Terraform process failed.'
        }
        success {
            echo 'Terraform process completed successfully.'
        }
    }
}
