pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    environment {

        APP_NAME = "hospital-management-system"
        IMAGE_NAME = "hms-app"
        CONTAINER_NAME = "hms-container"

        // =========================
        // DATABASE ENV VARIABLES
        // =========================

        DB_URL = "jdbc:postgresql://YOUR_SUPABASE_HOST:6543/postgres"
        DB_USER = "YOUR_DB_USER"
        DB_PWD = "YOUR_DB_PASSWORD"

        // =========================
        // REDIS ENV VARIABLES
        // =========================

        REDIS_HOST = "YOUR_REDIS_HOST"
        REDIS_PORT = "6379"
        REDIS_PASSWORD = "YOUR_REDIS_PASSWORD"
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

                sh 'mvn clean compile'
            }
        }

        stage('Run Unit Tests') {

            steps {

                echo '======================================'
                echo 'Running test cases...'
                echo '======================================'

                sh 'mvn test'
            }
        }

        stage('Package Application') {

            steps {

                echo '======================================'
                echo 'Packaging JAR file...'
                echo '======================================'

                sh 'mvn package -DskipTests'
            }
        }

        stage('Verify Target Folder') {

            steps {

                echo '======================================'
                echo 'Checking generated JAR file...'
                echo '======================================'

                sh 'ls -la target'
            }
        }

        stage('Build Docker Image') {

            steps {

                echo '======================================'
                echo 'Building Docker image...'
                echo '======================================'

                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Remove Old Container') {

            steps {

                echo '======================================'
                echo 'Removing old Docker container...'
                echo '======================================'

                sh "docker stop ${CONTAINER_NAME} || true"
                sh "docker rm ${CONTAINER_NAME} || true"
            }
        }

        stage('Deploy Container') {

            steps {

                echo '======================================'
                echo 'Deploying new Docker container...'
                echo '======================================'

                sh """
                docker run -d \
                -p 8081:8080 \
                --name ${CONTAINER_NAME} \
                -e DB_URL='${DB_URL}' \
                -e DB_USER='${DB_USER}' \
                -e DB_PWD='${DB_PWD}' \
                -e REDIS_HOST='${REDIS_HOST}' \
                -e REDIS_PORT='${REDIS_PORT}' \
                -e REDIS_PASSWORD='${REDIS_PASSWORD}' \
                ${IMAGE_NAME}
                """
            }
        }

        stage('Verify Deployment') {

            steps {

                echo '======================================'
                echo 'Verifying running containers...'
                echo '======================================'

                sh 'docker ps'
            }
        }

        stage('Container Logs') {

            steps {

                echo '======================================'
                echo 'Printing container logs...'
                echo '======================================'

                sh "docker logs ${CONTAINER_NAME}"
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