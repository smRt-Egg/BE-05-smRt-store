package com.programmers.smrtstore.domain.keep.infrastructure;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;

import java.util.List;

public interface KeepRepositoryCustom {
    List<KeepRankingResponse> findTopProductIdsWithCount(int limit);
}
