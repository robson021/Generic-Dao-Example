package com.example.dao;

import com.example.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserDao extends GenericDao<User> {
    public UserDao(EntityManager em) {
        super(em);
    }
}
