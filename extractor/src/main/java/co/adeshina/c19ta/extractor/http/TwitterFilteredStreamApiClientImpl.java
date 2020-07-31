package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.DeleteRulesDto;
import co.adeshina.c19ta.extractor.http.dto.LabsErrors;
import co.adeshina.c19ta.extractor.http.dto.RulesDto;
import co.adeshina.c19ta.extractor.http.dto.TweetDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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

public class TwitterFilteredStreamApiClientImpl implements TwitterFilteredStreamApiClient {

    private final Logger logger = LoggerFactory.getLogger(TwitterFilteredStreamApiClient.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private static final String RULES_URL = "https://api.twitter.com/labs/1/tweets/stream/filter/rules";
    private static final String STREAM_URL = "https://api.twitter.com/labs/1/tweets/stream/filter?tweet.format=detailed";

    private final ObjectMapper mapper = new ObjectMapper();

    private final TwitterBearerTokenApiClient bearerTokenApiClient;
    private final OkHttpClient httpClient;

    private String bearerToken;

    public TwitterFilteredStreamApiClientImpl(
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
            if (rulesDto != null && rulesDto.getData() != null && !rulesDto.getData().isEmpty()) {

                List<String> ids = new ArrayList<>();
                for (RulesDto.Rule rule : rulesDto.getData()) {
                    ids.add(rule.getId());
                }

                DeleteRulesDto.Delete delete = new DeleteRulesDto.Delete();
                delete.setIds(ids);

                DeleteRulesDto deleteRulesDto = new DeleteRulesDto();
                deleteRulesDto.setDelete(delete);

                String json = mapper.writeValueAsString(deleteRulesDto);

                RequestBody requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);

                request = new Request.Builder()
                        .url(RULES_URL)
                        .header("Authorization", "Bearer " + bearerToken)
                        .post(requestBody)
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

        logger.info("Streaming rules reset successful");
    }

    @Override
    public void connect(Consumer<TweetDto> tweetConsumer) throws ApiClientException {

        bearerToken = bearerTokenApiClient.token();
        Request request = new Request.Builder()
                .url(STREAM_URL)
                .header("Authorization", "Bearer " + bearerToken)
                .build();

        // TODO: problem to fix now.......
        //      1. Application is running out heap space: Solutions
        //              - Work through code, make sure unneeded objects are not held in scope.
        //              - Start the program using -Xmx which guarantees maximum heap space is used.

        try {
            Response response = execute(request);
            BufferedSource bufferedSource = response.body().source();

            logger.info("Opened new streaming connection to Twitter Filtered Stream API v1");

            boolean openConnection = true;

            TweetDto.Data data;
            TweetDto dto;
            String line;
            String formatString = "New data [author_id: %s | text: %s]";

            while (openConnection) {
                if (!bufferedSource.exhausted()) {
                    line = bufferedSource.readUtf8Line();
                    dto = mapper.readValue(line, TweetDto.class);
                    data = dto.getData();
                    logger.info(String.format(formatString, data.getAuthorId(), data.getText()));
                    tweetConsumer.accept(dto);
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

        if (!response.isSuccessful()) {
            if (responseBody != null) {
                LabsErrors errorsDto = mapper.readValue(responseBody.string(), LabsErrors.class);
                throw new ApiClientException(errorsDto.getErrors().get(0).toString());
            }
            throw new ApiClientException(response.toString());
        }

        return response;
    }
}
