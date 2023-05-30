pipeline {
    environment {
        imageName = "samir.nepal/jenkin/auth-server"
        uatImageName = "samir.nepal/jenkin/auth-server"
        liveImageName = "samir.nepal/jenkin/auth-server"
        registryCredential = 'prodRegistry'
    }

    agent any

    tools {
        jdk 'Java11 AdoptOpenJDK'
    }

    stages {

        stage('Build Project') {

            when {
                expression { BRANCH_NAME ==~ /(devMaster|master)/ }
            }

            steps {
                sh './mvnw clean install -Dmaven.test.skip=true'
            }
        }

        stage('Dev Push Image') {

            when {
                expression { BRANCH_NAME ==~ /(devMaster)/ }
            }

            steps {
                script {
                    def app;
                    docker.withRegistry("https://samir.nepal.com", registryCredential) {
                        app = docker.build(imageName)
                        app.push("${env.BUILD_NUMBER}")
                    }
                }
            }
        }

        stage('UAT Push Image') {

            when {
                expression { BRANCH_NAME ==~ /(uatMaster)/ }
            }

            steps {
                script {
                    def app;
                    DATE_TAG = java.time.LocalDate.now().toString().replaceAll("-", ".")

                    docker.withRegistry("https://samir.nepal.com", registryCredential) {
                        app = docker.build(uatImageName)
                        app.push("${DATE_TAG}-${env.BUILD_NUMBER}")
                    }
                }
            }
        }

        stage('Live Push Image') {

            when {
                expression { BRANCH_NAME ==~ /(master)/ }
            }

            steps {
                script {
                    def app;
                    DATE_TAG = java.time.LocalDate.now().toString().replaceAll("-", ".")

                    docker.withRegistry("https://samir.nepal.com", registryCredential) {
                        app = docker.build(liveImageName)
                        app.push("${DATE_TAG}-${env.BUILD_NUMBER}")
                    }
                }
            }
        }

//        stage('Dev Api Testing') {
//
//            when {
//                expression { BRANCH_NAME ==~ /(devMaster|testDevMaster)/ }
//            }
//
//            steps {
//
//                input 'Has the project been deploy? Api Testing will start to verify latest deployment !!! '
//
//                script {
//                    def jobBuild = build job: 'Test oauth-test-framework-karate/devMaster', propagate: false,
//                            parameters: [string(name: 'jiraKeys', value: jiraKeys), string(name: 'env', value: 'dev')]
//
//                    def jobResult = jobBuild.getResult()
//
//                    echo "Build of 'testJob' returned result: ${jobResult}"
//
//                    if (jobResult != 'SUCCESS') {
//                        error("testJob failed with result: ${jobResult}")
//                    }
//                }
//            }
//        }

//        stage('UAT Api Testing') {
//
//            when {
//                expression { BRANCH_NAME ==~ /(testMaster|master|uatMaster)/ }
//            }
//
//            steps {
//
//                input 'Has the project been deploy? Api Testing will start to verify latest deployment !!! '
//
//                script {
//                    def jobBuild = build job: 'Test fonepay-test-framework-karate/devMaster', propagate: false,
//                            parameters: [string(name: 'jiraKeys', value: jiraKeys), string(name: 'env', value: 'uat')]
//
//                    def jobResult = jobBuild.getResult()
//
//                    echo "Build of 'testJob' returned result: ${jobResult}"
//
//                    if (jobResult != 'SUCCESS') {
//                        error("testJob failed with result: ${jobResult}")
//                    }
//                }
//            }
//        }

//        stage("Push Tags") {
//
//            when {
//                expression { BRANCH_NAME ==~ /(master)/ }
//            }
//
//            environment {
//                GIT_AUTH = credentials('Tag.Mukesh.Maskey')
//            }
//
//            steps {
//
//                input 'Tag and push tag to gitlab ?'
//
//                script {
//                    DATE_TAG = java.time.LocalDate.now().toString().replaceAll("-", ".")
//                    sh """
//                    git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"
//                    git tag ${DATE_TAG}-${env.BUILD_NUMBER}
//                    git push origin --tags
//                    """
//                }
//
//            }
//        }


    }


    options {
        buildDiscarder(logRotator(numToKeepStr: '50', artifactNumToKeepStr: '2'))
    }
}
