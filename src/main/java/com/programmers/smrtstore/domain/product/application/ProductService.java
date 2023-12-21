package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.domain.product.presentation.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.UpdateProductRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.res.CreateProductResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductResponse;
import java.util.List;

public interface ProductService {

    CreateProductResponse createProduct(CreateProductRequest request);

    ProductResponse getProductById(Long productId);

    List<ProductResponse> getAllProducts();

    Long releaseProduct(Long productId);

    Long makeProductAvailable(Long productId);

    Long makeProductNotAvailable(Long productId);

    Long deleteProduct(Long productId);

    Long updateProduct(Long productId, UpdateProductRequest request);
}
