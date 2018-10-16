package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Pc_habit;
import com.github.binarywang.demo.wechat.entity.User_habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface Pc_habitRepository extends JpaRepository<Pc_habit,Integer> {
    List<Pc_habit> findAllByUserid(String userid);

    Pc_habit findByUserid(String userid);

    @Query("SELECT u.history_number FROM Pc_habit u WHERE u.userid=?1")
    Integer findNumberByUserid(String userid);

    @Query("UPDATE Pc_habit u SET u.history_number=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updateHistory_Number(int history_number,String userid);

    @Query("UPDATE Pc_habit u SET u.today=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updatetoday_time(Date today_Time, String userid);

    @Query("UPDATE Pc_habit u SET u.history_time=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updatehistory_time(Date history_time, String userid);
}
