package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Tourism;
import com.github.binarywang.demo.wechat.entity.User_habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface User_habitRepository extends JpaRepository<User_habit,Integer> {
    User_habit findByUserid(Integer userid);

    @Query("SELECT u.history_number FROM User_habit u WHERE u.userid=?1")
    Integer findNumberByUserid(int userid);

    @Query("SELECT u.adver_number FROM User_habit u WHERE u.userid=?1")
    Integer findAdNumber(int userid);

    @Query("UPDATE User_habit u SET u.history_number=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updateHistory_Number(int history_number,int userid);

    @Query("UPDATE User_habit u SET u.adver_number=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updateAdver_Number(int adver_number,int userid);

    @Query("UPDATE User_habit u SET u.today=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updatetoday_time(Date today_Time, int userid);

    @Query("UPDATE User_habit u SET u.history_time=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updatehistory_time(Date history_time, int userid);
}
