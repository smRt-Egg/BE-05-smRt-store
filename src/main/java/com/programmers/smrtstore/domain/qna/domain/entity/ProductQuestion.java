package com.programmers.smrtstore.domain.qna.domain.entity;

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

    public void updateContent(String updateContent) {
        this.content = updateContent;
    }

    public void addProductAnswer(ProductAnswer productAnswer) {
        productAnswerList.add(productAnswer);
        productAnswer.setProductQuestion(this);
    }

    public void removeProductAnswer(ProductAnswer productAnswer) {
        productAnswerList.remove(productAnswer);
    }

}
