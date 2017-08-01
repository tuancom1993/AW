cd /var/lib/jenkins/workspace/renton_test/Development/source/Plato/BackEnd/lms-backend/
sh /shared/apache-maven-3.3.9/bin/mvn clean
sh /shared/apache-maven-3.3.9/bin/mvn tomcat7:undeploy -Ptest
sh /shared/apache-maven-3.3.9/bin/mvn tomcat7:deploy -Ptest -Dmaven.test.skip=true
