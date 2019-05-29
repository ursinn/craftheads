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
				sh 'mvn clean package -Djar.finalName=CraftHeads-${JOB_NAME}-CI-Build-#${BUILD_NUMBER}'
			}
			post {
				success {
					archiveArtifacts artifacts: '**/target/ursinn/CraftHeads/*.jar', fingerprint: true
                }
			}
        }
    }
}
