package com.programmers.smrtstore.domain.keep.presentation.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindKeepByCategoryRequest {
    Long userId;
    Category category;
}
