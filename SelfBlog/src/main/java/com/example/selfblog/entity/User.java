package com.example.selfblog.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Column(columnDefinition = "varchar(100)")
    private String password;

    private Timestamp regiTime;
    private Timestamp updateTime;

    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities=new ArrayList<>(0);
        if("123456789".equals(this.phoneNumber)){
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        return authorities;
    }
}