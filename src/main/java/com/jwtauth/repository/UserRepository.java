package com.jwtauth.repository;
import com.jwtauth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    @Query("Select user from Users user where user.email=:email")
    Optional<Users> findByEmail(String email);
}
