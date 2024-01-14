package com.programmers.smrtstore.domain.coupon.domain.application;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import com.programmers.smrtstore.domain.coupon.application.ProductCouponService;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableProduct;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponPublicationType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CustomerManageBenefitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.vo.CouponValue;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableProductJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.facade.CouponQuantityFacade;
import com.programmers.smrtstore.domain.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.ProductCouponResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductQuantity;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1. download() 에 대한 검증 테스트 -> 478 Line 까지
 * <p>
 * 2. ProductDetail Page의 CouponList()에 대한 검증 테스트 -> 479 Line부터~
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RedisTestConfig.class)
@Transactional
public class ProductCouponTest {

    private static User user;
    private static Coupon coupon;

    private static Product product;

    private static CouponValue couponValue;

    @Autowired
    private CouponQuantityFacade couponQuantityFacade;
    @Autowired
    private ProductCouponService productCouponService;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CouponJpaRepository couponJpaRepository;
    @Autowired
    private CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;
    @Autowired
    private CouponAvailableProductJpaRepository couponAvailableProductJpaRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setProductAndUserAndCoupon() throws MalformedURLException {

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

        product = Product.builder()
                .name("Test Product")
                .price(100_000)
                .category(Category.CLOTHES)
                .combinationYn(false)
                .thumbnail(new URL("http://example.com/thumbnail.jpg"))
                .contentImage(new URL("http://example.com/content.jpg"))
                .build();

    }


    @Test
    @DisplayName("user가 product 페이지에서 Download 에 성공한다.")
    public void downloadSuccess() {
        //given
        couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
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

        User savedUser = userJpaRepository.save(user);
        Coupon savedCoupon = couponJpaRepository.save(coupon1);
        Long couponId = savedCoupon.getId();

        //when
        Long downloadId = productCouponService.download(new SaveCouponRequest(couponId), savedUser.getId());

        //then
        Assertions.assertThat(downloadId).isEqualTo(savedCoupon.getId());

    }

    @Test
    @DisplayName("존재하지 않는 쿠폰 다운로드 실행시 예외가 발생한다.")
    public void downloadFailNotFoundCoupon() {
        //given
        User savedUser = userJpaRepository.save(user);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(1234L), savedUser.getId())
        ).isInstanceOf(CouponException.class);


    }

    @Test
    @DisplayName("존재하지 않는 유저가 다운로드 실행시 예외가 발생한다")
    public void downloadFailNotFoundUser() {
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

        Coupon savedCoupon = couponJpaRepository.save(coupon);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), 1245L)
        ).isInstanceOf(UserException.class);


    }

    @Test
    @DisplayName("Membership이 아닌 user는 MembershipCoupon을 다운로드하면 예외가 발생한다.")
    public void downloadFailMembershipCoupon() {
        //given
        boolean userMembership = false;
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
        Coupon savedCoupon = couponJpaRepository.save(coupon);

        user = User.builder().
                nickName("k")
                .birth("19980101")
                .email("test@example.com")
                .phone("123-4567-8901")
                .gender(Gender.MALE)
                .role(Role.ROLE_ADMIN)
                .point(10)
                .marketingAgree(true)
                .membershipYn(userMembership)
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userJpaRepository.save(user);

        //when&& then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId())
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("유효기간이 지난 쿠폰을 다운로드할 경우 예외가 발생한다.")
    public void downloadFailInvalidEndDate() {
        LocalDateTime time = LocalDateTime.of(2022, 1, 1, 1, 1).plusDays(30);

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
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(time)
                .couponQuantity(CouponQuantity.from(100))
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);

        User savedUser = userJpaRepository.save(user);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId())
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("무효화된 쿠폰을 다운로드하면 예외가 발생한다.")
    public void downloadFailInvalidCoupon() {
        boolean available = false;
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
                .availableYn(available)
                .couponType(CouponType.CART)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);
        User savedUser = userJpaRepository.save(user);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId())
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("쿠폰 재다운로드 시, 발급 횟수를 초과하면 예외가 발생한다. ")
    public void downloadFailReIssueExceed() {
        Integer issuableCount = 0;
        couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(issuableCount)
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
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        User savedUser = userJpaRepository.save(user);
        Coupon savedCoupon = couponJpaRepository.save(coupon);

        CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(savedCoupon, savedUser));
        savedCouponAvailableUser.useCoupon(); //쿠폰을 사용한다

        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId())
        ).isInstanceOf(CouponException.class);


    }

    @Test
    @DisplayName("쿠폰 재다운로드 시, 사용하지 않은 동일한 쿠폰이 있다면 예외가 발생한다.")
    public void downloadFailExistCoupon() {
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
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);
        User savedUser = userJpaRepository.save(user);

        CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(savedCoupon, savedUser));


        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId())
        ).isInstanceOf(CouponException.class);


    }

    @Test
    @DisplayName("쿠폰 다운로드시 수량이 1씩 감소하는데 성공한다.")
    public void couponQuantityDecreaseSuccess() {
        CouponQuantity couponQuantity = CouponQuantity.from(100);
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
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(couponQuantity)
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);
        User savedUser = userJpaRepository.save(user);

        //when
        productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId());

        //then
        Assertions.assertThat(savedCoupon.getCouponQuantity().getValue())
                .isEqualTo(99);


    }

    @Test
    @DisplayName("쿠폰 수량이 0 이하일 때 다운로드하면 예외가 발생한다.")
    public void couponQuantityDecreaseFail() {
        CouponQuantity couponQuantity = CouponQuantity.from(0);
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
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(couponQuantity)
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);
        User savedUser = userJpaRepository.save(user);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> productCouponService.download(new SaveCouponRequest(savedCoupon.getId()), savedUser.getId())
        ).isInstanceOf(CouponException.class);


    }

    @Test
    @DisplayName("product detail 페이지에서 쿠폰 리스트 불러오기를 성공한다")
    public void getCouponByProductIdAndUserIdSuccess() {
        CouponQuantity couponQuantity = CouponQuantity.from(0);
        CouponQuantity couponQuantity2 = CouponQuantity.from(0);
        couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();
        CouponValue couponValue2 = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(10000)
                .maxDiscountValue(10000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        coupon = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.PRODUCT)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(couponQuantity)
                .build();

        Coupon coupon1 = Coupon.builder()
                .couponValue(couponValue2)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.CART)
                .benefitUnitType(BenefitUnitType.AMOUNT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(couponQuantity2)
                .build();

        Product savedProduct = productJpaRepository.save(product);
        Coupon savedCoupon = couponJpaRepository.saveAndFlush(coupon);
        Coupon savedCoupon1 = couponJpaRepository.saveAndFlush(coupon1);
        User savedUser = userJpaRepository.save(user);
        couponAvailableProductJpaRepository.saveAndFlush(CouponAvailableProduct.of(savedCoupon, savedProduct));
//        couponAvailableProductJpaRepository.save(CouponAvailableProduct.of(savedCoupon1, savedProduct));
        em.clear();
        ProductCouponResponse couponByProductIdAndUserId = productCouponService.getCouponByProductIdAndUserId(savedProduct.getId(), savedUser.getId());

        Assertions.assertThat(couponByProductIdAndUserId.getIssuableCoupons().size()).isEqualTo(1);
        Assertions.assertThat(couponByProductIdAndUserId.getMaxDiscountCoupons().getTotalDiscountAmount()).isEqualTo(15000);


    }

