package com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_EXTRA_PRICE_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_ORG_PRICE_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_QUANTITY_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_OPTION_NOT_MATCH;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.OrderSheet;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.exception.OrderedProductException;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ordered_product_TB")
@Entity
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // TODO: Setter 때문에 cascade 를 없앨지 고민
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_sheet_id")
    private OrderSheet orderSheet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductDetailOption productOption;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "extra_price")
    private Integer extraPrice;

    @Getter
    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "org_price")
    private Integer orgPrice;

    @Column(name = "immediate_discount")
    private Integer immediateDiscount;

    @Column(name = "coupon_discount", nullable = true)
    private Integer couponDiscount;

    @Column(name = "point_discount", nullable = true)
    private Integer pointDiscount;

    @Builder
    public OrderedProduct(
        Long id, OrderSheet orderSheet, Product product,
        ProductDetailOption productOption, Integer quantity, Integer extraPrice, Integer orgPrice,
        Integer immediateDiscount, Integer couponDiscount, Integer pointDiscount
    ) {
        this.id = id;
        this.orderSheet = orderSheet;
        this.product = product;
        this.productOption = productOption;
        this.quantity = quantity;
        this.extraPrice = extraPrice;
        this.orgPrice = orgPrice;
        this.immediateDiscount = immediateDiscount;
        this.totalPrice = getCalculatedTotalPrice();
        this.couponDiscount = couponDiscount;
        this.pointDiscount = pointDiscount;
        validateMatchingProductAndOption(product.getId(), productOption.getProduct().getId());
        validateQuantity(quantity);
        validateExtraPrice(extraPrice);
        validateOrgPrice(orgPrice);
        validateImmediateDiscount(immediateDiscount);
        validateCouponDiscount(couponDiscount);
        validatePointDiscount(pointDiscount);
    }

    public static OrderedProduct createBeforeOrder(
        Product product, ProductDetailOption productOption, Integer extraPrice,
        Integer quantity, Integer orgPrice, Integer immediateDiscount
    ) {
        return OrderedProduct.builder()
            .product(product)
            .productOption(productOption)
            .extraPrice(extraPrice)
            .quantity(quantity)
            .orgPrice(orgPrice)
            .immediateDiscount(immediateDiscount)
            .couponDiscount(null)
            .pointDiscount(null)
            .build();
    }

    private void validateExtraPrice(Integer extraPrice) {
        if (extraPrice < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_EXTRA_PRICE_INVALID);
        }
    }

    private void validateMatchingProductAndOption(
        Long productId, Long productIdInProductOption
    ) {
        if (!productId.equals(productIdInProductOption)) {
            throw new OrderedProductException(PRODUCT_OPTION_NOT_MATCH);
        }
    }

    private void validateQuantity(Integer quantity) {
        // 수량 자체가 유효한지
        if (quantity <= 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_QUANTITY_INVALID);
        }
        // 현재 재고 내에서 수량이 유효한지
        if (productOption.isAvailableOrder(quantity)) {
            throw new OrderedProductException(ORDERED_PRODUCT_QUANTITY_INVALID);
        }
    }

//    private void validateTotalPrice(Integer totalPrice) {
//        if (totalPrice < 0) {
//            throw new OrderedProductException(ORDERED_PRODUCT_TOTAL_PRICE_INVALID);
//        }
//        Integer calTotalPrice = getCalculatedTotalPrice();
//        if (!calTotalPrice.equals(this.totalPrice)) {
//            throw new OrderedProductException(ORDERED_PRODUCT_TOTAL_PRICE_INVALID);
//        }
//    }

    private void validateOrgPrice(Integer orgPrice) {
        if (orgPrice < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_ORG_PRICE_INVALID);
        }
    }

    private void validateImmediateDiscount(Integer immediateDiscountRatio) {
        if (immediateDiscountRatio <= 0 || immediateDiscountRatio >= 100) {
            throw new OrderedProductException(ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID);
        }
    }

    // TODO: 생성자에서 null 만 받고 Update 고려 안할지 생각
    private void validateCouponDiscount(Integer couponDiscount) {
        if (couponDiscount == null) {
            return;
        }
        if (couponDiscount < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID);
        }
    }

    private void validatePointDiscount(Integer pointDiscount) {
        if (pointDiscount == null) {
            return;
        }
        if (pointDiscount < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID);
        }
    }

    // OrderedProduct 에 있는 가격 정보들로 total price 를 계산한 금액
    private Integer getCalculatedTotalPrice() {
        return getOptionAppliedPriceAfterCouponAndPoint() * this.quantity;
    }

    // 즉시할인 된 금액에서 옵션 추가금까지 붙인 후 쿠폰 할인 금액, 포인트 할인 금액을 뺀 금액
    private Integer getOptionAppliedPriceAfterCouponAndPoint() {
        Integer optionAppliedPrice = getOptionAppliedPrice();
        if (this.couponDiscount != null) {
            optionAppliedPrice -= this.couponDiscount;
        }
        if (this.pointDiscount != null) {
            optionAppliedPrice -= this.pointDiscount;
        }
        return optionAppliedPrice;
    }

    // 즉시할인 된 금액에서 옵션 추가금까지 붙인 금액
    private Integer getOptionAppliedPrice() {
        return getProductSalePrice() + this.extraPrice;
    }

    // 즉시할인 된 금액
    private Integer getProductSalePrice() {
        return this.orgPrice - this.immediateDiscount;
    }

}
