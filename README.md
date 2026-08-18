# 쿠팡형 커머스 서버 재구현 요구사항 명세

> 이 문서는 저장소의 커밋 38개(초기 커밋 포함, 실제 학습 단계 37개)를 오래된 순서로 분석해서 만든 **요구사항 명세**입니다.
> 정답 코드는 없고, "무엇을 만들어야 하는가"만 정리했습니다. 필드/시그니처 수준까지만 명시되어 있고 구현 로직은 스스로 채워야 합니다.

## 0. 프로젝트 배경

- 쿠팡 커머스를 흉내낸 예제로, 결국 **3개의 독립 서버**를 만드는 것이 목표입니다.
  - **Admin API/WEB** — 사내 MD/개발자가 상품·유저를 CRUD 하는 관리자용
  - **Internal API** — 실제 고객이 리액트 화면에서 상품을 조회하고 결제/결제취소하는 API
  - **External API** — 외부 택배사가 배송 시작/완료를 우리 서버에 알려주는 API
- 저장소는 진행되며 다음 순서로 진화합니다: 단일 모듈 → 3-Layer → DDD → Hexagonal → 멀티모듈 → 예외/검증 체계화 → 공통 응답 포맷.
- DB는 실제로 붙이지 않고 **Map 기반 인메모리 저장소**로 CRUD를 흉내냅니다 (JPA 연관관계 없음, ID 참조만 존재).

### 최종 완성형 도메인 참고표

아래 표는 최종적으로 도달하는 모습입니다. 실제로는 버전 1의 각 Step에서 점진적으로 채워집니다. 처음부터 이 표대로 만들지 말고, 각 Step 설명을 따라가며 채워나가세요.

**BaseEntity (추상 클래스, 모든 도메인이 상속)**

| 필드 | 타입 | 용도 |
|---|---|---|
| id | Integer | PK, 도메인별로 독립적인 채번(정적 카운터) |
| deleted | boolean | 소프트 삭제 플래그 |
| createdAt | LocalDateTime | 생성 시각 |
| createdBy | Integer | 생성한 유저 ID |
| updatedAt | LocalDateTime | 마지막 수정 시각 |
| updatedBy | Integer | 마지막 수정한 유저 ID |

**User extends BaseEntity**

| 필드 | 타입 | 용도 |
|---|---|---|
| name | String | 유저 이름 |
| grade | UserGrade(enum) | 회원 등급 → 포인트 적립률 결정 |
| point | int | 누적 포인트 |
| thumbnail | String | 프로필 이미지 저장 경로 |

**Product extends BaseEntity**

| 필드 | 타입 | 용도 |
|---|---|---|
| name | String | 상품명 |
| price | int | 가격 |
| stock | int | 재고 수량 |
| thumbnail | String | 상품 썸네일 이미지 저장 경로 |

**Payment extends BaseEntity**

| 필드 | 타입 | 용도 |
|---|---|---|
| productIds | List\<Integer\> | 결제에 포함된 상품 ID 목록 (다대다를 ID 리스트로 단순화) |
| status | PaymentStatus(enum) | 결제/배송 상태 |
| paidPrice | int | 결제 총액 |
| purchasedAt | LocalDateTime | 결제 완료 시각 |
| deliveredAt | LocalDateTime | 배송 완료 시각 |
| cancelledAt | LocalDateTime | 취소 완료 시각 |

**연관관계**: JPA를 쓰지 않으므로 진짜 FK 매핑은 없습니다. `Payment.createdBy`가 `User.id`를 값으로만 참조(1:N, User 1명이 여러 Payment)하고, `Payment.productIds`가 `Product.id` 목록을 값으로 참조(N:M을 ID 리스트로 단순화)합니다. 연관된 엔티티가 필요하면 그때그때 Service를 통해 ID로 재조회합니다.

---

## 버전 1 — 커밋 순서 그대로 따라가기

### Step 0 — 프로젝트 뼈대 (init)
- Spring Boot 단일 모듈 프로젝트 생성 (Java 17, Gradle). 이후 모든 개발의 출발점.

### Step 1 — 3개 서버를 위한 컨트롤러 뼈대
- **기능**: 앞으로 만들 3개 서버(Admin API/WEB, Internal API, External API)의 컨트롤러 껍데기만 패키지별로 배치합니다.
- **구조**: 아래 패키지에 빈 컨트롤러 클래스를 만드세요.
  - `controller.admin.api` — `ProductApiController`, `UserApiController` (`@RestController`)
  - `controller.admin.web` — `ProductWebController`, `UserWebController` (`@Controller`, SSR)
  - `controller.internal.api` — `ProductController`, `PaymentController` (`@RestController`)
  - `controller.external.api` — `DeliveryController` (`@RestController`)
- **검증/예외**: 없음 (아직 로직 없음).

### Step 2 — 중앙 저장소(Repository) 골격
- **기능**: 유저/상품/결제 정보를 담을 저장소를 만들고 각 컨트롤러가 주입받게 합니다.
- **신규 클래스**: `User`, `Product`, `Payment` (아직 빈 클래스), `UserRepository`, `ProductRepository`, `PaymentRepository`.
- **Repository 책임**: 각 Repository는 `@Repository` 빈으로 등록하고, 내부에 `Map<Integer, T>` 형태의 정적(static) 저장소 필드 하나를 갖습니다. (아직 CRUD 메서드는 없음)
- 각 Controller는 대응하는 Repository를 생성자 주입받습니다.

### Step 3 — 도메인 필드 + Repository CRUD
- **기능**: User/Product/Payment 각각의 실제 필드를 채우고, Repository에 기본 CRUD 메서드를 추가합니다.
- **Entity 필드**
  - `User`: `id`(Integer), `name`(String), `deleted`(boolean)
  - `Product`: `id`(Integer), `name`(String), `price`(int), `stock`(int), `deleted`(boolean)
  - `Payment`: `id`(Integer), `productIds`(List\<Integer\>), `status`(PaymentStatus enum), `paidPrice`(int), `purchasedAt`/`deliveredAt`/`cancelledAt`(LocalDateTime), `deleted`(boolean)
  - `PaymentStatus` enum: `IN_PAYMENT`, `PAYMENT_COMPLETE`, `IN_DELIVERY`, `DELIVERY_COMPLETE`, `CANCEL_COMPLETE` — 각 값은 설명 문구와 "취소 가능 여부(boolean)"를 같이 들고 있어야 합니다.
