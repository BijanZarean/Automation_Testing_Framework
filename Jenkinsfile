//"pipeline" is the root of the declartive Jenkins Pipeline
// Everything Jenkins should do for this CI is in this:
pipeline {
    //"agent" tells Jenkins WHERE this pipeline is allowed to run.
    agent {
        //Jenkins looks for a node/agent with this label:
        label 'petclinic-test-agent'
    }

    //"tools" asks Jenkins to make specific tools installations available to this build
    tools {
        //"JDK21" and "Maven3" must match the installation names configured under:
        // Manage Jenkins -> Tools.
        //Jenkins puts the installations on path for this build.
        jdk 'JDK21'
        maven 'Maven3'
    }

    //Pipeline wide behavior/settings:
    options {
        //Adds timestamps to every line in the Jenkins console log:
        timestamps()
        //Declarative pipeline normally performs an automatic SCM checkout when it enters the top level agent.
        //We disable that because we explicitly control checkout later with "checkout scm":
        skipDefaultCheckout(true)
        //Prevents two executions of this same pipeline job from overlapping:
        disableConcurrentBuilds()
        //Keeps only the last 20 Jenkins builds for this job to limit Jenkins disk usage:
        buildDiscarder(logRotator(numToKeepStr: '20'))
        //Tells Jenkins to abort the job if it takes longer than 30 minutes:
        timeout(time: 30, unit: 'MINUTES')
    }

    //Pipeline wide environment variables.
    //These become OS env variables for commands executed by Jenkins.
    environment {
        BASE_URL = 'http://localhost:4200'
        API_BASE_URL = 'http://localhost:9966/petclinic/api'
        DB_URL = 'jdbc:mysql://localhost:3306/petclinic'
        API_HEALTH_URL = 'http://localhost:9966/petclinic/actuator/health'
    }

    //"stages" contain the ordered phases of the Pipeline:
    stages {
        //STAGE 1, get the source code:
        stage('Checkout') {
            //"steps" contains commands/actions that Jenkins executes
            steps {
                //"scm" represents the source control configuration that caused this multibranch build.
                //For main: checkout main
                //For a PR: checkout the PR revision/sythetic merge selected by GitHub branch source
                checkout scm
            }
        }
        //STAGE 2, verify build machine
        stage('Verify Environment') {
            steps {
                //"sh" executes shell commands on a Unix like Jenkins agent.
                //tripple single qoutes allows a multiline shell scripts:
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
        //STAGE 3, verify the application
        stage('Verify Petclinic') {
            steps {
                sh '''
                    echo "Checking Petclinic UI..."
#'curl' sends an HTTP request to the Angular UI.
#'--fail' Return a failure exit code for HTTP errors
#'--silent' hide normal curl progress output
#'--show-error' still prints errors even though --silent is enabled
#'> /dev/null' throw away the response body because you only care whether the UI responds successfully
                    curl --fail --silent --show-error "$BASE_URL" > /dev/null

                    echo "Petclinic UI is available."


                    echo "Checking Petclinic API..."
#calls sprint boot actuator. unlike the UI check, this output is not redirected.
#so Jenkins will show the {"status":"UP"} in the log.
                    curl --fail --silent --show-error "$API_HEALTH_URL"

                    echo ""
                    echo "Petclinic API is available."
                '''
            }
        }
        //STAGE 4, compile test framework
        stage('Compile') {
            steps {
                sh '''
#'mvn' Run Maven.
#'-B' Batch mode, appropriate for CI because Maven should not expect interactive terminal input
#'-ntp' disable transfer progress noise while dependencies are downloaded
#'clean' delete the existing target/ directory and start from fresh
#'test-compile' compile application and test source without running the tests
                    mvn -B -ntp clean test-compile
                '''
            }
        }
        //STAGE 5, cucumber structure check
        stage('Cucumber Dry Run') {
            steps {
                sh '''
#Executes the Maven "test" phase while activating your custom dry_run profile.
#The POM.xml's dry_run profile selects DryRunner.java.
                    mvn -B -ntp test -Pdry_run
                '''
            }
        }
//PR-only test group. This parent stage contains two nested stages.
        //Purpose: if this is a Pull Request, acquire Petclinic and run the API + UI smoke tests.
        stage('Petclinic smoke Tests') {
            //Decides whether this entire parent stage should execute:
            when {
                //Stage level options are normally entered before Jenkins evaluates the stage's when condition.
                //Your stage level option below acquires a LOCK.
                //Without "beforeOptions true", even a main build that should skip this PR stage could attempt to
                //acquire the Petclinic resource first. This tells Jenkins:
                //1. check whether this is a PR.
                //2. only if true, apply the LOCK.
                beforeOptions true
                //True when this Multibranch represents a pull request / change request.
                changeRequest()
//"beforeOptions true" allows the when condition to be executed before "options" is executed.
            }
            // Options applying to this whole parent stage.
            options {
                //Acquire the shared Petclinic env.
                //If another main/PR/nightly build owns this resource, this waits.
                lock(
                    //Logical resource we configured in Jenkins
                    resource: 'petclinic-local-env',
                    //Human readable explanation shown by Jenkins while this resource is held.
                    reason: 'Running Petclinic integration tests'
                )
            }
            //A stage can contain sequential nested stages instead of ordinary "steps".
            //Because the LOCK belongs to the parent stage, both child stages execute while the LOCK is held.
            stages {
                //PR API SMOKE STAGE
                stage('PR API Smoke Tests') {
                    steps {
                        //Retrieves a Jenkins credential temporarily.
                        withCredentials([
                            //The Jenkins credential we created is a Username/Password credential:
                            usernamePassword(
                                    //Jenkins credential identifier:
                                    credentialsId: 'petclinic-db',
                                    //Jenkins creates the env variables for the duration of the block {sh...} below:
                                    usernameVariable: 'DB_USERNAME',
                                    passwordVariable: 'DB_PASSWORD'
                                )]) {
                            sh '''
#Activates the "api_smoke" maven profile which executes only the critical TestNG API Smoke Tests:
                                mvn -B -ntp test -Papi_smoke
                            '''
                        }
                    }
                }
                //PR UI Smoke
                stage('PR UI Smoke Tests') {
                    steps {
                        withCredentials([
                            usernamePassword(
                                    credentialsId: 'petclinic-db',
                                    usernameVariable: 'DB_USERNAME',
                                    passwordVariable: 'DB_PASSWORD'
                                )]) {
                            sh '''
#Activate ui_tests profile and force chrome and headless mode, regardless of env.properties file while running only @smoke tests.
                                mvn -B -ntp test -Pui_tests -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@smoke"
                            '''
                        }
                    }
                }
            }
        }
        //MAIN BRANCH REGRESSION GROUP
        stage('Main Regression') {
            when {
                //Evaluate the branch condition before attempting to acquire the Petclinic LOCK.
                beforeOptions true
                //Only true when this multibranch job represents main.
                branch 'main'
            }
            options {
                //Same resource used by PR and Nightly jobs; so each one executes once at a time.
                lock(
                    resource: 'petclinic-local-env',
                    reason: 'Running Petclinic main regression'
                )
            }
            //Both of these child stages execute once the parent stage owns the LOCK.
            stages {
                //FULL API REGRESSION
                stage('Main API regression') {
                    steps {
                        withCredentials([
                                usernamePassword(
                                    credentialsId: 'petclinic-db',
                                    usernameVariable: 'DB_USERNAME',
                                    passwordVariable: 'DB_PASSWORD'
                                )]) {
                            sh '''
#The full API Regression test suite
                                mvn -B -ntp test -Papi_tests
                            '''
                        }
                    }
                }
                //Chrome UI Regression
                stage('Main UI regression'){
                    steps {
                        withCredentials([
                                usernamePassword(
                                    credentialsId: 'petclinic-db',
                                    usernameVariable: 'DB_USERNAME',
                                    passwordVariable: 'DB_PASSWORD'
                                )]) {
                            sh '''
#Full cucumber @regression suite.
                        mvn -B -ntp test -Pui_tests -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@regression"
                    '''
                        }
                    }
                }
            }
        }
    }
//PIPELINE WIDE POST ACTIONS
    //These run after the stages are finished:
    post {
        //"always" means that these always execute whether the build succeeds, fails, or otherwise finishes.
        always {
            //Jenkins test result publisher
            //Jenkins can read JUnit format XML generated for TestNG tests.
            junit(
                //Ant style file pattern
                //Find any TEST-*.xml under surefire reports.
                testResults: 'target/surefire-reports/TEST-*.xml',
                //If no report file is found, do not fail the build soley because the report is missing:
                allowEmptyResults: true
            )
            //Preserve Cucumber reports outside the temporary workspace:
            archiveArtifacts(
                //Archive every file beneath this directory:
                artifacts: 'target/cucumber-reports/**',
                //Don't fail if non exists:
                allowEmptyArchive: true,
                //Jenkins computes a fingerprint / hash for archived files so it can track indentical artifacts across builds
                fingerprint: true
            )
        }
        //"success" blocks run only if the overall pipeline succeeds.
        success {
            echo '''
                Petclinic automation pipeline PASSED.
            '''
        }
        //"failure" blocks run only if the overall pipeline fails:
        failure {
            echo '''
                Petclinic automation pipeline FAILED.
            '''
        }
    }
}