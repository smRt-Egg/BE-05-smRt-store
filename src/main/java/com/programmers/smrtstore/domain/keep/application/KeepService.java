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
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeepService {
    private final KeepJpaRepository keepRepository;

    public CreateKeepResponse createKeep(CreateKeepRequest request) {
        Keep keep = Keep.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .build();
        Keep saveKeepEntity = keepRepository.save(keep);
        return CreateKeepResponse.of(saveKeepEntity);
    }

    public List<KeepResponse> getAllKeepsByUserId(Long userId) {
        List<Keep> keepListFindByUserId = keepRepository.findAllByUserId(userId);
        return keepListFindByUserId.stream().map(KeepResponse::of).toList();
    }

    public DeleteKeepResponse deleteKeep(DeleteKeepRequest request) {
        Long deleteId = request.getId();
        Keep deleteKeep = keepRepository.findById(deleteId).orElseThrow(() -> new KeepException(ErrorCode.KEEP_NOT_FOUND_ERROR));
        keepRepository.delete(deleteKeep);
        return DeleteKeepResponse.from(deleteId);
    }

    public List<KeepResponse> findKeepByUserAndCategory(FindKeepByCategoryRequest request) {
        Long userId = request.getUserId();
        Category category = request.getCategory();
        return keepRepository.findKeepByUserAndCategory(userId, category);
    }

    public List<KeepRankingResponse> getKeepRanking(int limit) {
        return keepRepository.findTopProductIdsWithCount(limit);
    }
}
