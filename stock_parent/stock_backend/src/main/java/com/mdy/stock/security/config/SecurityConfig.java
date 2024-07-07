package com.mdy.stock.security.config;

import com.mdy.stock.security.filter.JwtAuthorizationFilter;
import com.mdy.stock.security.filter.JwtLoginAuthenticationFilter;
import com.mdy.stock.security.handler.StockAccessDenyHandler;
import com.mdy.stock.security.handler.StockAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author mdy
 * @date 2024-07-07 6:14
 * @description
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启动注解使用权限控制
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 定义公共的无需被拦截的资源
     * @return 公共资源url字符串数组
     */
    private String[] getPubPath() {
        // 公共访问资源
        return new String[]{
                "/**/*.css", "/**/*.js", "/favicon.ico", "/doc.html",
                "/druid/**", "/webjars/**", "/v2/api-docs", "/api/captcha",
                "/swagger/**", "/swagger-resources/**", "/swagger-ui.html"
        };
    }

    /**
     * 配置过滤规则
     * @param http
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable()
                .authorizeRequests() // 对资源进行认证处理
                // 公共资源都允许访问
                .antMatchers(getPubPath()).permitAll()
                .anyRequest().authenticated();

//        // 开启允许iframe嵌套。security默认禁用iframe跨域与缓存
//        http.headers().frameOptions().disable().cacheControl().disable();
//        // session禁用
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 设置使用自定义的过滤器
        http.addFilterBefore(jwtLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), JwtLoginAuthenticationFilter.class);
        // 配置权限访问拒绝处理器
        http.exceptionHandling().accessDeniedHandler(new StockAccessDenyHandler())
                .authenticationEntryPoint(new StockAuthenticationEntryPoint());
    }

    /**
     * 自定义认证过滤器bean
     * @return JwtLoginAuthenticationFilter
     */
    @Bean
    public JwtLoginAuthenticationFilter jwtLoginAuthenticationFilter() throws Exception {
        // 设置该过滤器过滤的访问路径
        JwtLoginAuthenticationFilter filter = new JwtLoginAuthenticationFilter("/api/login");
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    /**
     * 自定义授权过滤器
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter();
    }

}
