package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.qna.presentation.dto.res.QuestionResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyQnaResponse {

    private String nickName;

    private String username;

    private Long orderDeliveryCount;

    private Integer couponCount;

    private Integer point;

    private List<QuestionResponse> qnaList;
}
