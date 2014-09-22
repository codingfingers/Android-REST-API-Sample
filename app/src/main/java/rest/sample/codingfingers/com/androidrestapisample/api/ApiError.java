package rest.sample.codingfingers.com.androidrestapisample.api;

public class ApiError {

    public enum ErrorType {IO, SERIALIZER, HTTP}

    public final ErrorType errorType;
    public final int httpErrorCode;
    private final Object tag;

    public ApiError(ErrorType errorType, int optionalHttpErrorCode, Object optionalTag) {
        this.errorType = errorType;
        this.httpErrorCode = optionalHttpErrorCode;
        this.tag = optionalTag;
    }

}
