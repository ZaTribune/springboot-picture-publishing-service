package zatribune.spring.pps.data.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

/**
 * I'd usually make a separate table for user Authorisation/Authentication
 * away from user business logic so that to be linked together using a foreign key
 * e.g [BasicUser] <-> [Client {with common properties like first_name,last_name,address ....etc}] but I'll keep it simple.
 **/

@Getter
@Setter
@Entity
@Table(name = "basic_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String username;

    @NotBlank
    @Size(min = 8,max = 255)// by default hibernate on the database creates this column with a length of 255
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany (fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // this one is optional==but is required case you named identity attributes as “id”
    @JoinTable (name = "basic_user_role", joinColumns = @JoinColumn (name = "basic_user"),
            inverseJoinColumns =   @JoinColumn (name = "role"))
    private Set<Role> roles;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    //detached entity passed to persist
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},mappedBy = "user")
    private Set<Pic> pics;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
