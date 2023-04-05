package dev.n1t.service;

import dev.n1t.dto.UserDTO;
import dev.n1t.model.User;
import dev.n1t.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User readUserById(int id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(User user) {
        if (checkUserExistById(user.getId())) {
            return userRepository.save(user);
        }
        throw new EntityNotFoundException();
    }

    @Transactional
    public void deleteUserById(int id) {
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
                    user.getPassword()
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
                0,
                0
                );
    }

    private boolean checkUserExistById(int id) {
        return userRepository.findById(id).isPresent();
    }
}
