package com.kbtg.bootcamp.posttest.user.user_ticket;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {

    Optional<UserTicket> findByUseridAndTicket(String userid, String ticket);

    @Modifying
    @Transactional
    @Query(value ="DELETE FROM user_ticket WHERE ticket=:ticket AND userid =:userid", nativeQuery = true)
    void deleteTicketByuserId(@Param("ticket") String ticket,@Param("userid") String userid);
}

