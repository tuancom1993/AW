cd /var/lib/jenkins/workspace/renton_phase2_dev/lms-backend/
sh /shared/apache-maven-3.3.9/bin/mvn clean
sh /shared/apache-maven-3.3.9/bin/mvn tomcat7:undeploy -Pdev
sh /shared/apache-maven-3.3.9/bin/mvn tomcat7:redeploy -Pdev -Dmaven.test.skip=true
