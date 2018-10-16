package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Tourism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TourismRepository extends JpaRepository<Tourism,Integer> {
    Tourism findByUserid(int userid);

    List<Tourism> findByTourismcommendid(int tourismcommendid);

    @Query("SELECT u.tourismcommendid FROM Tourism u WHERE u.userid=?1")
    Integer findCommendidByUserid(int userid);
}
