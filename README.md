# Introduction
This is a Java API client for communicating with the [Nuts Node](https://github.com/nuts-foundation/nuts-node),
the reference implementation of the [Nuts Specification](https://nuts-foundation.gitbook.io/).
It is generated from the latest version of the OpenAPI specifications of the Nuts Node REST API.

# Installation
Maven (look up the latest version from Maven central, TODO: add link):

```xml
<dependency>
    <groupId>nl.reinkrul.nuts</groupId>
    <artifactId>java-client</artifactId>
    <version>latest</version>
</dependency>
```

# Usage
The example below instantiates the API client for VDR and calls `getDID` for `subjectDID`:
```java
var apiClient = new nl.reinkrul.nuts.client.ApiClient();
apiClient.setBasePath("http://localhost:1323");

var didApi = new nl.reinkrul.nuts.client.vdr.DidApi(apiClient);
var didDocument = didApi.getDID(subjectDID, null, null);

// do something with the resolved DID Document
```

Since each module in the Nuts Node has its own OpenAPI specification, there is a client API generated for each of them.
You can find in their own subpackage in `nl.reinkrul.nuts` (e.g. `nl.reinkrul.nuts.vdr`).

# Development

Run `make`:

1. Fetches the latest version of the OpenAPI specifications.
2. Generates the API client.
3. Builds it and installs it in the local Maven repository.
