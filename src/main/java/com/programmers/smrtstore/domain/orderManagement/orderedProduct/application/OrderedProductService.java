package com.programmers.smrtstore.domain.orderManagement.orderedProduct.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_MISMATCH_ERROR;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_NOT_AVAILABLE_ORDER;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_OPTION_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_OPTION_NOT_MATCH;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.exception.OrderSheetException;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.application.dto.CreateOrderedProductRequest;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderedProductService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductDetailOptionJpaRepository productDetailOptionJpaRepository;

    public OrderedProduct createOrderedProduct(CreateOrderedProductRequest request) {
        Product dbProduct = checkProductExistence(request.getProductId());
        ProductDetailOption dbOption = checkProductOptionExistence(
            request.getOption().getOptionId());

        // TODO: 재고 체크 동시성에 대해 고민해 보기
        validateProduct(dbProduct, dbOption, request.getQuantity());
        validateCreateOrderedProductRequest(dbProduct, dbOption, request);

        return OrderedProduct.createBeforeOrder(
            dbProduct, dbOption, request.getOption().getExtraPrice(), request.getQuantity(),
            request.getOrgPrice(), request.getSellerImmediateDiscount());
    }

    // 현재도 유효한 상품인지 검증
    public void validateOrderedProduct(OrderedProduct orderedProduct) {
        // 존재 여부 체크
        Product dbProduct = checkProductExistence(orderedProduct.getId());
        ProductDetailOption dbOption = checkProductOptionExistence(
            orderedProduct.getId());

        // product 자체가 유효한지 (주문 가능 여부, 재고 남아있는지)
        validateProduct(dbProduct, dbOption, orderedProduct.getQuantity());
        // 현재도 이 가격으로 살 수 있는지 (orgPrice, immediateDiscount, extraPrice)
        validateOrderedProductPriceAgainstDatabase(
            orderedProduct.getOrgPrice(), orderedProduct.getImmediateDiscount(),
            orderedProduct.getExtraPrice(),
            dbProduct.getPrice(), dbProduct.getImmediateDiscount(), dbOption.getPrice()
        );
    }

    private void validateProduct(
        Product dbProduct, ProductDetailOption dbOption, Integer quantityToPurchase
    ) {
        // 삭제 된 상품은 살 수 없음
        if (dbProduct.getDeletedAt() != null || dbOption.getDeletedAt() != null) {
            throw new OrderSheetException(PRODUCT_NOT_AVAILABLE_ORDER);
        }
        // 살 수 없으면 Sale 상태가 아님
        if (!dbProduct.isAvailableOrder()) {
            throw new OrderSheetException(PRODUCT_NOT_AVAILABLE_ORDER);
        }
        // 살 수 없으면 재고가 없음
        if (dbOption.isAvailableOrder(quantityToPurchase)) {
            throw new OrderSheetException(PRODUCT_QUANTITY_NOT_ENOUGH);
        }
    }

    private ProductDetailOption checkProductOptionExistence(Long optionId) {
        return productDetailOptionJpaRepository.findById(optionId)
            .orElseThrow(() -> new OrderSheetException(PRODUCT_OPTION_NOT_FOUND));
    }

    private Product checkProductExistence(Long productId) {
        return productJpaRepository.findById(productId)
            .orElseThrow(() -> new OrderSheetException(PRODUCT_NOT_FOUND));
    }

    /**
     * 주문 상품 가격정보가 db에 있는 상품, 옵션 정보와 일치하는지 검증.
     */
    private void validateCreateOrderedProductRequest(
        Product dbProduct, ProductDetailOption dbOption, CreateOrderedProductRequest request
    ) {
        // 존재하는 product,productOption 과 같은 값을 가지는 request 인지 검증
        validateOrderedProductPriceAgainstDatabase(
            request.getOrgPrice(), request.getSellerImmediateDiscount(),
            request.getOption().getExtraPrice(),
            dbProduct.getPrice(), dbProduct.getImmediateDiscount(), dbOption.getPrice());

        // product option 과 product 가 올바르게 매핑 되어 있는지 검증
        if (!dbOption.getProduct().getId().equals(request.getProductId())) {
            throw new OrderSheetException(PRODUCT_OPTION_NOT_MATCH);
        }
    }

    // TODO: 함수 분리 고려
    private void validateOrderedProductPriceAgainstDatabase(
        Integer orgPrice, Integer sellerImmediateDiscount, Integer extraPrice,
        Integer dbOrgPrice, Integer dbSellerImmediateDiscount, Integer dbExtraPrice
    ) {
        Map<String, String> validateResult = new HashMap<>();
        // 원가 비교
        if (!dbOrgPrice.equals(orgPrice)) {
            validateResult.put("orgPrice", String.valueOf(orgPrice));
        }
        // 즉시 할인 퍼센트 비교
        if (!dbSellerImmediateDiscount.equals(sellerImmediateDiscount)) {
            validateResult.put("sellerImmediateDiscount",
                String.valueOf(sellerImmediateDiscount));
        }
        // 추가 금액 비교
        if (!dbExtraPrice.equals(extraPrice)) {
            validateResult.put("extraPrice", String.valueOf(extraPrice));
        }

        if (!validateResult.isEmpty()) {
            String validationErrors = validateResult.entrySet().stream()
                .map(result -> result.getKey() + " : " + result.getValue())
                .collect(Collectors.joining(System.lineSeparator()));
            throw new OrderSheetException(ORDERED_PRODUCT_MISMATCH_ERROR, validationErrors);
        }
    }
}
