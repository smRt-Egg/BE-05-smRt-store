package com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_ORG_PRICE_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_QUANTITY_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_TOTAL_PRICE_INVALID;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_OPTION_NOT_MATCH;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.OrderSheet;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.exception.OrderedProductException;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ordered_product_TB")
@Entity
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_sheet_id")
    private OrderSheet orderSheet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "org_price")
    private Integer orgPrice;

    @Column(name = "immediate_discount")
    private Integer ImmediateDiscountRatio;

    @Column(name = "coupon_discount")
    private Integer couponDiscount;

    @Builder
    public OrderedProduct(
        Long id, OrderSheet orderSheet, Product product, ProductOption productOption,
        Integer quantity, Integer totalPrice, Integer orgPrice, Integer ImmediateDiscountRatio,
        Integer couponDiscount
    ) {
        this.id = id;
        this.orderSheet = orderSheet;
        this.product = product;
        this.productOption = productOption;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orgPrice = orgPrice;
        this.ImmediateDiscountRatio = ImmediateDiscountRatio;
        this.couponDiscount = couponDiscount;
        validateMatchingProductAndOption(product, productOption);
        validateQuantity(quantity);
        validateTotalPrice(totalPrice);
        validateOrgPrice(orgPrice);
        validateImmediateDiscount(ImmediateDiscountRatio);
        validateCouponDiscount(couponDiscount);
    }

    private void validateMatchingProductAndOption(Product product, ProductOption productOption) {
        var productId = product.getId();
        var productIdInOption = productOption.getProduct().getId();

        if (!productId.equals(productIdInOption)) {
            throw new OrderedProductException(PRODUCT_OPTION_NOT_MATCH);
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_QUANTITY_INVALID);
        }
        if (quantity > this.productOption.getStockQuantity()) {
            throw new OrderedProductException(ORDERED_PRODUCT_QUANTITY_INVALID);
        }
    }

    private void validateTotalPrice(Integer totalPrice) {
        if (totalPrice < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_TOTAL_PRICE_INVALID);
        }
        Integer calTotalPrice = getCalculatedTotalPrice();
        if (!calTotalPrice.equals(this.totalPrice)) {
            throw new OrderedProductException(ORDERED_PRODUCT_TOTAL_PRICE_INVALID);
        }
    }

    private void validateOrgPrice(Integer orgPrice) {
        if (orgPrice < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_ORG_PRICE_INVALID);
        }
    }

    private void validateImmediateDiscount(Integer ImmediateDiscountRatio) {
        if (ImmediateDiscountRatio <= 0 || ImmediateDiscountRatio >= 100) {
            throw new OrderedProductException(ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID);
        }
    }

    private void validateCouponDiscount(Integer couponDiscount) {
        if (couponDiscount < 0) {
            throw new OrderedProductException(ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID);
        }
    }

    private Integer getCalculatedTotalPrice() {
        return ((this.orgPrice * (this.ImmediateDiscountRatio / 100) + this.productOption.getPrice())
            * this.quantity) - this.couponDiscount;
    }


}
