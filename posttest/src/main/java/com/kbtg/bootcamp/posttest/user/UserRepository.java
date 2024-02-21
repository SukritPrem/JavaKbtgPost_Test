package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.security.CustomUserDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //    SELECT * FROM user_profile WHERE userid='2';
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM user_proflie WHERE userid=:userid", nativeQuery = true)
    User finduserByuserId(@Param("userid") String userid);

    Optional<User> findByuserid(String userid);

}