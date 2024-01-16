package com.programmers.smrtstore.domain.review.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.review.application.dto.req.CreateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.ReviewLikeRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.UpdateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewResponse;
import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.programmers.smrtstore.domain.review.domain.entity.ReviewScore;
import com.programmers.smrtstore.domain.review.exception.ReviewException;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewJpaRepository;
import com.programmers.smrtstore.domain.user.domain.enums.Gender;
import com.programmers.smrtstore.domain.user.domain.enums.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
@Import(RedisTestConfig.class)
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewJpaRepository reviewRepository;
    @Autowired
    ProductJpaRepository productJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;

    User user;
    Product product;

    @BeforeEach
    void init() throws MalformedURLException {
        user = userJpaRepository.save(User.builder()
                .nickName("nickName")
                .email("void@email.com")
                .phone("010-5555-5555")
                .birth("1900-01-01")
                .gender(Gender.MALE)
                .role(Role.ROLE_USER)
                .point(0)
                .marketingAgree(false)
                .membershipYn(false)
                .build());
        product = productJpaRepository.save(Product.builder()
                .name("productName")
                .price(1000)
                .category(Category.IT)
                .thumbnail("https://www.naver.com")
                .contentImage("https://www.naver.com")
                .build());
    }

    @DisplayName("오더가 생생 되어 있지 않다면 리뷰를 생성할 수 없다.")
    @Test
    void createReviewWithoutBeforeCreatingOrderTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;

        CreateReviewRequest request = CreateReviewRequest.builder()
                .title(title)
                .content(content)
                .reviewScore(score)
                .userId(user.getId())
                .productId(product.getId())
                .build();
        //When //Then
        assertThatThrownBy(() -> reviewService.createReview(user.getId(), request))
                .isInstanceOf(ReviewException.class);
    }

    @DisplayName("리뷰 아이디로 리뷰를 조회할 수 있다.")
    @Test
    void getReviewByIdTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;

        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());
        //When
        ReviewResponse response = reviewService.getReviewById(user.getId(), review.getId());
        //Then
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getReviewScore()).isEqualTo(score);
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getProductId()).isEqualTo(product.getId());
        assertThat(response.getLikeCount()).isZero();
    }

    @DisplayName("상품에 등록된 리뷰를 조회할 수 있다.")
    @Test
    void getReviewByProductIdTest() {
        //Given
        int testSize = 10;
        List<Review> reviewList = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            Review review = Review.builder()
                    .user(user)
                    .product(product)
                    .title("title" + i)
                    .content("content" + i)
                    .reviewScore(ReviewScore.FOUR)
                    .build();
            reviewList.add(review);
        }
        reviewRepository.saveAll(reviewList);
        //When
        List<ReviewResponse> reviewListByProductId = reviewService.getReviewsByProductId(user.getId(), product.getId());
        //Then
        assertThat(reviewListByProductId).hasSize(testSize);
    }

    @DisplayName("유저가 등록한 리뷰를 조회할 수 있다.")
    @Test
    void getReviewsByUserIdTest() {
        //Given
        int testSize = 10;
        List<Review> reviewList = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            Review review = Review.builder()
                    .user(user)
                    .product(product)
                    .title("title" + i)
                    .content("content" + i)
                    .reviewScore(ReviewScore.FOUR)
                    .build();
            reviewList.add(review);
        }
        reviewRepository.saveAll(reviewList);
        //When
        List<ReviewResponse> reviewListByUserId = reviewService.getReviewsByUserId(user.getId(), user.getId());
        //Then
        assertThat(reviewListByUserId).hasSize(testSize);
    }

    @DisplayName("등록 되어 있는 리뷰를 수정할 수 있다.")
    @Test
    void updateReviewTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;
        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());

        String updateTitle = "update Title";
        String updateContent = "update Content";
        ReviewScore updateScore = ReviewScore.ONE;
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .reviewId(review.getId())
                .userId(user.getId())
                .title(updateTitle)
                .content(updateContent)
                .reviewScore(updateScore)
                .build();
        //When
        ReviewResponse updateReviewResponse = reviewService.updateReview(user.getId(), request);
        //Then
        assertThat(updateReviewResponse.getUserId()).isEqualTo(user.getId());
        assertThat(updateReviewResponse.getTitle()).isEqualTo(updateTitle);
        assertThat(updateReviewResponse.getContent()).isEqualTo(updateContent);
        assertThat(updateReviewResponse.getReviewScore()).isEqualTo(updateScore);
    }

    @DisplayName("등록 되어 있는 리뷰를 삭제할 수 있다.")
    @Test
    void deleteReviewTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;
        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());
        //When
        Long deleteId = reviewService.deleteReview(user.getId(), review.getId());
        //Then
        assertThat(deleteId).isEqualTo(review.getId());
    }

    @DisplayName("등록 되어 있는 리뷰에 좋아요를 누를 수 있다.")
    @Test
    void likeReviewTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;
        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());
        ReviewLikeRequest request = ReviewLikeRequest.builder()
                .userId(user.getId())
                .reviewId(review.getId())
                .build();
        //When
        reviewService.likeReview(user.getId(), request);
        //Then
        assertThat(review.getReviewLikeCount()).isOne();
    }

    @DisplayName("유저 당 리뷰에 좋아요는 한 번만 누를 수 있다.")
    @Test
    void likeReviewMultipleTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;
        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());
        ReviewLikeRequest request = ReviewLikeRequest.builder()
                .userId(user.getId())
                .reviewId(review.getId())
                .build();
        //When //Then
        reviewService.likeReview(user.getId(), request);
        assertThatThrownBy(() -> reviewService.likeReview(user.getId(), request))
                .isInstanceOf(ReviewException.class);
    }

    @DisplayName("좋아요가 등록되어 있는 리뷰에 좋아요를 해제할 수 있다.")
    @Test
    void dislikeReviewTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;
        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());
        ReviewLikeRequest request = ReviewLikeRequest.builder()
                .userId(user.getId())
                .reviewId(review.getId())
                .build();
        reviewService.likeReview(user.getId(), request);
        //When
        Long dislikeReviewId = reviewService.dislikeReview(user.getId(), request);
        //Then
        assertThat(dislikeReviewId).isEqualTo(review.getId());
        assertThat(review.getReviewLikeCount()).isZero();
    }

    @DisplayName("좋아요가 등록되어 있지 않은 리뷰에 좋아요를 해제할 수 없다.")
    @Test
    void dislikeReviewTestWithOutLikeTest() {
        //Given
        String title = "review title";
        String content = "review content";
        ReviewScore score = ReviewScore.FIVE;
        Review review = reviewRepository.save(Review.builder()
                .user(user)
                .product(product)
                .title(title)
                .content(content)
                .reviewScore(score)
                .build());
        ReviewLikeRequest request = ReviewLikeRequest.builder()
                .userId(user.getId())
                .reviewId(review.getId())
                .build();
        //When //Then
        assertThatThrownBy(() -> reviewService.dislikeReview(user.getId(), request))
                .isInstanceOf(ReviewException.class);
    }
}