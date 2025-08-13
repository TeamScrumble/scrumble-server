package com.project.scrumbleserver.global.error

enum class ProjectErrorCode(
    val code: String,
    val description: String
) {
    // 공통
    MEMBER_NOT_FOUND_MSG("COMMON_001", "존재하지 않는 회원입니다."),
    PROJECT_NOT_FOUND_MSG("COMMON_002", "존재하지 않는 프로젝트입니다."),
    NOT_PROJECT_MEMBER_MSG("COMMON_003", "프로젝트에 속한 회원이 아닙니다."),
    MUST_HAVE_AT_LEAST_ONE_OWNER_MSG("COMMON_004", "프로젝트에 최소 한 명 이상의 OWNER 권한이 존재해야 합니다."),
    INSUFFICIENT_PROJECT_PERMISSION_MSG("COMMON_005", "해당 작업을 수행할 권한이 없습니다."),
}