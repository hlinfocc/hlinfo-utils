package net.hlinfo.opt;

import java.beans.PropertyDescriptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hlinfo.annotation.MColumn;



/**
 * 一些静态的工具方法
 *  @author 玉之银
 */
public class Func {
	/**
	 * 基础字符数组0-9a-zA-Z
	 */
	private static final char[] baseCharacter = "0123456789abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	/**
	 * 角度与弧度的换算
	 * @param d
	 * @return 弧度
	 */
	public static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 我国常用三个坐标系，WGS84,、北京54及西安80，而WGS84椭球的长半轴就为6378137.0，
	 */
	public static final double EARTH_RADIUS = 6378.137;

	/**
	 * 验证手机号
	 *
	 * @param phone
	 * @return 是手机号返回true，否则返回false
	 */
	public static boolean checkPhone(String phone) {
		 String CN_PHONE_REG = "^1[3|4|5|6|7|8|9]\\d{9}$";
		 Pattern pattern = Pattern.compile(CN_PHONE_REG);
		 Matcher matcher = pattern.matcher(phone);
		 return matcher.matches();
	}

	/**
	 * 生成15位数ID
	 * @return 15位数ID
	 */
	public static String getNumId() {
		long millis = System.currentTimeMillis();
		Random random = new Random();
		int end2 = random.nextInt(99);
		String string = millis + String.format("%02d", end2);
		return string;
	}
	
	public static String getId() {
		return Func.Times.nowDateBasic() + Func.longuuid();
	}
	public static String genId() {
		LocalDate ld = LocalDate.now();
		return ld.format(DateTimeFormatter.ofPattern("yy")) + "0" + Func.longuuid();
	}

	/**
	 * 过滤掉手机端输入的表情字符
	 *
	 * @param source 源字符串
	 * @return 过滤后的字符串
	 */
	public static String filterEmoji(String source) {
		if (source == null) {
			return source;
		}
		Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
				Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		Matcher emojiMatcher = emoji.matcher(source);
		if (emojiMatcher.find()) {
			source = emojiMatcher.replaceAll("*");
			return source;
		}
		return source;
	}

