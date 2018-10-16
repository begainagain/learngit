package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface AnalysisRepository extends JpaRepository<Analysis,Integer> {
    Analysis findByRefdate(Date refdate);

    @Query("UPDATE Analysis u SET u.updatetime=?1 WHERE u.refdate=?2")
    @Modifying
    @Transactional
    void updateUpdatetime(Date time,Date date);
}
