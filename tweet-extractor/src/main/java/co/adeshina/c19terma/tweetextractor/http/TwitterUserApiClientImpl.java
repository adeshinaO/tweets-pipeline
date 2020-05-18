package co.adeshina.c19terma.tweetextractor.http;

import co.adeshina.c19terma.tweetextractor.exception.ApiClientException;
import co.adeshina.c19terma.tweetextractor.http.dto.ErrorsDto;
import co.adeshina.c19terma.tweetextractor.http.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TwitterUserApiClientImpl implements TwitterUserApiClient {

    private String USER_DATA_URL = "https://api.twitter.com/1.1/users/show.json";
    private final ObjectMapper mapper = new ObjectMapper();

    private final TwitterBearerTokenApiClient bearerTokenApiClient;
    private final OkHttpClient httpClient;

    public TwitterUserApiClientImpl(TwitterBearerTokenApiClient bearerTokenApiClient, OkHttpClient httpClient) {
        this.bearerTokenApiClient = bearerTokenApiClient;
        this.httpClient = httpClient;
    }

    @Override
    public UserDto findUser(String id) throws ApiClientException {

        USER_DATA_URL += USER_DATA_URL + "?user_id=" + id;

        String authToken = bearerTokenApiClient.token();

        // todo: use in loop?
        Request request = new Request.Builder()
                .url(USER_DATA_URL)
                .header("Authorization", "Bearer " + authToken)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            boolean failedRequest = !response.isSuccessful();

            if (failedRequest && Integer.toString(response.code()).equals("401")) {

                authToken = bearerTokenApiClient.refreshToken();

                // todo: So what happens if this call is also unsuccessful?
                //       Rework this, use a while-loop to try requests twice.
                response = httpClient.newCall(request).execute();
                responseBody = response.body();

            } else if (failedRequest & responseBody != null) {
                ErrorsDto errors = mapper.readValue(responseBody.string(), ErrorsDto.class);
                throw new ApiClientException(errors.getErrors().get(0).toString());
            } else {
                throw new ApiClientException(response.toString());
            }

            return mapper.readValue(responseBody.string(), UserDto.class);

        } catch (IOException e) {
            throw new ApiClientException("Failed to retrieve user with ID: " + id, e);
        }
    }
}
