package com.programmers.smrtstore.domain.qna.infrastructure;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAnswerRepository extends JpaRepository<ProductAnswer, Long> {
}
