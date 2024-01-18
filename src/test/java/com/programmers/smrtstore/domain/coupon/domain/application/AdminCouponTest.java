
package com.programmers.smrtstore.domain.coupon.domain.application;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import com.programmers.smrtstore.domain.coupon.application.AdminCouponService;
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
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponAvailableProductRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponAvailableUserRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.domain.enums.Gender;
import com.programmers.smrtstore.domain.user.domain.enums.Role;
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
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RedisTestConfig.class)
public class AdminCouponTest {

    private static CreateCouponRequest createCouponRequest;
    private static Product product;
    private static User user;

    @Autowired
    private EntityManager em;
    @Autowired
    private AdminCouponService adminCouponService;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private CouponJpaRepository couponJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CouponAvailableProductJpaRepository couponAvailableProductJpaRepository;
    @Autowired
    private CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;

    @BeforeEach
    public void 테스트전_쿠폰_데이터_생성() throws MalformedURLException {
        createCouponRequest = new CreateCouponRequest(
                "TestCoupon",
                "TestCouponContent",
                100,
                50000,
                200,
                5,
                true,
                true,
                true,
                CouponType.CART,
                BenefitUnitType.PERCENT,
                CustomerManageBenefitType.ALL,
                CouponPublicationType.DOWNLOAD,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                100
        );

        product = Product.builder()
                .name("Test Product")
                .price(100_000)
                .category(Category.CLOTHES)
                .combinationYn(false)
                .thumbnail(String.valueOf(new URL("http://example.com/thumbnail.jpg")))
                .contentImage(String.valueOf(new URL("http://example.com/content.jpg")))
                .build();

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

    }

    @Test
    @DisplayName("정상적으로 쿠폰을 생성한다.")
    public void createCouponSuccess() {
        //given&&when
        Long couponId = adminCouponService.createCoupon(createCouponRequest);

        //then
        Assertions.assertThat(couponId).isNotNull();
    }

