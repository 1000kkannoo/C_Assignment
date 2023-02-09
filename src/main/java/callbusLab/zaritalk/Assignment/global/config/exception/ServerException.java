package callbusLab.zaritalk.Assignment.global.config.exception;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {

    private CustomErrorCode customErrorCode;
    private String detaliMessage;

    public ServerException(CustomErrorCode customErrorCode) {
        super(customErrorCode.getStatusMessage());
        this.customErrorCode = customErrorCode;
        this.detaliMessage = customErrorCode.getStatusMessage();
    }

    public ServerException(CustomErrorCode customErrorCode, String detaliMessage) {
        super(detaliMessage);
        this.customErrorCode = customErrorCode;
        this.detaliMessage = detaliMessage;
    }
}
