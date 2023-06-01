package dev.n1t.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String firstname;

    @NotNull
    @Column(nullable = false)
    private String lastname;

    @NotNull
    @Email
    @Column(nullable = false)
    private String email;



    @NotNull
    @Column(nullable = false, name = "emailValidated")
    private boolean emailValidated;

    @NotNull
    @Column(nullable = false)
    private String password;


    @NotNull
    @Column(nullable = false)
    private boolean active;

    @NotNull
    @Column(nullable = false, name = "birthDate")
    private Long birthDate;

    @ManyToOne()
    @JoinColumn(name = "addressId", referencedColumnName = "id", nullable = false)
    private Address address;

    @ManyToOne()
    @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
    private Role role;
    @Column(nullable = true)
    private String securityQuestion;
    @Column(nullable = true)
    private String securityAnswer;
    @Column
    private String resetToken;

    public User(Long id, String firstname, String lastname, String email, boolean emailValidated, String password, boolean active, Long birthDate, Address address, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.emailValidated = emailValidated;
        this.password = password;
        this.active = active;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
    }
}