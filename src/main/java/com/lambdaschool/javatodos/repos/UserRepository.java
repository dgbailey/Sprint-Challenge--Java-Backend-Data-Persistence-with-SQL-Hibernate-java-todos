package com.lambdaschool.javatodos.repos;

import com.lambdaschool.javatodos.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
