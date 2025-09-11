package com.project.scrumbleserver.global.error

interface ErrorCode {
    val code: String
    val description: String
}

object InternalServerError : ErrorCode {
    override val code: String = "S001"
    override val description: String = "서버에서 예상하지 못한 문제가 발생했습니다."
}

enum class CommonError(
    override val code: String,
    override val description: String
) : ErrorCode {
    INVALID_PARAMETER("C001", "잘못된 요청 파라미터입니다."),
    INVALID_FORMAT("C002", "잘못된 요청 형식입니다."),
    MISSING_PARAMETER("C003", "필수 요청 파라미터가 누락되었습니다."),
    IMAGE_UPLOAD_FAILED("C004", "이미지 업로드에 실패했습니다."),
}

enum class AuthError(
    override val code: String,
    override val description: String
) : ErrorCode {
    NOT_FOUND_MEMBER("A001", "존재하지 않는 사용자 입니다."),
    FORBIDDEN("A002", "접근 권한이 없습니다."),
    UNAUTHORIZED("A003", "인증 정보가 없습니다."),
    INVALID_AUTHENTICATION("A004", "인증 정보가 잘못되었습니다."),
    TOKEN_EXPIRED("A005", "토큰이 만료되었습니다."),
}

enum class ProjectError(
    override val code: String,
    override val description: String
): ErrorCode {
    NOT_FOUND_PROJECT("P001", "존재하지 않는 프로젝트입니다."),
    IS_NOT_PROJECT_MEMBER("P002", "프로젝트에 속한 회원이 아닙니다."),
    MUST_HAVE_AT_LEAST_ONE_OWNER("P003", "프로젝트에 최소 한 명 이상의 OWNER 권한이 존재해야 합니다."),
    INSUFFICIENT_PROJECT_PERMISSION("P004", "해당 작업을 수행할 권한이 없습니다."),
    NOT_FOUND_INVITATION_CODE("P005", "초대 코드를 찾을 수 없습니다."),
}

enum class BacklogError(
    override val code: String,
    override val description: String
): ErrorCode {
    NOT_FOUND_PRODUCT_BACKLOG("B001", "존재하지 않는 프로덕트 백로그입니다."),
    // "존재하지 않은 멤버가 포함되어 있습니다."
    NOT_FOUND_ASSIGNEE("B002", "존재하지 않는 담당자입니다."),
}

enum class TagError(
    override val code: String,
    override val description: String
): ErrorCode {
    NOT_FOUND_TAG("T001", "존재하지 않는 태그입니다."),
    NOT_FOUND_TAGS("T002", "존재하지 않는 태그가 포함되어 있습니다."),
}