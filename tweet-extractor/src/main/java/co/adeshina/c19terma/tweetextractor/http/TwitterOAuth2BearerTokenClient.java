package co.adeshina.c19terma.tweetextractor.http;

class TwitterOAuth2BearerTokenClient {

    private final String BEARER_TOKEN_URL = "";

    private final String consumerSecret;
    private final String consumerKey;

    private String bearerToken;

    public TwitterOAuth2BearerTokenClient(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public String token() {
        return bearerToken == null ? refreshToken() : bearerToken;
    }

    // todo: only called if old token has expired. i.e HTTP response code = 401 and error.code = 89
    public String refreshToken() {
        // todo: obtain a new Token....


        // todo encode client credentials in Base64
        //      see https://developer.twitter.com/en/docs/basics/authentication/oauth-2-0/application-only

        bearerToken = "";

        return bearerToken;
    }
}
