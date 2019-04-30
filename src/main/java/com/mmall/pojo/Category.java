package com.mmall.pojo;

import lombok.*;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor      // 无参构造器
@AllArgsConstructor     // 全参构造器
@EqualsAndHashCode(of = {"id"})
public class Category {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}