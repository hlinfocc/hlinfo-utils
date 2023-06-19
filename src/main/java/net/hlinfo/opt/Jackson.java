package net.hlinfo.opt;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParser;

/**
 * Jackson常用操作工具
 * @author 玉之银
 *
 */
public class Jackson {
	private static Logger log = Logger.getLogger(Jackson.class.toString());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
        // 对于空的对象转json的时候不抛出错误
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许属性名称没有引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化时忽略值为null的属性
//        mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
         //序列化时自定义时间日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //忽略bean的字段名大小写
//        mapper.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);
        //将驼峰转下划线
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }
	/**
	 * 将对象转换为json字符串
	 * @param object 数据对象
	 * @return json字符串
	 */
	public static String toJSONString(Object object) {
		return toJSONString(object,false);
	}
	/**
	 * 将对象转换为json字符串，是否格式化输出
	 * @param object 数据对象
	 * @param pretty 是否格式化输出
	 * @return 格式化的json字符串
	 */
	public static String toJSONString(Object object,boolean pretty) {
		try {
			if(object==null) {
				throw new NullPointerException("paramter is null");
			}
			if(pretty) {
				return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			}
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return "";
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return "";
		}
	}
	/**
	 * 将序列化对象转化为json字符串
	 * @param data 序列化对象实例
	 * @param pretty 是否格式化输出
	 * @param ignoreNull 序列化时是否忽略值为null的属性
	 * @param s 属性命名策略,如将驼峰转下划线：{@link PropertyNamingStrategies.SNAKE_CASE}
	 * @return 写入内容后的输出流
	 */
	public static String toJSONString(Serializable data,boolean pretty,boolean ignoreNull,PropertyNamingStrategy s) {
		try {
			if(data==null) {
				throw new NullPointerException("paramter is null");
			}
			String result = "";
			ObjectMapper mymapper = mapper.copy();
			if(s!=null) {
				mymapper = mymapper.setPropertyNamingStrategy(s);
			}
			if(ignoreNull) {
				mymapper = mymapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
			}
			if(pretty) {
				result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
			}else {
				result = mapper.writeValueAsString(data);
			}
			return result;
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}  catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}  catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 将序列化对象转化为json字符串,默认格式化输出
	 * @param data 序列化对象实例
	 * @param ignoreNull 序列化时是否忽略值为null的属性
	 * @param s 属性命名策略,如将驼峰转下划线：{@link PropertyNamingStrategies.SNAKE_CASE}
	 * @return 写入内容后的输出流
	 */
	public static String toJSONString(Serializable data,boolean ignoreNull,PropertyNamingStrategy s) {
		return toJSONString(data, true, ignoreNull, s);
	}
	/**
	 * 将json字符串反序列化为Java对象
	 * @param json json字符串
	 * @param clazz the class of T
	 * @return 对象T
	 */
	public static <T> T toJavaObject(String json,Class<T> clazz) {
		try {
			if(json==null || "".equals(json)) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(json, clazz);
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 将对象反序列化为Java对象
	 * @param object 对象
	 * @param clazz the class of T
	 * @return 对象T
	 */
	public static <T> T toJavaObject(Object object,Class<T> clazz) {
		try {
			if(object==null) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(mapper.writeValueAsString(object), clazz);
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 将对象反序列化为Map对象
	 * @param object 对象
	 * @return map对象
	 */
	public static Map<String,Object> toMap(Object object) {
		try {
			if(object==null) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(mapper.writeValueAsString(object), Map.class);
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 从文件读取json字符串反序列化为Java对象
	 * @param src json文件
	 * @param clazz the class of T
	 * @return 对象T
	 */
	public static <T> T toJavaObject(File src,Class<T> clazz) {
		try {
			if(src==null || !src.exists()) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(src, clazz);
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**   
     * 获取泛型的Collection Type  
     * @param mapper ObjectMapper
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类   
     * @return JavaType Java类型   
     */   
	 public static JavaType getCollectionType(ObjectMapper mapper,Class<?> collectionClass, Class<?>... elementClasses) {   
	     return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	 }
	 /**
	  * 将json字符串反序列化为List,Collection Type方式
	  * @param json json数组字符串
	  * @param clazz the class of T
	  * @return List<T>对象
	  */
	public static <T> List<T> toList(String json,Class<T> clazz) {
		try {
			if(json==null || "".equals(json)) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(json, getCollectionType(mapper,List.class,clazz));
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return new ArrayList<T>();
	}
	/**
	 * 将json对象反序列化为List,Collection Type方式
	 * @param object 对象
	 * @param clazz the class of T
	 * @return List<T>对象
	 */
	public static <T> List<T> toList(Object object,Class<T> clazz) {
		try {
			if(object==null) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(mapper.writeValueAsString(object), getCollectionType(mapper,List.class,clazz));
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return new ArrayList<T>();
	}
	/**
	  * 将json字符串反序列化为List,TypeReference方式
	  * @param json json数组字符串
	  * @return List<T> 对象
	  */
	public static <T> List<T> toList(String json) {
		try {
			if(json==null || "".equals(json)) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readValue(json, new TypeReference<List<T>>() { });
		} catch (JsonMappingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return new ArrayList<T>();
	}
	/**
	  * 将json字符串反序列化为JsonNode
	  * @param jsonString json字符串
	  * @return JsonNode对象
	  */
	public static JsonNode toJsonObject(String jsonString) {
		try {
			if(jsonString==null || "".equals(jsonString)) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**
	  * 将对象反序列化为JsonNode
	  * @param object json数据字符串对象
	  * @return JsonNode对象
	  */
	public static JsonNode toJsonObject(Object object) {
		try {
			if(object==null) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.readTree(mapper.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}  catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	
	/**
	  * 创建json数组对象
	  * @return ArrayNode对象
	  */
	public static ArrayNode arrayNode() {
		return mapper.createArrayNode(); 
	}
	
	/**
	  * 创建json对象
	  * @return ObjectNode对象
	  */
	public static ObjectNode objectNode() {
		return mapper.createObjectNode();
	}
	
	/**
	 * 返回指定实体对象的字符串表示
	 * @param obj 实体对象
	 * @return 实体对象字符串表示
	 */
	public static String entityToString(Object obj) {
		try {
			if(obj==null) {
				throw new NullPointerException("paramter is null");
			}
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return obj.getClass().getName() + "@" + Integer.toHexString(obj.getClass().hashCode());
		}catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return "";
		}
	}
	
	/**
	 * 将序列化对象转化为OutputStream或者ByteArrayOutputStream等
	 * @param out 输出流
	 * @param data 写入的内容,序列化对象实例
	 * @param ignoreNull 序列化时是否忽略值为null的属性
	 * @param s 属性命名策略,如将驼峰转下划线：{@link PropertyNamingStrategies.SNAKE_CASE}
	 * @return 写入内容后的输出流
	 */
	public static OutputStream toOutputStream(OutputStream out,Serializable data,boolean ignoreNull,PropertyNamingStrategy s) {
		try {
			if(data==null) {
				throw new NullPointerException("paramter is null");
			}
			if(s!=null) {
				if(ignoreNull) {
					// 序列化时忽略值为null的属性
					mapper.setPropertyNamingStrategy(s).setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
					.writeValue(out, data);
				}else {
					mapper.setPropertyNamingStrategy(s).writeValue(out, data);
				}
			}else {				
				if(ignoreNull) {
					// 序列化时忽略值为null的属性
					mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
					.writeValue(out, data);
				}else {
					mapper.writeValue(out, data);
				}
			}
			return out;
		} catch (JsonProcessingException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}  catch (NullPointerException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}  catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 将序列化对象转化为OutputStream或者OutputStream的派生类，如:ByteArrayOutputStream等
	 * @param out 输出流
	 * @param data 写入的内容,序列化对象实例
	 * @param ignoreNull 序列化时是否忽略值为null的属性
	 * @return 写入内容后的输出流
	 */
	public static OutputStream toOutputStream(OutputStream out,Serializable data,boolean ignoreNull) {
		return toOutputStream(out, data, ignoreNull, null);
	}
	/**
	 * 将序列化对象转化为OutputStream或者OutputStream的派生类，如:ByteArrayOutputStream等，忽略值为null的属性
	 * @param out 输出流
	 * @param data 写入的内容,序列化对象实例
	 * @return 写入内容后的输出流
	 */
	public static OutputStream toOutputStreamIgnoreNull(OutputStream out,Serializable data) {
		return toOutputStream(out, data, true, null);
	}
	/**
	 * 将序列化对象转化为OutputStream或者OutputStream的派生类，如:ByteArrayOutputStream等,不忽略值为null的属性
	 * @param out 输出流
	 * @param data 写入的内容,序列化对象实例
	 * @return 写入内容后的输出流
	 */
	public static OutputStream toOutputStream(OutputStream out,Serializable data) {
		return toOutputStream(out, data, false, null);
	}
	/**
	 * 获取ObjectMapper对象实例
	 * @return ObjectMapper对象实例
	 */
	public static ObjectMapper getMapper() {
		return mapper;
	}
}
