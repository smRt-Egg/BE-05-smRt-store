package com.programmers.smrtstore.domain.keep.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import com.programmers.smrtstore.domain.keep.exception.KeepException;
import com.programmers.smrtstore.domain.keep.infrastructure.KeepJpaRepository;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class KeepServiceTest {

    @Autowired
    private KeepService keepService;
    @Autowired
    private KeepJpaRepository keepRepository;
    @Autowired
    private ProductJPARepository productRepository;

    private Long userId = 1L;
    private Long productId1;
    private Long productId2;
    @BeforeEach
    void init() throws Exception{
        List<Product> productList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            Product product = Product.builder()
                    .name("productName" + i)
                    .price(i * 1000)
                    .stockQuantity(i+1)
                    .category(Category.TEMP)
                    .contentImage(new URL("https://www.naver.com"))
                    .thumbnail(new URL("https://www.naver.com"))
                    .build();
            productList.add(product);
        }
        productRepository.saveAll(productList);
        productId1 = productList.get(0).getId();
        productId2 = productList.get(1).getId();

        for(int i = 0; i < 20; i++) {
            Keep keep = Keep.builder()
                    .userId(Integer.toUnsignedLong(i))
                    .product(productList.get(i))
                    .build();
            keepRepository.save(keep);
        }
    }

    @DisplayName("keep을 생성할 수 있다.")
    @Test
    void createKeepTest() {
        //Given
        CreateKeepRequest createKeepRequest = CreateKeepRequest.builder()
                .userId(userId)
                .productId(productId1)
                .build();
        //When
        CreateKeepResponse keep = keepService.createKeep(createKeepRequest);
        //Then
        assertThat(keep.getUserId()).isEqualTo(userId);
        assertThat(keep.getProductId()).isEqualTo(productId1);
        assertThat(keep.getCreatedAt().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth());
    }

    @DisplayName("userId를 활용해 조회할 수 있다.")
    @Test
    void getByUserIdTest() {
        //Given //When
        List<KeepResponse> keepsByUserId = keepService.getAllKeepsByUserId(userId);
        //Then
        assertThat(keepsByUserId).isNotEmpty();
        assertThat(keepsByUserId.stream().map(KeepResponse::getUserId).collect(Collectors.toSet())).hasSize(1);
    }

    @DisplayName("id를 통해 찜을 삭제할 수 있다.")
    @Test
    void deleteKeepTest() {
        //Given
        Long deleteId = 1L;
        DeleteKeepRequest request = DeleteKeepRequest.builder()
                .id(deleteId).build();
        //When
        DeleteKeepResponse deleteKeepResponse = keepService.deleteKeep(request);
        //Then
        assertThat(deleteKeepResponse.getId()).isEqualTo(deleteId);
    }

    @DisplayName("존재하지 않은 찜을 삭제하려하면 예외가 발생한다.")
    @Test
    void deleteKeepUsingInvalidIdTest() {
        //Given
        Long deleteInvalidId = 1000000L;
        DeleteKeepRequest request = DeleteKeepRequest.builder()
                .id(deleteInvalidId).build();
        //When //Then
        assertThatThrownBy(() -> keepService.deleteKeep(request)).isInstanceOf(KeepException.class);
    }

    @DisplayName("찜 랭킹은 내림차순이다.")
    @Test
    void getKeepRankingTest() {
        //Given
        int requestTopSize = 5;
        //When
        List<KeepRankingResponse> keepRanking = keepService.getKeepRanking(requestTopSize);
        //Then
        assertThat(keepRanking).hasSize(requestTopSize);
        assertThat(keepRanking.stream().map(KeepRankingResponse::getCount).toList()).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @DisplayName("유저의 상품 카테고리를 활용해 찜을 조회할 수 있다.")
    @Test
    void getKeepFromUserIdAndCategory(){
        //Given
        FindKeepByCategoryRequest request = FindKeepByCategoryRequest.builder()
                .userId(1L)
                .category(Category.TEMP)
                .build();
        //When
        List<KeepResponse> keepByUserAndCategory = keepService.findKeepByUserAndCategory(request);
        //Then
        assertThat(keepByUserAndCategory).isNotEmpty();
     }

}