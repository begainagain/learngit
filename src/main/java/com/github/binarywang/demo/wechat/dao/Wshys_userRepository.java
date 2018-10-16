package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Wshys_user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface Wshys_userRepository extends JpaRepository<Wshys_user,Integer> {
    Wshys_user findByOpenid(String openid);

    Wshys_user findById(int id);

    @Query("SELECT u.nickname,u.headimgurl FROM Wshys_user u WHERE u.id=?1")
    List<Wshys_user> findMessageById(int id);

    List<Wshys_user> findByCommenduserid(String commenduserid);

    @Query("SELECT u.id FROM Wshys_user u  WHERE u.openid=?1")
    Integer findIdByOpenid(String openid);

    @Query("SELECT u.openid FROM Wshys_user u WHERE u.id=?1")
    String findOpenidByID(int id);

    @Query("SELECT u.openid FROM Wshys_user u WHERE u.subscribe=?1")
    List<String> findBySubscribe(String subscribe);

    @Query("FROM Wshys_user u WHERE u.complete IS NOT NULL")
    List<Wshys_user> findAllComplete();

    @Query(value = "SELECT * FROM (SELECT * FROM wshys_user u WHERE u.subscribe='true') as n WHERE n.id=ANY(SELECT t.userid FROM tourism t WHERE t.tourismcommendid=?1)",nativeQuery = true)
    List<Wshys_user> findByIdAndSubscribe(int tourismcommendid);

    @Query("UPDATE Wshys_user u SET u.subscribe=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updateSubscribe(String subscribe,String openid);

    @Query("UPDATE Wshys_user u SET u.uuid=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updateUuid(String uuid,String openid);

    @Query("UPDATE Wshys_user u SET u.mediaid=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updateMediaId(String mediaId,String openId);

    @Query("UPDATE Wshys_user u SET u.complete=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updateComplete(String complete,String openId);

    @Query("UPDATE Wshys_user u SET u.postertime=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updatePosterTime(Date posterTime, String openId);
}
