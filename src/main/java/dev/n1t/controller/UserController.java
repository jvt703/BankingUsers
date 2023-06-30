package dev.n1t.controller;

import dev.n1t.dto.PasswordResetDTO;
import dev.n1t.dto.SecurityPasswordDTO;
import dev.n1t.dto.SecurityQuestionDTO;
import dev.n1t.dto.UserDTO;

import dev.n1t.model.Address;
import dev.n1t.model.Role;
import dev.n1t.model.User;
import dev.n1t.service.UserService;
import dev.n1t.util.DummyDataInitializer;
import dev.n1t.util.EmailServiceImpl;
import dev.n1t.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;

    private final DummyDataInitializer dummyDataInitializer;



    @Autowired
    public UserController(UserService userService, EmailServiceImpl emailService, PasswordEncoder passwordEncoder, DummyDataInitializer dummyDataInitializer) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.dummyDataInitializer = dummyDataInitializer;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        emailService.sendSimpleMessage(
                userDTO.email(),
                "Welcome to Nine Ten - Verify",
                "Welcome to Nine Ten " + userDTO.firstname() +
                        "! Please use this code to verify your email: 392887");
        return ResponseEntity.ok(userService.createUserDTO(userDTO));
    }

    // reset password route

    @PostMapping("/securityQuestion")

    public ResponseEntity<String> securityQuestionCheck(@RequestBody SecurityPasswordDTO securityPasswordDTO){
        User user = userService.readUserByEmail(securityPasswordDTO.getEmail());
        if(!user.getSecurityAnswer().equals(securityPasswordDTO.getSecurityAnswer())){

            throw new RuntimeException();
        }
        String resetToken = TokenGenerator.generateResetToken();

        // Save the reset token to the user
        user.setResetToken(resetToken);
        userService.updateUser(user);

        // Generate the password reset link with the token
        String resetLink = "http://localhost:3000/reset-password?userId=" + user.getId() + "&token=" + resetToken;

        // Send the password reset email with the link
        emailService.sendpassResetEmail(user.getEmail(), resetLink);

        // Return the appropriate response
        return ResponseEntity.ok("Password reset email sent"); // Modify UserDTO as needed
    }

 @PostMapping("/passwordReset")
 public ResponseEntity<String> passwordReset(@RequestBody PasswordResetDTO passwordResetDTO){
        //check token is correct
        User user = userService.readUserById(passwordResetDTO.getId());
     System.out.println(user.getResetToken().toString()+"thereset");
     System.out.println(passwordResetDTO.getResetToken().toString()+"theonesent");
        if(!user.getResetToken().equals(passwordResetDTO.getResetToken())){

            throw new RuntimeException("Token does not match");
        }
        user.setPassword(passwordEncoder.encode(passwordResetDTO.getUpdatedPassword()));
        user.setResetToken(null);
        //update password
        //delete token
        userService.updateUser(user);



     return ResponseEntity.ok("Password Reset Successfully");
 }
    @GetMapping("/securityQuestion/{email}")
    public ResponseEntity<SecurityQuestionDTO> readUserByEmail(@PathVariable("email") String email) {

        User user = userService.readUserByEmail(email);
        if (user == null) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        SecurityQuestionDTO securityQuestionDTO = new SecurityQuestionDTO(user.getEmail(), user.getSecurityQuestion());
        return ResponseEntity.ok(securityQuestionDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> readUserById(@PathVariable("id") int id) {
//        UserDTO userDTO = new UserDTO(1,
//                "Mario",
//                "Mario",
//                "mario.mario@email.com",
//                "password",
//                0L,
//                new Address(1L, "city", "state", "street", "zipCode"),
//                new Role(1L, "roleName"));
//        return ResponseEntity.ok(userDTO);
        UserDTO userDTO = userService.readUserByIdDTO(id);
        if (userDTO == null) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    @PreAuthorize("((#Role.equals('admin'))")
    public ResponseEntity<List<UserDTO>> readAllUsers(
            @RequestParam Map<String, String> query,
            @RequestHeader("Role") String Role) {
        return ResponseEntity.ok(userService.readAllUsersDTO(query));
    }

    @PutMapping("/{userId}/update")
    @PreAuthorize("(#Role.equals('User') and #userId == #requestId) or (#Role.equals('admin'))")
    public ResponseEntity<UserDTO> updateUser(
            @RequestBody UserDTO userDTO,
            @RequestHeader("Role") String Role,
            @RequestHeader("id") Long requestId
    ) {
        UserDTO userDTO1 = userService.updateUserDTO(userDTO);
        if (userDTO1 == null) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        return ResponseEntity.ok(userDTO1);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("((#Role.equals('admin'))")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
