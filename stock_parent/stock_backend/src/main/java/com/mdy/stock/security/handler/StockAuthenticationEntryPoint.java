package com.mdy.stock.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mdy
 * @date 2024-07-07 19:54
 * @description 未登录用户拒绝处理器
 */
public class StockAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 用户未登录时访问除公共资源以外的资源时触发
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String respStr = new ObjectMapper().writeValueAsString(R.error(ResponseCode.NOT_PERMISSION));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respStr);
    }
}
