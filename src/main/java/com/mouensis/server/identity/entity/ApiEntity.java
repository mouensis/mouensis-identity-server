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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Api实体类
 *
 * @author zhuyuan
 * @date 2020/10/27 9:02
 */
@Entity
@Table(name = "api")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class ApiEntity implements Serializable {
    private static final long serialVersionUID = -9156357926970003725L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 菜单名称
     */
    @Column(nullable = false, length = 32)
    private String ApiName;
    /**
     * 菜单描述
     */
    private String description;
    /**
     * 显示顺序
     */
    private Long displayOrder;
    /**
     * 菜单路径
     */
    private String url;
    /**
     * 是否被禁用
     */
    @Column(name = "is_disabled")
    private boolean disabled;

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
