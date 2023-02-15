package dev.n1t.service;

import dev.n1t.dto.UserDTO;
import dev.n1t.model.User;
import dev.n1t.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

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
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(User user) {
        if (readUserById(user.getId()) == null) {return null;}
        return userRepository.save(user);
    }

    @Transactional
    public boolean deleteUser(int id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
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
    public List<UserDTO> readAllUsersDTO() {
        return userDTOMapper(readAllUsers());
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
}
