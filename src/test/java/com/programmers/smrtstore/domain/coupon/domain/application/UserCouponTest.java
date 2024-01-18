package com.programmers.smrtstore.domain.coupon.domain.application;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponPublicationType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CustomerManageBenefitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.vo.CouponValue;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.domain.enums.Gender;
import com.programmers.smrtstore.domain.user.domain.enums.Role;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RedisTestConfig.class)
public class UserCouponTest {

    private static User user;
    private static Coupon coupon;

    private static CouponValue couponValue;

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CouponJpaRepository couponJpaRepository;
    @Autowired
    private CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;

    @BeforeEach
    public void setUserAndCoupon() {

        user = User.builder().
                nickName("k")
                .birth("19980101")
                .email("test@example.com")
                .phone("123-4567-8901")
                .gender(Gender.MALE)
                .role(Role.ROLE_ADMIN)
                .point(10)
                .marketingAgree(true)
                .membershipYn(true)
                .createdAt(LocalDateTime.now())
                .build();

        userJpaRepository.save(user);
    }


    @Test
    @DisplayName("마이페이지의 내 쿠폰 리스트를 성공적으로 불러온다")
    public void getCouponsByUserIdSuccess() {
        //given
        couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        coupon = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.CART)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.now())
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();
        couponJpaRepository.save(coupon);
        couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));

        //when
        List<Coupon> userCoupons = couponJpaRepository.findUserCoupons(user.getId());

        //then
        Assertions.assertThat(userCoupons.size()).isEqualTo(1);

        Assertions.assertThat(userCoupons.get(0).getId()).isGreaterThan(1L);
        Assertions.assertThat(userCoupons.get(0).getCouponValue().getName()).isEqualTo("Example Coupon");
    }

    @Test
    @DisplayName("유저의 잔여 쿠폰 개수를 성공적으로 불러온다")
    public void getUserCouponQuantitySuccess() {

        //given
        couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        coupon = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.CART)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.now())
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        Coupon coupon1 = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.CART)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.now())
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        couponJpaRepository.save(coupon);
        couponJpaRepository.save(coupon1);
        couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));
        couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon1, user));

        //when
        Long userCouponCount = couponJpaRepository.findUserCouponCount(user.getId());

        //then
        Assertions.assertThat(userCouponCount).isEqualTo(2);
    }




}
