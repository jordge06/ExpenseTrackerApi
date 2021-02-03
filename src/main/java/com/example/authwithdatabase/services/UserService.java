package com.example.authwithdatabase.services;

import com.example.authwithdatabase.domain.User;
import com.example.authwithdatabase.exceptions.EtAuthException;

public interface UserService {

    User validateUser(String email, String pass) throws EtAuthException;

    User registerUser(String firstname, String lastname, String email, String password) throws EtAuthException;

}
