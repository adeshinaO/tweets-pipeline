package co.adeshina.c19terma.tweetextractor.http;

import co.adeshina.c19terma.tweetextractor.exception.ApiClientException;
import co.adeshina.c19terma.tweetextractor.http.dto.ErrorsDto;
import co.adeshina.c19terma.tweetextractor.http.dto.TweetDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class TwitterFilteredStreamApiApiClientImpl implements TwitterFilteredStreamApiClient {

    private final String RULES_URL = "";
    private final String STREAM_URL = "https://api.twitter.com/labs/1/tweets/stream/filter";

    private final ObjectMapper mapper = new ObjectMapper();

    private final TwitterBearerTokenApiClient bearerTokenApiClient;
    private final OkHttpClient httpClient;

    private String bearerToken;

    public TwitterFilteredStreamApiApiClientImpl(
            TwitterBearerTokenApiClient bearerTokenApiClient,
            OkHttpClient httpClient) {
        this.bearerTokenApiClient = bearerTokenApiClient;
        this.httpClient = httpClient;
    }

    @Override
    public void resetRules(List<String> rules) throws ApiClientException {

        // todo: first delete all existing rules..

        // todo: build two requests and call this method.
        try {
            execute(null);
        } catch (IOException e) {
            throw new ApiClientException(null, e); // todo
        }

        // todo: then create new ones....

        // if error code
    }

    @Override
    public void connect(Consumer<TweetDto> tweetConsumer) throws ApiClientException {

        bearerToken = bearerTokenApiClient.token();

        Request request = new Request.Builder()
                .url(STREAM_URL)
                .header("Authorization", "Bearer " + bearerToken)
                .build();

        // Todo: need to test and perfect this impl of http streaming with a small program before testing this..
        try {

            Response response = execute(request);
            BufferedSource bufferedSource = response.body().source();
            boolean openConnection = true;

            while (openConnection) {
                if (!bufferedSource.exhausted()) {
                    TweetDto data = mapper.readValue(bufferedSource.readString(StandardCharsets.UTF_8), TweetDto.class);
                    tweetConsumer.accept(data);
                } else {
                    openConnection = false;
                }
            }
        } catch (IOException e) {
            throw new ApiClientException("Failed to establish stream connection", e);
        }
    }

    private Response execute(Request request) throws ApiClientException, IOException {

        Response response = httpClient.newCall(request).execute();
        ResponseBody responseBody = response.body();

        if (!response.isSuccessful() && Integer.toString(response.code()).equals("401")) {

            bearerToken = bearerTokenApiClient.refreshToken();
            response = httpClient.newCall(request).execute();
        } else if (responseBody != null) {

            ErrorsDto errors = mapper.readValue(responseBody.string(), ErrorsDto.class);
            throw new ApiClientException(errors.getErrors().get(0).toString());
        } else {
            throw new ApiClientException(response.toString());
        }

        return response;
    }
}
