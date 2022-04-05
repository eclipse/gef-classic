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
		jdk 'openjdk-jdk11-latest'
	}
	stages {
		
		stage('Build') {
			steps {
				sh '''
				mvn clean verify -Dmaven.repo.local=$WORKSPACE/.m2/repository \
					-DapiBaselineTargetDirectory=${WORKSPACE} \
					-Dgpg.passphrase="${KEYRING_PASSPHRASE}" \
					-Dproject.build.sourceEncoding=UTF-8 
				'''
			
			}
			post {
				always {
					archiveArtifacts artifacts: '.*log,org.eclipse.gef.repository/target/repository/*', allowEmptyArchive: true
				}
			}
		}
	}
}