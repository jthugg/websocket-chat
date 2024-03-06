package neo.chat.api.application.auth.model;

import neo.chat.api.persistence.entity.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ChatUserDetails implements UserDetails {

    private final Member member;

    public ChatUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> member.getRole().getName());
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
