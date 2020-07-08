package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.ErrorsDto;
import co.adeshina.c19ta.extractor.http.dto.UserDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterUserApiClientImpl implements TwitterUserApiClient {

    private Logger logger = LoggerFactory.getLogger(TwitterUserApiClient.class);

    private String userApiUrl = "https://api.twitter.com/1.1/users/show.json";
    private final ObjectMapper mapper = new ObjectMapper();

    private final TwitterBearerTokenApiClient bearerTokenApiClient;
    private final OkHttpClient httpClient;

    public TwitterUserApiClientImpl(TwitterBearerTokenApiClient bearerTokenApiClient, OkHttpClient httpClient) {
        this.bearerTokenApiClient = bearerTokenApiClient;
        this.httpClient = httpClient;
    }

    @Override
    public UserDto findUser(String id) throws ApiClientException {

        userApiUrl += userApiUrl + "?user_id=" + id;
        String authToken = bearerTokenApiClient.token();
        Request request = requestHelper(id, authToken);

        try {
            Response response = httpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            boolean failedRequest = !response.isSuccessful();

            if (failedRequest && Integer.toString(response.code()).equals("401")) {

                logger.info("Bearer token has expired. Will attempt token refresh");
                authToken = bearerTokenApiClient.refreshToken();

                response = httpClient.newCall(requestHelper(id, authToken)).execute();
                responseBody = response.body();

                if(!response.isSuccessful()) {
                    ErrorsDto errors = mapper.readValue(responseBody.string(), ErrorsDto.class);
                    throw new ApiClientException(errors.getErrors().get(0).toString());
                }

            } else if (failedRequest & responseBody != null) {
                ErrorsDto errors = mapper.readValue(responseBody.string(), ErrorsDto.class);
                throw new ApiClientException(errors.getErrors().get(0).toString());
            } else {
                throw new ApiClientException(response.toString());
            }

            logger.info("Found user with id: " + id);
            return mapper.readValue(responseBody.string(), UserDto.class);

        } catch (IOException e) {
            throw new ApiClientException("Failed to retrieve user with ID: " + id, e);
        }
    }

    private Request requestHelper(String userId, String authToken) {
        userApiUrl += userApiUrl + "?user_id=" + userId;

        return new Request.Builder()
                .url(userApiUrl)
                .header("Authorization", "Bearer " + authToken)
                .build();
    }
}
