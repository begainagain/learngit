package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ShareRepository extends JpaRepository<Share,Integer> {
    Share findByUserid(String userid);

    @Query("SELECT u.shareid FROM Share u WHERE u.userid=?1")
    String findShareidByUserid(String userid);

    @Query(value = "SELECT * FROM share u WHERE u.userid=?1 AND FIND_IN_SET(?2,?3)",nativeQuery = true)
    List<Integer>  checkid(String userid,String uid,String ushareid);


    @Query("SELECT u.helpid FROM Share u WHERE u.userid=?1")
    String findHelpidByUserid(String shareid);

    @Query("SELECT COUNT(u) FROM Share u WHERE u.userid=?1")
    String countByopenid(String openid);

    @Query("UPDATE Share u SET u.shareid=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updateShareid(String shareid,String userid);

    @Query("UPDATE Share u SET u.helpid=?1 WHERE u.userid=?2")
    @Modifying
    @Transactional
    void updateHelpid(String helpid ,String userid);
}
