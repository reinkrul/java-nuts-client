package nl.reinkrul.nuts.discovery;

import jakarta.ws.rs.core.GenericType;
import nl.reinkrul.nuts.ApiClient;
import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.ApiResponse;
import nl.reinkrul.nuts.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscoveryApi extends BaseDiscoveryApi {

    private final ApiClient apiClient;

    public DiscoveryApi(ApiClient apiClient) {
        super(apiClient);
        this.apiClient = apiClient;
    }

    public ApiResponse<List<SearchResult>> searchPresentationsWithHttpInfo(String serviceID, Map<String, String> query) throws ApiException {
        // Check required parameters
        if (serviceID == null) {
            throw new ApiException(400, "Missing the required parameter 'serviceID' when calling searchPresentations");
        }

        // Path parameters
        String localVarPath = "/internal/discovery/v1/{serviceID}"
                .replaceAll("\\{serviceID}", apiClient.escapeString(serviceID));

        // Query parameters
        // FIX: free form query parameters (style: form, explode: true) generates invalid query parameters
        //      when using Jersey (ends up being {key=value}). It's fixed for Okhttp (https://github.com/OpenAPITools/openapi-generator/issues/19225),
        //      but not yet for Jersey.
        List<Pair> localVarQueryParams = query.entrySet().stream().map(entry -> new Pair(entry.getKey(), entry.getValue())).collect(Collectors.toList());

        String localVarAccept = apiClient.selectHeaderAccept("application/json", "application/problem+json");
        String localVarContentType = apiClient.selectHeaderContentType();
        String[] localVarAuthNames = new String[] {"jwtBearerAuth"};
        GenericType<List<SearchResult>> localVarReturnType = new GenericType<List<SearchResult>>() {};
        return apiClient.invokeAPI("DiscoveryApi.searchPresentations", localVarPath, "GET", localVarQueryParams, null,
                new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                localVarAuthNames, localVarReturnType, false);
    }
}
