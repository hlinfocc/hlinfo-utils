package net.hlinfo.opt.pager;


public interface PageInfo {
	 /**
    * @return  一共有多少页
     */
    int getPageCount();

    /**
    * @return 当前是第几页， 从 1 开始
     */
    int getPageNumber();

    /**
     * 设置页码
     * @param pageNumber 页数
     * @return 当前分页信息
     */
    PageInfo setPageNumber(int pageNumber);

    /**
    * @return 一页可以有多少条记录
     */
    int getPageSize();

    /**
     * 设置一页可以有多少条记录
     * @param pageSize 一页可以有多少条记录
     * @return 当前分页信息
     */
    PageInfo setPageSize(int pageSize);

    /**
    * @return 整个查询，一共有多少条记录
     */
    int getRecordCount();

    /**
     * 设置整个查询一共有多少条记录
     * @param recordCount 总记录数
     * @return 当前分页信息
     */
    PageInfo setRecordCount(int recordCount);

    /**
     * @return 当前页之前，还应该有多少条记录
     */
    int getOffset();

    /**
     * @return 是否是第一页
     */
    boolean isFirst();

    /**
     * @return 是否是最后一页
     */
    boolean isLast();

    /**
     * @return 是否存在下一页
     */
    boolean hasNext();

    /**
     * @return 是否存在上一页
     */
    boolean hasPrevious();
}
