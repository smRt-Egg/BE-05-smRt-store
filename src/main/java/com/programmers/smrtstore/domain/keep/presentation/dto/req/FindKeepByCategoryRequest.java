package com.programmers.smrtstore.domain.keep.presentation.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindKeepByCategoryRequest {
    Long userId;
    Long categoryId; // Category category;
}
