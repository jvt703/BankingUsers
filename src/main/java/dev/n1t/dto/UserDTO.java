package dev.n1t.dto;

import dev.n1t.model.Address;
import dev.n1t.model.Role;

public record UserDTO(long id, String firstname, String lastname, String email, String password
        , long birthDate, Address address, Role role) {
}
