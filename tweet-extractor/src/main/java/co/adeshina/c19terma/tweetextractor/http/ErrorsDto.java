package co.adeshina.c19terma.tweetextractor.http;

import java.util.List;

public class ErrorsDto {

    private List<Error> errors;

    // todo: whenever twitter return 40X, map response body to this

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public static class Error {
        String message;
        int code;
    }
}
