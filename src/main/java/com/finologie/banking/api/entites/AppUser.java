package com.finologie.banking.api.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Table(name = "app_user")
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "^[a-z][a-z0-9]*$", message = "value  does not mach regex {regexp}")
    @Column(name = "user_name", unique = true, updatable = false)
    private String username;

    @Column(name = "f_action_count")
    private int forbiddenActionsCount = 0;

    // we could add a pattern for password but lets keep it simple
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @ManyToMany
    @JoinTable(
            name = "user_role_assoc",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<AppUserRole> roles = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "user_bank_account_assoc",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "bank_account_id")
    )
    private Set<BankAccount> bankAccounts = new HashSet<>();

    @Column(name = "non_expired")
    private Boolean isAccountNonExpired;

    @Column(name = "non_locked")
    private Boolean isAccountNonLocked;

    @Column(name = "enabled")
    private Boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {

        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // get along if we implement user expiration mechanism
        //for the moment we will keep it always true
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }


    //audit attributes

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updateDate;


}
