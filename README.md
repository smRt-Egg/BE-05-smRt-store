# ☘️ 네이버 스마트 스토어 클론코딩 (23.12.15~24.01.12)

![dd](https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/05db649d-a679-441b-b7b5-486bfe85e256)

## 💡 프로젝트 소개
> ### "디지털 익스피어런스: 스마트 스토어의 멋진 세계”

### 📍 어떤 서비스인가요?
네이버 쇼핑에 속해 있는 네이버 스마트 스토어를 구현한 것으로,
판매자는 자신이 갖고 있는 상품을 손쉽게 판매할 수 있고, 사용자는 상품을 자유롭게 구매할 수 있도록 하는 서비스입니다.

<br>

### 📍 왜 선정했나요?
- 일상에서 빈번하게 접할 수 있는 도메인이에요
- 단순한 CRUD가 아닌, 복잡한 정책과 기능을 분석하고 전반적인 흐름을 이해할 수 있어요
- 복잡하게 얽혀있는 도메인 간의 관계를 분석하고 설계할 수 있어요
- 연관된 도메인을 설계하고 통합하는 과정을 통한 협업 능력를 향상시킬 수 있어요

<br>

### 📍 무엇을 구현했나요?
- 판매할 상품을 등록하고 관리할 수 있어요
- 원하는 상품을 쿠폰과 포인트를 적용해서 주문할 수 있어요
- 주문한 상품에 대한 쿠폰과 포인트 사용 이력을 통해 부분 환불할 수 있어요
- 자동으로 최대 할인 금액으로 쿠폰을 적용할 수 있어요
- 포인트를 적립하고 사용할 수 있어요
- 멤버십 구독 회원이면 추가 적립을 할 수 있어요
- 원하는 상품을 찜할 수 있어요
- 리뷰를 작성하고 도움이 된 리뷰에 좋아요를 누를 수 있어요
- 장바구니에 상품을 담을 수 있어요
- 상품에 문의를 작성할 수 있어요
- 상품 문의에 답변을 작성할 수 있어요
- 쿠폰을 발급하고 사용자에게 지급할 수 있어요

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
DDD 스터디를 통해 우리 팀은 패키지 구조 스타일부터 객체의 역할과 책임에 대한 관점, 그리고 유효성 검증은 어디에 위치해야 할까 등등
차이가 있던 부분들을 조율할 수 있었습니다. 매 코드 리뷰를 통해 실제 프로젝트에 DDD 개념을 적용해보려는 노력을 기울였습니다.
```

<br>

---

## 도메인 분석

### 📍 주문(Order) 도메인 생성 단계
<img src="https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/37c14557-42ba-4f36-93be-082e74d57f30" width="700" height="auto">


<br>

### 📍 주문(Order) 도메인 취소
<img src="https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/0f4eccb2-e835-4d0e-ad8c-4accaecb265f" width="700" height="auto">


<br>

### 📍 주문(Order) 도메인 상태 관리
![상태관리](https://github.com/smRt-Egg/BE-05-smRt-store/assets/78072370/76463534-648b-4804-9442-599a75180bca)

<br>

## 📚 기술 스택
<table>
<ul>  
  <img src="https://img.shields.io/badge/java 17-FF4800?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/spring 3.2-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql 8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security 3.2-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
  <img src="https://img.shields.io/badge/JWT 4.4-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white"><br>
  <img src="https://img.shields.io/badge/QueryDsl 5.0-4169E1?style=for-the-badge&logo=QueryDsl&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
</ul>
</table>

<br>

## ⚙️ 아키텍처
### Server
![image](https://github.com/smRt-Egg/BE-05-smRt-store/assets/57834671/499b5413-a22c-41cb-8f2c-14b48d9007a0)

<br>

### ERD
![image](https://github.com/smRt-Egg/BE-05-smRt-store/assets/57834671/28a56593-298f-4bb4-8d2f-c75be1203e1e)

<br>

# 👨‍👩‍👧‍👦 팀원 소개
| Name | <center>[소승수](https://github.com/voidmelody)</center>| <center>[김용상](https://github.com/YongNyeo)</center> | <center>[김주환](https://github.com/happyjamy)</center> | <center>[이경민](https://github.com/tidavid1)</center> | <center>[임수진](https://github.com/suzzingv)</center> | <center>[홍지인](https://github.com/JIN-076)</center> |
| --- | --- | --- | --- | --- | --- | --- |
| Profile | <img width="100px" src="https://github.com/voidmelody.png" /> | <img width="100px" src="https://github.com/YongNyeo.png" /> | <img width="100px" src="https://github.com/happyjamy.png" /> | <img width="100px" src="https://github.com/tidavid1.png" /> | <img width="100px" src="https://github.com/suzzingv.png" /> | <img width="100px" src="https://github.com/JIN-076.png" /> |
| Role | `#Dev` | `#Dev` | `#PO` `#Dev` | `#SM` `#Dev` | `#Dev` | `#Dev` |
| Domain | `infra` <br> `keep` <br> `qna` <br> `review` | `infra` <br> `coupon` | `infra` <br> `order` | `security` <br> `cart` <br> `review` <br> `product` | `security` <br> `user` | `security` <br> `point` |

<br>






