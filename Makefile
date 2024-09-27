.PHONY: generate

generate:
	mvn clean process-sources

install:
	mvn clean install

release:
	mvn clean
	mvn versions:set
	mvn deploy -Psign-artifacts -DaltDeploymentRepository=ossrh::default::https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/