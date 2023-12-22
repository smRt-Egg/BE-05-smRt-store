package com.programmers.smrtstore.domain.keep.infrastructure;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeepJpaRepository extends JpaRepository<Keep, Long> {
    List<Keep> findAllByUserId(Long userId);

    @Query("SELECT new com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse(k.productId, COUNT(k.productId)) " +
            "FROM Keep k " +
            "GROUP BY k.productId " +
            "ORDER BY COUNT(k.productId) DESC")
    List<KeepRankingResponse> findTopProductIdsWithCount(Pageable pageable);
}
