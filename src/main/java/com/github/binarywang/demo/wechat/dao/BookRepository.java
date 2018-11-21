package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query("SELECT b.times FROM Book b WHERE b.type=?1")
    int findTimes(String type);

    @Query("SELECT b.number FROM Book b WHERE b.type=?1")
    int findNumber(String type);

    @Query("UPDATE Book u SET u.times=?1 WHERE u.type=?2")
    @Modifying
    @Transactional
    void updatetimes(int time, String type);
}
