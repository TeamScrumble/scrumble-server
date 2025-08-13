package com.project.scrumbleserver.global.error

interface ErrorCode {
    val code: String
    val description: String
}

enum class CommonError(
    override val code: String,
    override val description: String
): ErrorCode {
    NOT_FOUND_MEMBER("C001", "존재하지 않는 회원입니다.")
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
    NOT_FOUND_PRODUCT_BACKLOG("P001", "존재하지 않는 프로덕트 백로그입니다."),
    // "존재하지 않은 멤버가 포함되어 있습니다."
    NOT_FOUND_ASSIGNEE("P002", "존재하지 않는 담당자입니다."),
}