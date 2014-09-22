package rest.sample.codingfingers.com.androidrestapisample.api;

public class ApiResponse<T> {

    private final T value;
    private final int httpCode;
    private final ApiError error;
    private final Object tag;

    public ApiResponse(T value, int httpCode, ApiError error, Object tag) {
        this.value = value;
        this.httpCode = httpCode;
        this.error = error;
        this.tag = tag;
    }

    public boolean hasError() {
        return error != null;
    }

    public T getBody() {
        return value;
    }

    public ApiError getError() {
        return error;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public Object getTag() {
        return tag;
    }

}
