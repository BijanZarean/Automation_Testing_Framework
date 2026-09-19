pipeline {
    agent {
        label 'petclinic-test-agent'
    }

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    options {
        timestamps()

        skipDefaultCheckout(true)

        //This current framework / database is not designed for multiple
        //Jenkins builds running against an application simultaneously.pipeline
        disableConcurrentBuilds()

        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 30, unit: 'MINUTES')
    }

    parameters {
        choice(name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Browser used for Selenium UI testing')
    }

    environment {
        BASE_URL = 'http://localhost:4200'

        API_BASE_URL = 'http://localhost:9966/petclinic/api'

        DB_URL = 'jdbc:mysql://localhost:3306/petclinic'

        API_HEALTH_URL = 'http://localhost:9966/petclinic/actuator/health'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Verify Environment') {
            steps {
                sh '''
                    echo "===== JENKINS NODE ====="
                    echo "$NODE_NAME"

                    echo "===== USER ====="
                    whoami

                    echo "===== JAVA ====="
                    java -version

                    echo "===== MAVEN ====="
                    mvn -version

                    echo "===== GIT ====="
                    git --version

                    echo "===== CHROME ====="
                    "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" --version
                '''
            }
        }
        stage('Verify Petclinic') {
            steps {
                sh '''
                    echo "Checking Petclinic UI..."

                    curl --fail --silent --show-error "$BASE_URL" > /dev/null

                    echo "Petclinic UI is available."


                    echo "Checking Petclinic API..."

                    curl --fail --silent --show-error "$API_HEALTH_URL"

                    echo ""
                    echo "Petclinic API is available."
                '''
            }
        }
        stage('Compile') {
            steps {
                sh '''
                    mvn -B -ntp clean test-compile
                '''
            }
        }
        stage('Cucumber Dry Run') {
            steps {
                sh '''
                    mvn -B -ntp test -Pdry_run
                '''
            }
        }
        stage('Petclinic smoke Tests') {
            when {
                beforeOptions true
                expression {
                    return params.RUN_API || params.RUN_UI
                }
            }
            options {
                lock(
                    resource: 'petclinic-local-env',
                    reason: 'Running Petclinic integration tests'
                )
            }
            stages {
                stage('PR API Smoke Tests') {
                    when {
                        changeRequest()
                    }
                    steps {
                        withCredentials([
                            usernamePassword(
                                    credentialsId: 'petclinic-db',
                                    usernameVariable: 'DB_USERNAME',
                                    passwordVariable: 'DB_PASSWORD'
                                )]) {
                            sh '''
                                mvn -B -ntp test -Papi_smoke
                            '''
                        }
                    }
                }
                stage('PR UI Smoke Tests') {
                    when {
                        changeRequest()
                    }
                    steps {
                        withCredentials([
                            usernamePassword(
                                    credentialsId: 'petclinic-db',
                                    usernameVariable: 'DB_USERNAME',
                                    passwordVariable: 'DB_PASSWORD'
                                )]) {
                            sh '''
                                mvn -B -ntp test -Pui_tests -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@smoke"
                            '''
                        }
                    }
                }
            }
        }
        stage('Main UI Regression') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([
                    usrnamePassword(
                            credentialsId: 'petclinic-db',
                            usernameVariable: 'DB_USERNAME',
                            passwordVariable: 'DB_PASSWORD'
                        )]) {
                    sh '''
                        mvn -ntp test -Pui_tests -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@regression"
                    '''
                }
            }
        }
    }

    post {
        always {
            junit(testResults: 'target/surefire-reports/TEST-*.xml',
                allowEmptyResults: true
            )
            archiveArtifacts(artifacts: 'target/cucumber-reports/**',
                allowEmptyArchive: true,
                fingerprint: true
            )
        }
        success {
            echo '''
                Petclinic automation pipeline PASSED.
            '''
        }
        failure {
            echo '''
                Petclinic automation pipeline FAILED.
            '''
        }
    }
}