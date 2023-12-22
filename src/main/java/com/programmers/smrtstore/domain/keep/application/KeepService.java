package com.programmers.smrtstore.domain.keep.application;

import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface KeepService {

    CreateKeepResponse createKeep(CreateKeepRequest request);

    List<KeepResponse> getAllKeepsByUserId(Long userId);

    DeleteKeepResponse deleteKeep(DeleteKeepRequest request);

    List<KeepResponse> findKeepByUserAndCategory(FindKeepByCategoryRequest request);

    List<KeepRankingResponse> getKeepRanking(PageRequest pageRequest);
}
