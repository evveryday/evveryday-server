package evveryday.evveryday.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 에러
    INTERNAL_SERVER_ERROR(500,"C001","서버 에러입니다."),
    INVALID_REQUEST_ERROR(400,"C002","잘못된 요청입니다."),
    AUTHENTICATION_REQUIRED(401, "C003", "인증이 필요합니다."),
    ACCESS_DENIED(403, "C002", "권한이 없는 사용자입니다."),
    IMAGE_FILE_MISSING(400, "P001", "상품의 사진 파일을 입력해주세요.");

    private final int status;
    private final String code;
    private final String message;
}
