package konkuk.shop.global.security;

import konkuk.shop.domain.member.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
        this.userId = member.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
