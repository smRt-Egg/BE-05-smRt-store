package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailUserResponse {

    private Long id;

    private String loginId;

    private Integer age;

    private String nickName;

    private String email;

    private String phone;

    private String birth;

    private Gender gender;

    private String thumbnail;

    private Role role;

    private Integer point;

    private boolean marketingAgree;

    private boolean membershipYN;

    private boolean repurchase;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;


    public static DetailUserResponse toDetailUserResponse(User user) {
        return new DetailUserResponse(
            user.getId(),
            user.getAuth().getLoginId(),
            user.getAge(),
            user.getNickName(),
            user.getEmail(),
            user.getPhone(),
            user.getBirth(),
            user.getGender(),
            user.getThumbnail(),
            user.getRole(),
            user.getPoint(),
            user.isMarketingAgree(),
            user.isMembershipYN(),
            user.isRepurchase(),
            user.getUpdatedAt(),
            user.getCreatedAt(),
            user.getDeletedAt()
        );
    }
}
