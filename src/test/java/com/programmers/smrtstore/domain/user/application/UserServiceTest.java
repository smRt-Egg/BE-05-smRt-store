package com.programmers.smrtstore.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.programmers.smrtstore.domain.auth.application.AuthService;
import com.programmers.smrtstore.domain.auth.application.dto.req.SignUpRequest;
import com.programmers.smrtstore.domain.auth.infrastructure.AuthJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.CreateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.CreateShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthJpaRepository authJpaRepository;

    SignUpRequest kazuha = SignUpRequest.builder()
        .username("kazuha")
        .password("1234")
        .age(25)
        .nickName("카즈하")
        .email("kazuha@naver.com")
        .phone("01012345678")
        .birth("20000311")
        .gender(Gender.MALE)
        .role(Role.ROLE_USER)
        .marketingAgree(true)
        .membershipYn(false)
        .repurchaseYn(false)
        .build();

    Long kazuhaId;

    CreateShippingRequest request1 = new CreateShippingRequest(
        "집", "카즈하", "서울", "광진구", "12345",
        "01000000000", null, false
    );
    CreateShippingRequest request2 = new CreateShippingRequest(
        "학교", "카즈하", "서울", "광진구", "12345",
        "01000000000", "01012345678", false
    );
    CreateShippingRequest request3 = new CreateShippingRequest(
        "회사", "카즈하", "경기도", "분당시", "12345",
        "01000000000", "01012345678", false
    );
    CreateShippingRequest request4 = new CreateShippingRequest(
        "동방", "라이덴", "경기도", "분당시", "12345",
        "01000000000", "01012345678", true
    );
    CreateShippingRequest request5 = new CreateShippingRequest(
        "동방", "푸리나", "경기도", "분당시", "12345",
        "01000000000", "01012345678", true
    );

    @BeforeEach
    public void beforeEach() {
        kazuhaId = authService.signUp(kazuha).getId();
    }

    @AfterEach
    public void afterEach() {
        authJpaRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("배송지를 추가할 수 있다.")
    void createShippingAddress() {
        CreateShippingResponse response = userService.createShippingAddress(kazuhaId, request1);

        assertThat(response.getName()).isEqualTo(request1.getName());
        assertThat(response.getRecipient()).isEqualTo(request1.getRecipient());
        assertThat(response.getAddress1Depth()).isEqualTo(request1.getAddress1Depth());
        assertThat(response.getAddress2Depth()).isEqualTo(request1.getAddress2Depth());
        assertThat(response.getZipCode()).isEqualTo(request1.getZipCode());
        assertThat(response.getPhoneNum1()).isEqualTo(request1.getPhoneNum1());
        assertThat(response.getPhoneNum2()).isEqualTo(request1.getPhoneNum2());
        assertThat(response.isDefaultYn()).isEqualTo(request1.isDefaultYn());
    }

    @Test
    @DisplayName("user가 가지고 있는 배송지 목록을 조회할 수 있다.")
    void getShippingAddressList() {
        userService.createShippingAddress(kazuhaId, request1);
        userService.createShippingAddress(kazuhaId, request2);
        userService.createShippingAddress(kazuhaId, request3);
        userService.createShippingAddress(kazuhaId, request4);

        DeliveryAddressBook response = userService.getShippingAddressList(kazuhaId);

        assertThat(userRepository.findById(1L).get().getShippingAddresses().size()).isEqualTo(4);
        assertThat(response.getDefaultDeliveryAddress().getRecipient()).isEqualTo("라이덴");
        assertThat(response.getDeliveryAddresses().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("수정 시 배송지 정보를 배송지 id로 조회할 수 있다.")
    void findByShippingId() {
        CreateShippingResponse response = userService.createShippingAddress(kazuhaId, request1);

        CreateShippingResponse byShippingId = userService.findByShippingId(response.getId());

        assertThat(byShippingId.getName()).isEqualTo(request1.getName());
        assertThat(byShippingId.getRecipient()).isEqualTo(request1.getRecipient());
        assertThat(byShippingId.getAddress1Depth()).isEqualTo(request1.getAddress1Depth());
        assertThat(byShippingId.getAddress2Depth()).isEqualTo(request1.getAddress2Depth());
        assertThat(byShippingId.getZipCode()).isEqualTo(request1.getZipCode());
        assertThat(byShippingId.getPhoneNum1()).isEqualTo(request1.getPhoneNum1());
        assertThat(byShippingId.getPhoneNum2()).isEqualTo(request1.getPhoneNum2());
        assertThat(byShippingId.isDefaultYn()).isEqualTo(request1.isDefaultYn());
    }
}