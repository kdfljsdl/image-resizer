@echo off

set JAVA_HOME=C:\Java\jdk1.7.0_65_x64\
set classpath=../lib/amqp-client-3.6.3.jar
set classpath=%classpath%;../lib/apache-mime4j-0.6.jar
set classpath=%classpath%;../lib/commons-codec-1.3.jar
set classpath=%classpath%;../lib/commons-logging-1.1.1.jar
set classpath=%classpath%;../lib/dropbox-client-1.5.3.jar
set classpath=%classpath%;../lib/dropbox-core-sdk-1.7.7.jar
set classpath=%classpath%;../lib/guava-18.0.jar
set classpath=%classpath%;../lib/http-client-1.0.0.RELEASE.jar
set classpath=%classpath%;../lib/httpclient-4.0.3.jar
set classpath=%classpath%;../lib/httpcore-4.0.1.jar
set classpath=%classpath%;../lib/httpmime-4.0.3.jar
set classpath=%classpath%;../lib/image-resizer-1.0-DEV-SNAPSHOT.jar
set classpath=%classpath%;../lib/imgscalr-lib-4.2.jar
set classpath=%classpath%;../lib/jackson-annotations-2.5.0.jar
set classpath=%classpath%;../lib/jackson-core-2.4.0-rc3.jar
set classpath=%classpath%;../lib/jackson-databind-2.5.1.jar
set classpath=%classpath%;../lib/json-simple-1.1.jar
set classpath=%classpath%;../lib/log4j-api-2.6.2.jar
set classpath=%classpath%;../lib/log4j-core-2.6.2.jar
set classpath=%classpath%;../lib/spring-amqp-1.6.1.RELEASE.jar
set classpath=%classpath%;../lib/spring-aop-4.3.2.RELEASE.jar
set classpath=%classpath%;../lib/spring-beans-4.3.2.RELEASE.jar
set classpath=%classpath%;../lib/spring-context-4.3.2.RELEASE.jar
set classpath=%classpath%;../lib/spring-core-4.3.2.RELEASE.jar
set classpath=%classpath%;../lib/spring-expression-4.3.2.RELEASE.jar
set classpath=%classpath%;../lib/spring-messaging-4.2.7.RELEASE.jar
set classpath=%classpath%;../lib/spring-rabbit-1.6.1.RELEASE.jar
set classpath=%classpath%;../lib/spring-retry-1.1.3.RELEASE.jar
set classpath=%classpath%;../lib/spring-test-4.3.2.RELEASE.jar
set classpath=%classpath%;../lib/spring-tx-4.2.7.RELEASE.jar
set classpath=%classpath%;../lib/spring-web-4.2.7.RELEASE.jar

%JAVA_HOME%\bin\java -cp %classpath% my.dertraktor.imageresizer.bot.Bot %*