package com.programmers.smrtstore.domain.qna.infrastructure;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long>, ProductQuestionRepositoryCustom {
}
