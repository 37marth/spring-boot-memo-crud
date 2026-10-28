# Spring Boot Memo CRUD

메모 등록·조회·수정·삭제 API를 구현한 개인 학습 프로젝트입니다.

> 최초 CRUD 코드는 AI 코드 생성 없이 직접 작성했고, 이후 AI의 코드 리뷰를 참고해 직접 수정했습니다. 설정 분리와 README 정리에는 AI의 도움을 받았습니다.

## 기술 스택

Java 17 · Spring Boot 4.1.2 · Spring MVC · Spring Data JPA · MySQL · Gradle · Lombok

## API

기본 주소: `http://localhost:8080`

| 메서드 | 경로 | 기능 | 성공 응답 |
|--------|------|------|-----------|
| GET | `/api/memos` | 전체 조회 | 200 |
| GET | `/api/memos/{id}` | 단건 조회 | 200 |
| POST | `/api/memos` | 등록 | 201 |
| PATCH | `/api/memos/{id}` | 부분 수정 | 200 |
| DELETE | `/api/memos/{id}` | 삭제 | 200 |

- 요청·응답은 `MemoDto`(`id`, `title`, `content`)를 사용합니다.
- 단건 조회·수정·삭제에서 ID가 없으면 404를 반환합니다.
- 등록 시 `id`는 DB가 생성합니다. 요청에 포함해도 무시합니다.
- PATCH는 전달한 필드만 수정합니다. 생략하거나 `null`로 보낸 필드는 기존 값을 유지합니다.

요청 흐름: `MemoApiController` → `MemoService` → `MemoRepository` → DB

## 실행 방법

1. JDK 17과 실행 중인 MySQL을 준비하고 DB를 만듭니다.
   ```sql
   CREATE DATABASE memo CHARACTER SET utf8mb4;
   ```
2. 예시 파일을 복사해 DB 주소·계정·비밀번호를 입력합니다. (이 파일은 Git에서 제외됩니다.)
   ```bash
   cp application-local.properties.example application-local.properties
   ```
   설정 파일 없이 실행하려면 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 환경변수를 지정합니다.
3. 프로젝트 루트에서 실행합니다. (Windows는 `gradlew.bat bootRun`)
   ```bash
   ./gradlew bootRun
   ```

`ddl-auto=update`로 설정되어 있으므로 학습용 DB에서 실행하세요.

## 남아 있는 한계

- 컨트롤러에 `@Valid`를 적용하지 않아 입력값 검증이 없습니다. `MemoRequest`, `MemoResponse`는 아직 연결하지 않았습니다.
- 테스트는 `contextLoads` 1건뿐이며 MySQL 연결이 필요합니다. CRUD 동작 테스트는 없습니다.
- 인증, 페이지네이션, 공통 오류 응답 형식은 구현하지 않았습니다.
