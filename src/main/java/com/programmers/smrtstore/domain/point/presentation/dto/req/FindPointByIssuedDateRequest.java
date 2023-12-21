package com.programmers.smrtstore.domain.point.presentation.dto.req;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindPointByIssuedDateRequest {

    private Long userId;
    private LocalDate issuedAt;

}
