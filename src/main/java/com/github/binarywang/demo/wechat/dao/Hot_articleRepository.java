package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Hot_article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface Hot_articleRepository extends JpaRepository<Hot_article,Integer> {
    Hot_article findByTitle(String title);

    List<Hot_article> findByType(String type);

    @Query("SELECT DISTINCT u.type FROM Hot_article u")
    List<String> findAllType();

    @Query(value = "SELECT u.url FROM hot_article u WHERE u.image_one IS NULL AND u.updatetime>?1 limit ?2,25;",nativeQuery = true)
    List<String> findAllUrl(Date time,int n);

    @Query(value = "SELECT u.url FROM hot_article u WHERE u.type!='广告' AND updatetime>?1 ORDER BY u.id DESC",nativeQuery = true)
    List<String> findUrl(Date time);

    @Query(value = "SELECT u.url FROM hot_article u WHERE u.type!='广告' AND u.html IS NULL ORDER BY u.id DESC",nativeQuery = true)
    List<String> findAllArticle();

    @Query("UPDATE Hot_article u SET u.image_one=?1 WHERE u.url=?2")
    @Modifying
    @Transactional
    void updateOne(String image_one,String url);

    //根据url删除纪录
    @Query(value = "DELETE FROM hot_article WHERE url=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteURL(String url);

    @Query("UPDATE Hot_article u SET u.image_two=?1 WHERE u.url=?2")
    @Modifying
    @Transactional
    void updateTwo(String image_two,String url);



    @Query("UPDATE Hot_article u SET u.image_three=?1 WHERE u.url=?2")
    @Modifying
    @Transactional
    void updateThree(String image_three,String url);

    @Query("UPDATE Hot_article u SET u.html=?1 WHERE u.url=?2")
    @Modifying
    @Transactional
    void updateHtml(String html,String url);

    @Query("UPDATE Hot_article u SET u.originalflag=?1 WHERE u.html=?2")
    @Modifying
    @Transactional
    void updateOriginalflag(int originalflag,String html);

    @Query(value = "SELECT * FROM hot_article u WHERE u.type=?1 ORDER BY u.updatetime DESC",nativeQuery = true)
    List<Hot_article> findByTime(String type);

    @Query(value = "SELECT * FROM hot_article u WHERE u.type=?1 AND u.html IS NOT NULL ORDER BY u.updatetime DESC",nativeQuery = true)
    List<Hot_article> findByTypePC(String type);

    @Query(value = "SELECT * FROM hot_article u WHERE '1'='1' ORDER BY u.updatetime DESC",nativeQuery = true)
    List<Hot_article> findAllByTime();

    @Query(value = "SELECT * FROM (SELECT * FROM hot_article u WHERE TYPE=?1 ORDER BY u.updatetime ASC) AS total limit ?2,1;",nativeQuery = true)
    List<Hot_article> advertisement(String type,int num);

    @Query(value = "SELECT * FROM (SELECT * FROM hot_article u WHERE (TYPE=?1 OR TYPE=?2) AND (image_one is not NULL AND trim(image_one)!='') AND html IS NOT NULL ORDER BY u.updatetime DESC) AS total limit ?3,1;",nativeQuery = true)
    List<Hot_article> findForNow(String type1,String type2, int num);

    @Query(value = "SELECT * FROM (SELECT * FROM hot_article u WHERE (TYPE=?1 OR TYPE=?2) AND (image_one is not NULL AND trim(image_one)!='') AND trim(originalflag)!=233 AND html IS NOT NULL ORDER BY u.updatetime DESC) AS total limit ?3,1;",nativeQuery = true)
    List<Hot_article> findNotVideo(String type1,String type2, int num);


    @Query(value = "SELECT * FROM hot_article u WHERE updatetime>?1 AND (image_one IS NOT NULL AND trim(image_one)!='') AND html IS NOT NULL ORDER BY u.updatetime ASC;",nativeQuery = true)
    List<Hot_article> findForVideo(Date time);


}
