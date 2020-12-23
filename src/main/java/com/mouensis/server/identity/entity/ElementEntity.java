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
import java.util.Date;

/**
 * 页面元素实体类型
 *
 * @author zhuyuan
 * @date 2020/10/27 9:00
 */
@Entity
@Table(name = "element")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class ElementEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 元素名称
     */
    @Column(nullable = false, length = 32)
    private String elementName;
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
    @Column(nullable = false, unique = true, length = 128)
    private String elementCode;
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
