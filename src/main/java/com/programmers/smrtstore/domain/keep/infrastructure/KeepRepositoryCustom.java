package com.programmers.smrtstore.domain.keep.infrastructure;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Category;

import java.util.List;

public interface KeepRepositoryCustom {
    List<KeepRankingResponse> findTopProductIdsWithCount(int limit);
    List<KeepResponse> findKeepByUser(Long userId);
    List<KeepResponse> findKeepByUserAndCategory(Long userId, Category category);
}
