package com.lambdaschool.javatodos.repos;

import com.lambdaschool.javatodos.model.Todo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<Todo,Long> {

    Todo findBydescription(String name);
}
