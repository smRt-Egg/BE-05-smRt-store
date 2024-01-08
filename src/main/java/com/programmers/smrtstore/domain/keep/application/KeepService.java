package com.programmers.smrtstore.domain.keep.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import com.programmers.smrtstore.domain.keep.exception.KeepException;
import com.programmers.smrtstore.domain.keep.infrastructure.KeepJpaRepository;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;

import java.util.List;

import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeepService {
    private final KeepJpaRepository keepRepository;
    private final ProductJpaRepository productRepository;
    private final UserJpaRepository userJpaRepository;

    public CreateKeepResponse createKeep(Long securityUserId, CreateKeepRequest request) {
        checkUserExists(securityUserId);
        Keep keep = Keep.builder()
                .user(userJpaRepository.findById(request.getUserId()).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null)))
                .product(productRepository.findById(request.getProductId()).orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND)))
                .build();
        Keep saveKeepEntity = keepRepository.save(keep);
        return CreateKeepResponse.of(saveKeepEntity);
    }

    public List<KeepResponse> getAllKeepsByUserId(Long securityUserId, Long userId) {
        checkUserExists(securityUserId);
        return keepRepository.findAllByUserId(userId);
    }

    public DeleteKeepResponse deleteKeep(Long securityUserId, DeleteKeepRequest request) {
        checkUserExists(securityUserId);
        Long deleteId = request.getId();
        Keep deleteKeep = keepRepository.findById(request.getId()).orElseThrow(() -> new KeepException(ErrorCode.KEEP_NOT_FOUND_ERROR));
        keepRepository.delete(deleteKeep);
        return DeleteKeepResponse.from(deleteId);
    }

    public List<KeepResponse> findKeepByUserAndCategory(Long securityUserId, FindKeepByCategoryRequest request) {
        checkUserExists(securityUserId);
        return keepRepository.findKeepByUserAndCategory(request.getUserId(), request.getCategory());
    }

    public List<KeepRankingResponse> getKeepRanking(Long securityUserId, int limit) {
        checkUserExists(securityUserId);
        return keepRepository.findTopProductIdsWithCount(limit);
    }

    private void checkUserExists(Long securityUserId) {
        userJpaRepository.findById(securityUserId).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
    }
}
