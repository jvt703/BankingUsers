package dev.n1t.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "Users")
public class UserB {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String firstname;
    private String lastname;
    private String email;
    private boolean emailValidated;
    private String password;
    private boolean active;
    private int addressId;
    private int roleId;
}