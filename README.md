# Spring Boot Memo CRUD

메모 등록·조회·수정·삭제 API를 구현한 개인 학습 프로젝트입니다. Java와 Spring Boot로 CRUD를 직접 작성하면서 요청 처리, DTO 변환, JPA 저장, HTTP 응답의 흐름을 점검했습니다.

**최초 CRUD 코드는 AI 코드 생성 없이 독립적으로 작성했습니다. 이후 AI의 코드 리뷰와 피드백을 참고해 문제를 확인하고, 여러 차례 직접 수정했습니다.** 이 저장소는 그 과정을 거친 현재 코드를 보존합니다. 공개를 위한 설정 분리와 README 정리에는 AI의 도움을 받았습니다.

## 기술 스택

| 구분 | 사용 기술 |
| --- | --- |
| 언어 | Java 17 |
| 프레임워크 | Spring Boot **4.1.2** |
| 웹 / 데이터 접근 | Spring MVC, Spring Data JPA |
| 데이터베이스 | MySQL |
| 빌드 | Gradle Wrapper 9.8.2 |
| 기타 | Lombok, Jakarta Validation 의존성 |

Spring Boot 버전은 `build.gradle`의 실제 설정을 기준으로 작성했습니다.

## 주요 기능

- 메모 전체 조회와 ID로 단건 조회
- 제목(`title`), 내용(`content`)을 받아 메모 등록
- DB에서 ID 자동 생성
- PATCH로 제목 또는 내용 부분 수정
- 메모 삭제
- 조회·수정·삭제할 대상이 없으면 HTTP 404 반환
- 조회·등록·수정 결과를 `MemoDto`로 응답

## 프로젝트 구조

```text
.
├── build.gradle
├── gradlew / gradlew.bat
├── gradle/wrapper/
├── application-local.properties.example
└── src/
    ├── main/
    │   ├── java/com/example/memo/
    │   │   ├── MemoApplication.java
    │   │   ├── controller/MemoApiController.java
    │   │   ├── service/MemoService.java
    │   │   ├── repository/MemoRepository.java
    │   │   ├── entity/Memo.java
    │   │   └── dto/
    │   │       ├── MemoDto.java
    │   │       ├── request/MemoRequest.java
    │   │       └── response/MemoResponse.java
    │   └── resources/
    │       ├── application.properties
    │       └── data.sql
    └── test/java/com/example/memo/MemoApplicationTests.java
```

요청은 `MemoApiController → MemoService → MemoRepository → DB` 순서로 처리합니다. 서비스에서 Entity를 DTO로 변환하고 컨트롤러가 상태 코드와 응답을 반환합니다. 현재 API는 요청과 응답 모두 `MemoDto`를 사용하며, `MemoRequest`와 `MemoResponse`는 아직 연결하지 않았습니다.

## API 엔드포인트

기본 주소: `http://localhost:8080`

| 메서드 | 경로 | 기능 | 성공 응답 |
| --- | --- | --- | --- |
| GET | `/api/memos` | 전체 조회 | 200, DTO 배열 |
| GET | `/api/memos/{id}` | 단건 조회 | 200, DTO |
| POST | `/api/memos` | 등록 | 201, 생성된 DTO |
| PATCH | `/api/memos/{id}` | 부분 수정 | 200, 수정된 DTO |
| DELETE | `/api/memos/{id}` | 삭제 | 200, 본문 없음 |

단건 조회·수정·삭제에서 해당 ID가 없으면 404를 반환합니다. 전체 조회 결과가 없으면 빈 배열을 반환합니다.

등록 요청 예시:

```http
POST /api/memos
Content-Type: application/json

{
  "title": "CRUD 학습",
  "content": "메모 등록부터 구현하기"
}
```

등록 시 `id`는 전달할 필요가 없습니다. 요청 본문에 ID가 있더라도 Entity를 생성할 때 `null`로 설정하고, DB가 새 ID를 생성합니다.

부분 수정 요청 예시:

```http
PATCH /api/memos/1
Content-Type: application/json

{
  "content": "부분 수정 동작 확인하기"
}
```

위 요청은 제목을 유지하고 내용만 바꿉니다. 수정 대상은 경로의 ID로 정하며 본문의 ID는 사용하지 않습니다. 생략한 필드와 명시적으로 `null`을 보낸 필드는 모두 기존 값을 유지하므로, 현재 방식으로는 필드 값을 `null`로 지울 수 없습니다.

## 실행 방법과 DB 설정

### 1. 준비

- JDK 17
- 실행 중인 MySQL 서버와 접속 가능한 DB 계정
- 최초 빌드 시 Gradle과 의존성을 내려받을 네트워크 연결

```shell
git clone https://github.com/37marth/spring-boot-memo-crud.git
cd spring-boot-memo-crud
```

MySQL에 접속한 뒤 사용할 데이터베이스를 준비합니다. 아래 이름은 실행 예시입니다.

```sql
CREATE DATABASE memo CHARACTER SET utf8mb4;
```

### 2. 로컬 전용 설정 작성

프로젝트 루트에서 예시 파일을 복사합니다.

Windows PowerShell:

```powershell
Copy-Item application-local.properties.example application-local.properties
```

macOS / Linux:

```shell
cp application-local.properties.example application-local.properties
```

