package com.amway.lms.backend.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.entity.User;

public class CustomUserDetail implements UserDetails{
    
    /**
     * 
     */
    private static final long serialVersionUID = 280053166649386696L;
    private User user;

    public CustomUserDetail() {
        super();
    }

    public CustomUserDetail(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoleId() == Roles.ADMIN.getIntValue())
            authorities.add(new SimpleGrantedAuthority(Roles.ADMIN.getROLE_NAME()));
        else if (user.getRoleId() == Roles.APPROVAL_MANAGER.getIntValue())
            authorities.add(new SimpleGrantedAuthority(Roles.APPROVAL_MANAGER.getROLE_NAME()));
        else if (user.getRoleId() == Roles.TRAINEE.getIntValue())
            authorities.add(new SimpleGrantedAuthority(Roles.TRAINEE.getROLE_NAME()));
        else if (user.getRoleId() == Roles.CODINATOR.getIntValue())
            authorities.add(new SimpleGrantedAuthority(Roles.CODINATOR.getROLE_NAME()));
        else if (user.getRoleId() == Roles.HOD.getIntValue())
            authorities.add(new SimpleGrantedAuthority(Roles.HOD.getROLE_NAME()));
        else if (user.getRoleId() == Roles.HR.getIntValue())
            authorities.add(new SimpleGrantedAuthority(Roles.HR.getROLE_NAME()));
        return authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getUserID();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return user.getActive() == 1 ? true : false;
    }

}
