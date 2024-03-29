package com.programmers.smrtstore.domain.qna.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.qna.exception.QnAException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_question_TB")
@Entity
public class ProductQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "productQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAnswer> productAnswerList = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public ProductQuestion(Long userId, Long productId, String content) {
        this.userId = userId;
        this.productId = productId;
        this.content = content;
    }

    public void updateContent(Long userId, String updateContent) {
        if (!checkUserEquals(userId)) {
            throw new QnAException(ErrorCode.QUESTION_ACCESS_DENIED);
        }
        this.content = updateContent;
    }

    public Boolean checkUserEquals(Long userId) {
        return this.userId.equals(userId);
    }
}
