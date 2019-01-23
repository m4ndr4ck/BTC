pipeline {
    agent any

    triggers {
        gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                withMaven( mavenSettingsConfig: 'BTC') {
                    //sh 'mvn tomcat7:redeploy'
                    sh 'mvn --batch-mode jgitflow:release-start jgitflow:release-finish -DallowSnapshots=true'
                }
            }
        }
    }
}
