package com.programmers.smrtstore.domain.review.infrastructure;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
    List<Review> findByUser(User user);
    Optional<Review> findByIdAndUser(Long id, User user);
}