- **생성 규칙**: 각 엔티티는 `public` 생성자 대신 정적 팩토리 메서드(`create(...)`)로만 생성 가능하게 하고, ID는 도메인별 정적 카운터로 자동 채번합니다. `Payment.create()`는 `Product` 목록을 받아 `productIds`와 `paidPrice`(가격 합산)를 계산해서 채웁니다.
- **Repository 책임** (User/Product는 U 제외, Payment는 CRUD 모두):
  - `findAll()`: 전체 조회
  - `findById(id)`: 단건 조회 (Optional)
  - `create(entity)`: 단건 생성, 이미 같은 id가 있으면 예외
  - (Payment만) `update(entity)`: 단건 갱신, 없으면 예외
  - `remove(id)`: 단건 삭제, 없으면 예외
- **예외**: 이미 존재하는 id로 생성 시도 / 존재하지 않는 id로 조회·수정·삭제 시도 → 예외 발생 (아직은 범용 RuntimeException으로 충분).

### Step 4 — BaseEntity로 공통 필드 추출
- **기능**: User/Product/Payment에 중복되는 `id`, `deleted`, 생성/수정 감사(Audit) 필드를 `BaseEntity` 추상 클래스로 뽑아냅니다.
- **신규 Entity**: `BaseEntity`(추상 클래스) — 필드는 위 "최종 완성형" 표 참고. 생성자는 `id`와 "누가 생성했는지(userId)"를 받고, `updated(userId)`류 메서드로 수정 감사 필드를 갱신할 수 있어야 합니다.
- **연관관계**: `User`, `Product`, `Payment` 모두 `BaseEntity`를 상속.
- **설계 포인트**: `BaseEntity`는 필드는 있지만 `abstract class`로 선언해 직접 `new` 하지 못하게 막아야 합니다 (템플릿 역할만).
- 각 엔티티의 `create(...)` 팩토리 메서드는 "누가 생성했는지"를 파라미터로 받아 `BaseEntity` 생성자에 넘기도록 시그니처가 바뀝니다.

### Step 5 — IRepository 인터페이스로 OCP 적용
- **기능**: User/Product/Payment Repository가 갖는 CRUD 메서드를 공통 인터페이스로 뽑습니다.
- **Repository 책임**: `IRepository<ID, ENTITY>` 인터페이스를 정의하고 `findAll`, `findById`, `create`, `update`, `delete` 5개 메서드를 선언합니다. 각 Repository 구현체가 이를 구현하도록 바꾸세요 (Product/User도 이제 `update` 지원).

### Step 6 — 제네릭 AbstractRepository로 중복 제거
- **기능**: 3개 Repository의 CRUD 구현이 타입만 다르고 완전히 동일하므로 제네릭 추상 클래스로 통합합니다.
- **Repository 책임**: `AbstractRepository<ENTITY extends BaseEntity>` 추상 클래스가 `IRepository`를 구현하고, 내부에 `Map<Integer, ENTITY>` 인스턴스 필드(더 이상 static 아님) 하나로 CRUD 5개 메서드를 모두 구현합니다. `UserRepository`/`ProductRepository`/`PaymentRepository`는 이제 이 클래스를 상속만 받는 빈 클래스가 됩니다.
- **주의**: 이전 Step에서 static Map을 썼다면 여기서 인스턴스 필드로 바꿔야 도메인별 상속이 가능합니다.

### Step 7 — 상품 조회 API (Internal API)
- **기능**: 고객이 상품 목록/상세를 조회할 수 있는 첫 실제 API를 만듭니다.
- **Controller 책임** (`internal.api.ProductController`):
  - `GET /internal/api/products`: 전체 상품 조회
  - `GET /internal/api/products/{id}`: 단건 상품 조회
- **신규 DTO**: `ProductResponseDto` — id, name, price, stock 노출. 엔티티를 DTO로 변환하는 정적 팩토리(`from(entity)`) 필요.
- **예외**: 존재하지 않는 id 조회 시 예외.

### Step 8 — 결제 생성/취소 API (복잡한 로직)
- **기능**: 고객이 상품을 결제하거나, 이미 한 결제를 취소할 수 있는 API를 만듭니다. 이 커밋은 아직 컨트롤러에 로직을 다 몰아넣은 상태입니다.
- **Controller 책임** (`internal.api.PaymentController`):
  - `POST /internal/api/payments`: 상품 id 목록 + 요청자 id를 받아 결제 생성
  - `PATCH /internal/api/payments/{id}/cancel`: 결제 id + 요청자 id를 받아 결제 취소
- **결제 생성 로직 요구사항**:
  1. 요청받은 각 상품 id가 실제 존재하는지, 재고가 1개 이상인지 검증
  2. 검증된 상품들로 `Payment` 생성, 상태를 결제완료로 바꾸고 결제완료시각 기록
  3. 결제 완료된 상품들의 재고를 1개씩 차감
- **결제 취소 로직 요구사항**:
  1. 취소하려는 결제 건이 존재하는지 확인
  2. 결제 상태가 "취소 가능" 상태인지 확인 (아니면 예외)
  3. 상태를 취소완료로 바꾸고 취소시각 기록
  4. 결제에 포함됐던 모든 상품의 재고를 1개씩 롤백 증가
- **신규 DTO**: `PaymentCreateRequestDto`(상품 id 목록 + 요청자 id), `RequestingUserDto`(요청자 id만 담는 베이스 DTO — 다른 요청 DTO들이 상속해서 재사용), `PaymentResponseDto`(결제 정보 + 관련 상품 목록을 함께 반환).
- **예외/검증**: 존재하지 않는 상품/결제 id, 재고 부족, 취소 불가능한 상태에서의 취소 시도 → 모두 예외.

