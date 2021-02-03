package com.example.authwithdatabase.services;

import com.example.authwithdatabase.domain.User;
import com.example.authwithdatabase.exceptions.EtAuthException;
import com.example.authwithdatabase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User validateUser(String email, String pass) throws EtAuthException {
        email = Optional.ofNullable(email).map(String::toLowerCase).orElse("");
        return userRepository.findByEmailAndPassword(email, pass);
    }

    @Override
    public User registerUser(String firstname, String lastname, String email, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        email = Optional.ofNullable(email).map(String::toLowerCase).orElse("");
        //if (email != null) email = email.toLowerCase();
        if (!pattern.matcher(email).matches()) throw new EtAuthException("Invalid Email Format");
        Integer count = userRepository.getCountByEmail(email);
        if (count > 0)
            throw new EtAuthException("Email Already in Use");
        Integer userId = userRepository.create(firstname, lastname, email, password);
        return userRepository.findById(userId);
    }
}
