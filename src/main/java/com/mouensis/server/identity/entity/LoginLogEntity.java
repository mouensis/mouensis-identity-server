package com.mouensis.server.identity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志实体类
 *
 * @author zhuyuan
 * @date 2020-12-13 23:01
 */
@Entity
@Table(name = "login_log")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class LoginLogEntity implements Serializable {
    private static final long serialVersionUID = 2800195136076317821L;
    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    /**
     * 用户名
     */
    @Column(length = 36, nullable = false, updatable = false)
    private String username;

    /**
     * 远端IP
     */
    @Column(nullable = false, length = 40, updatable = false)
    private String remoteIp;

    /**
     * 登录时间
     */
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date loginTime;

    /**
     * 登出时间
     */
    @Column(insertable = false)
    @UpdateTimestamp
    private Date logoutTime;
}
