pipeline {
    agent any
    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "MyMaven"
    }

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/shreekhedkar/RestAssured.git'
                }
        }    
        stage('Compile') {
            steps {
                sh "mvn clean install -DskipTests=true"
            }
        }    
        stage('Unit tests') {
            steps {
              sh "mvn clean test -Dsurefire.suiteXmlFiles=${WORKSPACE}/src/test/resources/testng.xml"
                
            }
        }    
        stage('Sonar')  {
            when { expression { params.skip_sonar != true } }
            steps {
                    script {
                    def scannerHome = tool 'JenkinsSonarQube';
                    withSonarQubeEnv('MySonarQube') { 
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=github-jenkins-sonar-Declarative-Pipeline"
                    }
                }
            }
        }
        post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
    }
}
