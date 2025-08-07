package com.example.selfblog.controller;
import com.example.selfblog.entity.Blog;
import com.example.selfblog.entity.User;
import com.example.selfblog.entity.UserLikedBlog;
import com.example.selfblog.repository.BlogRepository;
//import com.example.selfblog.repository.UserLikedBlogRepository;
import com.example.selfblog.repository.UserLikedBlogRepository;
import com.example.selfblog.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    BlogRepository blogRepository;
    @PutMapping("blog")
    @ResponseBody
    public String publishBlog(@RequestBody Blog blog){
        blog.setPublishTime(new Timestamp(System.currentTimeMillis()));
        blog.setLikeCount(0);
        blog.setScanCount(0);
        blogRepository.save(blog);
        return "1";
    }
    @GetMapping({"blog", ""})
    public String blogs(@RequestParam(required = false, defaultValue = "0") int page,
                        @RequestParam(required = false, defaultValue = "5") int size,
                        Map<String, Object> map) {
        Page<Blog> blogs = blogRepository.findAll(PageRequest.of(page, size));
        map.put("blogs", blogs.getContent());
        map.put("pageLast", page == 0 ? 0 : page - 1);
        map.put("pageNext", page + 1);
        map.put("size", size);
        map.put("total", blogs.getTotalPages());
        map.put("currentPage", page + 1);
        map.put("user", UserUtil.getUser());
        return "index";
    }
    @GetMapping("blog/{id}")
    public String blog(@PathVariable int id, Map<String, Object> map) {
        Optional<Blog> blogOpt = blogRepository.findById(id);
        if (blogOpt.isEmpty()) {
            return "error";
        }
        Blog blog = blogOpt.get();
        map.put("id", blog.getId());
        map.put("title", blog.getTitle());
        map.put("publishTime", new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                .format(blog.getPublishTime()));
        map.put("scanCount", blog.getScanCount());
        map.put("likeCount", blog.getLikeCount());
        map.put("content", blog.getContent());

        User user = UserUtil.getUser();
        if (user !=null){
            int hasLiked = userLikedBlogRepository.countByBlogIdAndUserId(id,user.getId());
            if (hasLiked > 0){
                map.put("hasLiked", "1");
            }else {
                map.put("hasLiked","0");
            }
        }
        return "blog";
    }
    @PostMapping("blog/{id}/scan")
    @ResponseBody
    public int scan(@PathVariable int id) {
        blogRepository.addScanCount(id);
        return blogRepository.getById(id).getScanCount();
    }
    @Autowired // 自动注入用户点赞数据访问层
    UserLikedBlogRepository userLikedBlogRepository;
    @Transactional // 声明为事务方法
    @PostMapping("/blog/{id}/like") // 处理/blog/{id}/like的POST请求
    @ResponseBody
    public int like(@PathVariable int id) {
        User user = UserUtil.getUser(); // 获取当前登录用户
        if (user == null) {
            return 0;
        }
        Blog blog = blogRepository.getById(id); // 获取博客对象
        if (blog == null) {
            return 0;
        }
        UserLikedBlog userLikedBlog = new UserLikedBlog();
        userLikedBlog.setUser(user);
        userLikedBlog.setBlog(blog);
        userLikedBlog.setLikeTime(new Timestamp(System.currentTimeMillis()));

        userLikedBlogRepository.save(userLikedBlog); // 保存点赞记录
        blogRepository.addLikeCount(id); // 增加博客点赞数
        return 1;
    }
    @Transactional
    @PostMapping("blog/{id}/dislike")
    @ResponseBody
    public int dislike(@PathVariable int id) {
        User user = UserUtil.getUser();
        if (user == null) {
            return 0;
        }
        userLikedBlogRepository.delUserLikedBlogs(id, user.getId());
        blogRepository.subLikeCount(id);
        return 1;
    }
}

