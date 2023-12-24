package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteKeepResponse {
    Long id;

    private DeleteKeepResponse(Long id) {
        this.id = id;
    }

    public static DeleteKeepResponse from(Long id){
        return new DeleteKeepResponse(id);
    }
}
