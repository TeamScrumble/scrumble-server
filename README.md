# Scrumble Server 🌀

Scrumble은 스크럼(Scrum) 프로세스를 관리하기 위한 백엔드 서버입니다.  
이 서버는 프로젝트 등록부터 백로그, 스프린트 관리까지의 흐름을 지원합니다.

## 📦 주요 기능

- 프로젝트 등록
- 제품 백로그(Product Backlog) 등록
- 여러 제품 백로그를 선택하여 스프린트 생성
- 스프린트 백로그(Sprint Backlog)로 제품 백로그 관리
- 태그(Tag)를 통한 백로그 분류
- Swagger를 통한 API 문서 제공

## 🛠️ 기술 스택


![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?logo=Kotlin&style=flat)
![SpringBoot](https://img.shields.io/badge/Spring_Boot-3.3.10-6DB33F?logo=Spring-Boot&style=flat)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=Gradle&style=flat)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?logo=Hibernate&style=flat)
![MySQL](https://img.shields.io/badge/Database-MySQL_8.3-4479A1?logo=MySQL&style=flat)
![Swagger](https://img.shields.io/badge/Swagger_SpringDoc-2.2.0-%23009688?logo=Swagger&style=flat)

## 📂 프로젝트 구조

```
scrumble-server/
├── src
│   ├── main
│   │   ├── kotlin
│   │   │   └── com/project/scrumbleserver
│   │   │       ├── controller
│   │   │       ├── domain
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       └── ScrumbleServerApplication.kt
│   │   └── resources
├── build.gradle.kts
└── README.md
```

## 📖 API 문서

SpringDoc 기반의 Swagger UI가 내장되어 있습니다.  
서버 실행 후 아래 주소에서 API 문서를 확인할 수 있습니다.

```
http://localhost:8080/swagger-ui.html
```

## ✅ 테스트

- Kotest / JUnit 도입 예정
- Testcontainers를 활용한 통합 테스트 환경 계획 중
