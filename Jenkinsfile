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
				wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
					sh '''
					export GDK_BACKEND=x11
					mvn clean verify -Dmaven.repo.local=$WORKSPACE/.m2/repository \
						-DapiBaselineTargetDirectory=${WORKSPACE} \
						-Dgpg.passphrase="${KEYRING_PASSPHRASE}" \
						-Dproject.build.sourceEncoding=UTF-8 \
						-Peclipse-sign
					'''
				}
			}
			post {
				always {
					archiveArtifacts artifacts: '.*log,org.eclipse.gef.repository/target/repository/*', allowEmptyArchive: true
	 				junit(
	 					allowEmptyResults: true,
	 					testResults: '**/target/surefire-reports/*.xml'
	 				)
	 			}
				failure {
		            mail bcc: '', body: "<b>GEF Classic: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: GEF Classic -> ${env.JOB_NAME}", to: "alois.zoitl@gmx.at";
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