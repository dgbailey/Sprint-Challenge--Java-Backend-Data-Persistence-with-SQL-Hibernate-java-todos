package com.lambdaschool.javatodos;

import com.lambdaschool.javatodos.model.Role;
import com.lambdaschool.javatodos.model.Todo;
import com.lambdaschool.javatodos.model.User;
import com.lambdaschool.javatodos.model.UserRoles;
import com.lambdaschool.javatodos.repos.RoleRepository;
import com.lambdaschool.javatodos.repos.ToDoRepository;
import com.lambdaschool.javatodos.repos.UserRepository;
import com.lambdaschool.javatodos.service.QuoteService;
import com.lambdaschool.javatodos.service.UserService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Component
public class SeedData implements CommandLineRunner {
    RoleRepository rolerepos;
    UserService userrepos;
    ToDoRepository todorepos;

    public SeedData(RoleRepository rolerepos, UserService userrepos, ToDoRepository todorepos) {
        this.rolerepos = rolerepos;
        this.userrepos = userrepos;
        this.todorepos = todorepos;
    }

    @Override
    public void run(String[] args) throws Exception {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));

        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));

        rolerepos.save(r1);
        rolerepos.save(r2);

        User u1 = new User("barnbarn", "ILuvM4th!", users);
        User u2 = new User("admin", "password", admins);
        User u3 = new User("Bob", "password", users);
        u3.getTodos().add(new Todo("Walk the dogs", u3));
        u3.getTodos().add(new Todo("provide feedback to my instructor", u3));

        User u4 = new User("Jane", "password", users);

        // the date and time string should get coverted to a datetime Java data type. This is done in the constructor!
        u4.getTodos().add(new Todo("Finish java-orders-swagger",  u4));
        u4.getTodos().add(new Todo("Feed the turtles", u4));
        u4.getTodos().add(new Todo("Complete the sprint challenge", u4));

        //"2019-01-13 04:04:04", "2019-03-01 04:04:04"
        //"2019-02-22 04:04:04", "2019-01-17 04:04:04"//"2019-02-13 04:04:04"




        userrepos.save(u1);
        userrepos.save(u2);
        userrepos.save(u3);
        userrepos.save(u4);

    }


}

