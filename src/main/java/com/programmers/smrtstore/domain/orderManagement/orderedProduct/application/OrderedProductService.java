package com.programmers.smrtstore.domain.orderManagement.orderedProduct.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_MISMATCH_ERROR;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERED_PRODUCT_OPTION_MISMATCH_ERROR;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_OPTION_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_OPTION_NOT_MATCH;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.exception.OrderSheetException;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.OrderSheetProductOptionReq;
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

        validateProductConsistency(dbProduct, dbOption, request);

        return OrderedProduct.createWithDefaultCouponDiscount(
            dbProduct, dbOption, request.getQuantity(), request.getTotalPrice(),
            request.getOrgPrice(), request.getSellerImmediateDiscountRatio());
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
     * 주문 상품이 db에 있는 상품, 옵션 정보와 일치하는지 검증.
     */
    private void validateProductConsistency(
        Product dbProduct, ProductDetailOption dbOption, CreateOrderedProductRequest request
    ) {
        // 존재하는 product 와 같은 값을 가지는 request 인지 검증
        validateProductPriceAgainstDatabase(dbProduct, request);
        // 존재하는 productOption 과 같은 값을 가지는 request 인지 검증
        validateProductOptionAgainstDatabase(dbOption, request.getOption());

        // product option 과 product 가 올바르게 매핑 되어 있는지 검증
        if (!dbOption.getProduct().getId().equals(request.getProductId())) {
            throw new OrderSheetException(PRODUCT_OPTION_NOT_MATCH);
        }

        // totalPrice 가 올바르게 계산 되었는지 검증
        Integer totalPriceBasedRequest = calculateOrderedProductTotalPrice(
            request.getSalePrice(), request.getOption().getExtraPrice(), request.getQuantity()
        );
        //TODO: 에러 메세지 dto 에서 나는걸로 수정
        if (!totalPriceBasedRequest.equals(request.getTotalPrice())) {
            throw new OrderSheetException(ORDERED_PRODUCT_MISMATCH_ERROR,
                "totalPrice : " + request.getTotalPrice());
        }
    }

    private Integer calculateOrderedProductTotalPrice(
        Integer salePrice, Integer optionPrice, Integer quantity
    ) {
        return (salePrice + optionPrice) * quantity;
    }

    private void validateProductPriceAgainstDatabase(
        Product dbProduct, CreateOrderedProductRequest request
    ) {
        Map<String, String> validateResult = new HashMap<>();
        // 원가 비교
        if (!dbProduct.getPrice().equals(request.getOrgPrice())) {
            validateResult.put("orgPrice", String.valueOf(request.getOrgPrice()));
        }
        // 즉시 할인 퍼센트 비교
        if (!dbProduct.getDiscountRatio().equals(request.getSellerImmediateDiscountRatio())) {
            validateResult.put("sellerImmediateDiscountRatio",
                String.valueOf(request.getSellerImmediateDiscountRatio()));
        }
        // 즉시 할인 적용 후 값 비교
        if (!dbProduct.getSalePrice().equals(request.getSalePrice())) {
            validateResult.put("salePrice", String.valueOf(request.getSalePrice()));
        }

        if (!validateResult.isEmpty()) {
            String validationErrors = validateResult.entrySet().stream()
                .map(result -> result.getKey() + " : " + result.getValue())
                .collect(Collectors.joining(System.lineSeparator()));
            throw new OrderSheetException(ORDERED_PRODUCT_MISMATCH_ERROR, validationErrors);
        }
    }

    private void validateProductOptionAgainstDatabase(
        ProductDetailOption dbProductOption, OrderSheetProductOptionReq request
    ) {
        Map<String, String> validateResult = new HashMap<>();

        // 추가 금액 비교
        if (!dbProductOption.getPrice().equals(request.getExtraPrice())) {
            validateResult.put("extraPrice", String.valueOf(request.getExtraPrice()));
        }
        // 옵션 타입 비교
        if (!dbProductOption.getOptionType().equals(request.getOptionType())) {
            validateResult.put("optionType", String.valueOf(request.getOptionType()));
        }

        if (!validateResult.isEmpty()) {
            String validationErrors = validateResult.entrySet().stream()
                .map(result -> result.getKey() + " : " + result.getValue())
                .collect(Collectors.joining(System.lineSeparator()));
            throw new OrderSheetException(ORDERED_PRODUCT_OPTION_MISMATCH_ERROR,
                validationErrors);
        }
    }
}
