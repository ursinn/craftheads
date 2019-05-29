pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
	
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
				sh 'mvn clean package -Djar.finalName=${JOB_NAME}_CI_Build_#${BUILD_NUMBER}'
			}
			post {
				success {
					archiveArtifacts artifacts: '**/target/ursinn/*.jar', fingerprint: true
                }
			}
        }
    }
}
