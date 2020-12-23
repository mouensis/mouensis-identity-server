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
 * 权限实体类
 *
 * @author zhuyuan
 * @date 2020/10/26 20:35
 */
@Entity
@Table(name = "permission")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class PermissionEntity implements Serializable {
    private static final long serialVersionUID = 1570670841078310835L;
    /**
     * 主键-自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 权限名称
     */
    @Column(nullable = false, length = 32)
    private String permissionName;
    /**
     * 权限描述
     */
    private String description;
    /**
     * 显示顺序
     */
    private Long displayOrder;
    /**
     * 父权限
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PermissionEntity parent;
    /**
     * 子权限列表
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<PermissionEntity> children;
    /**
     * 权限拥有的菜单信息
     */
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "pemssion_menu",
            joinColumns = {@JoinColumn(name = "pemssion_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))})
    private List<MenuEntity> menuList;
    /**
     * 权限拥有的元素信息
     */
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "pemssion_element",
            joinColumns = {@JoinColumn(name = "pemssion_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "element_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))})
    private List<ElementEntity> elementList;
    /**
     * 权限拥有的元素信息
     */
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "pemssion_api",
            joinColumns = {@JoinColumn(name = "pemssion_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "api_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT))})
    private List<ApiEntity> apiList;

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
