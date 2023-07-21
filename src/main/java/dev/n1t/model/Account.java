package dev.n1t.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "accountTypeId", referencedColumnName = "id")
    private AccountType accountType;

    @Column(nullable = false) //no min as overspending could potentially result in a negative balance
    private Double balance;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @DecimalMin("0")
    @Column(name = "pointsBalance")
    private Long pointsBalance;

    @NotBlank
    @Column(name = "accountName", nullable = false)
    private String accountName;

    @Column(name = "createdDate", nullable = false)
    private Long createdDate;
}
