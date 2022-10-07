pipeline {
	options {
		timeout(time: 240, unit: 'MINUTES')
		buildDiscarder(logRotator(numToKeepStr:'20'))
		disableConcurrentBuilds(abortPrevious: true)
	}
	agent {
		label "centos-7"
	}
	tools {
		maven 'apache-maven-latest'
		jdk 'openjdk-jdk17-latest'
	}
	stages {
		
		stage('Build') {
			steps {
				sh '''
				mvn clean verify -Dmaven.repo.local=$WORKSPACE/.m2/repository \
					-DapiBaselineTargetDirectory=${WORKSPACE} \
					-Dgpg.passphrase="${KEYRING_PASSPHRASE}" \
					-Dproject.build.sourceEncoding=UTF-8 \
					-Peclipse-sign
				'''
			
			}            
			post {
				always {
					archiveArtifacts artifacts: '.*log,org.eclipse.gef.repository/target/repository/*', allowEmptyArchive: true
				}
			}

		}
        stage('Deploy') {
            steps {
                sshagent ( ['projects-storage.eclipse.org-bot-ssh']) {
                    sh '''
                        ssh -o BatchMode=yes genie.gef@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/tools/gef/classic/latest
                        ssh -o BatchMode=yes genie.gef@projects-storage.eclipse.org mkdir -p /home/data/httpd/download.eclipse.org/tools/gef/classic/latest
                        scp -o BatchMode=yes -r org.eclipse.gef.repository/target/repository/* genie.gef@projects-storage.eclipse.org:/home/data/httpd/download.eclipse.org/tools/gef/classic/latest
                    '''
                    }
                }
        }
	}
}