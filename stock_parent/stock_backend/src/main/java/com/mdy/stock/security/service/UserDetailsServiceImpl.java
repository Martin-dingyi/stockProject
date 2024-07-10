package com.mdy.stock.security.service;

import com.mdy.stock.face.impl.UserCacheFaceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mdy
 * @date 2024-07-07 4:14
 * @description
 */

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserCacheFaceImpl userCacheFace;

    /**
     * @param userName 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userCacheFace.getLoginUserDetail(userName);
    }
}
