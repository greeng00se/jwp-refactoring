# 키친포스

## 요구 사항

### 상품

- 상품명과 상품 가격을 입력받아 상품을 등록할 수 있다.
    - 상품의 가격은 0원 이상이어야 한다.
- 상품 목록을 조회할 수 있다.

### 메뉴

- 메뉴명, 가격, 메뉴 그룹 번호, 메뉴 상품 목록을 입력받아 메뉴를 등록할 수 있다.
    - 메뉴의 가격은 0원 이상이어야 한다.
    - 존재하는 메뉴 그룹 번호를 입력받아야 한다.
    - 각각의 메뉴 상품의 경우 상품이 존재해야 한다.
    - 메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없다.
- 메뉴 목록을 조회할 수 있다.

### 메뉴 그룹

- 메뉴 그룹명을 입력받아 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹의 목록을 조회할 수 있다.

### 주문

- 테이블 번호와 주문 항목 목록을 받아 주문을 등록할 수 있다.
    - 빈 테이블이 아닌 번호를 입력 받아야 한다.
    - 주문 항목 목록은 비어있을 수 없다.
    - 등록되지 않은 메뉴를 주문할 수 없다.
    - 주문이 등록되는 경우 주문의 상태가 조리중(COOKING)으로 변경된다.
- 주문의 상태를 변경할 수 있다.
    - 조리중(COOKING), 식사중(MEAL), 완료(COMPLETION) 상태가 존재한다.
    - 주문의 상태가 이미 완료(COMPLETION)인 경우 상태를 변경할 수 없다.
- 주문 목록을 조회할 수 있다.

### 단체 지정

- 테이블의 번호를 입력받아 단체 지정 할 수 있다.
    - 입력받은 테이블 번호가 1개 이하라면 단체 지정 할 수 없다.
    - 빈 테이블이 아닌 경우 단체 지정 할 수 없다.
    - 이미 단체 지정이 된 테이블의 경우 단체 지정 할 수 없다.
    - 단체 지정을 하는 경우 테이블의 상태가 주문 테이블로 변경된다.
- 그룹 번호를 입력받아 단체 지정을 해제할 수 있다.

### 테이블

- 테이블을 등록할 수 있다.
- 테이블의 상태를 변경할 수 있다.
    - 빈 테이블, 주문 테이블 상태가 존재한다.
    - 단체 지정이 되어있는 테이블인 경우 테이블의 상태를 변경할 수 없다.
    - 테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 상태를 변경할 수 없다.
- 테이블에 방문한 손님 수를 지정할 수 있다.
    - 지정시 방문한 손님 수는 0명 이상이어야 한다.
    - 빈 테이블인 경우 손님 수를 지정할 수 없다.
- 테이블 목록을 조회할 수 있다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

## 테이블

<img width="1011" alt="image" src="https://github.com/greeng00se/greeng00se.github.io/assets/58586537/1c2a352e-bed7-4c0f-89ed-8d6d31487b9c">

## 생각

ID를 받는 생성자를 제거하는 과정에서 생긴 고민

1. 모든 필드를 지정해줄 수 있는 주생성자를 만드는 건 구현 기술에 침투적인 생성자가 생기는 것인가?

id 생성자를 제거한다면, 결국 도메인을 식별하기 위한 값을 넣으려면 jpa와 같은 기술에 의존적인 생성을 해야하는데, 오히려 제거하는 것이 구현 기술에 침투적인 생성자를 만드는 것이 아닌가?
결국 해당 도메인을 식별하기 위한 값으로 본다면, jpa를 사용하지 않았을 때 값을 지정해줘야 할텐데, 해당 부분을 위해 남겨둬야하지 않을까?