//    @Test
//    @DisplayName("쿠폰 수량이 동시성 테스트")
//    public void couponQuantityConcurrency() throws InterruptedException {
//        CouponQuantity couponQuantity = CouponQuantity.from(100);
//        couponValue = CouponValue.builder()
//                .name("Example Coupon")
//                .content("Coupon content")
//                .benefitValue(15L)
//                .maxDiscountValue(5000L)
//                .minOrderPrice(20000L)
//                .idPerIssuableCount(5)
//                .build();
//
//        coupon = Coupon.builder()
//                .couponValue(couponValue)
//                .membershipCouponYn(true)
//                .duplicationYn(false)
//                .availableYn(true)
//                .couponType(CouponType.CART)
//                .benefitUnitType(BenefitUnitType.PERCENT)
//                .customerManageBenefitType(CustomerManageBenefitType.ALL)
//                .couponPublicationType(CouponPublicationType.ALLOCATE)
//                .validPeriodStartDate(LocalDateTime.of(2022, 1, 1, 1, 1))
//                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
//                .couponQuantity(couponQuantity)
//                .build();
//
//        Coupon savedCoupon = couponJpaRepository.saveAndFlush(coupon);
//        User savedUser = userJpaRepository.saveAndFlush(user);
//        System.out.println(savedCoupon.getCouponQuantity().getValue());
//        em.flush();}
//        em.clear();
//        //when&&then
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(threadCount);

//        for (int i = 0; i < 150; i++) {
//            executorService.execute(() -> {
//                        try {
//                            int size = couponJpaRepository.findAll().size();
////                            couponQuantityFacade.decrease(savedCoupon.getId());
//                        } finally {
//                            latch.countDown();
//                        }
//                    }
//            );
//        }
//        latch.await();
//        System.out.println(savedCoupon.getCouponQuantity().getValue());
//

//    }
//    @Test
//    @DisplayName("")


//    @Test
//    @DisplayName("상품(단품)의 가격이 Coupon의 최소주문금액보다 적다면 Coupon 적용에 실패한다.")
//    public void getCouponsFailMinPrice() {
//        //given
//        couponValue = CouponValue.builder()
//                .name("Example Coupon")
//                .content("Coupon content")
//                .benefitValue(15L)
//                .maxDiscountValue(5000L)
//                .minOrderPrice(1000L)
//                .idPerIssuableCount(5)
//                .build();
//
//        coupon = Coupon.builder()
//                .couponValue(couponValue)
//                .membershipCouponYn(true)
//                .duplicationYn(false)
//                .availableYn(true)
//                .couponType(CouponType.CART)
//                .benefitUnitType(BenefitUnitType.PERCENT)
//                .customerManageBenefitType(CustomerManageBenefitType.ALL)
//                .couponPublicationType(CouponPublicationType.ALLOCATE)
//                .validPeriodStartDate(LocalDateTime.now())
//                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
//                .couponQuantity(CouponQuantity.from(100))
//                .build();
//        Coupon savedCoupon = couponJpaRepository.save(coupon);
//
//        userJpaRepository.save(user);
//    }
//    @Test
//    @DisplayName()
//    public void
}
