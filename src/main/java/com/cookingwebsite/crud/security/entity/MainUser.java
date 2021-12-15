package com.cookingwebsite.crud.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.cookingwebsite.crud.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainUser implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String firstname;
	private String lastname;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static MainUser build(User user){
        List<GrantedAuthority> authorities =
        		user.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol
                .getRoleName().name())).collect(Collectors.toList());
        return new MainUser(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), authorities);
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

}
