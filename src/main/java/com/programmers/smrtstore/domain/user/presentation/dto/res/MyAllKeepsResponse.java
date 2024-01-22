package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyAllKeepsResponse {

    private String nickName;

    private String username;

    private Long orderDeliveryCount;

    private Integer couponCount;

    private Integer point;

    private List<KeepResponse> allKeeps;
}
