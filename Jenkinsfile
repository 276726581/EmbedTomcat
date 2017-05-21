pipeline {
  agent none
  stages {
    stage('test1') {
      steps {
        echo 'build'
        sh '''#!/bin/sh

java -version'''
      }
    }
    stage('test2') {
      steps {
        sh '''#!/bin/sh

${M2_HOME}/mvn -version'''
      }
    }
  }
  environment {
    M2_HOME = '/app/maven'
  }
}