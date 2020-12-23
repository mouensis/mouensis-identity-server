package com.mouensis.server.identity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 * @author zhuyuan
 * @date 2020/10/23 8:55
 */
@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class RoleEntity implements Serializable {
    private static final long serialVersionUID = -1132135061105832236L;
    /**
     * 主键-自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 角色名称
     */
    @Column(nullable = false, length = 32)
    private String roleName;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 显示顺序
     */
    private Long displayOrder;
    /**
     * 父角色
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RoleEntity parent;
    /**
     * 子角色列表
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<RoleEntity> children;

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