### Step 9 — 3계층(3-Layered) 아키텍처: Service 계층 도입
- **기능**: 컨트롤러에 있던 결제/상품 로직을 통째로 `Service` 클래스로 옮깁니다. (아직 하나의 Service에 다 몰아넣은 상태 — 다음 Step에서 개선)
- **Service 책임**: `PaymentService`, `ProductService`를 신설하고 Step 7~8의 컨트롤러 로직을 그대로 옮깁니다. Controller는 요청을 받아 Service를 호출하고 결과를 반환하는 역할만 하도록 축소합니다.
- **의도적 한계**: 이 구조는 Service가 여전히 비대하다는 문제가 있음을 인지하고 다음 Step에서 개선.

### Step 10 — 개선안 1: Service를 2개 계층으로 분리
- **기능**: 비대한 Service를 "비즈니스 목적을 수행하는 계층"과 "순수 CRUD 계층"으로 나눕니다.
- **Service/Application 책임**:
  - `PaymentApplication`, `ProductApplication` (신설) — 비즈니스 절차(상태를 바꾸는 목적/시나리오)를 담당. 예: 결제 생성 시나리오 전체, 결제 취소 시나리오 전체.
  - `PaymentService`, `ProductService` (역할 축소) — 오직 CRUD만 담당하는 "도메인 서비스"로 격하. 각 도메인 서비스는 자신의 Repository 하나만 필드로 가져야 합니다 (다른 도메인 Repository 참조 금지).
- Controller는 이제 Application을 호출합니다.

### Step 11 — DDD 적용: 로직을 엔티티로 이동, Setter 제거
- **기능**: Application에 있던 "상태를 바꾸는 로직"을 해당 엔티티 내부 메서드로 옮기고, 외부에서 필드를 직접 바꾸는 Setter를 전부 제거합니다.
- **BaseEntity 변경**: 필드 접근제어자를 `protected`로 완화(자식 클래스가 직접 다루도록), `updated(userId)`를 `protected`로 제한.
- **Product 신규 메서드**: `buyable()`(재고 없으면 예외), `decrease()`(재고 1 차감), `increase()`(재고 1 증가).
- **Payment 신규 메서드**: `complete(requestedUserId)`(요청자와 생성자가 같은지 검증 후 결제완료 처리), `cancel(requestedUserId)`(취소 가능 상태 검증 후 취소 처리).
- **검증**: `complete`/`cancel` 모두 "요청한 유저 = 결제를 생성한 유저"인지 검증해야 하고, 아니라면 예외.
- Application(`PaymentApplication`)은 이제 `product.buyable()`, `product.decrease()`, `payment.complete()`, `payment.cancel()`처럼 엔티티 메서드를 호출하는 조율자 역할만 합니다.

### Step 12 — Application 코드 정리
- **기능**: Step 11에서 만든 코드의 가독성을 다듬습니다 (for 루프를 stream으로, 빌더 패턴 정리 등). 새로운 기능 요구사항은 없습니다.
- **Service 책임 추가**: `ProductService`에 여러 상품을 한 번에 갱신하는 메서드(`update(List<Product>)`)를 추가하면 편합니다.

### Step 13 — Hexagonal 아키텍처: Input/Output Port 도입
- **기능**: Application과 Repository에 인터페이스(Port)를 도입해 Controller와 Domain Service가 구체 클래스가 아닌 인터페이스에 의존하게 합니다.
- **신규 인터페이스**: `IPaymentApplication`, `IProductApplication` (Input Port — Application의 메서드 시그니처만 선언).
- **적용 범위**: `PaymentController`/`ProductController`는 구체 클래스가 아닌 `IPaymentApplication`/`IProductApplication`을 주입받도록 변경. `PaymentService`/`ProductService`는 Step 5에서 만든 `IRepository`(Output Port)를 필드 타입으로 사용하도록 변경.
- **레이어 매핑 이해**: Controller = Primary(Driving) Adaptor, Application 인터페이스 = Input Port, Repository 인터페이스 = Output Port, Repository 구체클래스 = Secondary(Driven) Adaptor.

### Step 14 — ThreadLocal로 "요청자 정보" 전파
- **기능**: 지금까지 모든 메서드에 `requestedUserId`를 매번 파라미터로 넘기던 것을, 요청 스코프 동안 유지되는 컨텍스트로 대체합니다.
- **신규 클래스**: `UserContext` — 내부에 `ThreadLocal<Integer>` 하나를 갖고 `setUserId`, `getUserId`, `clear` 정적 메서드를 제공합니다. try-with-resources로 안전하게 정리할 수 있도록 `AutoCloseable` 스코프 객체(`withUser(userId)` 호출 시 세팅하고, `close()` 시 자동 clear)도 제공해야 합니다.
- **적용 범위**: `PaymentApplication`, `Payment`, `BaseEntity`의 메서드들에서 `requestedUserId` 파라미터를 제거하고, 대신 내부에서 `UserContext.getUserId()`로 조회하도록 변경. Controller에서는 요청을 처리하는 동안 `try (UserContext.withUser(requestedUserId)) { ... }` 형태로 감싸야 합니다.
- **검증**: `BaseEntity.updated()`는 ThreadLocal 값이 없을 경우 `createdBy`로 대체하는 기본값 로직이 있어야 합니다(다음 Step의 External API처럼 요청자 개념이 없는 경우 대비).

### Step 15 — External API: 배송 상태 변경 + 포인트 적립
- **기능**: 외부 택배사가 배송 시작/완료를 알려주는 API를 만들고, 배송 완료 시 결제자에게 포인트를 적립합니다.
- **Entity 필드 추가**
  - `User`: `grade`(UserGrade enum, 기본값 BRONZE), `point`(int, 기본 0)
  - `UserGrade` enum: `BRONZE`, `SILVER`, `GOLD`, `PLATINUM` — 각 값은 포인트 적립률(double)을 들고 있어야 합니다.
- **Payment 신규 메서드**: `delivering()`(배송중 상태로 전환), `delivered()`(배송완료 상태로 전환 + 배송완료시각 기록).
- **User 신규 메서드**: `earn(paidPrice)` — 결제금액 × 등급별 적립률만큼 포인트 증가.
- **신규 클래스**: `UserService`(User CRUD 담당 도메인 서비스), `DeliveryApplication`(배송 처리 Application).
- **Controller 책임** (`external.api.DeliveryController`):
  - `PATCH /external/api/payments/{id}/in-delivery`: 배송 시작 처리
  - `PATCH /external/api/payments/{id}/delivery-complete`: 배송 완료 처리 → 결제자를 조회해서 포인트 적립까지 이어져야 함
