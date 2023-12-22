package com.programmers.smrtstore.domain.keep.application;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import com.programmers.smrtstore.domain.keep.exception.KeepNotFoundException;
import com.programmers.smrtstore.domain.keep.infrastructure.KeepJpaRepository;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeepServiceImpl implements KeepService {
    private final KeepJpaRepository keepRepository;

    @Override
    public CreateKeepResponse createKeep(CreateKeepRequest request) {
        Keep keep = Keep.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .build();
        Keep saveKeepEntity = keepRepository.save(keep);
        return CreateKeepResponse.of(saveKeepEntity);
    }

    @Override
    public List<KeepResponse> getAllKeepsByUserId(Long userId) {
        List<Keep> keepListFindByUserId = keepRepository.findAllByUserId(userId);
        return keepListFindByUserId.stream().map(KeepResponse::of).toList();
    }

    @Override
    public DeleteKeepResponse deleteKeep(DeleteKeepRequest request) {
        Long deleteId = request.getId();
        if (!keepRepository.existsById(deleteId)) {
            throw new KeepNotFoundException();
        }
        keepRepository.deleteById(deleteId);
        return DeleteKeepResponse.of(deleteId);
    }

    @Override
    public List<KeepResponse> findKeepByUserAndCategory(FindKeepByCategoryRequest request) {
        return null;
    }

    @Override
    public List<KeepRankingResponse> getKeepRanking(PageRequest pageRequest) {
        return keepRepository.findTopProductIdsWithCount(pageRequest);
    }
}
