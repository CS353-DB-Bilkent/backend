package com.ticketseller.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.dao.UserDao;
import com.ticketseller.backend.dto.response.ApiResponse;
import com.ticketseller.backend.entity.User;
import com.ticketseller.backend.exceptions.runtimeExceptions.BaseRuntimeException;
import com.ticketseller.backend.exceptions.runtimeExceptions.TokenRuntimeException;
import com.ticketseller.backend.exceptions.runtimeExceptions.UserRuntimeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final List<String> EXCLUDE_URL_STARTS_WITH = Arrays.asList("/health", "/public", "/auth", "/documentation", "/swagger-ui");
    private final List<String> INCLUDE_URLS = Arrays.asList("/auth/logout", "/auth/change-password");
    private final String EMPTY_URL = "/";

    private final JwtTokenUtil jwtTokenUtil;

    private final ObjectMapper objectMapper;

    private final UserDao userDao;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException, BaseRuntimeException {
        try {
            if (!hasAuthorizationBearer(request))
                throw new TokenRuntimeException("No authorization header is present", ErrorCodes.JWT, HttpStatus.UNAUTHORIZED);

            String accessToken = getAccessToken(request);

            if (!jwtTokenUtil.validateToken(accessToken))
                throw new TokenRuntimeException("Unauthorized", ErrorCodes.JWT, HttpStatus.UNAUTHORIZED);

            String email = jwtTokenUtil.getSubject(accessToken);

            Optional<User> optionalUser = userDao.getUserByEmail(email);

            if (optionalUser.isEmpty())
                throw new UserRuntimeException("User not found!", ErrorCodes.NO_SUCH_USER, HttpStatus.UNAUTHORIZED);

            User user = optionalUser.get();

            request.setAttribute("user", user);
            request.setAttribute("accessToken", accessToken);
            filterChain.doFilter(request, response);
        } catch (BaseRuntimeException exception) {
            this.handleException(response, exception);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) throws ServletException {
        return  INCLUDE_URLS.stream().noneMatch(include -> request.getServletPath().equals(include))
                && EXCLUDE_URL_STARTS_WITH.stream().anyMatch(exclude -> request.getServletPath().startsWith(exclude)
                    || request.getServletPath().equals(EMPTY_URL));
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        return !(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer"));
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        String[] res = header.split(" ");

        if (res.length == 1) return "";

        return header.split(" ")[1].trim();
    }

    // This is the correct way to handle filter exceptions
    private void handleException(HttpServletResponse response, BaseRuntimeException exception) throws IOException {
        response.setContentType("application/json");
        response.setStatus(exception.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.builder()
                .operationResult(ApiResponse.OperationResult.builder()
                        .returnCode(Integer.toString(exception.getErrorCode()))
                        .returnMessage(exception.getMessage())
                        .build())
                .build()));
    }

}
