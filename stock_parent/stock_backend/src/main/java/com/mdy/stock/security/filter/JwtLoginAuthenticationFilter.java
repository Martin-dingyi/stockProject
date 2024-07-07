package com.mdy.stock.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdy.stock.security.detail.LoginUserDetail;
import com.mdy.stock.utils.JwtTokenUtil;
import com.mdy.stock.viewObject.request.ReqLoginVO;
import com.mdy.stock.viewObject.response.LoginRespVOExt;
import com.mdy.stock.viewObject.response.R;
import com.mdy.stock.viewObject.response.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author mdy
 * @date 2024-07-07 3:19
 * @description 认证过滤器
 */
public class JwtLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public JwtLoginAuthenticationFilter(String loginUrl) {
        super(loginUrl);
    }

    /**
     * @param request  http请求对象
     * @param response http响应对象
     * @return Authentication
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 判断请求方法必须是post提交，且提交的数据的内容必须是application/json格式的数据
        if (!("POST".equals(request.getMethod())
                && request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE))) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 获取请求参数
        ServletInputStream in = request.getInputStream();
        ReqLoginVO reqLoginVO = new ObjectMapper().readValue(in, ReqLoginVO.class);

        // 验证请求信息
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        if (reqLoginVO == null || org.apache.commons.lang3.StringUtils.isBlank(reqLoginVO.getUsername())
                || org.apache.commons.lang3.StringUtils.isBlank(reqLoginVO.getPassword())
                || org.apache.commons.lang3.StringUtils.isBlank(reqLoginVO.getCode())
                || StringUtils.isBlank(reqLoginVO.getSessionId())) {
            response.getWriter().write(String.valueOf(R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR.getMessage())));
            return null;
        }

        // 做验证码验证
        String checkCode = redisTemplate.opsForValue().get(reqLoginVO.getSessionId());
        if (StringUtils.isBlank(checkCode) || !checkCode.equalsIgnoreCase(reqLoginVO.getCode())) {
            response.getWriter().write(String.valueOf(R.error(ResponseCode.CHECK_CODE_ERROR.getMessage())));
            return null;
        }

        String username = reqLoginVO.getUsername();
        username = username.trim();
        String password = reqLoginVO.getPassword();

        // 将用户名和密码信息封装到认证票据对象下
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        // setDetails(request, authRequest);

        // 调用认证管理器认证指定的票据对象
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 用户认证成功后回调的方法
     * 认证成功后，响应前端token信息
     * @param request http请求对象
     * @param response http响应对象
     * @param chain security的过滤器链
     * @param authResult 验证结果
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 获取用户的详情信息
        LoginUserDetail userDetail = (LoginUserDetail)authResult.getPrincipal();

        // 权限集合转换为String
        List<GrantedAuthority> authorities = userDetail.getAuthorities();
        String auStrList = authorities.toString();

        // 创建响应对象
        LoginRespVOExt resp = new LoginRespVOExt();
        BeanUtils.copyProperties(userDetail, resp);
        // 生成token字符串:将用户名称和权限信息价格生成token字符串
        String tokenStr = JwtTokenUtil.createToken(userDetail.getUsername(), auStrList);
        resp.setAccessToken(tokenStr);

        String respStr = new ObjectMapper().writeValueAsString(R.ok(resp));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respStr);
    }

    /**
     * 认证失败后，回调的方法
     * @param request http请求对象
     * @param response http响应对象
     * @param failed 异常信息
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String respStr = new ObjectMapper().writeValueAsString(R.error(ResponseCode.SYSTEM_PASSWORD_ERROR));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respStr);
    }
}
