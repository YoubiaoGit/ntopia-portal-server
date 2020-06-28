package com.hcycom.sso.service;


import com.hcycom.sso.client.AuthorizedUserFeignClient;
import com.hcycom.sso.domain.User;
import com.hcycom.sso.dto.UserDTO;
import com.hcycom.sso.dto.UserDTO2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@AuthorizedUserFeignClient(name = "uaa")
public interface UaaFeignClient {
    @RequestMapping(value = "/api/account", method = RequestMethod.GET)
    public UserDTO account();

    @RequestMapping(value = "/api/users/authorities", method = RequestMethod.GET)
    public String authorities();

    @RequestMapping(value = "/api/new/users", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody UserDTO2 userDTO);

    @RequestMapping(value = "/api/new/users", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@RequestBody UserDTO2 userDTO);

    @RequestMapping(value = "/api/new/users/loginname", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUserLogin(@RequestBody UserDTO2 userDTO);

    @RequestMapping(value = "/api/new/users/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "login") String login);

}


