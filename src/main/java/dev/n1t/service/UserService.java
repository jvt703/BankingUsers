package dev.n1t.service;

import dev.n1t.dto.AuthenticationRequest;
import dev.n1t.dto.OutgoingAccountDto;
import dev.n1t.dto.UserDTO;
import dev.n1t.exception.runtime.AuthenticationException;
import dev.n1t.model.User;
import dev.n1t.repository.AddressRepository;
import dev.n1t.repository.RoleRepository;
import dev.n1t.repository.UserRepository;
import dev.n1t.util.EmailServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;

    private final EmailServiceImpl emailService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, AddressRepository addressRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.emailService = emailService;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User createUser(User user) {
        addressRepository.save(user.getAddress());
        roleRepository.save(user.getRole());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User readUserById(long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public User readUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(User user) {
        if (checkUserExistById(user.getId())) {
            addressRepository.save(user.getAddress());
            roleRepository.save(user.getRole());
            return userRepository.save(user);
        }
        throw new EntityNotFoundException();
    }

    @Transactional
    public void deleteUserById(long id) {
        if (checkUserExistById(id)) {
            userRepository.deleteById(id);
        }
        throw new EntityNotFoundException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserDTO createUserDTO(UserDTO userDTO) {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(createUser(userMapper(userDTO)));
        return userDTOMapper(userList).get(0);
    }

    @Transactional(readOnly = true)
    public UserDTO readUserByIdDTO(int id) {
        User user = readUserById(id);
        if (user == null) {return null;}
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        return userDTOMapper(userList).get(0);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> readAllUsersDTO(Map<String, String> query) {
        List<UserDTO> userDTOS = userDTOMapper(readAllUsers());
        if (userDTOS.isEmpty()) {return userDTOS;}

        List<UserDTO> returnList = userDTOS;

        if (query.containsKey("id")) {
            try {
                int id = Integer.parseInt(query.get("id"));
                List<UserDTO> tempList = returnList;
                tempList = returnList.stream()
                        .filter(user -> (user.id() == id))
                        .collect(Collectors.toList());
                returnList = tempList;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        if (query.containsKey("fn")) {
            String fn = query.get("fn");
            List<UserDTO> tempList = returnList;
            tempList = returnList.stream()
                    .filter(user -> (user.firstname().equalsIgnoreCase(fn)))
                    .collect(Collectors.toList());
            returnList = tempList;
        }

        if (query.containsKey("ln")) {
            String ln = query.get("ln");
            List<UserDTO> tempList = returnList;
            tempList = returnList.stream()
                    .filter(user -> (user.lastname().equalsIgnoreCase(ln)))
                    .collect(Collectors.toList());
            returnList = tempList;
        }

        if (query.containsKey("email")) {
            String email = query.get("email");
            List<UserDTO> tempList = returnList;
            tempList = returnList.stream()
                    .filter(user -> (user.email().equalsIgnoreCase(email)))
                    .collect(Collectors.toList());
            returnList = tempList;
        }
        return returnList;
    }

    @Transactional
    public UserDTO updateUserDTO(UserDTO userDTO) {
        User user = updateUser(userMapper(userDTO));
        if (user == null) {return null;}
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        return userDTOMapper(userList).get(0);
    }

    private List<UserDTO> userDTOMapper(List<User> userList) {
        ArrayList<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getBirthDate(),
                    user.getAddress(),
                    user.getRole()
            );
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    private User userMapper(UserDTO userDTO) {
        return new User(
                userDTO.id(),
                userDTO.firstname(),
                userDTO.lastname(),
                userDTO.email(),
                false,
                userDTO.password(),
                false,
                userDTO.birthDate(),
                userDTO.address(),
                userDTO.role()
                );
    }

    private boolean checkUserExistById(long id) {
        return userRepository.findById((id)).isPresent();
    }

    public void getBalanceFromAccount(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //so what I have to do actually is set it up so that there is mtls and managing certificates which is a lot instead I can for now create an admin acc
        // and first ping the auth route for a valid JWT token to get this
        System.out.println("gothere");
//        headers.set("Role", "admin"); // Set the Role header to "admin"
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        String jwtToken = getJWTToken();
//        headers.set("Authorization", "Bearer " + jwtToken); // Set the JWT token in the Authorization header
        HttpEntity<String> entity = new HttpEntity<>(headers);

    // So what we're going to do is map all of these accounts into a nested map which has the username as the key and the value is
        // another map which has all of the accounts and values associated with that user
        ResponseEntity<List<OutgoingAccountDto>> response = restTemplate.exchange(
                "http://localhost:8081/accounts/all/admin",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        System.out.println(response+"here the response");
        List<OutgoingAccountDto> accounts = response.getBody();
        System.out.println(accounts.toString() + "accounts");
        //we turn into a stream then collect it grouped by the userId and a map of the corresponding account Names and balances
        Map<String, Map<String, Double>> userAccounts = accounts.stream()
                .collect(Collectors.groupingBy(
                        account -> account.getEmail(),
                        Collectors.toMap(
                                OutgoingAccountDto::getAccountName,
                                OutgoingAccountDto::getBalance,
                                (balance1, balance2) -> balance2)
                ));
        //now that we have our map of maps we will iterate through the map and send an email with the account balances
        userAccounts.forEach((email, accountMap)->{
            emailService.sendBalanceEmail(email, accountMap);
        });

    }



    public String getJWTToken(){
        String url = "http://localhost:8082/authenticateAdmin";
        RestTemplate restTemplate = new RestTemplate();
        //set the body with username and password for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("user@example.com");
        authenticationRequest.setPassword("password123");

        HttpEntity<AuthenticationRequest> requestEntity = new HttpEntity<>(authenticationRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String JWTtoken = responseEntity.getBody();
            return JWTtoken;
        } else {
            throw new AuthenticationException("Authentication failed: Invalid credentials");
        }
    }
}
