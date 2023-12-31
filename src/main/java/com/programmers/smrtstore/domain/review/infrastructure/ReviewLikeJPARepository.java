package com.programmers.smrtstore.domain.review.infrastructure;

import com.programmers.smrtstore.domain.review.domain.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeJPARepository extends JpaRepository<ReviewLike, Long> {

}