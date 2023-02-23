package net.hlinfo.opt;

import java.util.List;

import net.hlinfo.opt.pager.MPager;
/**
 * 统一返回对象
 */
public class Resp<T> {
	public static int OK = 200; //操作成功
	public static int NOT_LOGIN = 401; //未登录
	public static int ERROR = 402; //操作失败
	public static int NOT_PERM = 403; //无权限
	public static int NOT_FOUND = 404; //资源不存在
	public static int NOT_DATA = 405; //查询的时候没有数据了
	public static int SERVER_ERROR = 500; //服务器内部错误
	
	/**
	 * 200:操作成功, 401:未登录, 402:操作失败, 403:无权限, 404:资源不存在, 405:查询(列表查询/ID查询等)的时无数据了, 500:服务器内部错误
	 */
//	@ApiModelProperty(value = "200:操作成功, 401:未登录, 402:操作失败, 403:无权限, 404:资源不存在, 405:查询(列表查询/ID查询等)的时无数据了, 500:服务器内部错误")
	private int code;
	/**
	 * 消息
	 */
//	@ApiModelProperty(value = "消息")
	private String msg;
	
	/**
	 * 数据对象
	 */
//	@ApiModelProperty(value = "数据")
	private T data;
	
	public Resp() {}
	
	public static<T> Resp<T> NEW() {
		return new Resp<T>();
	}
	public static<T> Resp<T> NEW(Class<T> cls) {
		return new Resp<T>();
	}
	public Resp(int code) {
		this.code = code;
	}
	public static <T> Resp<T> NEW(int code) {
		return new Resp<T>(code);
	}
	
