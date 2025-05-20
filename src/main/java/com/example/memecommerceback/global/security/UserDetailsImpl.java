package com.example.memecommerceback.global.security;

import com.example.memecommerceback.domain.user.entity.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, OAuth2User {

    private final Users user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // UserRole role = user.getRole();
        // String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority
            = new SimpleGrantedAuthority(null);
        Collection<GrantedAuthority> authorities
            = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword(){
        return null;
    }

    @Override
    public String getUsername() {
        return null;
        // return this.user.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
        // return Map.of("email", user.getEmail(), "oauthId", user.getOauthId());
    }

    public String getEmail(){
        return null;
        // return this.user.getEmail();
    }
    @Override
    public String getName() {
        return null;
        // return user.getOauthId(); // OAuth2 고유 식별자
    }
}
