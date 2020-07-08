package co.adeshina.c19ta.extractor.exception;

public class ApiClientException extends Exception {

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiClientException(String message) {
        super(message);
    }
}
