pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    environment {

        // =========================
        // DATABASE ENV VARIABLES
        // =========================

        DB_URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres"
        DB_USER = "postgres.bjioslmcldvqtmkaswmt"
        DB_PWD = "Suraj@123Pastgres"

        // =========================
        // REDIS ENV VARIABLES
        // =========================

        REDIS_HOST = "capable-bluegill-126086.upstash.io"
        REDIS_PORT = "6379"
        REDIS_PASSWORD = "gQAAAAAAAeyGAAIgcDIzQ2NjI2OTg3ODI0ZDQ1YTkyNDA1OTBjMzU3ZWI5Yw"
    }

    stages {

        stage('Checkout Code') {

            steps {

                echo '======================================'
                echo 'Cloning source code from GitHub...'
                echo '======================================'

                git branch: 'main',
                url: 'https://github.com/suraj-sahuco/hospital-management-system.git'
            }
        }

        stage('Build Application') {

            steps {

                echo '======================================'
                echo 'Building Spring Boot application...'
                echo '======================================'

                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Stop Old Containers') {

            steps {

                echo '======================================'
                echo 'Stopping old containers...'
                echo '======================================'

                sh 'docker-compose down || true'
            }
        }

        stage('Deploy Application') {

            steps {

                echo '======================================'
                echo 'Building and deploying containers...'
                echo '======================================'

                sh """
                export DB_URL=${DB_URL}
                export DB_USER=${DB_USER}
                export DB_PWD=${DB_PWD}

                export REDIS_HOST=${REDIS_HOST}
                export REDIS_PORT=${REDIS_PORT}
                export REDIS_PASSWORD=${REDIS_PASSWORD}

                docker-compose up -d --build
                """
            }
        }

        stage('Verify Running Containers') {

            steps {

                echo '======================================'
                echo 'Checking running containers...'
                echo '======================================'

                sh 'docker ps'
            }
        }

        stage('Container Logs') {

            steps {

                echo '======================================'
                echo 'Printing container logs...'
                echo '======================================'

                sh 'docker-compose logs'
            }
        }
    }

    post {

        success {

            echo '======================================'
            echo 'CI/CD Pipeline Executed Successfully 🚀'
            echo 'Application deployed successfully!'
            echo '======================================'
        }

        failure {

            echo '======================================'
            echo 'Pipeline Failed ❌'
            echo 'Check Console Output for errors.'
            echo '======================================'
        }

        always {

            echo '======================================'
            echo 'Pipeline Execution Completed.'
            echo '======================================'
        }
    }
}