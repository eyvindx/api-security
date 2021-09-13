package com.bancoazul.apisecurity.service;

import com.bancoazul.apisecurity.jwt.JwtUser;
import com.bancoazul.apisecurity.model.dao.UsuarioDao;
import com.bancoazul.apisecurity.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

//    @Autowired
//    private UserMapper userService;
    @Autowired
    private UsuarioDao client ;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = client.findByUsername(s);
        if (user == null) {
            throw new RuntimeException("user " + s + "non-existent"); // You can use assertions to make the code more concise and elegant
        }
        JwtUser jwtUser = new JwtUser(user);
        // Resolve the roles of the database to the permission set of UserDetails
        // AuthorityUtils.commaSeparatedStringToAuthorityList Convert a comma separated character set into a list of permission objects
        jwtUser.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole()));
        return jwtUser;
    }
}
