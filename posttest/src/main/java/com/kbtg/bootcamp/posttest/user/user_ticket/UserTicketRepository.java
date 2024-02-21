package com.kbtg.bootcamp.posttest.user.user_ticket;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {

//    SELECT * FROM user_lottery WHERE id='34' AND ticket='000001';
//    SELECT * FROM user_lottery WHERE id=:id AND ticket=:ticket;

//    @Modifying
//    @Transactional
//    @Query(value = "SELECT * FROM user_lottery WHERE userid=:userid AND ticket=:ticket", nativeQuery = true)
//    List<UserLottery> SearchUserHaveTicketByidAndticket(@Param("userid") String userid,@Param("ticket") String ticket);

    Optional<UserTicket> findByUseridAndTicket(String userid, String ticket);


//UPDATE user_lottery SET amount = '2' WHERE userid = 'Person1';

    @Modifying
    @Transactional
    @Query(value ="UPDATE user_ticket SET amount =:amount WHERE userid =:userid", nativeQuery = true)
    void updateAmountById(@Param("amount") String amount,@Param("userid") String userid);

    List<UserTicket> findByuserid(String userid);


//    DELETE FROM user_ticket WHERE ticket='000001' AND userid = '0123456789';
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM user_ticket WHERE ticket=:ticket AND userid =:userid", nativeQuery = true)
    void deleteTicketByuserId(@Param("ticket") String ticket,@Param("userid") String userid);
}

