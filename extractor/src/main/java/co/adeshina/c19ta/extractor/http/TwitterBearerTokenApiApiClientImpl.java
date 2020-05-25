package co.adeshina.c19ta.extractor.http;

import co.adeshina.c19ta.extractor.exception.ApiClientException;
import co.adeshina.c19ta.extractor.http.dto.BearerTokenDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TwitterBearerTokenApiApiClientImpl implements TwitterBearerTokenApiClient {

    private final static String BEARER_TOKEN_URL = "https://api.twitter.com/oauth/token";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private final ObjectMapper mapper = new ObjectMapper();

    private final String consumerSecret;
    private final String consumerKey;
    private final OkHttpClient httpClient;

    private String bearerToken;

    public TwitterBearerTokenApiApiClientImpl(String consumerKey, String consumerSecret, OkHttpClient httpClient) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.httpClient = httpClient;
    }

    @Override
    public String token() throws ApiClientException {
        return bearerToken == null ? refreshToken() : bearerToken;
    }

    @Override
    public String refreshToken() throws ApiClientException {

        String urlEncodedKey;
        String urlEncodedSecret;

        try {
            urlEncodedSecret = URLEncoder.encode(consumerSecret, "utf-8");
            urlEncodedKey = URLEncoder.encode(consumerKey, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApiClientException("Failed to obtain bearer token", e);
        }

        String bearerTokenCredentials = urlEncodedKey + ":" + urlEncodedSecret;
        bearerTokenCredentials = Base64.getEncoder().encodeToString(bearerTokenCredentials.getBytes());

        RequestBody requestBody = RequestBody.create("grant_type=client_credentials", JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(BEARER_TOKEN_URL)
                .header("Authorization", "Basic " + bearerTokenCredentials)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            ResponseBody responseBody = response.body();

            if (!response.isSuccessful() || responseBody == null) {
                throw new IOException(response.toString());
            }

            BearerTokenDto tokenDto = mapper.readValue(responseBody.string(), BearerTokenDto.class);
            bearerToken = tokenDto.getAccessToken();
        } catch (Exception e) {
            throw new ApiClientException("Failed to obtain bearer token", e);
        }

        return bearerToken;
    }
}
