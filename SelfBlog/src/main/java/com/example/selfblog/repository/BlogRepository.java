package com.example.selfblog.repository;

import com.example.selfblog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BlogRepository extends JpaRepository<Blog, Integer>,
        JpaSpecificationExecutor<Blog> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update blogs set scan_count = scan_count+1 where id =?1")
    int addScanCount(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update blogs set like_count = like_count+1 where id =?1")
    int addLikeCount(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "update blogs set like_count = like_count-1 where id = ?1")
    int subLikeCount(int id);
}