    @Test
    @DisplayName("퍼센트 할인 쿠폰은 할인율은 100을 초과하면 예외가 발생한다.")
    public void createCouponFailPercentValue() {
        //given
        CreateCouponRequest request = new CreateCouponRequest(
                "TestCoupon",
                "TestCouponContent",
                150,
                50000,
                200,
                5,
                true,
                true,
                true,
                CouponType.CART,
                BenefitUnitType.PERCENT,
                CustomerManageBenefitType.ALL,
                CouponPublicationType.DOWNLOAD,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                100
        );
        //when&&then
        Assertions.assertThatThrownBy(
                () -> adminCouponService.createCoupon(request)
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("쿠폰의 할인값은 최대할인값을 초과하면 예외가 발생한다.")
    public void createCouponFailMaxBenefitValue() {
        //given
        Integer benefitValue = 150000;
        Integer maxDiscountValue = 1000;
        CreateCouponRequest request = new CreateCouponRequest(
                "TestCoupon",
                "TestCouponContent",
                benefitValue,
                maxDiscountValue,
                200,
                5,
                true,
                true,
                true,
                CouponType.CART,
                BenefitUnitType.AMOUNT,
                CustomerManageBenefitType.ALL,
                CouponPublicationType.DOWNLOAD,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                100
        );
        //when&&then
        Assertions.assertThatThrownBy(
                () -> adminCouponService.createCoupon(request)
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("저장된 Coupon리스트를 불러오는데 성공한다.")
    public void getCouponsSuccess() {

        //given
        adminCouponService.createCoupon(createCouponRequest);

        //when
        List<CouponResponse> coupons = adminCouponService.getCoupons();

        //then
        Assertions.assertThat(coupons.size()).isGreaterThanOrEqualTo(1);

    }

    @Test
    @DisplayName("상품에 쿠폰을 적용하는데 성공한다.")
    public void addCouponToProductSuccess() throws MalformedURLException {

        //given
        Product savedProduct = productJpaRepository.save(product);

        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.PRODUCT)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.now())
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);

        //when
        Long save = adminCouponService.addCouponToProduct(savedCoupon.getId(), new CreateCouponAvailableProductRequest(savedProduct.getId()));

        //then
        Assertions.assertThat(save).isNotNull();

    }

    @Test
    @DisplayName("상품에 이미 쿠폰이 적용되어있을 때 또 추가하면 예외가 발생한다. ")
    public void addCouponToProductFailAlready() throws MalformedURLException {
        //given
        Product savedProduct = productJpaRepository.save(product);

        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.PRODUCT)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.now())
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);
        couponAvailableProductJpaRepository.save(CouponAvailableProduct.of(savedCoupon, savedProduct));

        //when&&then
        Assertions.assertThatThrownBy(
                () -> {
                    adminCouponService.addCouponToProduct(savedCoupon.getId(), new CreateCouponAvailableProductRequest(savedProduct.getId()));
                }
        ).isInstanceOf(CouponException.class);


    }

    @Test
    @DisplayName("유저에게 쿠폰을 지급하는데 성공한다.")
    public void addCouponToUserSuccess() throws MalformedURLException {

        //given
        User savedUser = userJpaRepository.save(user);

        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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

        //when
        Long save = adminCouponService.addCouponToUser(savedCoupon.getId(), new CreateCouponAvailableUserRequest(savedUser.getId()));

        //then
        Assertions.assertThat(save).isNotNull();

    }

    @Test
    @DisplayName("유저가 쿠폰을 보유하고 있을 때 추가로 지급하면 예외가 발생한다. ")
    public void addCouponToUserFailAlready() throws MalformedURLException {
        //given
        User savedUser = userJpaRepository.save(user);

        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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
        couponAvailableUserJpaRepository.save(CouponAvailableUser.of(savedCoupon, savedUser));

        //when&&then
        Assertions.assertThatThrownBy(
                () -> {
                    adminCouponService.addCouponToUser(savedCoupon.getId(), new CreateCouponAvailableUserRequest(savedUser.getId()));
                }
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("유효한 쿠폰을 무효화하는데 성공한다.")
    public void makeAvailableNoCouponSuccess() {
        //given
        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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

        User savedUser = userJpaRepository.save(user);

        //when
        Long result = adminCouponService.makeAvailableYn(savedUser.getId(), savedCoupon.getId(),false);

        //then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유효한 쿠폰을 무효화할때 Admin이 아니라면 예외가 발생한다..")
    public void makeAvailableNoFail() {
        //given
        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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

        User user1 = User.builder().
                nickName("k")
                .birth("19980101")
                .email("test@example.com")
                .phone("123-4567-8901")
                .gender(Gender.MALE)
                .role(Role.ROLE_USER)
                .point(10)
                .marketingAgree(true)
                .membershipYn(true)
                .createdAt(LocalDateTime.now())
                .build();

        Coupon savedCoupon = couponJpaRepository.save(coupon);

        User savedUser = userJpaRepository.save(user1);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> {
                    adminCouponService.makeAvailableYn(savedUser.getId(), savedCoupon.getId(),false);
                }
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("쿠폰 수량을 업데이트하는데 성공한다.")
    public void updateCouponQuantitySuccess() {
        //given
        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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
        Coupon savedCoupon = couponJpaRepository.saveAndFlush(coupon);

        //when
        adminCouponService.updateCouponQuantity(savedCoupon.getId(), 10);

        em.clear();

        //then
        Assertions.assertThat(coupon.getCouponQuantity().getValue()).isEqualTo(10);

    }

    @Test
    @DisplayName("쿠폰 수량을 0 미만으로 업데이트하면 예외가 발생한다.")
    public void updateCouponQuantityFailZeroAmount() {
        //given
        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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
        Coupon savedCoupon = couponJpaRepository.saveAndFlush(coupon);

        //when&&then
        Assertions.assertThatThrownBy(
                () -> {
                    adminCouponService.updateCouponQuantity(savedCoupon.getId(), -1);
                }
        ).isInstanceOf(CouponException.class);

    }

    @Test
    @DisplayName("쿠폰 발급된 유저의 쿠폰을 회수하는데 성공한다.")
    public void removeCouponOfUserSuccess() {
        //given
        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
                .couponValue(couponValue)
                .membershipCouponYn(true)
                .duplicationYn(false)
                .availableYn(true)
                .couponType(CouponType.PRODUCT)
                .benefitUnitType(BenefitUnitType.PERCENT)
                .customerManageBenefitType(CustomerManageBenefitType.ALL)
                .couponPublicationType(CouponPublicationType.ALLOCATE)
                .validPeriodStartDate(LocalDateTime.now())
                .validPeriodEndDate(LocalDateTime.now().plusDays(30))
                .couponQuantity(CouponQuantity.from(100))
                .build();
        Coupon savedCoupon = couponJpaRepository.saveAndFlush(coupon);
        Product savedProduct = productJpaRepository.saveAndFlush(product);

        CouponAvailableProduct saved = couponAvailableProductJpaRepository.save(CouponAvailableProduct.of(savedCoupon, savedProduct));

        //when
        adminCouponService.removeCouponOfProduct(savedCoupon.getId(),savedProduct.getId());

        //then
        Assertions.assertThat(couponAvailableProductJpaRepository.findById(saved.getId()))
                .isEmpty();
    }

    @Test
    @DisplayName("쿠폰이 적용된 Product를 적용 취소 시키는데 성공한다.")
    public void removeCouponOfProductSuccess() {
        //given
        CouponValue couponValue = CouponValue.builder()
                .name("Example Coupon")
                .content("Coupon content")
                .benefitValue(15)
                .maxDiscountValue(5000)
                .minOrderPrice(20000)
                .idPerIssuableCount(5)
                .build();

        Coupon coupon = Coupon.builder()
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
        Coupon savedCoupon = couponJpaRepository.saveAndFlush(coupon);
        User savedUser = userJpaRepository.saveAndFlush(user);

        CouponAvailableUser saved = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(savedCoupon, savedUser));

        //when
        adminCouponService.removeCouponOfUser(savedCoupon.getId(),savedUser.getId());

        //then
        Assertions.assertThat(couponAvailableUserJpaRepository.findById(saved.getId()))
                .isEmpty();
    }

}
