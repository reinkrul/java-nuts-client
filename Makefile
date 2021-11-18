.PHONY: all

all: regenerate build-install

regenerate:
	mvn clean generate-sources

build-install:
	cd generated && mvn install