# ☘️ 네이버 스마트 스토어 클론코딩 (23.12.15~24.01.12)

![dd](https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/05db649d-a679-441b-b7b5-486bfe85e256)

## 💡 프로젝트 소개

### 📍 어떤 서비스인가요?
네이버 쇼핑에 속해 있는 네이버 스마트 스토어를 구현한 것으로,
판매자는 자신이 갖고 있는 상품을 손쉽게 판매할 수 있고, 사용자는 상품을 자유롭게 구매할 수 있도록 하는 서비스입니다.

<br>

### 📍 왜 선정했나요?
- 일상에서 빈번하게 접할 수 있는 도메인입니다.
- 단순한 CRUD가 아닌, 복잡한 정책과 기능을 분석하고 전반적인 흐름을 이해할 수 있습니다.
- 복잡하게 얽혀있는 도메인 간의 관계를 분석하고 설계할 수 있습니다.
- 연관된 도메인을 설계하고 통합하는 과정을 통한 협업 능력를 향상시킬 수 있습니다.

<br>

### 📍 무엇을 구현했나요?
- 판매할 상품을 등록하고 관리할 수 있습니다.
- 원하는 상품을 쿠폰과 포인트를 적용해서 주문할 수 있습니다.
- 주문한 상품에 대한 쿠폰과 포인트 사용 이력을 통해 부분 환불할 수 있습니다.
- 자동으로 최대 할인 금액으로 쿠폰을 적용할 수 있습니다.
- 포인트를 적립하고 사용할 수 있습니다.
- 멤버십 구독 회원이면 추가 적립을 할 수 있습니다.
- 본인인증 후 로그인해서 사용자 정보를 조회할 수 있습니다.
- 원하는 상품을 찜할 수 있습니다.
- 리뷰를 작성하고 도움이 된 리뷰에 좋아요를 누를 수 있습니다.
- 장바구니에 상품을 담을 수 있습니다.
- 상품에 문의를 작성할 수 있습니다.
- 상품 문의에 답변을 작성할 수 있습니다.
- 쿠폰을 발급하고 사용자에게 지급할 수 있습니다.

<br>

### 📍프로젝트 목표
**공부하는 마음으로 꼼꼼한 코드 작성을 하자 🔥**
- DDD를 적용하여 도메인을 설계하고 구현하자
- 6명의 코드리뷰를 통해 가독성 좋은 코드에 대해 알아가고 개선하자
- 6명이 한번에 코드를 작성하는 만큼 의존성을 최소화하고 유연한 코드를 작성하자
- 기술보다는 도메인에 집중하자
- 명분이 있는 코드를 작성하자

#### DDD 적용에 대하여
```text
DDD 스터디를 통해 패키지 구조 스타일부터 객체의 역할과 책임에 대한 관점, 그리고 유효성 검증은 어디에 위치해야 할까 등등
차이가 있던 부분들을 조율할 수 있었습니다. 매 코드 리뷰를 통해 실제 프로젝트에 DDD 개념을 적용해보려는 노력을 기울였습니다.
```

<br>

---

## 도메인 분석

### 📍 주문(Order) 도메인 생성 단계
<img src="https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/37c14557-42ba-4f36-93be-082e74d57f30" width="700" height="auto">

- 주문서 유효성 검증
  - 토큰유저와 주문서 유저가 같은지 확인
  - 주문서의 배송 정보가 유효한지 확인

- 주문 상품 유효성 검증
  - 상품의 재고 수량이 주문 수량보다 많은지 확인
  - 상품의 판매 상태가 판매 중인지 확인
  - client 넘어온 값이 유효한 값인지 확인

- 추가 정보 가공
  - 예상 적립 포인트: 상품별 결제금액과 사용자의 멤버십 구독 여부에 따라 예상 적립 포인트 반환
    - 사용자의 월별 쇼핑금액과 최대 적립 한도 등 포인트 정책을 토대로 예상 적립 포인트를 반환
  - 적용 가능 포인트: 유저가 소유한 포인트 잔액
  - 최대 할인 쿠폰 조합: 상품별 결제금액과 사용자의 보유 쿠폰을 토대로 최대 할인 쿠폰 조합 반환
  - 적용 가능 쿠폰: 상품별 결제금액과 사용자의 보유 쿠폰을 토대로 적용 가능 쿠폰 반환
  - 주문상품에 대한 쿠폰 적용 결과 리스트: 최대 할인 쿠폰 조합을 토대로 주문상품별 쿠폰 적용 결과 리스트 반환
    - 주문 페이지 첫 접속시
      - 쿠폰 중복 사용, 쿠폰의 최소 주문 금액, 쿠폰 최대 할인값 검증
      - 유저가 보유한 쿠폰과 상품에 적용가능한 쿠폰을 최적의 알고리즘으로 계산된 최대 할인금액을 반환

