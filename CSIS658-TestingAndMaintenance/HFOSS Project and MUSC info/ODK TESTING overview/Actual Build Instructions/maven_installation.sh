mvn install:install-file -Dfile=javarosa-libraries-2013-09-30.jar -DgroupId=org.javarosa -DartifactId=javarosa-libraries -Dversion=2013-09-30 -Dpackaging=jar
mvn install:install-file -Dfile=odk-httpclient-gae-1.1.jar -DgroupId=org.opendatakit -DartifactId=odk-httpclient-gae -Dversion=1.1 -Dpackaging=jar


mvn install:install-file -Dfile=openid4java-parent-pom.xml -DgroupId=org.openid4java -DartifactId=openid4java-parent -Dversion=0.9.6.662.odk-SNAPSHOT -Dpackaging=pom

mvn install:install-file -Dfile=openid4java-nodeps-0.9.6.662.odk-SNAPSHOT.jar -DgroupId=org.openid4java -DartifactId=openid4java-nodeps -Dversion=0.9.6.662.odk-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=spring-security-crypto-3.1.3.odk-SNAPSHOT.jar -DgroupId=org.springframework.security -DartifactId=spring-security-crypto -Dversion=3.1.3.odk-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=spring-security-config-3.1.3.odk-SNAPSHOT.jar -DgroupId=org.springframework.security -DartifactId=spring-security-config -Dversion=3.1.3.odk-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=spring-security-core-3.1.3.odk-SNAPSHOT.jar -DgroupId=org.springframework.security -DartifactId=spring-security-core -Dversion=3.1.3.odk-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=spring-security-web-3.1.3.odk-SNAPSHOT.jar -DgroupId=org.springframework.security -DartifactId=spring-security-web -Dversion=3.1.3.odk-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=spring-security-openid-3.1.3.odk-SNAPSHOT.jar -DgroupId=org.springframework.security -DartifactId=spring-security-openid -Dversion=3.1.3.odk-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=odk-tomcatutil.jar -DgroupId=org.opendatakit -DartifactId=odk-tomcatutil -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=gwt-google-maps-v3-snapshot.jar -DgroupId=com.googlecode.gwt-google-maps-v3 -DartifactId=gwt-google-maps-v3 -Dversion=snapshot -Dpackaging=jar

mvn install:install-file -Dfile=gwt-visualization-1.1.1.jar -DgroupId=com.google.gwt.google-apis -DartifactId=gwt-visualization -Dversion=1.1.1 -Dpackaging=jar

mvn install:install-file -Dfile=appengine-remote-api.jar -DgroupId=com.google.appengine -DartifactId=appengine-remote-api -Dversion=1.5.4 -Dpackaging=jar