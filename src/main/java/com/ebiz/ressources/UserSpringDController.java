package com.ebiz.ressources;

import com.ebiz.dao.EsDAO.repository.UserRepository;
import com.ebiz.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ebiz on 12/02/2015.
 */
@RestController
@RequestMapping("/contact/spring/users")
public class UserSpringDController {

    @Autowired
    UserRepository repository;

    @RequestMapping
    public User getUser(){
        List<User> user = repository.findByUsername("Victor");
        System.out.println("Size : " );
        return user.get(0);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createUser(@RequestBody User u) {
        repository.save(u);
    }

    @RequestMapping(value="/adresses/{rue}",method = RequestMethod.GET)
    public User getUserByRue(@PathVariable String rue){
        List<User> user;
        user = repository.findByAdresses(rue);
        System.out.println("Size : " + user);
        return user.get(0);
    }
}