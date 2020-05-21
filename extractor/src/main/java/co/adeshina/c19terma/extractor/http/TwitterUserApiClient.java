package co.adeshina.c19terma.extractor.http;

import co.adeshina.c19terma.extractor.exception.ApiClientException;
import co.adeshina.c19terma.extractor.http.dto.UserDto;

/**
 *
 */
public interface TwitterUserApiClient {

    /**
     *
     * @param id
     * @return
     */
    UserDto findUser(String id) throws ApiClientException;
}