- 주문서 쿠폰 변경 감지
  - 예상 포인트는 쿠폰으로 할인 된 금액을 토대로 적립 포인트를 계산하기 때문에 쿠폰이 변경되면 예상 포인트도 변경되어야 한다.
  - 주문상품에 대한 쿠폰 적용 결과 리스트: 최대 할인 쿠폰 조합을 토대로 주문상품별 쿠폰 적용 결과 리스트 반환
    - 직접 쿠폰 선택시
      - 쿠폰 중복 사용, 쿠폰의 최소 주문 금액, 쿠폰 최대 할인값 검증
      - 선택된 쿠폰과 쿠폰이 적용되는 상품 금액을 계산하여 반환

> 🤔 Order 와 주문서를 분리한 이유 : 주문서는 주문을 생성하기 위한 데이터를 담고 있고, Order는 결제를 한 이후에 주문에 대한 데이터를 담고 있기 때문에 분리하였다.
> 또한 주문서 페이지를 새로고침 했을때 재사용 할 수 있고, 유저가 결제를 한 이후만 유효한 Order로 남기고 싶어 분리하였다.

<br>

### 📍 주문(Order) 도메인 취소
<img src="https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/0f4eccb2-e835-4d0e-ad8c-4accaecb265f" width="700" height="auto">

#### 전체취소
- 주문 전체 취소로 인한 쿠폰 사용 취소
   - 쿠폰 사용 트랜잭션에 저장된 취소하는 주문 상품 전체에 대한 쿠폰 사용 취소 처리
- 주문 전체 취소로 인한 포인트 환불: 주문에서 사용된 모든 포인트 전액 환불
  - 취소하는 주문에 대한 포인트 트랜잭션 이력을 토대로 사용한 포인트를 전체 환불

#### 부분취소

- 주문 부분 취소로 인한 쿠폰 부분 취소
  - 쿠폰 사용 트랜잭션에 저장된 부분 취소하는 상품에 대한 일부 쿠폰만 취소 처리
  - 최소 금액에 대한 쿠폰 사용 취소 처리는 환불 금액에서 제하고 돌려줌
- 주문 부분 취소로 인한 포인트 환불: 주문에서 취소하고자 하는 상품에 사용된 포인트 부분 환불
  - 취소하는 주문에 대한 포인트 트랜잭션 이력에서 주문상품별 차감된 포인트를 가져와 부분 환불

<br>

### 📍 주문(Order) 도메인 상태 관리
![상태관리](https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/76463534-648b-4804-9442-599a75180bca)

<br>

## 📚 기술 스택

- <b>Core</b>

  <img src="https://img.shields.io/badge/java 17-FF4800?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Boot 3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security 6.2-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
  <img src="https://img.shields.io/badge/JWT 4.4-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">
  <img src="https://img.shields.io/badge/QueryDsl 5.0-4169E1?style=for-the-badge&logo=QueryDsl&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=Spring JPA&logoColor=white">
  <img src="https://img.shields.io/badge/mysql 8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> 
 

- <b>DevOps</b>    
  <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

- <b>Infrastructure</b>    
  <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white">  
  
- <b>CI / CD</b>    
  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> 
  
- <b>Team Collaboration Tool</b>    
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> 
  <img src="https://img.shields.io/badge/notion-0052CC?style=for-the-badge&logo=notion&logoColor=white"> 
  <img src="https://img.shields.io/badge/slack-D24939?style=for-the-badge&logo=slack&logoColor=white"> 
  <img src="https://img.shields.io/badge/zoom-2496ED?style=for-the-badge&logo=zoom&logoColor=white">

<br>

## ⚙️ 아키텍처
### Server
![](https://github.com/smRt-Egg/BE-05-smRt-store/assets/85854384/d78905c1-c919-4e8d-83ab-2165d6a2ce5b)

<br>

### ERD
![](https://github.com/smRt-Egg/BE-05-smRt-store/assets/57834671/28a56593-298f-4bb4-8d2f-c75be1203e1e)

<br>

# 👨‍👩‍👧‍👦 팀원 소개
| Name | <center>[소승수](https://github.com/voidmelody)</center>| <center>[김용상](https://github.com/YongNyeo)</center> | <center>[김주환](https://github.com/happyjamy)</center> | <center>[이경민](https://github.com/tidavid1)</center> | <center>[임수진](https://github.com/suzzingv)</center> | <center>[홍지인](https://github.com/JIN-076)</center> |
| --- | --- | --- | --- | --- | --- | --- |
| Profile | <img width="100px" src="https://github.com/voidmelody.png" /> | <img width="100px" src="https://github.com/YongNyeo.png" /> | <img width="100px" src="https://github.com/happyjamy.png" /> | <img width="100px" src="https://github.com/tidavid1.png" /> | <img width="100px" src="https://github.com/suzzingv.png" /> | <img width="100px" src="https://github.com/JIN-076.png" /> |
| Role | `#Dev` | `#Dev` | `#PO` `#Dev` | `#SM` `#Dev` | `#Dev` | `#Dev` |
| Domain | `infra` <br> `keep` <br> `qna` <br> `review` | `infra` <br> `coupon` | `infra` <br> `order` | `security` <br> `cart` <br> `review` <br> `product` | `security` <br> `user` | `security` <br> `point` |

<br>






