package com.programmers.smrtstore.domain.keep.application;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import com.programmers.smrtstore.domain.keep.exception.KeepException;
import com.programmers.smrtstore.domain.keep.infrastructure.KeepJpaRepository;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class KeepServiceTest {

    @Autowired
    private KeepService keepService;

    @Autowired
    private KeepJpaRepository keepRepository;

    @BeforeEach
    void init() {
        List<Keep> keeps = new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            Keep keep = Keep.builder().userId(i).productId(i % 10).build();
            keeps.add(keep);
        }

        for (long i = 0; i < 10; i++) {
            for (long j = 0; j <= i; j++) {
                Keep keep = Keep.builder().userId(j).productId(i).build();
                keeps.add(keep);
            }
        }
        keepRepository.saveAll(keeps);
    }

    @DisplayName("keep을 생성할 수 있다.")
    @Test
    void createKeepTest() {
        //Given
        CreateKeepRequest createKeepRequest = CreateKeepRequest.builder()
                .userId(1L)
                .productId(2L)
                .build();
        //When
        CreateKeepResponse keep = keepService.createKeep(createKeepRequest);
        //Then
        assertThat(keep.getUserId()).isEqualTo(1L);
        assertThat(keep.getProductId()).isEqualTo(2L);
        assertThat(keep.getCreatedAt().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth());
    }

    @DisplayName("userId를 활용해 조회할 수 있다.")
    @Test
    void getByUserIdTest() {
        //Given
        Long userId = 1L;
        //When
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

}