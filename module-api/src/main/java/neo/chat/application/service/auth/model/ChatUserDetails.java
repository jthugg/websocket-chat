package neo.chat.application.service.auth.model;

import lombok.RequiredArgsConstructor;
import neo.chat.persistence.entity.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class ChatUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> member.getRole().name());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !member.isRemoved();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !member.isRemoved();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !member.isRemoved();
    }

    @Override
    public boolean isEnabled() {
        return !member.isRemoved();
    }

}