- **신규 DTO**: `DeliveryResponseDto`(결제 id, 상태, 배송완료시각만 노출).

### Step 16 — Admin API: User CRUD
- **기능**: 관리자가 유저 정보를 전체 조회/단건 조회/생성/수정/활성화/소프트삭제/하드삭제할 수 있는 API를 만듭니다.
- **BaseEntity 신규 메서드**: `delete()`(소프트삭제 처리 + 수정감사), `active()`(삭제 취소/복구 처리 + 수정감사) — 모든 엔티티가 공통으로 쓸 수 있도록 BaseEntity에 정의.
- **User 신규 메서드**: `update(name, grade, point)` — 관리자가 값을 직접 수정.
- **UserService 책임 추가**: `getUsers()`, `create(entity)`, `update(entity)`, `active(id)`, `softDelete(id)`, `hardDelete(id)`.
- **신규 Application**: `UserAdminApplication` — `retrieve()`(전체), `retrieve(id)`(단건), `create(entity)`, `update(id, request)`, `active(id)`, `softDelete(id)`, `hardDelete(id)`.
- **Controller 책임** (`admin.api.UserApiController`):
  - `GET /admin/api/users`, `GET /admin/api/users/{id}`
  - `POST /admin/api/users` (생성)
  - `PUT /admin/api/users/{id}` (수정, ThreadLocal에 요청자 세팅 필요)
  - `PATCH /admin/api/users/{id}/active`
  - `PATCH /admin/api/users/{id}/soft-delete`
  - `DELETE /admin/api/users/{id}/hard-delete`
- **신규 DTO**: `UserAdminCreateRequestDto`(name + 요청자id, 엔티티로 변환하는 `to()` 메서드 포함), `UserAdminUpdateRequestDto`(name/grade/point + 요청자id), `UserAdminResponseDto`(id/name/grade/point/deleted 노출).
- 수정/활성화/삭제류 API는 모두 `RequestingUserDto`(요청자 id)를 바디로 받아 ThreadLocal에 세팅한 뒤 처리해야 합니다.

### Step 17 — Admin API: Product CRUD
- **기능**: Step 16과 동일한 패턴을 상품에도 적용합니다.
- **Product 신규 메서드**: `update(name, price, stock)`.
- **ProductService 책임 추가**: `create(entity)`, `active(id)`, `softDelete(id)`, `hardDelete(id)` (update는 이미 존재).
- **신규 Application**: `ProductAdminApplication` — User와 동일한 6개 메서드 패턴.
- **Controller 책임** (`admin.api.ProductApiController`): User와 동일한 6개 엔드포인트를 `/admin/api/products` 경로로.
- **신규 DTO**: `ProductAdminUpsertRequestDto`(name/price/stock + 요청자id, `to()` 포함), `ProductAdminResponseDto`(id/name/price/stock/deleted).

### Step 18 — Admin Web: User 페이지 (SSR)
- **기능**: 관리자가 브라우저로 접속해 유저 목록/상세를 볼 수 있는 서버사이드 렌더링 페이지를 만듭니다.
- **Controller 책임** (`admin.web.UserWebController`, Thymeleaf):
  - `GET /admin/web/users`: 유저 목록을 모델에 담아 뷰 반환
  - `GET /admin/web/users/{id}`: 유저 상세 정보를 모델에 담아 뷰 반환
- **뷰 템플릿**: `templates/users/list.html`(목록 반복 출력), `templates/users/detail.html`(단건 상세 출력) — Thymeleaf `th:each`/`th:text` 사용.
- 이 Step은 기존 `UserAdminApplication`을 재사용하며 신규 Application/Service는 없습니다.

### Step 19 — Admin Web: Product 페이지 (SSR)
- **기능**: Step 18과 동일한 패턴을 상품에도 적용합니다.
- **Controller/뷰**: `admin.web.ProductWebController` + `templates/products/list.html`, `templates/products/detail.html`.

### Step 20 — 멀티모듈 전환 ⚠️ (여기서부터 모듈 구조 등장)
- **기능**: 지금까지 만든 단일 프로젝트를 4개의 Gradle 서브모듈로 분리합니다. 이 시점부터 새 코드는 반드시 아래 모듈 위치에 작성해야 합니다.
- **모듈 구조**
  - `codeit-core` — **라이브러리 전용(JAR)**, 실행 불가(bootJar 비활성화). 지금까지 만든 `common/context`(UserContext), `repository`(BaseEntity, IRepository, AbstractRepository, User/Product/Payment + 각 Repository), `service`(UserService, ProductService, PaymentService)를 모두 이 모듈로 이동.
  - `codeit-admin` — **실행 가능한 BootJar**. Admin API + Admin Web 전체(Controller, Application, DTO, 템플릿)가 이 모듈로 이동. `codeit-core`에 의존.
  - `codeit-internal-api` — **실행 가능한 BootJar**. Internal API 전체(PaymentController, ProductController, Application, DTO)가 이동. `codeit-core`에 의존.
  - `codeit-external-api` — **실행 가능한 BootJar**. External API 전체(DeliveryController, DeliveryApplication, DTO)가 이동. `codeit-core`에 의존.
- **루트 build.gradle 책임**: Spring Boot/Dependency-Management 플러그인 버전만 선언(`apply false`)하고, `subprojects { }` 블록으로 Java 17, Lombok, JUnit 등 모든 서브모듈 공통 설정을 내려줍니다.
- **각 서브모듈 build.gradle 책임**:
  - `codeit-core`: `bootJar { enabled = false }`, `jar { enabled = true }` — Spring Data JPA + DB 드라이버 의존성만 선언 (실제 연결은 안 하지만 하위 모듈이 자동설정 충돌 없게 하기 위함).
  - 나머지 3개 실행 모듈: `bootJar { enabled = true }`, `jar { enabled = false }` — `codeit-core` 의존 + webmvc(+admin은 thymeleaf) 의존성 선언.
- **settings.gradle**: `include 'codeit-core', 'codeit-admin', 'codeit-internal-api', 'codeit-external-api'`.
- 각 모듈에는 자체 `@SpringBootApplication` 진입점과 `application.properties`(spring.application.name)가 필요합니다.

