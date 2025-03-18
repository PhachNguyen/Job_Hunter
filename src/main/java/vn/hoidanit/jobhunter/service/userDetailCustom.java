package vn.hoidanit.jobhunter.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

// import vn.hoidanit.jobhunter.domain.User;

@Component("userDetailService")
public class userDetailCustom implements UserDetailsService {
    private final userService userService;

    public userDetailCustom(vn.hoidanit.jobhunter.service.userService userService) {
        this.userService = userService;
    }

    // Sử dụng tính đa h
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        vn.hoidanit.jobhunter.domain.User user = this.userService.handlerGetUserbyUserName(username);
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
