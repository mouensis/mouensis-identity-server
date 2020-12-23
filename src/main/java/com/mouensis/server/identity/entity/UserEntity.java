package com.mouensis.server.identity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 *
 * @author zhuyuan
 * @date 2020/10/21 17:58
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -8095661827766248986L;
    /**
     * 主键-自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户账号
     */
    @Column(length = 32, nullable = false, unique = true)
    private String username;

    /**
     * 手机号
     */
    @Column(length = 13, unique = true)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 13, unique = true)
    private String email;

    /**
     * 昵称
     */
    @Column(length = 32, nullable = false, unique = true)
    private String nickname;

    /**
     * 用户密码
     */
    @Column(length = 1000, nullable = false)
    private String password;

    /**
     * 用户是否禁用
     */
    @Column(name = "is_disabled", nullable = false)
    private boolean disabled;

    /**
     * 账号过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accountExpiredTime;

    /**
     * 密码过期时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date passwordExpiredTime;

    /**
     * 账号解锁时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accountUnlockedTime;

    /**
     * 归属组织结构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private OrganizationEntity organization;

    /**
     * 拥有的角色列表
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "role_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))})
    private List<RoleEntity> roleList;

    /**
     * 拥有的权限列表
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_permission",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "pemssion_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))})
    private List<PermissionEntity> permissionList;

    /**
     * 创建时间
     */
    @CreationTimestamp
    private Date createdAt;

    /**
     * 最后修改时间
     */
    @UpdateTimestamp
    private Date modifiedAt;
}
