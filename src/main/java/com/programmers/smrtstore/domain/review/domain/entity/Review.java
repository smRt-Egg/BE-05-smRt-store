package com.programmers.smrtstore.domain.review.domain.entity;

import com.programmers.smrtstore.core.base.TimestampBaseEntity;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "review_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends TimestampBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_score", nullable = false)
    private ReviewScore reviewScore;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewLike> reviewLikes = new LinkedList<>();

    @Builder
    private Review(User user, Product product, String title, String content,
        ReviewScore reviewScore) {
        this.user = user;
        this.product = product;
        this.title = title;
        this.content = content;
        this.reviewScore = reviewScore;
    }

    public int getReviewLikeCount() {
        return this.reviewLikes.size();
    }

    protected void addReviewLike(ReviewLike reviewLike) {
        this.reviewLikes.add(reviewLike);
    }

    private void updateReviewTitle(String title) {
        this.title = title;
    }

    private void updateReviewContent(String content) {
        this.content = content;
    }

    private void updateReviewScore(ReviewScore reviewScore) {
        this.reviewScore = reviewScore;
    }

    public void removeReviewLike(ReviewLike reviewLike) {
        this.reviewLikes.remove(reviewLike);
    }

    public void updateValues(String title, String content, ReviewScore reviewScore) {
        if (title != null) {
            this.updateReviewTitle(title);
        }
        if (content != null) {
            this.updateReviewContent(content);
        }
        if (reviewScore != null) {
            this.updateReviewScore(reviewScore);
        }
    }
}