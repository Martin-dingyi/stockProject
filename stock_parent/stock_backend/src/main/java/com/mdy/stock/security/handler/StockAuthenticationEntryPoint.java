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
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        //说明 票据解析出现异常，票据就失效了
        R<Object> r = R.error(ResponseCode.NOT_PERMISSION);
        String respStr = new ObjectMapper().writeValueAsString(r);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respStr);
    }
}
