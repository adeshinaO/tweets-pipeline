package co.adeshina.c19ta.extractor.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorsDto {

    private List<Error> errors;
    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {

        private String message;
        private int httpStatusCode;
        private int errorCode;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getHttpStatusCode() {
            return httpStatusCode;
        }

        public void setHttpStatusCode(int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public String toString() {
            return String.format("\nMessage: %s \nHTTP Code: %s\nError Code: %d", message, httpStatusCode, errorCode);
        }
    }
}
