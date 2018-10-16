package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface ChannelRepository extends JpaRepository<Channel,Integer> {
    Channel findByOpenid(String openid);

    @Query("UPDATE Channel u SET u.channel=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updateChannel(String channel,String openid);

    @Query("UPDATE Channel u SET u.recent_time=?1 WHERE u.openid=?2")
    @Modifying
    @Transactional
    void updateRecent_Time(Date recent_time,String openid);
}
