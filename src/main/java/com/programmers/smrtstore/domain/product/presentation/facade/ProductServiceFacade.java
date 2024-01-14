package com.programmers.smrtstore.domain.product.presentation.facade;

import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductDetailPageAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductThumbnailAPIResponse;
import java.util.List;

public interface ProductServiceFacade {

    List<ProductThumbnailAPIResponse> findAllProductThumbnail();

    ProductDetailPageAPIResponse findProductById(Long productId, Long userId);
}