### Step 21 — Admin API: 썸네일 이미지 업로드 (Multipart)
- **기능**: 상품/유저 생성·수정 시 이미지 파일을 함께 업로드받아 저장 경로를 엔티티에 기록합니다.
- **Entity 필드 추가**: `Product.thumbnail`(String), `User.thumbnail`(String) — 두 엔티티 모두 `upload(thumbnailPath)` 메서드로 채웁니다.
- **신규 인터페이스/클래스** (이 시점엔 `codeit-admin` 모듈 내부에 위치):
  - `MultipartFileUpload`(인터페이스) — `upload(MultipartFile): String` 하나만 선언
  - `MultipartFileAbstractUpload`(추상 클래스) — 파일 유효성 검증(빈 파일 예외) + 파일명 생성을 표준화하고, 실제 저장은 하위 클래스에 위임 (`generate(file)`, `upload(file, filename)` 추상 메서드로 분리)
  - `MultipartFileLocalUpload` — 로컬 디스크에 저장하는 구현체. UUID + 원본파일명(경로 구분자 제거한 안전한 이름)으로 파일명을 만들고, 설정된 디렉토리에 저장 후 절대경로를 반환.
- **Controller 변경**: 생성/수정 API가 `@RequestPart`로 DTO와 `MultipartFile`(선택적)을 함께 받도록 변경.
- **설정**: 업로드 디렉토리를 `application.properties`의 설정값으로 주입.
- **검증**: 업로드된 파일이 비어있으면 예외.

### Step 22 — 파일 업로드 기능을 공용 서브모듈로 분리
- **기능**: Step 21에서 `codeit-admin`에만 있던 업로드 기능을, 다른 API 서버도 재사용할 수 있도록 별도 모듈로 뽑습니다.
- **모듈 구조 추가**: `codeit-support:web` (계층형 모듈 이름, `codeit-support` 하위에 `web`) — **라이브러리 전용(JAR)**. Step 21에서 만든 `MultipartFileUpload`/`MultipartFileAbstractUpload`/`MultipartFileLocalUpload` 3개 클래스를 이 모듈로 이동 (패키지도 `multipart`로 정리).
- **build.gradle**: `codeit-support/web/build.gradle`은 `bootJar { enabled = false }`, `jar { enabled = true }`, webmvc 의존.
- `codeit-admin`은 이제 `codeit-core`뿐 아니라 `codeit-support:web`도 의존하도록 변경.
- **settings.gradle**: `include 'codeit-support:web'` 추가.

### Step 23 — 응답에 HTTP 상태코드 명시하기
- **기능**: API 응답에 의미 있는 상태코드(201 Created, 202 Accepted 등)를 실어 반환하도록 개선합니다.
- **방법 2가지 이해하고 상황에 맞게 적용**:
  1. `@ResponseStatus(HttpStatus.XXX)` — 메서드 레벨 고정 상태코드. 간편하지만 그 메서드가 반환하는 모든 응답이 같은 상태코드가 됨(예외 상황별 분기 불가).
  2. `ResponseEntity<T>` 직접 생성 — 번거롭지만 메서드 내부에서 상황별로 다른 상태코드를 줄 수 있음.
- **적용**: 단건 조회는 `ResponseEntity`로 직접 상태코드를 담아 반환하도록, 생성은 `@ResponseStatus(CREATED)`를 붙이도록 각각 다르게 적용해보세요 (두 방식의 장단점을 몸으로 익히는 것이 목적).

### Step 24-a — 컨트롤러에서 예외를 try-catch로 처리
- **기능**: 지금까지 던지던 모든 예외가 동일한 `RuntimeException`이라 전부 404로 처리되는 문제를 인지하고, 우선 컨트롤러 안에서 try-catch로 감싸 상태코드를 분기합니다.
- **검증/예외**: 컨트롤러 메서드 내부에서 `try { ... } catch (RuntimeException e) { ... 404 반환 } ` 형태로 감싸되, "모든 예외가 뭉뚱그려져서 세분화된 상태코드를 줄 수 없다"는 한계를 의도적으로 남겨둡니다. → 다음 Step에서 예외를 세분화합니다.

### Step 24-b — 예외를 클래스 레벨로 다양화
- **기능**: `RuntimeException`을 상속하는 구체적인 예외 클래스들을 만들어서, catch 블록에서 예외 종류별로 다르게 처리할 수 있게 합니다.
- **신규 예외 클래스**: `DatabaseConnectionException`, `UserNotFoundException` — 둘 다 `RuntimeException`을 상속하고 고정 메시지를 가짐.
- **적용**: 조회 로직에서 대상이 없으면 `UserNotFoundException`을 던지도록 Service를 변경.
- **검증/예외**: 클래스 다형성 덕분에 아직은 `catch(RuntimeException)` 하나로도 전부 잡히지만, 이후 Step에서 구체 타입별로 분기하게 됩니다.

### Step 25 — 다중 catch + 마지막엔 항상 RuntimeException
- **기능**: 컨트롤러의 catch 블록을 예외 타입별로 여러 개 나열하고, 그 무엇도 못 잡았을 경우를 대비해 **가장 마지막에 반드시** `catch(RuntimeException)`을 둡니다 (switch의 default와 같은 역할).
- **적용**: `catch (DatabaseConnectionException e)` → 500, `catch (UserNotFoundException e)` → 404, `catch (RuntimeException e)` → 500(우리가 예상 못한 예외) 순서로 배치.
- **검증/예외**: 예외 처리 순서가 중요합니다 — 구체적인 예외를 먼저, 범용 예외를 반드시 마지막에 둬야 합니다.

### Step 26 — 예외를 열거형(Enum)으로 통합 관리
- **기능**: 예외 클래스를 계속 늘리는 대신, 단일 예외 클래스 + 예외 종류를 나타내는 Enum 조합으로 바꿉니다.
- **신규 클래스**:
  - `CodeitRuntimeException extends RuntimeException` — 필드로 `ExceptionType type`을 가짐. 생성자는 `type`만 받고 메시지는 `type.getMessage()`로 채움.
  - `ExceptionType`(enum) — `USER_NOT_FOUND`, `DATABASE_CONNECTION_FAILED` 값과 각각의 메시지를 가짐.
