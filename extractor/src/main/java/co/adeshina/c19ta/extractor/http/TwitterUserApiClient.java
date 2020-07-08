package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.UserDto;

public interface TwitterUserApiClient {
    UserDto findUser(String id) throws ApiClientException;
}
