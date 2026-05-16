pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    environment {
        APP_NAME = "hospital-management-system"
        IMAGE_NAME = "hms-app"
        CONTAINER_NAME = "hms-container"
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

                bat 'mvn clean compile'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo '======================================'
                echo 'Running test cases...'
                echo '======================================'

                bat 'mvn test'
            }
        }

        stage('Package Application') {
            steps {
                echo '======================================'
                echo 'Packaging JAR file...'
                echo '======================================'

                bat 'mvn package -DskipTests'
            }
        }

        stage('Verify Target Folder') {
            steps {
                echo '======================================'
                echo 'Checking generated JAR file...'
                echo '======================================'

                bat 'dir target'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '======================================'
                echo 'Building Docker image...'
                echo '======================================'

                bat 'docker build -t %IMAGE_NAME% .'
            }
        }

        stage('Remove Old Container') {
            steps {
                echo '======================================'
                echo 'Removing old Docker container...'
                echo '======================================'

                bat 'docker stop %CONTAINER_NAME% || exit 0'
                bat 'docker rm %CONTAINER_NAME% || exit 0'
            }
        }

        stage('Deploy Container') {
            steps {
                echo '======================================'
                echo 'Deploying new Docker container...'
                echo '======================================'

                bat 'docker run -d -p 8080:8080 --name %CONTAINER_NAME% %IMAGE_NAME%'
            }
        }

        stage('Verify Deployment') {
            steps {
                echo '======================================'
                echo 'Verifying running containers...'
                echo '======================================'

                bat 'docker ps'
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