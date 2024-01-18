package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.coupon.presentation.res.UserCouponResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyCouponsResponse {
    private List<UserCouponResponse> couponList;
}
