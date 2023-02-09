package callbusLab.zaritalk.Assignment.global.config.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private CustomErrorCode customErrorCode;
    private String detaliMessage;

    public CustomException(CustomErrorCode customErrorCode) {
        super(customErrorCode.getStatusMessage());
        this.customErrorCode = customErrorCode;
        this.detaliMessage = customErrorCode.getStatusMessage();
    }

    public CustomException(CustomErrorCode customErrorCode, String detaliMessage) {
        super(detaliMessage);
        this.customErrorCode = customErrorCode;
        this.detaliMessage = detaliMessage;
    }
}
