package com.webproject.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webproject.ecommerce.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Collection;
import java.util.List;
import com.webproject.ecommerce.entities.Product;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User implements UserDetails {
    public User(String firstName,String lastName,String email,String password,int age)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.CLIENT;
        this.email = email;
        this.password = password;

        this.age = age;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @NotBlank(message="First name is mandatory")
    @Length(min=2,message = "First Name must contain atleast 2 characters")
    private String firstName;
    @Length(min=2,message = "First Name must contain atleast 2 characters")
    @NotBlank(message="Last name is mandatory")
    private String lastName;
    @Min(value=13)
    private Integer age;
    @Email(message="Please respect the email format x@y.z")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer" )
    @JsonIgnoreProperties(value = {"customer"}, allowSetters = true)
    private Set<ProductOrder> orders;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
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
