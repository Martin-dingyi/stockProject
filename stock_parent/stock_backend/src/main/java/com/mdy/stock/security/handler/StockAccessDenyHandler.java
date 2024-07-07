package com.mdy.stock.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mdy
 * @date 2024-07-07 19:52
 * @description 拒绝处理器
 */

public class StockAccessDenyHandler implements AccessDeniedHandler {

    /**
     * 当访问未授权的资源会触发该处理器
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {

        String respStr = new ObjectMapper().writeValueAsString(R.error(ResponseCode.NOT_PERMISSION));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respStr);
    }
}
