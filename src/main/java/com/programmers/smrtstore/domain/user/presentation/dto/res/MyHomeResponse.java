package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyHomeResponse {

    private String nickName;

    private String thumbnail;

    private Long orderDeliveryCount;

    private Integer cartCount;

    private Integer reviewPoint;

    private Integer couponCount;

    List<KeepResponse> allKeeps;

    Integer allKeepsCount;

    List<KeepResponse> clothesKeeps;

    Integer clothesKeepsCount;

    List<KeepResponse> foodKeeps;

    Integer foodKeepsCount;

    List<KeepResponse> electricKeeps;

    Integer electricKeepsCount;

    List<KeepResponse> houseKeeps;

    Integer houseKeepsCount;

    List<KeepResponse> itKeeps;

    Integer itKeepsCount;
}
