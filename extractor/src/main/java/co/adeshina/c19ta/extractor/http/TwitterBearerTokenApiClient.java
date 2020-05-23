package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;

// todo:: javadoc
public interface TwitterBearerTokenApiClient {

    // todo: returns the Oauth 2 bearer token..
    String token() throws ApiClientException;

    // todo: only called if old token has expired. i.e HTTP response code = 401 and error.code = 89
    String refreshToken() throws ApiClientException;
}
