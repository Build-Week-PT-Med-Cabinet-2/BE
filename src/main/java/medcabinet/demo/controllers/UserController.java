package medcabinet.demo.controllers;


import medcabinet.demo.models.User;
import medcabinet.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/api/user/{userid}", produces = {"application/json"})
    public ResponseEntity<?> getUserById(@PathVariable long userid) {
        User user = userService.findUserById(userid);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/api/user/signup", consumes = {"application/json"})
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User newUser){
        newUser.setUserid(0);
        newUser = userService.save(newUser);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newUser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/user/{userid}", consumes = {"application/json"})
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, @PathVariable long userid) {
        user.setUserid(userid);
        userService.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping(value = "/api/user/{userid}", consumes = {"application/json"})
    public ResponseEntity<?> deleteByUserId(@PathVariable  long userid){
        userService.deleteById(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
