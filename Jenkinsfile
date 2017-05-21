pipeline {
  agent none
  stages {
    stage('test1') {
      steps {
        echo 'build'
        sh 'java -version'
      }
    }
    stage('test2') {
      steps {
        sh 'java -version'
      }
    }
  }
}