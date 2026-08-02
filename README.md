# 페어코딩 스터디 코드리뷰 내역

## 코드 리뷰 반영 내역

코드 리뷰를 통해 인지한 문제를 확인하고 수정한 내역을 정리합니다.

### 1. Member 생성자 캡슐화 및 id 정적 할당

- **Before**: `Member` 생성자가 `public`으로 열려 있었고, `MemberRepository.create()`에서 id 채번 후 `entity.getJob().getJobType()`으로
  enum을 문자열로 되돌린 뒤 `Member`를 통째로 새로 생성해서 저장했습니다. 넘겨받은 엔티티를 그대로 활용하지 않고 Repository가 새 객체를 다시 만들어내는 방식이 마음에 들지 않았습니다.
- **After**:
    - `Member` 생성자를 `private`으로 감추고, 최초 생성 시에는 `Member.toEntity(MemberUpsertRequestDto request)` 정적 팩토리 메서드로 id를 `null`로
      고정해서 생성하도록 변경했습니다.
    - `Human`에 `assignId(Integer id)`를 추가해 id가 이미 지정된 경우 예외(`이미 ID가 지정된 회원입니다.`)를 던지도록 가드를 두었고, `id` 필드도 `final`을 제거해 이
      메서드를 통해서만 최초 1회 할당되도록 했습니다.
    - `MemberRepository.create()`는 더 이상 엔티티를 재생성하지 않고, 채번한 id를 `entity.assignId(id)`로 위임한 뒤 같은 참조를 그대로 저장하도록 단순화했습니다.

### 2. `@PathVariable` 단건 유효성 검증

- **Before**: `getUser(@PathVariable Integer id)`처럼 `@PathVariable`/`@RequestParam` 값에는 `@Valid`가 적용되지 않아 별도 검증이 없었고,
  클래스 레벨에 `@Validated`를 붙여야 동작하는 것으로 알고 있었습니다.
- **After**:
    - `id`에 `@Positive` 제약 애노테이션을 추가했습니다.
    - 실제로 `@Validated` 없이 테스트해보니 정상적으로 검증이 동작하는 것을 확인했고, Spring 6+ 부터는 제약 애노테이션만 있어도 Spring MVC가 자동으로 검증을 수행한다는 점을 파악해
      `@Validated`는 주석으로 남겨 이유를 기록해두었습니다.
