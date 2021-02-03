package com.example.authwithdatabase.repositories;

import com.example.authwithdatabase.domain.User;
import com.example.authwithdatabase.exceptions.EtAuthException;

public interface UserRepository {

    Integer create(String firstname, String lastname, String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
