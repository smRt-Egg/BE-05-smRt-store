package com.programmers.smrtstore.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.programmers.smrtstore.domain.auth.application.AuthService;
import com.programmers.smrtstore.domain.auth.application.dto.req.SignUpRequest;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.presentation.dto.req.CreateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.CreateShippingResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
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
        .membershipYN(false)
        .repurchaseYN(false)
        .build();

    @BeforeEach
    public void beforeEach() {
        authService.signUp(kazuha);
    }

    @Test
    @DisplayName("배송지를 추가할 수 있다.")
    void createShippingAddress() {
        CreateShippingRequest request = new CreateShippingRequest(
            "집", "카즈하", "서울", "광진구", "12345",
            "01000000000", null, false
        );
        CreateShippingResponse response = userService.createShippingAddress(1L, request);

        assertThat(request.getName()).isEqualTo(response.getName());
        assertThat(request.getRecipient()).isEqualTo(response.getRecipient());
        assertThat(request.getAddress1Depth()).isEqualTo(response.getAddress1Depth());
        assertThat(request.getAddress2Depth()).isEqualTo(response.getAddress2Depth());
        assertThat(request.getZipCode()).isEqualTo(response.getZipCode());
        assertThat(request.getPhoneNum1()).isEqualTo(response.getPhoneNum1());
        assertThat(request.getPhoneNum2()).isEqualTo(response.getPhoneNum2());
        assertThat(request.isDefaultYN()).isEqualTo(response.isDefaultYN());
    }

    @Test
    @DisplayName("user가 가지고 있는 배송지 목록을 조회할 수 있다.")
    void getShippingAddressList() {
        CreateShippingRequest request1 = new CreateShippingRequest(
            "집", "카즈하", "서울", "광진구", "12345",
            "01000000000", null, true
        );
        CreateShippingRequest request2 = new CreateShippingRequest(
            "학교", "카즈하", "서울", "광진구", "12345",
            "01000000000", "01012345678", false
        );
        CreateShippingRequest request3 = new CreateShippingRequest(
            "회사", "카즈하", "경기도", "분당시", "12345",
            "01000000000", "01012345678", false
        );

        userService.createShippingAddress(1L, request1);
        userService.createShippingAddress(1L, request2);
        userService.createShippingAddress(1L, request3);
        DeliveryAddressBook response = userService.getShippingAddressList(1L);

        assertThat(response.getDefaultDeliveryAddress().getName()).isEqualTo("집");
        assertThat(response.getDeliveryAddresses().size()).isEqualTo(2);
    }

}