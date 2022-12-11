.PHONY: all

all: regenerate build-install vet

regenerate:
	mvn clean generate-sources

vet:
	mvn surefire:test

build-install:
	cd generated && mvn install