package com.jwtauth.users;

import com.jwtauth.entity.Users;
import com.jwtauth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserTestCase {

    @Autowired
    UserRepository userRepository;



    @Test
    public void TestCreateUser(){

        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String password="@123Test";
        String encodePassword=passwordEncoder.encode(password);
        System.out.println(encodePassword);
//        Users newUser=new Users("samir@gmail.com",encodePassword);
//       Users savedUser= userRepository.save(newUser);
//       assertThat(savedUser).isNotNull();
//       assertThat(savedUser.getId()).isGreaterThan(0);

    }

}