복사한 `application-local.properties`를 편집해 DB 주소·계정·비밀번호를 입력합니다. 이 파일은 Git에서 제외되며, `src/main/resources` 밖에 두어 애플리케이션 JAR에도 포함하지 않습니다. 실제 접속정보를 예시 파일이나 README에 적지 않습니다.

로컬 설정 파일 없이 실행할 때는 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 환경변수를 지정합니다. `DB_URL`의 기본값은 `jdbc:mysql://localhost:3306/memo`입니다. 로컬 설정 파일이 있으면 그 안의 DB 설정을 환경변수 자리표시자가 있는 기본 설정보다 우선 사용합니다.

현재 `ddl-auto=update`로 설정되어 있어 실행 시 Entity에 맞춰 테이블을 생성·갱신합니다. 개인 학습용 DB에서 실행합니다. `data.sql`에는 예시 INSERT 한 건이 있지만, 현재 MySQL 설정에서는 SQL 초기화를 별도로 활성화하지 않았으므로 자동으로 데이터가 들어간다고 가정하지 않습니다.

### 3. 프로젝트 루트에서 실행

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

macOS / Linux:

```shell
chmod +x gradlew
./gradlew bootRun
```

실행 후 `http://localhost:8080/api/memos`로 조회할 수 있습니다. 로컬 설정의 경로가 현재 작업 폴더 기준이므로 프로젝트 루트에서 실행합니다.

### 4. 빌드와 기본 테스트

```powershell
.\gradlew.bat build
```

macOS / Linux에서는 `./gradlew build`를 사용합니다. 기존 테스트는 `@SpringBootTest` 기반의 `contextLoads` 한 건이며, 실제 MySQL 연결이 필요합니다. CRUD 동작을 검증하는 자동화 테스트는 아직 작성하지 않았습니다.

공개 준비 과정에서는 Java 17에서 `clean build`와 기존 테스트 1건의 통과를 확인했습니다. 이때 `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`, `SPRING_SQL_INIT_MODE=never`로 테이블 변경과 초기 데이터 입력을 비활성화했습니다. CRUD 전체 동작을 자동 검증했다는 뜻은 아닙니다.

## 문제를 확인하고 개선한 과정

아래 내용은 구현 중 경험한 문제와 AI 리뷰·피드백을 참고해 직접 수정한 결과입니다. 현재 소스에서 확인할 수 있는 동작을 함께 정리했습니다.

| 문제 | 수정한 내용과 현재 동작 |
| --- | --- |
| 신규 등록 API에 불필요한 ID 경로 변수가 있었음 | `POST /api/memos`로 등록하고, Entity의 ID를 `null`로 생성해 `GenerationType.IDENTITY`로 DB에서 자동 생성하도록 수정 |
| 응답에 필요한 정보를 DTO로 전달할 필요가 있었음 | `id`, `title`, `content`를 담은 `MemoDto`로 변환해 조회·등록·수정 응답에 사용 |
| 존재하지 않는 데이터에 대한 처리가 필요했음 | `findById().orElseThrow(...)`와 `ResponseStatusException`으로 단건 조회·수정·삭제에 404 적용 |
| PATCH에서 전달하지 않은 필드가 `null`로 덮어써졌음 | `Memo.patch()`에서 값이 `null`이 아닌 필드만 변경하도록 수정 |
| 부분 수정에서 ID 검증과 입력 조건이 충돌했음 | 수정 대상은 경로 ID로 조회하고, 본문의 ID를 검증하지 않으며 제목·내용 중 전달된 값만 반영 |

부분 수정의 충돌을 정리했지만, 생성 요청과 수정 요청에 맞춘 입력 검증 규칙을 완성한 상태는 아닙니다.

## 학습하면서 알게 된 점

- 등록 시에는 클라이언트가 정한 ID 대신 DB가 생성한 ID를 사용하도록 Entity를 만들어야 한다는 점
- Entity 저장과 API 응답 DTO 변환은 각각 어떤 정보를 다루는지 구분해야 한다는 점
- 데이터를 찾지 못했을 때 예외를 던지는 것뿐 아니라, 클라이언트가 이해할 HTTP 상태 코드를 정해야 한다는 점
- PATCH에서는 전달하지 않은 값까지 덮어쓰지 않도록 변경할 필드를 구분해야 한다는 점
- 생성과 부분 수정은 필요한 입력 조건이 달라 검증 규칙을 구분해야 한다는 점

## 남아 있는 한계

- 실제 컨트롤러는 `MemoDto`를 받으며 `@Valid`를 적용하지 않았습니다. `MemoRequest`의 `@NotBlank`는 현재 API 요청을 검증하지 않으므로 빈 제목 등 입력값 검증이 남아 있습니다.
- 요청·응답용으로 만든 `MemoRequest`, `MemoResponse`가 사용되지 않습니다. 학습 기록을 보존하기 위해 그대로 두었습니다.
- 인증·사용자별 데이터 구분, 페이지네이션, 공통 오류 응답 형식은 구현하지 않았습니다.
- CRUD 성공·실패와 PATCH 동작을 확인하는 자동화 테스트가 없습니다.
- `ddl-auto=update`를 사용하며 별도의 DB 마이그레이션 관리는 없습니다.

현재 범위는 백엔드 기초를 점검하는 메모 CRUD API입니다. 배포나 성능 개선 성과를 검증한 프로젝트는 아닙니다.
