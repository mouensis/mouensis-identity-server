package com.mouensis.server.identity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * 组织机构实体类
 *
 * @author zhuyuan
 * @date 2020/10/26 20:42
 */
@Entity
@Table(name = "organization")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class OrganizationEntity implements Serializable {
    private static final long serialVersionUID = 6225181282742274408L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 组织机构名称
     */
    @Column(nullable = false, length = 32)
    private String organizationName;
    /**
     * 组织机构描述
     */
    private String description;
    /**
     * 显示顺序
     */
    private Long displayOrder;
    /**
     * 父组织机构
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private OrganizationEntity parent;
    /**
     * 子组织机构列表
     */
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent")
    private List<OrganizationEntity> children;

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
