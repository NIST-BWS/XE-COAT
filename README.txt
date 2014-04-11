Cloud Orchestration Automation Templating
--

COAT is a web-services front end to Apache Velocity.

To build the COAT project using Apache Maven, open a command prompt and change your working directory to the root of the XE project. Enter the following commands:

	mvn clean install
	cd service
	mvn clean compile assembly:single

COAT needs the full JDK, not just the JRE, since it programatically invokes the Java compiler. Other dependencies will be automatically downloaded and managed by Maven.

<a href="https://scan.coverity.com/projects/1774">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/1774/badge.svg"/>
</a>
