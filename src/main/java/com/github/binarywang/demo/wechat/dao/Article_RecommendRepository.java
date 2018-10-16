package com.github.binarywang.demo.wechat.dao;

import com.github.binarywang.demo.wechat.entity.Recommend.Article_Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Article_RecommendRepository extends JpaRepository<Article_Recommend,Integer> {

}
