package co.adeshina.c19terma.tweetextractor.http;

import co.adeshina.c19terma.tweetextractor.http.dto.UserDto;

/**
 *
 */
public interface TwitterUserApiClient {

    /**
     *
     * @param id
     * @return
     */
    UserDto findUser(String id);
}
