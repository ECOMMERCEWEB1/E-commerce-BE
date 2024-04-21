package com.webproject.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
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

    // Moetez : ???
    // IN CART PRODUCTS --> COMMENTED WHILE PRODUCTS AREN'T MADE
    //@ManyToMany
    //@JoinTable(
    //        name="user_products",
    //        joinColumns=@JoinColumn(name="user_id"),
    //        inverseJoinColumns=@JoinColumn(name="product_id")
    //)
    //private List<Product> products;
}
