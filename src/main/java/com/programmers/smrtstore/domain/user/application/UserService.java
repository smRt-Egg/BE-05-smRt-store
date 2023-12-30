package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse.from;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyHomeResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        return from(user);
    }

    public ProfileUserResponse update(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        user.updateUser(request.getAge(), request.getNickName(), request.getEmail(),
            request.getPhone(), request.getBirth(), request.getGender(), request.getThumbnail(),
            request.isMarketingAgree());
        return from(user);
    }

    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        user.saveDeleteDate();
    }

    @Transactional(readOnly = true)
    public MyHomeResponse getMyHome(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        //주문배송중 상품 개수 가져오기
        Integer orderDeliveryCount;
        //카트 상품 개수 가져오기
        Integer cartCount;
        //아직 작성하지 않은 리뷰 작성 시 얻을 수 있는 포인트 가져오기
        Integer reviewPoint;
        //쿠폰 개수 가져오기
        Integer couponCount;
        //전체 찜 목록
        //카테고리별 찜목록
        //최근 본 상품 목록
        return null;
    }
}
