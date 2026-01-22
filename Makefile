.PHONY: generate

generate:
	mvn clean process-sources

install:
	mvn clean install

release:
	mvn clean
	mvn versions:set
	mvn deploy -Psign-artifacts