	/**
	 * 获取UUID（32位）
	 * @return 不包含-号的uuid
	 */
	public static String UUID() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}
	/**
	 * 获取UUID（36位）
	 * @return 包含-号的uuid
	 */
	public static String UUID36() {
		return java.util.UUID.randomUUID().toString();
	}

	/**
	 * JAVA生成短8位UUID
	 * 来源：https://blog.csdn.net/andy_miao858/article/details/9530245
	 * @return 8位UUID
	 */
	public static String generateShortUuid() {
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
				"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}
	/**
	 * 数字小于10,在前面加前导0
	 * @param x 传入的数字
	 * @return 加前导0的数字字符串
	 */
	public static String numAddPrefixZero(int x) {
		if (x < 10) {
			return "0" + x;
		}
		return x + "";
	}
	/**
	 * 生成uuid并转为数字，长度共18位
	 * @return 数字类型UUID
	 */
	public static long longuuid() {
		return longuuid(java.util.UUID.randomUUID().toString());
	}
	/**
	 * uuid转为数字，根据https://blog.csdn.net/andy_miao858/article/details/9530245改写,
	 * 前后各加了一位随机数，长度共18位
	 * @return 数字类型UUID
	 */
	public static long longuuid(String uuid) {
		if(isBlank(uuid)) {
			uuid = java.util.UUID.randomUUID().toString();
		}
		StringBuffer shortBuffer = new StringBuffer();
		Random random = new Random();
		shortBuffer.append((random.nextInt(9) + 1));
		uuid = uuid.trim().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(numAddPrefixZero(x % 0x3E));
		}
		shortBuffer.append((random.nextInt(9) + 1));
		return string2Long(shortBuffer.toString());
	}

	/**
	 * 中文转unicode编码
	 * @param gbString 中文字符串
	 * @return unicode编码
	 */
	public static String gbEncoding(final String gbString) {
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int i = 0; i < utfBytes.length; i++) {
			String hexB = Integer.toHexString(utfBytes[i]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	/**
	 * BigDecimal转换为长整型
	 * @param bd1 BigDecimal对象
	 * @return 长整型
	 */
	public static long dec2Value(BigDecimal bd1) {
		BigDecimal bd2 = bd1.multiply(new BigDecimal(100));
		long total_fee = bd2.longValue();
		return total_fee;
	}

	/**
	 * 去除文本中的html
	 *
	 * @param htmlStr html文本
	 * @return 去除html的字符串
	 */
	public static String delHTMLTag(String htmlStr) {
		if(Func.isBlank(htmlStr)) {
			return "";
		}
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签
		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr.replaceAll("\\s*|\t|\r|\n", "").trim(); // 返回文本字符串
	}

	/**
	 * 在html中获取图片地址
	 * @param html HTML字符串
	 * @return 图片地址列表
	 */
	public static List<String> getImgByHtml(String html) {
		List<String> srcList = new ArrayList<String>(); // 用来存储获取到的图片地址
		Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");// 匹配字符串中的img标签
		Matcher matcher = p.matcher(html);
		boolean hasPic = matcher.find();
		if (hasPic == true)// 判断是否含有图片
		{
			while (hasPic) // 如果含有图片，那么持续进行查找，直到匹配不到
			{
				String group = matcher.group(2);// 获取第二个分组的内容，也就是 (.*?)匹配到的
				Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");// 匹配图片的地址
				Matcher matcher2 = srcText.matcher(group);
				if (matcher2.find()) {
					srcList.add(matcher2.group(3));// 把获取到的图片地址添加到列表中
				}
				hasPic = matcher.find();// 判断是否还有img标签
			}

		}
		return srcList;
	}

	/**
	 * 将字节 B转换为KB, MB等等
	 * @param size 字节
	 * @return 转换后的数据
	 */
	public static String getPrintSize(long size) {
		// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
		if (size < 1024) {
			return String.valueOf(size) + " B";
		} else {
			size = size / 1024;
		}
		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
		// 因为还没有到达要使用另一个单位的时候
		// 接下去以此类推
		if (size < 1024) {
			return String.valueOf(size) + " KB";
		} else {
			size = size / 1024;
		}
		if (size < 1024) {
			// 因为如果以MB为单位的话，要保留最后1位小数，
			// 因此，把此数乘以100之后再取余
			size = size * 100;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + " MB";
		} else {
			// 否则如果要以GB为单位的，先除于1024再作同样的处理
			size = size * 100 / 1024;
			return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + " GB";
		}
	}
	/**
	 * 正则表达式匹配文本
	 * @param text 字符串
	 * @param regex 正则表达式
	 * @return 匹配的文本
	 */
	public static String getText(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find())
			return matcher.group(1);
		return null;
	}
	/**
	 * 字符串转整型
	 * @param s 含数字的字符串
	 * @return 整型数据
	 */
	public static int string2int(String s) {
		if (s == null)
			return 0;
		s = getText(s, "(\\d+)");
		if ("".equals(s) || s == null)
			return 0;
		return Integer.parseInt(s);
	}
	/**
	 * 对象转整型
	 * @param s 含数字的对象
	 * @return 整型数据
	 */
	public static int string2int(Object s) {
		if (s == null) {return 0;}
		String str = s.toString();
		str = getText(str, "(\\d+)");
		if ("".equals(str) || str == null){return 0;}
		return Integer.parseInt(str);
	}
	/**
	 * 字符串转长整型
	 * @param s 含数字的字符串
	 * @return 长整型数据
	 */
	public static Long string2Long(String s) {
		if (s == null)
			return 0l;
		s = getText(s, "(\\d+)");
		if ("".equals(s) || s == null)
			return 0l;
		return Long.parseLong(s);
	}
	/**
	 * 字符串转浮点型
	 * @param s 含数字的字符串
	 * @return 浮点型数据
	 */
	public static float string2Float(String s) {
		if (s == null)
			return 0l;
		s = getText(s, "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");
		if ("".equals(s) || s == null)
			return 0l;
		return Float.parseFloat(s);
	}
	/**
	 * 对象转长整型
	 * @param obj 含数字的对象
	 * @return 长整型数据
	 */
	public static long obj2long(Object obj) {
		if (obj == null)
			return 0;
		try {
			return Long.parseLong(obj.toString());
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			System.out.println("error=");
			return 0;
		}
	}
	/**
	 * 对象toString
	 * @param s 对象
	 * @return 字符串
	 */
	public static String objectToString(Object s) {
		return s==null?"":s.toString();
	}
	/**
	 * 判断BigDecimal类型是否为空或者0
	 * @param bd
	 * @return 为null或者0返回true,否则返回false
	 */
	public static boolean BigDecimalIsEmpty(BigDecimal bd) {
		if(bd==null) {return true;}
		if(bd.compareTo(BigDecimal.ZERO)==0) {return true;}
		return false;
	}
	/**
	 * 判断BigDecimal类型是否不为空或者0
	 * @param bd
	 * @return 为null或者0返回flase,否则返回true
	 */
	public static boolean BigDecimalIsNotEmpty(BigDecimal bd) {
		return !BigDecimalIsEmpty(bd);
	}
    /**
     * 判断是否IPV4 地址
     * @param input 输入字符串
     * @return 是IPV4返回true，否则false
     */
	public static boolean isIPv4Address(final String input) {
		 Pattern IPv4Pattern = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        return IPv4Pattern.matcher(input).matches();
    }
	/**
     * 判断是否IPV6 地址
     * @param input 输入字符串
     * @return 是IPV6返回true，否则false
     */
    public static boolean isIPv6StdAddress(final String input) {
    	Pattern IPv6StdPattern = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
        return IPv6StdPattern.matcher(input).matches();
    }
    /**
     * 判断是否IPV6 地址
     * @param input 输入字符串
     * @return 是IPV6返回true，否则false
     */
    public static boolean isIPv6HexCompressedAddress(final String input) {
    		Pattern IPV6HexCompressedPattrern = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
        return IPV6HexCompressedPattrern.matcher(input).matches();
    }
    /**
     * 判断是否IPV6 地址
     * @param input 输入字符串
     * @return 是IPV6返回true，否则false
     */
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }
	
	/**
     * 获得访问者的IP地址, 反向代理过的也可以获得,针对Java EE
     *
     * @param request
     *            请求的req对象
     * @return 来源ip
     */
    public static String getIpAddr(javax.servlet.http.HttpServletRequest request) {
        if (request == null)
            return "0.0.0.0";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
	    				// 根据网卡取本机配置的IP
	    				InetAddress inet = null;
	    				try {
	    					inet = InetAddress.getLocalHost();
	    				} catch (UnknownHostException e) {
	    					e.printStackTrace();
	    				}
	    				ip = inet.getHostAddress();
    				}
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        if (isBlank(ip))
            return "0.0.0.0";
        if (isIPv4Address(ip) || isIPv6Address(ip)) {
            return ip;
        }
        return "0.0.0.0";
    }
    
	/**
	 * 获取学历
	 * @param edu 学历数字
	 * @return 学历中文
	 */
	public static String getEducation(int edu) {
		String edutxt = "无";
		if(edu==0) {return "无";}
		String[] eduarr = {"无","小学","中学","初中","中专","高中","专科","本科","硕士研究生","博士研究生"};
		String[] edumode = {"","以下","以上"};
		int a = edu%10;//对10取余，可得到个位数
		int b = edu/10;//除10得到十位数，由于b为整形，小数位会自动省略
		edutxt = eduarr[b]+edumode[a];
		return edutxt;
	}

	/**
	 * 判断实体类中每个属性是否都为空
	 * @param o 实体对象
	 * @return 每个属性都为空true,否则flase
	 */
	public static boolean allFieldIsNULL(Object o){
		if(o==null) {return true;}
		try {
			for (Field field : o.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(o);
				if (value instanceof CharSequence) {
					if (!isEmpty(value)) {
						return false;
					}
				} else {
					if (null != value) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("判断对象属性为空异常" + e);
		}
		return true;
	}
	/**
	 * 将实体类中为空字符串的属性设为null
	 * @param obj
	 * @return 处理后的对象
	 */
	public static <T> T setFieldIsEmptyToNull(T obj){
		if(obj==null) {return obj;}
		try {
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value instanceof CharSequence) {
					if (isEmpty(value)) {
						field.set(obj, null);
					}
				}
			}
			return obj;
		} catch (Exception e) {
			System.out.println("判断对象属性为空异常" + e);
			return obj;
		}

	}
	/**
	 * 对象是否为空
	 * @param obj 对象
	 * @return 为空返回true，否则false
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof Optional) {
			return !((Optional<?>) obj).isPresent();
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		// else
		return false;
	}



	/**
	 * map转obj
	 * @param cls 对象类型
	 * @param map 数据
	 * @return 对象
	 */
	public static <T> T map2obj(Class<T> cls, Map map){
		T object = null;
		try {
			object = cls.newInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//检查是否空值
		if(map == null || map.isEmpty()){
			return null;
		}
		//获取Object属性数组
		Field[] fields = object.getClass().getDeclaredFields();
		//遍历数组
		for(Field field : fields){
			if(!field.isAccessible()){
				field.setAccessible(true);
			}
			//赋值
			try {
				String type = field.getType().getSimpleName();
				if("int".equals(type)) {
					//field.setInt(object, Funs.string2int(map.get(field.getName())+""));
				}else if("double".equals(type)){
					//field.setDouble(object, map.get(field.getName()));
				}else if("float".equals(type)){
					//field.setFloat(object, map.get(field.getName()));
				}else {
					field.set(object, map.get(field.getName()));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("map->obj:" + e.getMessage());
			}
		}
		return object;
	}

	/**
	 * 将list转为map
	 * list=[{age:1, name:'n1', addr:"a1"}, {age:2,name:"n2", addr:"a3"}] key=name
	 * 返回的map={
	 * 	n1: {age:1, name:'n1', addr:"a1"},
	 *  n2: {age:2,name:"n2", addr:"a3"}
	 * }
	 * @param list 需要转为Map的list
	 * @param key 作为map的key值，为list中item的其中一个字段的值
	 * 			，不能是数字字段，必须为字符串且不重复
	 * @return Map对象
	 */
	public static <T> Map list2map(List<T> list, String key) {
		if(list == null || list.size() <= 0 || Func.isBlank(key)) {
			System.out.println("Funs.list2map -> 所传参数为空");
			return null;
		}
		T t = list.get(0);
		try {
			Field field = t.getClass().getDeclaredField(key);
			if(field == null) {
				System.out.println("Funs.list2map -> 所传key不是list项中的字段");
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Map<String, T> map = new HashMap<>();
		for(T item:list) {
			try {
				Field field = t.getClass().getDeclaredField(key);
				field.setAccessible(true);
				Method me = item.getClass().getMethod("get" + Func.upperFirst(key));
				map.put((String)me.invoke(item), item);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
	/**
	 * 判断list是否为空
	 * @param list list数据
	 * @return 为空返回true
	 */
	public static boolean listIsEmpty(List<?> list) {
		return list==null || list.isEmpty();
	}
	/**
	 * 判断list是否不为空
	 * @param list list数据
	 * @return 不为空返回true
	 */
	public static boolean listIsNotEmpty(List<?> list) {
		return !listIsEmpty(list);
	}
	/**
	 * 返回数组中指定元素所在位置，未找到返回-1
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回-1
	 */
	public static int indexOf(char[] array, char value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return -1;
	}
	/**
	 * 过滤字符串中指定的多个字符，如有多个则全部过滤
	 *
	 * @param s 字符串
	 * @param charArr 字符列表
	 * @return 过滤后的字符
	 */
	public static String removeChar(CharSequence s, char... charArr) {
		if (null == s || charArr.length<=0) {
			return objectToString(s);
		}
		final int len = s.length();
		if (0 == len) {
			return objectToString(s);
		}
		final StringBuilder builder = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = s.charAt(i);
			if (indexOf(charArr, c)<=-1) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * 过滤所有换行符，包括：
	 *
	 * <pre>
	 * 1. \r
	 * 1. \n
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 过滤后的字符串
	 */
	public static String removeAllLineBreaks(CharSequence str) {
		return removeChar(str, '\r', '\n');
	}
	/**
	 * 是否为数字，支持包括：
	 *
	 * <pre>
	 * 1、10进制
	 * 2、16进制数字（0x开头）
	 * 3、科学计数法形式（1234E3）
	 * 4、类型标识形式（123D）
	 * 5、正负数标识形式（+123、-234）
	 * </pre>
	 *
	 * @param str 字符串值
	 * @return 是否为数字
	 */
	public static boolean isNumber(CharSequence str) {
		if (isBlank(str)) {
			return false;
		}
		char[] chars = str.toString().toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (false == foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return false == allowSigns && foundDigit;
	}
	/**
	 * 正则匹配
	 * @param pattern 正则对象
	 * @param content 待匹配内容
	 * @return 是否匹配成功
	 */
	public static boolean isMatch(Pattern pattern, String content) {
        if (content == null || pattern == null)
            // 提供null的字符串为不匹配
            return false;
        return pattern.matcher(content).matches();
    }
	/**
	 * 判断是否统一社会信用代码
	 * @param s 统一社会信用代码字符串
	 * @return 是否统一社会信用代码true，否则false
	 */
	public static boolean isUSCC(String s) {
        if (isBlank(s)) {return false;}
        Pattern P_USCC = Pattern.compile("^(11|12|13|19|21|29|31|32|33|34|35|39|41|49|51|52|53|59|61|62|69|71|72|79||81|82|91|92|93|A1|A2|N1|N2|N3|N9|Y1)[1-9]{1}[0-9]{5}[0-9A-Z]{9}[0-9A-Z]{1}$");
		return isMatch(P_USCC,s);
    }
   
	/**
	 * 正则过滤指定的html标签(只过滤html标签及属性，不过滤html标签内的内容)，如：<per>&lt;html lang="zh-CN"&gt;&lt;head&gt;&lt;body&gt;</per>
	 * @param htmlstr html内容
	 * @param isEndTag 是否过滤结束标签
	 * @param tagarr HTML标签数组,不包括&lt;或&gt;符号，如：new String[] {"html","body","p"}
	 * @return 过滤后不含指定html标签的内容
	 */
	public static String delHtmlTagOnly(String htmlstr,boolean isEndTag, String... tagarr) {
		if(isBlank(htmlstr) || tagarr==null || tagarr.length<=0) {
			return htmlstr;
		}
		//html 单标签
		List<String> stagList = new ArrayList<String>();
		stagList.add("br");
		stagList.add("hr");
		stagList.add("area");
		stagList.add("link");
		stagList.add("base");
		stagList.add("meta");
		stagList.add("basefont");
		stagList.add("param");
		stagList.add("col");
		stagList.add("frame");
		stagList.add("keygen");
		stagList.add("source");
		for (String tag : tagarr) {	
			if(tag==null || "".equals(tag)) {
				continue;
			}
			String reg = "<" + tag + ".*?>";
			Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(htmlstr);
			htmlstr = matcher.replaceAll("");
			if(isEndTag && !stagList.contains(tag.toLowerCase())) {
				String regEnd = "</" + tag + ".*?>";
				Pattern patternEnd = Pattern.compile(regEnd, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				Matcher matcherEnd = patternEnd.matcher(htmlstr);
				htmlstr = matcherEnd.replaceAll("");
			}
		}
		return htmlstr;
	}
	
	/**
	 * 根据出生年月日获取年龄
	 * @param birthDay 出生年月日Date对象
	 * @return 年龄
	 */
	public static int getAge(Date birthDay) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (birthDay != null) {
            now.setTime(new Date());
            born.setTime(birthDay);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            //System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }
	/**
	 * 根据出生年月日获取年龄
	 * @param birthDay 出生年月日
	 * @return 年龄
	 */
	public static int getAge(String birthDay) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (Func.isNotBlank(birthDay)) {
            now.setTime(new Date());
            born.setTime(Func.Times.string2Date(birthDay));
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            //System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }

	/**
     * 如果此字符串为 null 或者全为空白字符(包含：空格、tab 键、换行符)，则返回 true
     *
     * @param cs 字符串
     * @return 如果此字符串为 null 或者全为空白字符，则返回 true
     */
	public static boolean isBlank(CharSequence cs) {
        if (null == cs)
            return true;
        int length = cs.length();
        for (int i = 0; i < length; i++) {
            if (!(Character.isWhitespace(cs.charAt(i))))
                return false;
        }
        return true;
    }
	/**
     * 如果此字符串不为 null 或者全为空白字符(包含：空格、tab 键、换行符)，则返回 true
     *
     * @param cs 字符串
     * @return 如果此字符串不为 null 或者全为空白字符，则返回 true
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    /**
     * 检查两个字符串是否相等.
     *
     * @param s1
     *            字符串A
     * @param s2
     *            字符串B
     * @return true 如果两个字符串相等,且两个字符串均不为null
     */
    public static boolean equals(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }
    /**
     * 检查两个字符串是否不相等.
     *
     * @param s1
     *            字符串A
     * @param s2
     *            字符串B
     * @return 如果两个字符串不相等，返回true
     */
    public static boolean notequals(String s1, String s2) {
        return !equals(s1,s2);
    }
    /**
     * 检测URL地址是否能正常连接
     * @param url 需要测试的URL地址
     * @return 能正常连接返回true，否则返回false
     */
    public static boolean testURLConn(String url){
		int status = 404;
		try {
			URL conn = new URL(url);
			HttpURLConnection openConn = (HttpURLConnection) conn.openConnection();
			openConn.setUseCaches(false);
			openConn.setConnectTimeout(3000); //设置超时时间
		   status = openConn.getResponseCode();//获取请求状态
		   if (200 == status) {
		    return true;
		   }
		} catch (Exception e) {
		   //e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return false;
	}

    /**
     * 实体对象转Map
     * @param object 实体对象
     * @return Map对象
     */
    public static Map<String, Object> entity2Map(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(object==null) {return map;}
	    for (Field field : object.getClass().getDeclaredFields()){
	        try {
	        	boolean flag = field.isAccessible();
	            field.setAccessible(true);
	            Object o = field.get(object);
	            map.put(field.getName(), o);
	            field.setAccessible(flag);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return map;
	}
    /**
     * 是否包含emoji表情
     * @param codepoint 字符
     * @return 含emoji表情true
     */
    private static boolean isemojicharacter(char codepoint) {
        return (codepoint == 0x0) || (codepoint == 0x9) || (codepoint == 0xa)
                || (codepoint == 0xd)
                || ((codepoint >= 0x20) && (codepoint <= 0xd7ff))
                || ((codepoint >= 0xe000) && (codepoint <= 0xfffd))
                || ((codepoint >= 0x10000) && (codepoint <= 0x10ffff));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source 源字符串
     * @return 过滤后的字符串
     */
    public static String filteremoji(String source) {
        if (Func.isBlank(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codepoint = source.charAt(i);
            if (isemojicharacter(codepoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codepoint);
            }
        }
        if (buf == null) {
            return source;
        } else {
            if (buf.length() == len) {
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }


    /**
     * 通过身份证号获取生日
     * @param identifyNumber 身份证号
     * @return 生日
     */
    public static String getBirthByIdcard(String identifyNumber){
        String dateOfBirth = "";
        //通过身份证获取性别和生日
        if(identifyNumber!=null&&!"".equals(identifyNumber)){
            if(identifyNumber.length()==15){
                dateOfBirth  = "19"+identifyNumber.substring(6, 8)+"-"+identifyNumber.substring(8, 10)+"-"+identifyNumber.substring(10, 12);
            }else if (identifyNumber.length()==18){
                dateOfBirth = identifyNumber.substring(6, 10)+"-"+identifyNumber.substring(10, 12)+"-"+identifyNumber.substring(12, 14);
            }
        }
        return dateOfBirth;
    }
    /**
     * 通过身份证号获取生日和性别
     * @param identifyNumber 身份证号
     * @return 1:男  2:女
     */
    public static int getSexByIdcard(String identifyNumber){
        int gender = 1;
        //通过身份证获取性别和生日
        if(identifyNumber!=null&&!"".equals(identifyNumber)){
            if(identifyNumber.length()==15){
                /*基数为男 偶数为女*/
                if(Integer.parseInt(identifyNumber.substring(14, 15)) % 2 == 0){
                    gender = 2;
                }else{
                    gender = 1;
                }
            }else if (identifyNumber.length()==18){
                /*基数为男 偶数为女*/
                if(Integer.parseInt(identifyNumber.substring(16, 17)) % 2 == 0){
                    gender = 2;
                }else{
                    gender = 1;
                }
            }
        }
        return gender;
    }
    /**
     * 将字符串首字母大写
     *
     * @param s
     *            字符串
     * @return 首字母大写后的新字符串
     */
    public static String upperFirst(CharSequence s) {
        if (null == s)
            return null;
        int len = s.length();
        if (len == 0)
            return "";
        char c = s.charAt(0);
        if (Character.isUpperCase(c))
            return s.toString();
        return new StringBuilder(len).append(Character.toUpperCase(c))
                                     .append(s.subSequence(1, len))
                                     .toString();
    }
    /**
     * 获取民族
     * @return 民族
     */
    public static String getNation(){
		String s = new String("{nation:\"汉族\"},"
				+"{nation:\"苗族\"},"
				+"{nation:\"布依族\"},"
				+"{nation:\"侗族\"},"
				+"{nation:\"土家族\"},"
				+"{nation:\"彝族\"},"
				+"{nation:\"仡佬族\"},"
				+"{nation:\"水族\"},"
				+"{nation:\"回族\"},"
				+"{nation:\"瑶族\"},"
				+"{nation:\"壮族\"},"
				+"{nation:\"畲族\"},"
				+"{nation:\"白族\"},"
				+"{nation:\"羌族\"},"
				+"{nation:\"蒙古族\"},"
				+"{nation:\"仫佬族\"},"
				+"{nation:\"毛南族\"},"
				+"{nation:\"满族\"},"
				+"{nation:\"藏族\"},"
				+"{nation:\"裕固族\"},"
				+"{nation:\"锡伯族\"},"
				+"{nation:\"乌孜别克族\"},"
				+"{nation:\"维吾尔族\"},"
				+"{nation:\"佤族\"},"
				+"{nation:\"土族\"},"
				+"{nation:\"塔塔尔族\"},"
				+"{nation:\"塔吉克族\"},"
				+"{nation:\"撒拉族\"},"
				+"{nation:\"普米族\"},"
				+"{nation:\"门巴族\"},"
				+"{nation:\"怒族\"},"
				+"{nation:\"纳西族\"},"
				+"{nation:\"珞巴族\"},"
				+"{nation:\"拉祜族\"},"
				+"{nation:\"僳僳族\"},"
				+"{nation:\"黎族\"},"
				+"{nation:\"柯尔克孜族\"},"
				+"{nation:\"景颇族\"},"
				+"{nation:\"京族\"},"
				+"{nation:\"基诺族\"},"
				+"{nation:\"赫哲族\"},"
				+"{nation:\"高山族\"},"
				+"{nation:\"高山族\"},"
				+"{nation:\"哈萨克族\"},"
				+"{nation:\"哈尼族\"},"
				+"{nation:\"鄂温克族\"},"
				+"{nation:\"俄罗斯族\"},"
				+"{nation:\"鄂伦春族\"},"
				+"{nation:\"独龙族\"},"
				+"{nation:\"德昂族\"},"
				+"{nation:\"傣族\"},"
				+"{nation:\"达斡尔族\"},"
				+"{nation:\"朝鲜族\"},"
				+"{nation:\"布朗族\"},"
				+"{nation:\"保安族\"},"
				+"{nation:\"东乡族\"},"
				+"{nation:\"阿昌族\"},"
				+"{nation:\"穿青人\"},"
				+"{nation:\"其他\"}");
		return "["+s+"]";
	}
    /**
     * 获取政治面貌
     * @return 政治面貌
     */
	public static String getPolicy(){
		String s=new String("{policy:\"群众\"},"
				+"{policy:\"中共党员\"},"
				+"{policy:\"中共预备党员\"},"
				+"{policy:\"共青团员\"},"
				+"{policy:\"民革党员\"},"
				+"{policy:\"民盟盟员\"},"
				+"{policy:\"民建会员\"},"
				+"{policy:\"民进会员\"},"
				+"{policy:\"农工党党员\"},"
				+"{policy:\"致公党党员\"},"
				+"{policy:\"九三学社社员\"},"
				+"{policy:\"台盟盟员\"},"
				+"{policy:\"无党派民主人士\"}");
		return "["+s+"]";
	}
	/**
	 * 截取末尾几位
	 * @param s 源字符串
	 * @param len 截取位数
	 * @return 截取的数据
	 */
	public static String laststr(String s,int len) {
		if(s==null || "".equals(s)) {return "";}
		return s.trim().substring(s.trim().length()-len);

	}
	/**
	 * 过滤字符串中的非数字
	 * @param text 源字符串
	 * @return 不含数字的字符串
	 */
	public static String getIntStr(String text) {
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(text);
		System.out.println(matcher.replaceAll(""));
		return matcher.replaceAll("");
	}
    
    /**
     * 获取指定对象的指定属性的值
     * @param object 实体对象
     * @param fieldName 属性名
     * @return 指定属性的值
     */
    public static Object getObjectPropertyValue(Object object,String fieldName) {
    	try {
			Field field = object.getClass().getDeclaredField(fieldName);
			PropertyDescriptor pd=new PropertyDescriptor(field.getName(), object.getClass());
			Method rM = pd.getReadMethod();//获得读方法
			Object value = rM.invoke(object);
			field.getType().toString();
			if(value!=null) {
				return value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	return "";
    }
    /**
     * 获取指定对象的所有属性(没有属性返回空)
     * @param beanClass 实体对象
     * @return 属性list
     */
    public static List<String> getObjectPropertyAllKey(Class<?> beanClass) {
    	try {
    		List<Field> fs = new ArrayList<>();
			do {
				Field[] pfs = beanClass.getDeclaredFields();
				fs.addAll(Arrays.asList(pfs));
				beanClass = beanClass.getSuperclass();
			}while(!equals(beanClass.getName(), Object.class.getName()));
    		
			List<String> fieldNameList = new ArrayList<String>();
			for (Field field : fs) {
				fieldNameList.add(field.getName());
			}
			return fieldNameList;
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	return new ArrayList<String>();
    }
    /**
     * 获取指定对象的所有属性(没有属性抛出异常)
     * @param beanClass 实体对象
     * @return 属性list
     * @throws Exception 没有找到对象属性
     */
    public static List<String> getObjectAllProperty(Class<?> beanClass) throws Exception {
		List<Field> fs = new ArrayList<>();
		do {
			Field[] pfs = beanClass.getDeclaredFields();
			fs.addAll(Arrays.asList(pfs));
			beanClass = beanClass.getSuperclass();
		}while(!equals(beanClass.getName(), Object.class.getName()));
		
		List<String> fieldNameList = new ArrayList<String>();
		for (Field field : fs) {
			fieldNameList.add(field.getName());
		}
		if(fieldNameList==null || fieldNameList.isEmpty()) {
			throw new Exception("没有找到该类的字段");
		}
		return fieldNameList;
    }
	/**
	 * 将实体对象的属性转为mysql查询语句的select 和from中的字段
	 * prex.user_name as fieldPrexuserName
	 * @param cls 实体对象类型
	 * @param prex 数据字段前缀
	 * @param fieldPrex 字段前缀
	 * @return mysql select 字段字符串 
	 */
	public static String vo2mysqlField(Class<?> cls
			, String prex
			, String fieldPrex) {
		prex = isBlank(prex)?"":prex+".";
		fieldPrex = isBlank(fieldPrex)?"":fieldPrex;

		List<Field> fs = new ArrayList<>();
		do {
			Field[] pfs = cls.getDeclaredFields();
			fs.addAll(Arrays.asList(pfs));
			cls = cls.getSuperclass();
		}while(!equals(cls.getName(), Object.class.getName()));

		if(fs.size() <= 0) {
			return "";
		}
		if(fs.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i = 0; i < fs.size(); i ++) {
			com.fasterxml.jackson.annotation.JsonIgnore jsonIgnore = fs.get(i).getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class);
			if(jsonIgnore != null) {
				continue;
			}
			MColumn cd = fs.get(i).getDeclaredAnnotation(MColumn.class);
			String fieldType = fs.get(i).getType().toString();
			if(cd != null) {
				if(Func.isNotBlank(cd.value())) {
					if(index > 0) {
						sb.append(",");
					}
					if(fieldType.endsWith("Date")) {
						sb.append("DATE_FORMAT("+prex+cd.value()+",'%Y-%m-%d %H:%i:%s') as " + fieldPrex + fs.get(i).getName());
					}else {
						sb.append(prex+cd.value() + " as " + fieldPrex + fs.get(i).getName());
					}
					index ++;
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
	 * prex.user_name as fieldPrexuserName
	 * @param cls 实体对象类型
	 * @param prex 数据字段前缀
	 * @param fieldPrex 字段前缀
	 * @return PgSQL select 字段字符串 
	 */
	public static String vo2PgsqlField(Class<?> cls
			, String prex
			, String fieldPrex) {
		prex = Func.isBlank(prex)?"":prex+".";
		fieldPrex = Func.isBlank(fieldPrex)?"":fieldPrex;

		List<Field> fs = new ArrayList<>();
		do {
			Field[] pfs = cls.getDeclaredFields();
			fs.addAll(Arrays.asList(pfs));
			cls = cls.getSuperclass();
		}while(!equals(cls.getName(), Object.class.getName()));

		if(fs.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i = 0; i < fs.size(); i ++) {
			com.fasterxml.jackson.annotation.JsonIgnore jsonIgnore = fs.get(i).getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class);
			if(jsonIgnore != null) {
				continue;
			}
			MColumn cd = fs.get(i).getDeclaredAnnotation(MColumn.class);
			String fieldType = fs.get(i).getType().toString();
			if(cd != null) {
				if(Func.isNotBlank(cd.value())) {
					if(index > 0) {
						sb.append(",");
					}
					if(fieldType.endsWith("Date")) {
						sb.append("to_char("+prex+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
					}else {
						sb.append(prex+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
					}
					index ++;
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
	 * prex.user_name as fieldPrexuserName
	 * @param cls 实体对象类型
	 * @param prex 数据字段前缀
	 * @param fieldPrex 字段前缀
	 * @param isExtends 是否有继承
	 * @return PgSQL select 字段字符串 
	 */
	public static String vo2PgsqlField(Class<?> cls
			, String prex
			, String fieldPrex,boolean isExtends) {
		prex = Func.isBlank(prex)?"":prex+".";
		fieldPrex = Func.isBlank(fieldPrex)?"":fieldPrex;

		List<Field> fs = new ArrayList<>();
		if(isExtends) {
			do {
				Field[] pfs = cls.getDeclaredFields();
				fs.addAll(Arrays.asList(pfs));
					cls = cls.getSuperclass();
			}while(!equals(cls.getName(), Object.class.getName()));
		}else {
			Field[] pfs = cls.getDeclaredFields();
			fs.addAll(Arrays.asList(pfs));
		}
		if(fs.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i = 0; i < fs.size(); i ++) {
			com.fasterxml.jackson.annotation.JsonIgnore jsonIgnore = fs.get(i).getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class);
			if(jsonIgnore != null) {
				continue;
			}
			MColumn cd = fs.get(i).getDeclaredAnnotation(MColumn.class);
			String fieldType = fs.get(i).getType().toString();
			if(cd != null) {
				if(Func.isNotBlank(cd.value())) {
					if(index > 0) {
						sb.append(",");
					}
					if(fieldType.endsWith("Date")) {
						sb.append("to_char("+prex+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
					}else {
						sb.append(prex+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
					}
					index ++;
				}
			}
		}
		return sb.toString();
	}


	/**
	 * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
	 * prex.user_name as fieldPrexuserName
	 * @param cls 实体对象类型
	 * @param alias 表的别名或表名
	 * @param fieldPrex 字段前缀
	 * @param filterField 需要过滤的字段，格式为pojo实体名,多个用竖线|分隔，如：userName|userPasswd
	 * @param allowField 只允许的字段，格式为pojo实体名,多个用竖线|分隔，如：userName|userPasswd
	 * @return PgSQL select 字段字符串 
	 */
	public static String vo2PgsqlField(Class<?> cls
			, String alias
			, String fieldPrex
			,String filterField
			,String allowField
			,boolean isExtends) {
		alias = Func.isBlank(alias)?"":alias+".";
		fieldPrex = Func.isBlank(fieldPrex)?"":fieldPrex;
		List<Field> fs = new ArrayList<>();

		if(isExtends) {
			do {
				Field[] dfs = cls.getDeclaredFields();
				fs.addAll(Arrays.asList(dfs));
					cls = cls.getSuperclass();
			}while(!equals(cls.getName(), Object.class.getName()));
		}else {
			Field[] dfs = cls.getDeclaredFields();
			fs.addAll(Arrays.asList(dfs));
		}

		if(fs.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i = 0; i < fs.size(); i ++) {
			com.fasterxml.jackson.annotation.JsonIgnore jsonIgnore = fs.get(i).getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class);
			if(jsonIgnore != null) {
				continue;
			}
			if(Func.isNotBlank(filterField)) {
				String[] filterFields = filterField.split("\\|");
				if(Arrays.asList(filterFields).contains(fs.get(i).getName())) {
					continue;
				}
			}
			if(Func.isNotBlank(allowField)) {
				String[] allowFields = allowField.split("\\|");
				if(!Arrays.asList(allowFields).contains(fs.get(i).getName())) {
					continue;
				}
			}
			MColumn cd = fs.get(i).getDeclaredAnnotation(MColumn.class);
			String fieldType = fs.get(i).getType().toString();
			if(cd != null) {
				if(Func.isNotBlank(cd.value())) {
					if(index > 0) {
						sb.append(",");
					}
					if(fieldType.endsWith("Date")) {
						sb.append("to_char("+alias+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
					}else {
						sb.append(alias+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
					}
					index ++;
				}
			}else {

			}
		}
		return sb.toString();
	}

	/**
	 * 获得最近几个月
	 * @see net.hlinfo.opt.Func.Times#getLastMonths
	 * @param size 数量
	 * @param asc 排序，正序：true，倒序：false
	 * @return 最近size个月列表
	 */
	@Deprecated
	public static List<String> getLastMonths(int size,boolean asc) {
        return Times.getLastMonths(size, asc);
    }

	 /**
	  * 正则校验是否金额
	  * @param str 字符串
	  * @return 是金额true，否则false
	  */
	 public static boolean isMoney(String str) {
	        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
	        Matcher match = pattern.matcher(str);
	        return match.matches();
	}
	 /**
	  * 正则校验是否不是钱
	  * @param str 字符串
	  * @return 不是钱true
	  */
	 public static boolean isNotMoney(String str) {
		 return !isMoney(str);
	 }
	 /**
	 * 计算百分比：如成绩及格率等
	 * @param total 总数
	 * @param sub 需要计算的数
	 * @return 百分比
	 */
	public static String percentage(int total,int sub) {
		if(total==0) {
			return "0%";
		}
		BigDecimal bdtotal = new BigDecimal(total+"");
		BigDecimal bdsub = new BigDecimal(sub+"");
		BigDecimal div = bdsub.divide(bdtotal,4,BigDecimal.ROUND_HALF_UP);
		BigDecimal rs = div.multiply(new BigDecimal("100"));
		return rs.stripTrailingZeros().toPlainString()+"%";
	}
	/**
     * 获取请求流内容，针对Java EE
     * @param request HttpServletRequest对象
     * @return 返回文本内容
     * @throws IOException IO异常
     */
    public static String getRequestBody(javax.servlet.http.HttpServletRequest request) throws Exception {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
        	javax.servlet.ServletInputStream stream = request.getInputStream();
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new Exception("读取数据流出现异常！");
        } finally {
            reader.close();
        }
        return sb.toString();
    }
    
    /**
	 * 将字符串转为字节数组byte[]
	 * @param s 字符串
	 * @param charsetName 编码，默认为utf-8
	 * @return 字节数组byte[]
	 */
	public static byte[] string2byte(String s,String charsetName) {
		byte[] pmdata = null;
		try {
			if(isBlank(charsetName)) {charsetName = "utf-8";}
			pmdata = s.getBytes(charsetName);
			return pmdata;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pmdata;
	}
	/**
	 * 处理SM4密钥：SM4密钥为128 bit，16字节，超过16字节截取，不足16字节的在后补0
	 @param key 字符串格式秘钥
	 @return byte数组格式秘钥
	 */
	public static byte[] genSM4key(String key) {
		byte[] keyBytes = key.getBytes();
		byte[] keyResult = new byte[16];
		if(keyBytes.length>16) {
			System.arraycopy(keyBytes, 0, keyResult, 0, 16);
		}else if(keyBytes.length<16){
			int diff = 16-keyBytes.length;
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<diff;i++) {
				sb.append("0");
			}
			byte[] pushBytes = sb.toString().getBytes();
			System.arraycopy(keyBytes, 0, keyResult, 0, keyBytes.length);
			System.arraycopy(pushBytes, 0, keyResult, keyBytes.length, diff);
		}else {
			return keyBytes;
		}
		return keyResult;
	}
	/**
	 * 获取随机数
	 * @param length 随机数长度
	 * @return 随机数
	 */
	public static String getRandom(int length) {
		Random random = new Random();
		StringBuilder rs = new StringBuilder();
		for (int i = 0; i < length; i++) {
			rs.append(random.nextInt(10));
		}
		return rs.toString();
	}
	/**
     * 返回指定长度由随机数字+小写字母组成的字符串
     * 
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String randomChars(int length) {
        return randomChars(length, false);
    }

    /**
     * 返回指定长度随机数字+字母(大小写敏感)组成的字符串
     * 
     * @param length 指定长度
     * @param caseSensitivity 是否区分大小写
     * @return 随机字符串
     */
    public static String randomChars(int length, boolean caseSensitivity) {
    	Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        int t = caseSensitivity ? baseCharacter.length : baseCharacter.length - 26;
        for (int i = 0; i < length; i++)
            sb.append(baseCharacter[random.nextInt(t)]);
        return sb.toString();
    }
	 /**
	  * 时间相关方法
	  * @author hlinfo.net
	  *
	  */
	 public static final class Times{
		 /**
		  * 获取当前年份
		  * @return 当前年份
		  */
		 public final static int getYear(){
			return LocalDate.now().getYear();
		 }
		 /**
		  * 获取当前日期:yyyy-MM-dd
		  * @return 当前日期
		  */
		 public final static String nowDate() {
				LocalDate ld = LocalDate.now();
				return ld.format(DateTimeFormatter.ISO_LOCAL_DATE);
		 }
		 /**
		 * 指定格式获取日期时间
		 * @param formatter 时间格式,如：yyyy-MM-dd HH:mm:ss
		 * @return 指定格式的日期时间
		 */
		public static String nowDate(String formatter) {
			try {
				if(isBlank(formatter)) {formatter = "yyyy-MM-dd HH:mm:ss";}
				return LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
			} catch (Exception e) {
				e.printStackTrace();
				return now();
			}
		}
		 /**
		  * 获取当前日期:yyyyMMdd
		  * @return 当前日期
		  */
		 public final static String nowDateBasic() {
				LocalDate ld = LocalDate.now();
				return ld.format(DateTimeFormatter.BASIC_ISO_DATE);
		 }
		 /**
		  * 获取整型的当前日期:yyyyMMdd
		  * @return 当前日期
		  */
		 public final static int nowDateBasicInt() {
			 LocalDate ld = LocalDate.now();
			 return string2int(ld.format(DateTimeFormatter.BASIC_ISO_DATE));
		 }
		 /**
		  * 获取长整型的当前日期:yyyyMMdd
		  * @return 当前日期
		  */
		 public final static long nowDateBasicLong() {
			 LocalDate ld = LocalDate.now();
			 return string2Long(ld.format(DateTimeFormatter.BASIC_ISO_DATE));
		 }
		 /**
		  * 获取当前日期，格式:yyyy/MM/dd
		  * @return 当前日期
		  */
		 public final static String nowDateSlash() {
				LocalDate ld = LocalDate.now();
				return ld.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		 }
		 /**
		  * 获取当前年月:yyyyMM
		  * @return 当前年月
		  */
		 public final static String nowYearMonth() {
				LocalDate ld = LocalDate.now();
				return ld.format(DateTimeFormatter.ofPattern("yyyyMM"));
		 }
		 /**
		 * 获取时间：yyyy-MM-dd HH:mm:ss
		 * @return 当前年月日时分秒
		 */
		public static String now() {
			return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		/**
		 * 获取时间：yyyyMMddHHmmss
		 * @return 当前年月日时分秒，不含分割符号
		 */
		public static String nowNumber() {
			return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		}
		/**
		 * 获取时间：yyyyMMddHHmmssSSS
		 * @return 当前年月日时分秒毫秒，不含分割符号
		 */
		public static String nowNumberFull() {
			return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		}
		/**
		 * Date转LocalDate
		 * @param date Date对象
		 * @return LocalDate对象
		 */
		public static LocalDate date2LocalDate(Date date) {
			if(date==null) {
				throw new NullPointerException("date is null");
			}
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		/**
		 * Date转LocalDateTime
		 * @param date Date对象
		 * @return LocalDateTime对象
		 */
		public static LocalDateTime date2LocalDateTime(Date date) {
			if(date==null) {
				throw new NullPointerException("date is null");
			}
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
		/**
		 * LocalDate转Date
		 * @param localdate LocalDate对象
		 * @return Date对象
		 */
		public static Date Localdate2Date(LocalDate localdate) {
			if(localdate==null) {
				throw new NullPointerException("localdate is null");
			}
			return Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		/**
		 * LocalDateTime转Date
		 * @param localDateTime LocalDateTime对象
		 * @return Date对象
		 */
		public static Date LocaldateTime2Date(LocalDateTime localDateTime) {
			if(localDateTime==null) {
				throw new NullPointerException("localDateTime is null");
			}
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		/**
		 * 时间添+秒s
		 * @param d 时间对象
		 * @param s 需要添加的秒数
		 * @return 添加后的时间对象
		 */
		public static Date dateAddSecond(Date d, int s) {
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.SECOND, s);
			return c.getTime();
		}
		/**
		 * 时间添+秒s
		 * @param d 时间对象
		 * @param s 需要添加的秒数
		 * @return 添加后的时间对象
		 */
		public  static Date dateAddSecond(Date d, long s) {
			return dateAddSecond(d, Long.valueOf(s).intValue());
		}
		/**
		 * 获取多少分钟之后的时间
		 * @param min 分钟
		 * @return 指定分钟后的时间对象
		 */
		public  static Date nowTimeByAfter(int min) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, min);
			return c.getTime();
		}
		/**
		 * 获取多少分钟之后的时间,指定格式
		 * @param min 分钟
		 * @param formatter 时间格式,如：yyyy-MM-dd HH:mm:ss
		 * @return 指定指定格式多少分钟之后的时间
		 */
		public  static String nowTimeByAfter(int min,String formatter) {
			if(isBlank(formatter)) {formatter = "yyyy-MM-dd HH:mm:ss";}
			LocalDateTime ld = nowTimeByAfter(min).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			return ld.format(DateTimeFormatter.ofPattern(formatter));
		}
		
		/**
		 * 返回时间格式如：2020-02-17 00:00:00
		 * @param time 时间对象
		 * @return 格式化的时间对象
		 */
		public static Date getStartOfDay(Date time) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(time);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();
	    }
		/**
		 * 返回时间格式如：2020-02-19 23:59:59
		 * @param time 时间对象
		 * @return 格式化的时间对象
		 */
	    public static Date getEndOfDay(Date time) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(time);
	        calendar.set(Calendar.HOUR_OF_DAY, 23);
	        calendar.set(Calendar.MINUTE, 59);
	        calendar.set(Calendar.SECOND, 59);
	        calendar.set(Calendar.MILLISECOND, 999);
	        return calendar.getTime();
	    }
	    /**
	     * 获取日期对象对应的指定天，
	     * @param date 日期对象
	     * @param day 任意天，原则上是本月，超过本月则为下一月的，负数为上一月的
	     * @param naxtMonth 是否强制下一月
	     * @param formatter 日期格式，默认yyyy-MM-dd
	     * @return 字符串日期
	     */
	   public static String monthAnyDay(Date date,int day,boolean naxtMonth,String formatter) {
			formatter = Func.isBlank(formatter)?"yyyy-MM-dd":formatter;
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			if(naxtMonth) {
				calendar.add(Calendar.MONTH,1);
			}
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 999);
			LocalDate ld = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String lastDay = ld.format(DateTimeFormatter.ofPattern(formatter));
			return lastDay;
		}
	   /**
	    * 获取指定日期对象月的第一天
	    * @param date 日期对象
	    * @param naxtMonth 是否强制下一月
	    * @return 字符串日期，格式：yyyy-MM-dd
	    */
	   public static String monthFirstDay(Date date,boolean naxtMonth) {
		   Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			if(naxtMonth) {
				calendar.add(Calendar.MONTH,1);
			}
		   return monthAnyDay(date,calendar.getActualMinimum(Calendar.DAY_OF_MONTH),naxtMonth,null);
	   }
	   /**
	    * 获取指定日期对象月的最后一天
	    * @param date 日期对象
	    * @param naxtMonth 是否强制下一月
	    * @return 字符串日期，格式：yyyy-MM-dd
	    */
	   public static String monthLastDay(Date date,boolean naxtMonth) {
		   Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			if(naxtMonth) {
				calendar.add(Calendar.MONTH,1);
			}
		   return monthAnyDay(date,calendar.getActualMaximum(Calendar.DAY_OF_MONTH),naxtMonth,null);
	   }
	    /**
		 * 格式化日期
		 * @param dateStr 日期字符串
		 * @param dtf DateTimeFormatter对象
		 * @return  格式化的日期字符串
		 */
		public static String parseDate(String dateStr,DateTimeFormatter dtf){
			try {
				if(dateStr==null || "".equals(dateStr)) {return "";}
				String result = "";
				if(dateStr.length()>10) {
					LocalDateTime date = LocalDateTime.parse(dateStr,dtf);
					result = date.toString();
				}else {
					LocalDate date = LocalDate.parse(dateStr,dtf);
					result =  date.toString();
				}
				return result;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
		/**
		 * 格式化日期
		 * @param dateTimeStr 日期字符串
		 * @param dtf DateTimeFormatter对象
		 * @return 格式化的日期时间对象
		 */
		public static Date string2Date(String dateTimeStr,DateTimeFormatter dtf){
			try {
				if(dateTimeStr==null || "".equals(dateTimeStr)) {
					throw new NullPointerException("dateTimeStr is null");
				}
				Date result = new Date();
				if(dateTimeStr.length()>10) {
					LocalDateTime date = LocalDateTime.parse(dateTimeStr,dtf);
					result = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
				}else {
					LocalDate date = LocalDate.parse(dateTimeStr,dtf);
					result =  Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
				}
				return result;
			} catch (Exception e) {
				throw e;
			}
		}
		/**
		 * 格式化日期
		 * @param dateTimeStr 日期字符串,格式：yyyy-MM-dd HH:mm:ss
		 * @return 格式化的日期时间对象
		 */
		public static Date string2Date(String dateTimeStr){
			return string2Date(dateTimeStr,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		/**
		 * 格式化日期
		 * @param date 日期对象
		 * @param dtf DateTimeFormatter对象
		 * @return 格式化的日期字符串
		 */
		public static String date2String(Date date,DateTimeFormatter dtf){
			try {
				if(date==null) {
					throw new NullPointerException("date is null");
				}
				LocalDateTime result = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				return result.format(dtf).toString();
			} catch (Exception e) {
				throw e;
			}
		}
		/**
		 * 格式化日期
		 * @param date 日期对象
		 * @return 格式化的日期字符串,格式：yyyy-MM-dd HH:mm:ss
		 */
		public static String date2String(Date date){
			return date2String(date,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	 
		/**
		 * 获得本月第一天,返回格式yyyy-MM-dd
		 * @return 字符串类型时间
		 */
		public static String monthFirstDay() {
		   return monthAnyDay(new Date(),1,false,null);
		}
		/**
		 * 获得本月第一天,指定返回格式
		 * @param formatter 时间格式，如：yyyy-MM-dd HH:mm:ss
		 * @return 字符串类型时间
		 */
		public static String monthFirstDay(String formatter) {
			return monthAnyDay(new Date(),1,false,formatter);
		}
		/**
		 * 获得指定时间月份的最后一天
		 * @param date 时间对象
		 * @param formatter 时间格式，如：yyyy-MM-dd HH:mm:ss
		 * @return 字符串类型时间
		 */
		public static String monthLastDay(Date date,String formatter) {
			formatter = isBlank(formatter)?"yyyy-MM-dd":formatter;
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
	        calendar.set(Calendar.MINUTE, 59);
	        calendar.set(Calendar.SECOND, 59);
	        calendar.set(Calendar.MILLISECOND, 999);
			LocalDate ld = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String lastDay = ld.format(DateTimeFormatter.ofPattern(formatter));
			return lastDay;
		}
		/**
		 * 获得本月最后一天,返回格式yyyy-MM-dd
		 * @return 字符串类型日期
		 */
		public static String monthLastDay() {
			return monthLastDay(new Date(),null);
		}
		/**
		 * 获得本月最后一天,指定返回格式
		 * @param formatter 时间格式，如：yyyy-MM-dd HH:mm:ss
		 * @return 字符串类型时间
		 */
		public static String monthLastDay(String formatter) {
			return monthLastDay(new Date(),formatter);
		}
		
		/**
		 * 获得最近几个月
		 * @param size 数量
		 * @param asc 排序，正序：true，倒序：false
		 * @return 最近size个月列表
		 */
		public static List<String> getLastMonths(int size,boolean asc) {
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        List<String> list = new ArrayList<String>(size);
	        for (int i=0;i<size;i++) {
	            c.setTime(new Date());
	            c.add(Calendar.MONTH, -i);
	            Date m = c.getTime();
	            list.add(Times.date2String(m, DateTimeFormatter.ofPattern("yyyy-MM")));
	        }
	       if(asc) {
	        	Collections.reverse(list);
	        }
	        return list;

	    }
		/**
		 * 获取指定时间月份的总天数
		 * @param date 时间对象
		 * @return 总天数
		 */
		public static int monthDaysCount(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date==null?new Date():date);
			calendar.set(Calendar.DATE, 1);
			calendar.roll(Calendar.DATE, -1);
			return calendar.get(Calendar.DATE);
		}
		/**
		 * 获取指定年月的总天数
		 * @param year 年度
		 * @param month 月份
		 * @return 总天数
		 */
		public static int monthDaysCount(int year, int month) {
			if(year<1900 || month<=0 || month>12) {return 0;}
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.DAY_OF_MONTH, month);
			calendar.set(Calendar.DATE, 1);
			calendar.roll(Calendar.DATE, -1);
			return calendar.get(Calendar.DATE);
		}
		
		/**
		 * 获取指定任意日期的所有天集合
		 * @param date 指定月份任意日期
		 * @return 指定月份所有天集合
		 */
		public static List<String> getMonthDays(Date date) {
			List<String> list = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH);
			int daysCount = monthDaysCount(year, m);
			//从1号开始
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			for (int i = 0; i < daysCount; i++, calendar.add(Calendar.DATE, 1)) {
				Date d = calendar.getTime();
				list.add(date2String(d, DateTimeFormatter.ISO_LOCAL_DATE));
			}
			return list;
		 }
		/**
		 * 获取今天还剩余多少时间，需要指定时间单位
		 * @param unit 时间单位，仅仅支持，ChronoUnit.MICROS、ChronoUnit.SECONDS、ChronoUnit.MINUTES、ChronoUnit.HOURS
		 * @return 今天剩余的时间
		 */
		public static long surplusTime(ChronoUnit unit) {
			LocalTime midnight = LocalTime.MIDNIGHT;
			LocalDate today = LocalDate.now();
			LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
			LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
			long surplus = TimeUnit.NANOSECONDS.toSeconds(Duration.between(LocalDateTime.now(), tomorrowMidnight).toNanos());
			switch (unit) {
				case MICROS:
					surplus = surplus * 1000;
					break;
				case MINUTES:
					surplus = surplus/60;
					break;
				case HOURS:
					surplus = surplus/60/60;
					break;
				default:
					break;
			}
			return surplus;
		}
		/**
		 * 获取今天还剩余多少分钟
		 * @return 今天剩余的分钟
		 */
		public static long surplusTime() {
			return surplusTime(ChronoUnit.MINUTES);
		}
		
	 }
}
