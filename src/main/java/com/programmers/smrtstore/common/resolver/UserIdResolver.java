package com.programmers.smrtstore.common.resolver;

import static com.programmers.smrtstore.core.properties.ErrorCode.MISSING_CREDENTIALS;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.auth.exception.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserId.class) != null;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        Long userId = (Long) webRequest.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        if (userId == null) {
            throw new AuthException(MISSING_CREDENTIALS);
        }
        return userId;
    }
}
