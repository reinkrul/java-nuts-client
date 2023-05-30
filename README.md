[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.reinkrul.nuts/java-client/badge.svg?style=flat)](https://search.maven.org/artifact/nl.reinkrul.nuts/java-client)

# Introduction
This is a Java API client for communicating with the [Nuts Node](https://github.com/nuts-foundation/nuts-node),
the reference implementation of the [Nuts Specification](https://nuts-foundation.gitbook.io/).
It is generated from the latest version of the OpenAPI specifications of the Nuts Node REST API.

# Installation

## Maven
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.reinkrul.nuts/java-client/badge.svg?style=flat)](https://search.maven.org/artifact/nl.reinkrul.nuts/java-client)

```xml
<dependency>
    <groupId>nl.reinkrul.nuts</groupId>
    <artifactId>java-client</artifactId>
    <version>5.3.0-rc.2</version>
</dependency>
```

Find all versions on [Maven central](https://search.maven.org/artifact/nl.reinkrul.nuts/java-client).

# Usage
The example below instantiates the API client for VDR and calls `getDID` for `subjectDID`:
```java
var apiClient = new nl.reinkrul.nuts.ApiClient();

var didApi = new nl.reinkrul.nuts.vdr.DidApi(apiClient);
var didDocument = didApi.getDID(subjectDID, null, null);

// do something with the resolved DID Document
```

Since each module in the Nuts Node has its own OpenAPI specification, there is a client API generated for each of them.
You can find in their own subpackage in `nl.reinkrul.nuts` (e.g. `nl.reinkrul.nuts.vdr`).

# Examples

See [src/test/java/CredentialExamples.java](src/test/java/CredentialExamples.java)
for how to issue `NutsOrganizationCredential`, `NutsAuthenticationCredential` and `NutsEmployeeCredential`.

# Versioning

This library will follow the major versioning of the Nuts Node. So if Nuts Node `1.x.x` is released, this library will follow with a `1.y.z` release.
Minor and patch versions will not follow the Nuts Node and are intended for features/bugfixes of the library.

# Development

Run `make`:

1. Fetches the latest version of the OpenAPI specifications.
2. Generates the API client.

# Publishing

1. Update `nuts.version` version in `pom.xml`.
2. Run `make`
3. Update artifact version to next major/minor (to `x.y.z`) and then release.
   ```shell
   make release
   ```

5. Log in to [Sonatype](https://s01.oss.sonatype.org/) and close & release staging repository (see https://central.sonatype.org/publish/publish-maven/).
6. Update dependency version in `pom.xml` to the new version, update examples if required, commit and push.
