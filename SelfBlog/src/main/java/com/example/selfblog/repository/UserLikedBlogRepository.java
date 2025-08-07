package com.example.selfblog.repository;

import com.example.selfblog.entity.UserLikedBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserLikedBlogRepository extends JpaRepository<UserLikedBlog, Integer>,
        JpaSpecificationExecutor<UserLikedBlog>{

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from user_liked_blog where blog_id=?1 and user_id=?2")
    int delUserLikedBlogs(int blogId, int userId);

    int countByBlogIdAndUserId(int blogId, int userId);
}