package callbusLab.zaritalk.Assignment.global.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomErrorCode {

    //로그인 & 로그아웃 검증
    Authentication_Entry_Point( "만료된 JWT 토큰입니다."),
    NOT_EXISTS_EMAIL("해당 계정은 존재하지 않습니다."),
    NOT_MATCHED_ID_OR_PASSWORD("아이디 또는 비밀번호를 잘못 입력하였습니다."),

    // 알수 없는 오류의 처리
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String statusMessage;
}
