package co.adeshina.c19terma.tweetextractor.http;

import co.adeshina.c19terma.tweetextractor.http.dto.UserDto;
import okhttp3.OkHttpClient;

public class TwitterUserApiClientImpl implements TwitterUserApiClient {

    private final String USER_DATA_URL = "https://api.twitter.com/1.1/users/show.json";

    private final TwitterBearerTokenApiClient bearerTokenApiClient;
    private final OkHttpClient httpClient;

    public TwitterUserApiClientImpl(TwitterBearerTokenApiClient bearerTokenApiClient, OkHttpClient httpClient) {
        this.bearerTokenApiClient = bearerTokenApiClient;
        this.httpClient = httpClient;
    }

    @Override
    public UserDto findUser(String id) {


    // todo:
    //    https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-users-show

        return new UserDto();
    }

}