	public Resp(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public static <T> Resp<T> NEW(int code, String msg) {
		return new Resp<T>(code, msg);
	}
	
	public static <T> Resp<T> NEW(int code, String msg, T obj) {
		return new Resp<T>(code, msg, obj);
	}
	
	public Resp(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	/**
	 *  Getter method for property <b>code</b>.
	 * @return property value of code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置参数 <b>code</b>.
	 *
	 * @param code 200:操作成功, 401:未登录, 402:操作失败, 403:无权限, 404:资源不存在, 405:查询(列表查询/ID查询等)的时无数据了, 500:服务器内部错误
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 *  Getter method for property <b>msg</b>.
	 * @return property value of msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Setter method for property <b>msg</b>.
	 *
	 * @param msg value to be assigned to property msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 *  Getter method for property <b>data</b>.
	 * @return property value of data
	 */
	public T getData() {
		return data;
	}

	/**
	 * Setter method for property <b>data</b>.
	 *
	 * @param data value to be assigned to property data
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 成功类型返回
	 * @param msg 成功信息
	 * @return 新Resp对象
	 */
	public static <T> Resp<T> OK(String msg) {
		Resp<T> resp = NEW(OK, msg);
		return resp;
	}
	/**
	 * 成功类型返回
	 * @param msg 成功信息
	 * @param data 数据
	 * @return 新Resp对象
	 */
	public static <T> Resp<T> OK(String msg, T data) {
		Resp<T> resp = NEW(OK, msg, data);
		return resp;
	}
	/**
	 * 成功类型返回
	 * @param msg 成功信息
	 * @return 当前Resp对象
	 */
	public Resp<T> ok(String msg) {
		this.setCode(OK);
		this.setMsg(msg);
		return this;
	}
	/**
	 * 操作成功
	 * @return 当前Resp对象
	 */
	public Resp<T> SUCCESS() {
		this.setCode(OK);
		this.setMsg("操作成功");
		return this;
	}
	/**
	 * 成功类型返回
	 * @param msg 成功信息
	 * @param data 数据
	 * @return 当前Resp对象
	 */
	public Resp<T> ok(String msg, T data) {
		this.setCode(OK);
		this.setMsg(msg);
		this.setData(data);
		return this;
	}
	/**
	 * 错误类型返回
	 * @param msg 错误信息
	 * @return 新Resp对象
	 */
	public static <T> Resp<T> ERROR(String msg) {
		Resp<T> resp = NEW(ERROR, msg);
		return resp;
	}
	/**
	 * 无数据
	 * @param msg 错误信息
	 * @param data 数据
	 * @return 新Resp对象
	 */
	public static <T> Resp<T> NO_DATA(String msg, T data) {
		Resp<T> resp = NEW(NOT_DATA, msg);
		resp.setData(data);
		return resp;
	}
	/**
	 * 无数据
	 * @param msg 错误信息
	 * @return 新Resp对象
	 */
	public static <T> Resp<T> NO_DATA(String msg) {
		Resp<T> resp = NEW(NOT_DATA, msg);
		return resp;
	}
	/**
	 * 错误类型返回
	 * @param msg 错误信息
	 * @param data 数据
	 * @return 新Resp对象
	 */
	public static <T> Resp<T> ERROR(String msg, T data) {
		Resp<T> resp = NEW(ERROR, msg, data);
		return resp;
	}
	/**
	 * 错误类型返回
	 * @param msg 错误信息
	 * @return 当前Resp对象
	 */
	public Resp<T> error(String msg) {
		this.setCode(ERROR);
		this.setMsg(msg);
		return this;
	}
	/**
	 * 操作失败
	 * @return 当前Resp对象
	 */
	public Resp<T> FAIL() {
		this.setCode(ERROR);
		this.setMsg("操作失败");
		return this;
	}
	/**
	 * 错误类型返回
	 * @param msg 错误信息
	 * @param data 数据
	 * @return 当前Resp对象
	 */
	public Resp<T> error(String msg, T data) {
		this.setCode(ERROR);
		this.setMsg(msg);
		this.setData(data);
		return this;
	}
	/**
	 * 设置code参数
	 * @param code 200:操作成功, 401:未登录, 402:操作失败, 403:无权限, 404:资源不存在, 405:查询(列表查询/ID查询等)的时无数据了, 500:服务器内部错误
	 * @return 当前Resp对象
	 */
	public Resp<T> code(int code) {
		this.setCode(code);
		return this;
	}
	/**
	 * 设置msg参数
	 * @param msg 消息数据
	 * @return 当前Resp对象
	 */
	public Resp<T> msg(String msg) {
		this.setMsg(msg);
		return this;
	}
	/**
	 * 设置数据
	 * @param data 数据对象
	 * @return 当前Resp对象
	 */
	public Resp<T> data(T data) {
		this.setData(data);
		return this;
	}
	/**
	 * 返回list数据
	 * @param list 数据对象
	 * @param okMsg 成功信息
	 * @param errorMsg 失败信息
	 * @return 当前Resp对象
	 */
	public static <T> Resp<List<T>> LIST(List<T> list, String okMsg, String errorMsg) {
		if(list != null && list.size() > 0) {
			return Resp.OK(okMsg, list);
		}else {
			return Resp.NO_DATA(errorMsg);
		}
	}
	/**
	 * 返回list数据，msg参数为：操作成功/失败
	 * @param list 数据对象
	 * @return 当前Resp对象
	 */
	public static <T> Resp<List<T>> LIST_O(List<T> list) {
		return LIST(list, "操作成功", "操作失败");
	}
	/**
	 * 返回list数据，msg参数为：查询成功/没有数据
	 * @param list 数据对象
	 * @return 当前Resp对象
	 */
	public static <T> Resp<List<T>> LIST_Q(List<T> list) {
		return LIST(list, "查询成功", "没有数据");
	}
	/**
	 * 返回分页list数据，msg参数为：查询成功/没有数据
	 * @param <T> 类型参数
	 * @param list 数据对象
	 * @param recordCount 总数量
	 * @param page 页码
	 * @param limit 每页数量
	 * @return 新Resp对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Resp<QueryPages<List<T>>> pages(List<T> list,int recordCount,int page,int limit) {
		MPager pager = new MPager(page,limit,recordCount);
		QueryPages result = new QueryPages(list, pager);
		if(list != null && list.size() > 0) {
			return Resp.OK("查询成功", result);
		}else {
			return Resp.NO_DATA("没有数据", result);
		}
	}
	/**
	 * 返回操作对象,msg参数为：操作成功/操作失败
	 * @param <T> 类型参数
	 * @param obj 数据对象
	 * @return Resp对象
	 */
	public static <T> Resp<T> OBJ_O(T obj) {
		boolean b = (
			obj instanceof Integer 
			&& obj != null 
			&& (Integer) obj > 0)
		|| (obj instanceof Boolean && obj != null && (Boolean)obj)
		|| (!(obj instanceof Integer) && obj != null);
		if(b) {
			return Resp.OK("操作成功", obj);
		}else {
			return Resp.ERROR("操作失败");
		}
	}
	/**
	 * 返回查询对象,msg参数为：查询成功/查询失败
	 * @param <T> 类型参数
	 * @param obj 数据对象
	 * @return Resp对象
	 */
	public static <T> Resp<T> OBJ_Q(T obj) {
		if(obj != null) {
			return Resp.OK("查询成功", obj);
		}else {
			return Resp.NO_DATA("没有数据了");
		}
	}
}
