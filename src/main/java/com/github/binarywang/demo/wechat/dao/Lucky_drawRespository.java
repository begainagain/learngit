package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Lucky_draw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface Lucky_drawRespository extends JpaRepository<Lucky_draw,Integer> {
    Lucky_draw findByOpenid(String openid);

    @Query("SELECT u.random_num1 FROM Lucky_draw u WHERE u.openid=?1")
    Integer findNumber1ByOpenid(String openid);

    @Query("SELECT u.random_num2 FROM Lucky_draw u WHERE u.openid=?1")
    Integer findNumber2ByOpenid(String openid);

    @Query("UPDATE Lucky_draw u SET u.random_num2=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void update2(int num2,String openid);

    @Query("UPDATE Lucky_draw u SET u.random_num3=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void update3(int num3,String openid);

    @Query("UPDATE Lucky_draw u SET u.random_num4=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void update4(int num4,String openid);
}
