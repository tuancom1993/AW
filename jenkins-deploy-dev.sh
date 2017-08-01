cd /var/lib/jenkins/workspace/renton_dev/Development/source/Plato/BackEnd/lms-backend/
sh /shared/apache-maven-3.3.9/bin/mvn clean
sh /shared/apache-maven-3.3.9/bin/mvn tomcat7:undeploy -Pdev
sh /shared/apache-maven-3.3.9/bin/mvn tomcat7:deploy -Pdev -Dmaven.test.skip=true
