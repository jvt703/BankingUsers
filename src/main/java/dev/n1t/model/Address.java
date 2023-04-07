package dev.n1t.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Size(min = 2)
    @Column(nullable = false)
    private String state;

    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotBlank
    @Pattern(regexp = "\\d{5}", message = "ZIP code must be 5 digits.")
    @Column(name = "zipCode", nullable = false)
    private String zipCode;
}