- **적용**: Service에서 `new CodeitRuntimeException(ExceptionType.USER_NOT_FOUND)`처럼 던지고, Controller는 `catch (CodeitRuntimeException e)` 하나로 잡은 뒤 내부에서 `switch (e.getType())`으로 분기해 상태코드를 정합니다. 마지막엔 여전히 `catch(RuntimeException)` 필요.

### Step 27-a — ExceptionType에 로그레벨/상태코드까지 내장
- **기능**: switch 분기 로직 자체도 없애기 위해, `ExceptionType` enum 값 안에 로그 레벨(Level)과 HTTP 상태코드까지 함께 정의합니다.
- **ExceptionType 필드 확장**: `level`(Level: WARN/ERROR 등), `status`(int, HTTP 상태코드), `message`(String).
- **적용**: Controller는 이제 `catch (CodeitRuntimeException e)` 하나로 잡아서 `e.getType().getLevel()`로 로깅하고 `e.getType().getStatus()`로 상태코드를 응답하면 끝 — switch/if 분기가 사라집니다.

### Step 27-b — `@Valid` 기반 요청 검증 도입 (방법 1)
- **기능**: 클라이언트가 보낸 요청 값 자체(가격이 음수라던가, 재고가 너무 많다던가)를 검증하는 기능을 추가합니다.
- **적용 대상**: `ProductAdminUpsertRequestDto`에 Bean Validation 애너테이션을 붙입니다.
  - `name`: 공백 불가 검증
  - `price`: 최소값 검증
  - `stock`: 최대값 검증
- **Controller 변경**: 요청 DTO 파라미터 앞에 `@Valid`를 붙여서 "이 객체 안의 필드 검증을 수행하라"고 명시.
- **검증/예외**: `@Valid` 검증 실패 시 발생하는 예외는 컨트롤러 내부의 try-catch로는 잡히지 않는다는 점을 인지해야 합니다(핸들러 진입 전에 실패) — 그래서 다음 Step에서 전역 처리기가 필요해집니다.

### Step 28 — `@RestControllerAdvice` + `@ExceptionHandler`로 중앙 예외처리
- **기능**: 각 컨트롤러마다 try-catch를 반복하는 대신, 스프링 요청 처리 파이프라인 전체(컨트롤러 앞단 포함)를 커버하는 전역 예외 처리기를 만듭니다.
- **왜 컨트롤러 내부가 아니라 앞단이어야 하는가**: `@PathVariable`/`@RequestParam`/`@ModelAttribute`/`@RequestPart`/`@RequestBody`를 객체로 변환하는 과정(ArgumentResolver)에서 발생하는 검증 예외(`@Valid`, `@Validated`)는 컨트롤러 메서드 진입 전에 터지므로, 컨트롤러 내부 try-catch로는 잡을 수 없습니다.
- **신규 클래스**: `GlobalExceptionHandler` (`@RestControllerAdvice`) —
  - `@ExceptionHandler(CodeitRuntimeException.class)`: type의 level/status로 로깅 및 응답
  - `@ExceptionHandler(Exception.class)` + `@ResponseStatus(INTERNAL_SERVER_ERROR)`: 우리가 예상 못한 모든 예외의 최종 안전망
- **적용**: 각 Controller에 있던 try-catch 블록을 모두 제거하고 순수 로직만 남깁니다.

### Step 29 — 3가지 파라미터 검증 방식 모두 도입
- **기능**: 스프링에서 파라미터를 검증하는 3가지 방식을 각각 최소 1곳에 적용해봅니다.
  1. `@RequestBody`/`@RequestPart` 등으로 받는 **객체** 검증 → 파라미터에 `@Valid` + DTO 필드에 제약 애너테이션(Step 27-b에서 이미 적용)
  2. `@PathVariable`/`@RequestParam` 등 **단순 값** 검증 → 파라미터 자체에 바로 제약 애너테이션(`@Min` 등)만 붙이면 스프링이 알아서 처리
  3. Controller가 아닌 **임의의 빈(Service 등) 메서드 파라미터** 검증 → 클래스에 `@Validated`를 붙이고 메서드 파라미터에 제약 애너테이션을 붙이면 AOP 프록시가 검증
- **적용**: `id` 조회용 컨트롤러 메서드 파라미터에 `@Min(1)` 직접 부착(방식 2), Application 클래스에 `@Validated` 부착 후 메서드 파라미터에 `@Min` 부착(방식 3).
- **검증/예외**: 세 방식 모두 서로 다른 예외 타입(`MethodArgumentNotValidException`, `HandlerMethodValidationException`, `ConstraintViolationException`)을 던진다는 점을 이해하고 각각 `GlobalExceptionHandler`에 핸들러를 추가해야 합니다.

### Step 30 — 검증 실패 상세 내용을 일관된 DTO로 로깅
- **기능**: Step 29의 3가지 검증 예외가 각자 다른 방식으로 실패 정보를 담고 있으므로, 이를 하나의 공통 DTO로 통일해서 로그로 남깁니다.
- **신규 DTO**: `InvalidParameterDto` — `parameter`(어떤 필드인지), `actualValue`(실제 들어온 값), `criteriaValue`(기준값), `criteria`(어떤 제약조건이었는지), `violationMessage`(위반 메시지).
- **GlobalExceptionHandler 책임 추가**: `MethodArgumentNotValidException`, `HandlerMethodValidationException`, `ConstraintViolationException` 3개 핸들러 각각에서, 예외 내부의 필드별 실패 정보를 순회하며 `InvalidParameterDto` 리스트로 변환한 뒤 경고 로그로 남깁니다. (아직 응답 바디로는 반환하지 않음, 상태코드만 400)

### Step 31 — 검증 실패 상세 내용을 응답 Body로도 반환
- **기능**: Step 30에서 로그로만 남기던 `InvalidParameterDto` 목록을 응답 바디에도 담아 클라이언트가 어떤 필드가 왜 실패했는지 알 수 있게 합니다.
- **적용**: 3개 검증 예외 핸들러가 `void`가 아니라 `List<InvalidParameterDto>`를 반환하도록 변경.
- **부가**: 제약 애너테이션에 커스텀 실패 메시지를 지정할 수 있다는 것도 함께 익히세요 (예: 최대값 제약에 사람이 읽을 수 있는 메시지 추가).

