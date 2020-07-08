package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.ErrorsDto;
import co.adeshina.c19ta.extractor.http.dto.RulesDto;
import co.adeshina.c19ta.extractor.http.dto.TweetDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import okio.BufferedSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterFilteredStreamApiApiClientImpl implements TwitterFilteredStreamApiClient {

    private Logger logger = LoggerFactory.getLogger(TwitterFilteredStreamApiClient.class);

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private static final String RULES_URL = "https://api.twitter.com/labs/1/tweets/stream/filter/rules";
    private static final String STREAM_URL = "https://api.twitter.com/labs/1/tweets/stream/filter";

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
    public void resetRules(String rulesJson) throws ApiClientException {

        bearerToken = bearerTokenApiClient.token();

        Request request = new Request.Builder()
                .url(RULES_URL)
                .header("Authorization", "Bearer " + bearerToken)
                .build();

        try {

            Response response = execute(request);
            RulesDto rulesDto = mapper.readValue(response.body().string(), RulesDto.class);

            // Delete any existing rules.
            if (!rulesDto.getData().isEmpty()) {

                List<String> ids = new ArrayList<>();
                for (RulesDto.Rule rule : rulesDto.getData()) {
                    ids.add(rule.getId());
                }

                RequestBody requestBody = RequestBody.create("{\n"
                        + "  \"delete\": {\n"
                        + "    \"ids\":" + ids.toString() + "\n"
                        + "  }\n"
                        + "}", JSON_MEDIA_TYPE);

                request = new Request.Builder()
                        .url(RULES_URL)
                        .header("Authorization", "Bearer " + bearerToken)
                        .delete(requestBody)
                        .build();

                execute(request);
            }

            RequestBody requestBody = RequestBody.create(rulesJson, JSON_MEDIA_TYPE);
            request = new Request.Builder()
                    .url(RULES_URL)
                    .header("Authorization", "Bearer " + bearerToken)
                    .post(requestBody)
                    .build();

            execute(request);

        } catch (IOException e) {
            throw new ApiClientException("Failed to establish connection to API", e);
        }
    }

    @Override
    public void connect(Consumer<TweetDto> tweetConsumer) throws ApiClientException {

        bearerToken = bearerTokenApiClient.token();

        Request request = new Request.Builder()
                .url(STREAM_URL)
                .header("Authorization", "Bearer " + bearerToken)
                .build();

        try {

            Response response = execute(request);
            BufferedSource bufferedSource = response.body().source();
            boolean openConnection = true;

            while (openConnection) {
                if (!bufferedSource.exhausted()) {
                    String data = bufferedSource.readString(StandardCharsets.UTF_8);
                    logger.info("New data from stream: " + data);
                    tweetConsumer.accept(mapper.readValue(data, TweetDto.class));
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

            logger.info("Bearer token has expired. Will attempt a token refresh");
            bearerToken = bearerTokenApiClient.refreshToken();
            response = httpClient.newCall(request).execute();

            if(!response.isSuccessful()){
                ErrorsDto errors = mapper.readValue(responseBody.string(), ErrorsDto.class);
                throw new ApiClientException(errors.getErrors().get(0).toString());
            }

        } else if (!response.isSuccessful() && responseBody != null) {
            ErrorsDto errors = mapper.readValue(responseBody.string(), ErrorsDto.class);
            throw new ApiClientException(errors.getErrors().get(0).toString());
        } else {
            throw new ApiClientException(response.toString());
        }

        return response;
    }
}
