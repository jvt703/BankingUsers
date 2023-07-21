package dev.n1t.dto;

import dev.n1t.model.Account;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OutgoingAccountDto {
    private Long id;
    private long userId;
    private String email;

    private String accountTypeName;
    private String accountTypeDescription;

    private double balance;
    private boolean active;
    private long pointsBalance;
    private String accountName;
    private long createdDate;

    public OutgoingAccountDto(Account account) {
        this.id = account.getId();
        this.userId = account.getUser().getId();
        this.email = account.getUser().getEmail();
        this.accountTypeName = account.getAccountType().getAccountTypeName();
        this.accountTypeDescription = account.getAccountType().getDescription();
        this.active = account.getActive();
        this.balance = account.getBalance();
        this.pointsBalance = account.getPointsBalance();
        this.accountName = account.getAccountName();
        this.createdDate = account.getCreatedDate();
    }
}