### Step 32 — 예외 처리를 모든 Application 모듈에서 재사용하도록 모듈 재편
- **기능**: `GlobalExceptionHandler`가 지금은 `codeit-admin`에만 있어서 `codeit-internal-api`/`codeit-external-api`가 재사용할 수 없는 문제를 해결합니다. 예외 타입 정의도 계층을 나눕니다.
- **모듈 구조 추가**: `codeit-common` — **라이브러리 전용(JAR)**, 가장 하위 계층. `CodeitExceptionType`(인터페이스: level/status/message getter 선언)과 `CodeitRuntimeException`을 이 모듈로 이동.
- **기존 모듈 조정**:
  - `codeit-core`의 `ExceptionType` enum은 이제 `CodeitExceptionType` 인터페이스를 구현하도록 변경 (`codeit-core`가 `codeit-common`에 의존).
  - `codeit-support:web`으로 `GlobalExceptionHandler`, `InvalidParameterDto`를 이동 (`codeit-support:web`이 `codeit-common`에 의존, validation 스타터도 이 모듈로 이동).
  - `codeit-admin`뿐 아니라 `codeit-internal-api`도 `codeit-support:web`을 의존하도록 변경.
- **설계 원칙 정리**: "왜 4단계로 나뉘는가"를 이해하고 넘어가세요 — `codeit-common`(모든 모듈이 참조하는 예외 타입 최소 계약) → `codeit-core`(도메인 Entity/Repository/Service) → `codeit-support:web`(웹 계층 공통 부품: 예외 핸들러, 파일 업로드) → 실제 실행 모듈(admin/internal-api/external-api).

### Step 33 — 파일 업로드 설정을 유연하게 (심화)
- **기능**: `codeit-support:web`을 여러 모듈이 공용으로 쓰는데, 업로드 디렉토리 설정(`application.yml`)이 강제되면 그 설정이 없는 모듈에서 빈 생성이 실패하는 문제를 해결합니다.
- **신규 클래스**:
  - `MultipartFileStorageType`(enum) — `LOCAL`, `S3`, `DUMMY` (당장은 LOCAL만 구현).
  - `MultipartFileUploadConfig`(`@Configuration`) — `file.upload-directory`, `file.storage-type` 설정값을 기본값과 함께 주입받아, 설정된 storageType에 맞는 `MultipartFileUpload` 구현체를 `@Bean`으로 등록. 설정을 안 해도 로컬 저장을 기본값으로 강제.
- **리팩터링**: 파일명 안전화(`sanitize`) 로직을 별도 유틸(`MultipartFileUtils`)로 분리해 재사용성을 높입니다.
- **설계 포인트**: 공용 모듈은 "설정하지 않아도 합리적인 기본값으로 동작"해야 한다는 원칙을 적용.

### Step 34 — 공통 응답 포맷 `ApiResponse<T>` 도입
- **기능**: 프론트엔드가 모든 API 응답에서 성공/실패 여부와 상세 메시지를 일관되게 파싱할 수 있도록 공통 응답 래퍼를 만듭니다.
- **신규 DTO**: `ApiResponse<T>` (`codeit-support:web`)
  - `success`(boolean): 상태코드와 별개로 명시적인 성공/실패 플래그
  - `message`(String): 실패 시 클라이언트에 보여줄 수 있는 메시지
  - `content`(T): 성공 시(혹은 실패해도 상세 내용이 필요하면) 담을 데이터
  - 정적 팩토리 4종: `success(content)`, `success()`(content 없음), `failure(message)`(content 없음), `failure(message, content)`
- **적용 범위**: 모든 Controller 응답 타입을 `ApiResponse<T>` 또는 `ResponseEntity<ApiResponse<T>>`로 감싸고, `GlobalExceptionHandler`의 각 핸들러도 `ApiResponse.failure(...)`를 반환하도록 변경.

### Step 35 — `ApiResponse` JSON 직렬화 다듬기
- **기능**: `ApiResponse<T>`를 JSON으로 내려줄 때, 성공 시에는 불필요한 `message` 필드를 숨기고 필드 순서를 일관되게 만듭니다.
- **적용**: `ApiResponse` 클래스에 Jackson 애너테이션 적용 — null인 필드는 응답에서 제외, 필드 노출 순서를 `success → message → content` 순으로 고정.
- **검증/예외**: 새로운 검증/예외 로직은 없음 (순수 직렬화 개선).

---

## 버전 2 — 단일 모듈로 완성 후 멀티모듈로 고도화

### 1단계 — 멀티모듈 분리 없이 하나의 프로젝트로 전체 기능 완성

버전 1의 Step 0~19, 23~35에 담긴 **기능 요구사항**을 모듈 구분 없이 하나의 Spring Boot 프로젝트 안에서 전부 구현하는 것이 목표입니다 (Step 20~22의 "모듈 쪼개기"만 2단계로 미룹니다). 패키지 구조로만 논리적 경계를 나누세요.

**패키지 구조 제안**
```
com.example.demo
├── repository/                 # Entity + Repository (도메인 계층)
│   ├── BaseEntity, IRepository, AbstractRepository
│   ├── user/  User, UserRepository, UserGrade
│   ├── product/  Product, ProductRepository
│   └── payment/  Payment, PaymentRepository, PaymentStatus
├── service/                    # 도메인 서비스 (순수 CRUD)
│   ├── user/UserService, product/ProductService, payment/PaymentService
├── application/                # 비즈니스 시나리오 조율자
│   ├── payment/{IPaymentApplication, PaymentApplication, DeliveryApplication}
│   ├── product/{IProductApplication, ProductApplication, ProductAdminApplication}
│   └── user/UserAdminApplication
├── controller/
│   ├── internal/api/  (ProductController, PaymentController + dto)
│   ├── external/api/  (DeliveryController + dto)
│   ├── admin/api/     (UserApiController, ProductApiController + dto)
│   └── admin/web/     (UserWebController, ProductWebController)
├── controller/advice/          # GlobalExceptionHandler, InvalidParameterDto, ApiResponse
├── exception/                  # CodeitExceptionType, CodeitRuntimeException, ExceptionType
├── multipart/                  # MultipartFileUpload 계열
└── common/context/UserContext
```

