package exceptions;

public class ErrorResponse {
    private String errorMessage;
    private Integer errorCode;
    private String url;

    public ErrorResponse(String message, Integer code, String url) {
        this.errorMessage= message;
        this.errorCode = code;
        this.url = url;
    }
}
