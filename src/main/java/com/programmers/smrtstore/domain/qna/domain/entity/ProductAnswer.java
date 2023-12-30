package com.programmers.smrtstore.domain.qna.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_answer_TB")
@Entity
public class ProductAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_question_id")
    private ProductQuestion productQuestion;

    @Column(name = "content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public void setProductQuestion(ProductQuestion productQuestion) {
        this.productQuestion = productQuestion;
    }

    @Builder
    public ProductAnswer(ProductQuestion productQuestion, String content) {
        this.productQuestion = productQuestion;
        this.content = content;
    }

}
