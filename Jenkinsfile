pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    environment {

        DB_URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres"
        DB_USER = "postgres.bjioslmcldvqtmkaswmt"
        DB_PWD = "Suraj@123Pastgres"

        REDIS_HOST = "capable-bluegill-126086.upstash.io"
        REDIS_PORT = "6379"
        REDIS_PASSWORD = "gQAAAAAAAeyGAAIgcDIzNjQ2NjI2OTg3ODI0ZDQ1YTkyNDA1OTBjMzU3ZWI5Yw"
    }

    stages {

        stage('Checkout Code') {

            steps {

                git branch: 'main',
                url: 'https://github.com/suraj-sahuco/hospital-management-system.git'
            }
        }

        stage('Build Application') {

            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Stop Old Containers') {

            steps {
                sh 'docker compose down || true'
            }
        }

        stage('Deploy Application') {

            steps {

                sh """
                export DB_URL=${DB_URL}
                export DB_USER=${DB_USER}
                export DB_PWD=${DB_PWD}

                export REDIS_HOST=${REDIS_HOST}
                export REDIS_PORT=${REDIS_PORT}
                export REDIS_PASSWORD=${REDIS_PASSWORD}

                docker compose up -d --build
                """
            }
        }

        stage('Verify Running Containers') {

            steps {
                sh 'docker ps'
            }
        }
    }

    post {

        success {
            echo 'Deployment Successful 🚀'
        }

        failure {
            echo 'Pipeline Failed ❌'
        }
    }
}