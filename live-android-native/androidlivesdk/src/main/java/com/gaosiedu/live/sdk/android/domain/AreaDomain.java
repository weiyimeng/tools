package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;

public class AreaDomain implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 编号
     */
    private Integer num;

    /**
     * 父节点编码
     */
    private Integer parent;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 分级(1:省级行政区,2市级行政区,3:县级行政区,4:区级行政区)
     */
    private Integer level;

    /**
     * 在同级中的排序
     */
    private Integer sort;

    /**
     * 状态(1:开放,其他:关闭)
     */
    private Integer status;

    /**
     * area
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 编号
     * @return num 编号
     */
    public Integer getNum() {
        return num;
    }

    /**
     * 编号
     * @param num 编号
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * 父节点编码
     * @return parent 父节点编码
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * 父节点编码
     * @param parent 父节点编码
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * 节点名称
     * @return name 节点名称
     */
    public String getName() {
        return name;
    }

    /**
     * 节点名称
     * @param name 节点名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 分级(1:省级行政区,2市级行政区,3:县级行政区,4:区级行政区)
     * @return level 分级(1:省级行政区,2市级行政区,3:县级行政区,4:区级行政区)
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 分级(1:省级行政区,2市级行政区,3:县级行政区,4:区级行政区)
     * @param level 分级(1:省级行政区,2市级行政区,3:县级行政区,4:区级行政区)
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 在同级中的排序
     * @return sort 在同级中的排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 在同级中的排序
     * @param sort 在同级中的排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 状态(1:开放,其他:关闭)
     * @return status 状态(1:开放,其他:关闭)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态(1:开放,其他:关闭)
     * @param status 状态(1:开放,其他:关闭)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", num=").append(num);
        sb.append(", parent=").append(parent);
        sb.append(", name=").append(name);
        sb.append(", level=").append(level);
        sb.append(", sort=").append(sort);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}
