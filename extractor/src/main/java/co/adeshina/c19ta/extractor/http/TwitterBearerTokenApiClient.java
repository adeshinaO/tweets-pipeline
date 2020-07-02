package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;

/**
 * Twitter API client for managing access tokens.
 */
public interface TwitterBearerTokenApiClient {

    /**
     * Obtains a new access token.
     *
     * @return a new access token.
     * @throws ApiClientException on failure to establish a connection to Twitter's API.
     */
    String token() throws ApiClientException;

    /**
     * Should be called when access tokens expire.
     *
     * @return A new access token.
     * @throws ApiClientException on failure to establish a connection to Twitter's API.
     */
    String refreshToken() throws ApiClientException;
}
