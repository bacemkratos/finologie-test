package com.finologie.banking.api.entites;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "role")
@Entity
@Data
public class AppUserRole implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true,updatable = false)
    private  String alias;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<AppUser> users;

    @Override
    public String getAuthority() {
        return "ROLE_"+alias;
    }

    //audit attributes

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