**필수 기능 체크리스트** (버전 1의 상세 요구사항을 그대로 적용, 순서는 자유)
- [ ] `BaseEntity` + `User`/`Product`/`Payment` 도메인 모델 (필드는 문서 상단 "최종 완성형 도메인 참고표" 참고)
- [ ] `IRepository`/`AbstractRepository` 기반 Map 인메모리 저장소, 도메인별 Repository
- [ ] 도메인 서비스(순수 CRUD, 자신의 Repository 하나만 의존) / Application(비즈니스 시나리오, Setter 없이 엔티티 메서드 호출)
- [ ] Input Port(`IPaymentApplication`, `IProductApplication`) 기반 Controller 의존
- [ ] `UserContext`(ThreadLocal)로 요청자 id 전파
- [ ] Internal API: 상품 조회 2종, 결제 생성/취소 2종 (+ 재고 검증/차감/롤백)
- [ ] External API: 배송중/배송완료 2종 (+ 포인트 적립)
- [ ] Admin API: User/Product 각각 6종 CRUD 엔드포인트 (+ Multipart 썸네일 업로드)
- [ ] Admin Web: User/Product 목록·상세 Thymeleaf 페이지
- [ ] 응답 상태코드: 생성은 201, 조회는 200/202 등 의미에 맞게
- [ ] 예외 체계: `CodeitRuntimeException` + `ExceptionType`(level/status/message 내장)
- [ ] 검증 체계: `@Valid`(객체), 파라미터 직접 제약(`@Min` 등), `@Validated`+AOP(서비스 계층) 3가지 모두
- [ ] `GlobalExceptionHandler`(`@RestControllerAdvice`)에서 4종 예외(`CodeitRuntimeException`, `MethodArgumentNotValidException`, `HandlerMethodValidationException`, `ConstraintViolationException`) + 최종 안전망(`Exception`) 처리, `InvalidParameterDto` 응답
- [ ] `ApiResponse<T>` 공통 응답 포맷 + Jackson 직렬화(성공 시 message 숨김, 필드 순서 고정)
- [ ] `MultipartFileUpload` 계열(로컬 저장, storage type 설정 기반 Bean 등록)

### 2단계 — 완성된 프로젝트를 실제 모듈 구조로 재편

1단계에서 만든 단일 프로젝트를 아래 6개 Gradle 모듈로 옮깁니다. **기준은 "누가 이 코드를 재사용해야 하는가"와 "이 모듈이 그 자체로 실행 가능한 서버여야 하는가"** 두 가지입니다.

| 모듈 | 실행 가능 여부 | 담을 것 | 분리 기준(왜 여기인가) |
|---|---|---|---|
| `codeit-common` | 라이브러리(JAR) | `CodeitExceptionType`(인터페이스), `CodeitRuntimeException` | 예외의 "최소 계약"만 정의. 도메인(core)도, 웹 계층(support:web)도 이걸 알아야 하므로 **의존성 방향의 가장 아래**에 둬야 순환 참조가 안 생김 |
| `codeit-core` | 라이브러리(JAR), `bootJar disabled` | `BaseEntity`, `IRepository`/`AbstractRepository`, User/Product/Payment 엔티티+Repository, 도메인 Service, `ExceptionType`(구체 enum), `UserContext` | 3개 API 서버(admin/internal/external) 모두가 **그대로 공유**해야 하는 도메인 로직. 특정 서버에 속하지 않고, 혼자서는 실행될 이유가 없음 |
| `codeit-support:web` | 라이브러리(JAR), `bootJar disabled` | `GlobalExceptionHandler`, `InvalidParameterDto`, `ApiResponse`, `MultipartFileUpload` 계열 | 도메인은 아니지만 **웹(Spring MVC) 계층에서 여러 서버가 공통으로 쓰는 부품**. Admin뿐 아니라 Internal/External API도 예외 응답 포맷과 검증 처리 방식을 통일해야 하므로 core와는 별도 모듈로 분리(순수 도메인과 웹 관심사를 섞지 않기 위해) |
| `codeit-admin` | 실행 가능(BootJar) | Admin API + Admin Web의 Controller/Application/DTO/템플릿, 파일업로드 사용처 | **위험한 CRUD 권한**을 가진, 사내 전용으로 독립 배포되어야 하는 서버. `codeit-core` + `codeit-support:web` 의존 |
| `codeit-internal-api` | 실행 가능(BootJar) | Internal API의 Controller/Application/DTO | 실제 고객 트래픽을 받는 서버로, Admin과는 **배포 주기/스케일 정책이 달라야** 하므로 별도 모듈. `codeit-core` + `codeit-support:web` 의존 |
| `codeit-external-api` | 실행 가능(BootJar) | External API의 Controller/Application/DTO | 외부 업체(택배사)에 노출되는 서버로 **보안 경계가 완전히 다름** — 절대 Admin/Internal과 같은 배포 단위에 있으면 안 됨. `codeit-core` + `codeit-support:web` 의존 |

**루트 build.gradle 기준**: 모든 모듈이 공통으로 필요한 것(Java 17 toolchain, Lombok, JUnit, `dependencyManagement`로 Spring Boot BOM import)만 `subprojects { }`에 선언. 서버별로 다른 것(Thymeleaf는 admin만, validation 스타터는 웹 계층만 등)은 각 모듈 `build.gradle`에 개별 선언.

**의존 방향 체크리스트**
- [ ] `codeit-common`은 어떤 모듈도 의존하지 않는다 (최하위)
- [ ] `codeit-core`, `codeit-support:web`은 `codeit-common`만 의존한다
- [ ] `codeit-admin`/`codeit-internal-api`/`codeit-external-api`는 `codeit-core` + `codeit-support:web`을 의존하되, 서로는 의존하지 않는다
- [ ] `codeit-core`만 `bootJar disabled` + DB 드라이버 관련 의존성을 갖고, 나머지 실행 모듈만 `bootJar enabled`
- [ ] `settings.gradle`에 6개 모듈 모두 `include` (계층형 이름은 `codeit-support:web`처럼 콜론으로 표기)
