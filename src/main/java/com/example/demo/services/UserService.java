package com.example.demo.services;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // Methods below

    public List<User> findAll(String username) {
        if(username != null){
            var user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("Could not find the user username %s. ", username)));
            return List.of(user);   // return list of type user.
        }
            return userRepository.findAll();
    }

    public User findById(String id){
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("Could not find the user id %s. ", id)));  // throw exception if it cant find id.
        // refers to the empty constructor in the User.
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public User save(User user){
        if(StringUtils.isEmpty(user.getPassword())){ // user.getPassword() == null || user.getPassword().isEmpty()
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I need a password!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // before we save the user we encrypt the password.
        return userRepository.save(user);
    }

    public void update(String id, User user){
        if(!userRepository.existsById(id)) {     // check if it exists before trying to update.
           // throw new RuntimeException();   // if it doesnt exist throw exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find the user id %s. ", id));
        }
        // if it exist we do this.
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void delete(String id){
        if(!userRepository.existsById(id)){
           // throw new RuntimeException();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find the user id %s. ", id));
        }
        userRepository.deleteById(id);
    }
}
