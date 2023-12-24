package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteKeepResponse {
    Long id;
    Boolean isDelete;

    private DeleteKeepResponse(Long id, Boolean isDelete) {
        this.id = id;
        this.isDelete = isDelete;
    }

    public static DeleteKeepResponse from(Long id){
        return new DeleteKeepResponse(id, true);
    }
}
