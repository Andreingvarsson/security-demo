package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users") // Gives all mappings the same start url.
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<List<User>> findAllUsers(@RequestParam(required = false) String username){// @RequestParam is obligatory. (required = false) makes it ok regardless.
        var users = userService.findAll(username);
      //  return new ResponseEntity<>(users, HttpStatus.OK); // status code 200
        // ResponseEntity.status(HttpStatus.OK);
        return ResponseEntity.ok(users); // same as the line above

    }

    @GetMapping("/{id}") // /api/v1/users/xxxxxxx
    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
    public ResponseEntity<User> findUserById(@PathVariable String id){      // if id have different names u have to clarify inside @Pathvariable("identity") for instance.
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<User> saveUser(@Validated @RequestBody User user){   // @RequestBody to find the user.
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String id, @Validated @RequestBody User user){     // with {id} use @PathVariable
        userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT) // response 204
    public void deleteUser(@PathVariable String id){
        userService.delete(id);
    }

    /*
    @GetMapping("/username/{username}")
    public User findUserByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

     */
}
