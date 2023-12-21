package com.programmers.smrtstore.domain.keep.application;

import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;

public interface KeepService {

    CreateKeepResponse createKeep(CreateKeepRequest request);

    KeepResponse getAllKeepsByUserId(Long userId);

    DeleteKeepResponse deleteKeep(DeleteKeepRequest request);

    KeepResponse findKeepByUserAndCategory(FindKeepByCategoryRequest request);

    KeepResponse getKeepRanking();
}
