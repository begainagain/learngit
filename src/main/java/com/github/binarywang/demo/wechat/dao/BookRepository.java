package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query("SELECT b.times FROM Book b WHERE b.type=?1")
    Integer findTimes(String type);

    @Query("SELECT b.number FROM Book b WHERE b.type=?1")
    int findNumber(String type);
}
