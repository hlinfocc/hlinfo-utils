package net.hlinfo.opt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.hlinfo.opt.pager.MPager;


/**
 * 封装了一个分页查询的结果集合，包括本页数据列表以及分页信息
 *
 */

public class QueryPages<T> implements Serializable {
    private static final long serialVersionUID = 1L;
	//@ApiModelProperty("数据列表")
    private List<T> list;
    //@ApiModelProperty("分页信息")
    private MPager pager;

    /**
     * 新建一个分页查询的结果集合
     */
    public QueryPages() {}

    /**
     * 一个分页查询的结果集合
     * @param listData 查询结果
     * @param pager 分页对象
     */
    public QueryPages(List<T> list, MPager pager) {
        this.list = list;
        this.pager = pager;
    }
    /**
     * 
     * @param list 查询结果
     */
    public QueryPages(List<T> list) {
		super();
		this.list = list;
	}

	/**
     * 获取结果集
     * @return 结果集
     */
    public List<?> getList() {
        return list;
    }

    /**
     * 按特定泛型获取结果集,属于直接强转,不带转换
     * @param eleType 泛型
     * @return 结果集
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(Class<T> eleType) {
        return (List<T>) list;
    }


    /**
     * 设置结果集
     * @param list 结果集
     * @return 当前对象,用于链式调用
     */
    public QueryPages<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    /**
     * 获取分页对象
     * @return 分页对象
     */
    public MPager getPager() {
        return pager;
    }

    /**
     * 设置分页对象
     * @param pager 分页对象
     * @return 当前对象,用于链式调用
     */
    public QueryPages<T> setPager(MPager pager) {
        this.pager = pager;
        return this;
    }

}

