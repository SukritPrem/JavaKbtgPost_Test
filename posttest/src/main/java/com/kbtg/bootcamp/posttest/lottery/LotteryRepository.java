package com.kbtg.bootcamp.posttest.lottery;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long>  {

    @Modifying
    @Transactional
    @Query("UPDATE Lottery SET amount =:amount WHERE ticket =:ticket")
    void updateAmountByticket(String amount, String ticket);

    @Modifying
    @Transactional
    @Query("UPDATE Lottery SET price =:price WHERE ticket =:ticket")
    void updatePriceByticket(String price, String ticket);


    Optional<Lottery> findByTicket(String ticket);
//    UPDATE lottery SET amount='0' WHERE ticket='00001';
    @Modifying
    @Transactional
    @Query(value = "UPDATE lottery SET amount='0' WHERE ticket=:ticket", nativeQuery = true)
    void updateAmountZeroByticket(@Param("ticket") String ticket);

    Optional<Lottery> findByticket(String ticket);

}