package co.adeshina.c19terma.tweetextractor.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorsDto {

    private List<Error> errors;

    // todo: whenever twitter return 40X, map response body to this

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        String message;
        int httpStatusCode;
        int errorCode;

        @Override
        public String toString() {

            return "";
        }
    }
}
