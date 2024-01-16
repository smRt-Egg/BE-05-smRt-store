package com.programmers.smrtstore.domain.user.presentation.dto.req;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DurationRequest {

    @PastOrPresent(message = "날짜는 과거나 현재여야 합니다.")
    private LocalDateTime startDate;

    @PastOrPresent(message = "날짜는 과거나 현재여야 합니다.")
    private LocalDateTime endDate;
}
