package com.programmers.smrtstore.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.programmers.smrtstore.domain.auth.application.AuthService;
import com.programmers.smrtstore.domain.auth.application.dto.req.SignUpRequest;
import com.programmers.smrtstore.domain.auth.infrastructure.AuthJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.ShippingAddressJpaRepository;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import com.programmers.smrtstore.domain.user.presentation.controller.UserFacade;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateShippingRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailShippingResponse;
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
    UserFacade userFacade;

    @Autowired
    AuthService authService;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    AuthJpaRepository authJpaRepository;

    @Autowired
    ShippingAddressJpaRepository shippingAddressJpaRepository;

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
        .build();

    Long kazuhaId;

    DetailShippingRequest request1 = new DetailShippingRequest(
        "집", "카즈하", "서울", "광진구", "12345",
        "01000000000", null, false
    );
    DetailShippingRequest request2 = new DetailShippingRequest(
        "학교", "나히다", "서울", "광진구", "12345",
        "01000000000", "01012345678", false
    );
    DetailShippingRequest request3 = new DetailShippingRequest(
        "회사", "야에 미코", "경기도", "분당시", "12345",
        "01000000000", "01012345678", false
    );
    DetailShippingRequest request4 = new DetailShippingRequest(
        "동방", "라이덴", "경기도", "분당시", "12345",
        "01000000000", "01012345678", true
    );
    DetailShippingRequest request5 = new DetailShippingRequest(
        "동방", "푸리나", "경기도", "분당시", "12345",
        "01000000000", "01012345678", true
    );

    UpdateShippingRequest request6 = new UpdateShippingRequest(
        "학교", "나히다", "서울", "광진구", "12345",
        "01000000000", "01012345678", false
    );
    UpdateShippingRequest request7 = new UpdateShippingRequest(
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
        userJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("배송지를 추가할 수 있다.")
    void createShippingAddress() {
        DetailShippingResponse response = userFacade.createShippingAddress(kazuhaId, request1);

        assertThat(shippingAddressJpaRepository.findById(response.getId())).isNotNull();
        assertThat(response.getName()).isEqualTo(request1.getName());
        assertThat(response.getRecipient()).isEqualTo(request1.getRecipient());
        assertThat(response.getAddress1Depth()).isEqualTo(request1.getAddress1Depth());
        assertThat(response.getAddress2Depth()).isEqualTo(request1.getAddress2Depth());
        assertThat(response.getZipCode()).isEqualTo(request1.getZipCode());
        assertThat(response.getPhoneNum1()).isEqualTo(request1.getPhoneNum1());
        assertThat(response.getPhoneNum2()).isEqualTo(request1.getPhoneNum2());
        assertThat(response.getIsDefaultYn()).isEqualTo(request1.getDefaultYn());
    }

    @Test
    @DisplayName("기본 배송지 등록 시 갱신")
    void createDefaultShippingAddress() {
        DetailShippingResponse response4 = userFacade.createShippingAddress(kazuhaId, request4);

        DetailShippingResponse response5 = userFacade.createShippingAddress(kazuhaId,
            request5);

        assertThat(shippingAddressJpaRepository.findById(response4.getId()).get().getDefaultYn()).isFalse();
        assertThat(shippingAddressJpaRepository.findById(response5.getId()).get().getDefaultYn()).isTrue();
    }

    @Test
    @DisplayName("user가 가지고 있는 배송지 목록을 조회할 수 있다.")
    void getShippingAddressList() {
        userFacade.createShippingAddress(kazuhaId, request1);
        userFacade.createShippingAddress(kazuhaId, request2);
        userFacade.createShippingAddress(kazuhaId, request3);
        userFacade.createShippingAddress(kazuhaId, request4);

        DeliveryAddressBook response = userFacade.getShippingAddressList(kazuhaId);

        assertThat(userJpaRepository.findById(kazuhaId).get().getShippingAddresses().size()).isEqualTo(4);
        assertThat(response.getDefaultDeliveryAddress().getRecipient()).isEqualTo("라이덴");
        assertThat(response.getDeliveryAddresses().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("수정 시 배송지 정보를 배송지 id로 조회할 수 있다.")
    void findByShippingId() {
        DetailShippingResponse response = userFacade.createShippingAddress(kazuhaId, request1);

        DetailShippingResponse byShippingId = userFacade.findByShippingId(response.getId());

        assertThat(byShippingId.getName()).isEqualTo(request1.getName());
        assertThat(byShippingId.getRecipient()).isEqualTo(request1.getRecipient());
        assertThat(byShippingId.getAddress1Depth()).isEqualTo(request1.getAddress1Depth());
        assertThat(byShippingId.getAddress2Depth()).isEqualTo(request1.getAddress2Depth());
        assertThat(byShippingId.getZipCode()).isEqualTo(request1.getZipCode());
        assertThat(byShippingId.getPhoneNum1()).isEqualTo(request1.getPhoneNum1());
        assertThat(byShippingId.getPhoneNum2()).isEqualTo(request1.getPhoneNum2());
        assertThat(byShippingId.getIsDefaultYn()).isEqualTo(request1.getDefaultYn());
    }

    @Test
    @DisplayName("기본 배송지가 아닌 배송지를 삭제할 수 있다.")
    void deleteNotDefaultShippingAddress() {
        DetailShippingResponse response1 = userFacade.createShippingAddress(kazuhaId, request1);
        userFacade.createShippingAddress(kazuhaId, request2);
        userFacade.createShippingAddress(kazuhaId, request3);
        userFacade.createShippingAddress(kazuhaId, request4);

        userFacade.deleteShippingAddress(kazuhaId, response1.getId());

        assertThat(userJpaRepository.findById(kazuhaId).get().getShippingAddresses().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("기본 배송지인 배송지를 삭제할 수 없다.")
    void deleteDefaultShippingAddress() {
        userFacade.createShippingAddress(kazuhaId, request1);
        userFacade.createShippingAddress(kazuhaId, request2);
        userFacade.createShippingAddress(kazuhaId, request3);
        DetailShippingResponse response4 = userFacade.createShippingAddress(kazuhaId,
            request4);

        assertThatThrownBy(() -> userFacade.deleteShippingAddress(kazuhaId, response4.getId()))
            .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("기본 배송지를 수정할 수 있다.")
    void updateDefaultShippingAddress() {
        DetailShippingResponse response4 = userFacade.createShippingAddress(kazuhaId,
            request4);

        userFacade.updateShippingAddress(kazuhaId, response4.getId(), request7);

        assertThat(shippingAddressJpaRepository.findById(response4.getId()).get().getRecipient()).isEqualTo("푸리나");
        assertThat(shippingAddressJpaRepository.findById(response4.getId()).get().getDefaultYn()).isTrue();
    }

    @Test
    @DisplayName("기본 배송지가 아닌 배송지를 기본 배송지로 수정할 수 있다.")
    void updateNotDefaultToDefaultShippingAddress() {
        DetailShippingResponse response1 = userFacade.createShippingAddress(kazuhaId,
            request1);

        userFacade.updateShippingAddress(kazuhaId, response1.getId(), request7);

        assertThat(shippingAddressJpaRepository.findById(response1.getId()).get().getRecipient()).isEqualTo("푸리나");
        assertThat(shippingAddressJpaRepository.findById(response1.getId()).get().getDefaultYn()).isTrue();
    }

    @Test
    @DisplayName("기본 배송지가 아닌 배송지를 기본배송지가 아닌 배송지로 수정할 수 있다.")
    void updateNotDefaultToNotDefaultShippingAddress() {
        DetailShippingResponse response1 = userFacade.createShippingAddress(kazuhaId,
            request1);

        userFacade.updateShippingAddress(kazuhaId, response1.getId(), request6);

        assertThat(shippingAddressJpaRepository.findById(response1.getId()).get().getRecipient()).isEqualTo("나히다");
        assertThat(shippingAddressJpaRepository.findById(response1.getId()).get().getDefaultYn()).isFalse();
    }
}