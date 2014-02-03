To build the XE project using Apache Maven, open a command prompt and change your working directory to the root of the XE project. Enter the following commands:

mvn clean install
cd service
mvn clean compile assembly:single

Note that COAT needs the full JDK, not just the JRE, since it programatically invokes the Java compiler.