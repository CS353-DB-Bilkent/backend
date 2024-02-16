package com.ticketseller.backend.security;

import com.ticketseller.backend.annotations.RequiredRole;
import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.enums.Role;
import com.ticketseller.backend.exceptions.runtimeExceptions.AuthRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Slf4j
@Component
public class RoleHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            RequiredRole classAnnotation =  handlerMethod.getBean().getClass().getAnnotation(RequiredRole.class);
            RequiredRole methodAnnotation = handlerMethod.getMethodAnnotation(RequiredRole.class);

            User user = (User) request.getAttribute("user");

            if (classAnnotation != null && !userHasRequiredRole(user, classAnnotation)) {
                log.info("User " + user.getEmail() + " tried to reach a class protected route: " + request.getServletPath() + " protected with roles " + Arrays.toString(classAnnotation.value()));
                throw new AuthRuntimeException("You have tried to reach a protected route", ErrorCodes.REQUIRED_ROLES_NOT_PRESENT, HttpStatus.UNAUTHORIZED);
            }

            if (methodAnnotation != null && !userHasRequiredRole(user, methodAnnotation)) {
                log.info("User " + user.getEmail() + " tried to reach a method protected route: " + request.getServletPath() + " protected with roles " + Arrays.toString(methodAnnotation.value()));
                throw new AuthRuntimeException("You have tried to reach a protected route", ErrorCodes.REQUIRED_ROLES_NOT_PRESENT, HttpStatus.UNAUTHORIZED);
            }
        }

        return true;
    }

    private boolean userHasRequiredRole(User user, RequiredRole roleAnnotation) {
        return (!Arrays.asList(roleAnnotation.value()).contains(Role.SUPER_ADMIN) && user.getRole().equals(Role.ADMIN))
                || Arrays.asList(roleAnnotation.value()).contains(user.getRole());
    }
}
