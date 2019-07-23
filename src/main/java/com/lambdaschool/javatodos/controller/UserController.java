package com.lambdaschool.javatodos.controller;

import com.lambdaschool.javatodos.model.Todo;
import com.lambdaschool.javatodos.model.User;
import com.lambdaschool.javatodos.service.QuoteService;
import com.lambdaschool.javatodos.service.UserService;
import com.lambdaschool.javatodos.model.User;
import com.lambdaschool.javatodos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{

    @Autowired
    private UserService userService;

    @Autowired
    QuoteService quoteService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users", produces = {"application/json"})
    public ResponseEntity<?> listAllUsers()
    {
        List<User> myUsers = userService.findAll();
        return new ResponseEntity<>(myUsers, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users/mine", produces = {"application/json"})
    public ResponseEntity<?> listme()
    {
        Object me = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(me, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/user/{userId}", produces = {"application/json"})
    public ResponseEntity<?> getUser(@PathVariable Long userId)
    {
        User u = userService.findUserById(userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }


    @GetMapping(value = "/getusername", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> getCurrentUserName(Authentication authentication)
    {
        return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/user", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User newuser) throws URISyntaxException
    {
        newuser =  userService.save(newuser);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newuser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/addtodo/{userid}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewQuote(@Valid @RequestBody Todo newtodo,@PathVariable long userid) throws URISyntaxException
    {

        User u = userService.findUserById(userid);
        u.getTodos().add(new Todo("Dustin java-quotes-swagger",  u));


        userService.save(u);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(u.getTodos())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }



    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User updateUser, @PathVariable long id)
    {
        userService.update(updateUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id)
    {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}