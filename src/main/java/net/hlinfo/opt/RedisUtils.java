package net.hlinfo.opt;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * Redis操作工具类
 * @author hlinfo.net
 *
 */
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisUtils {
	/**
	 * RedisTemplate实例化对象
	 */
	public RedisTemplate redisTemplate;
	/**
	 * 通过构造函数注入RedisTemplate
	 * @param redisTemplate RedisTemplate实例化对象
	 */
	public RedisUtils(RedisTemplate redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 缓存基本的对象，Integer、String、实体类等
	 *
	 * @param key   缓存的键
	 * @param value 缓存的值
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> setObject(String key, T value) {
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value);
		return operation;
	}
	/**
	 * 缓存基本的对象，Integer、String、实体类等
	 *
	 * @param key   缓存的键
	 * @param value 缓存的值
	 * @return 缓存的对象
	 */
	@Deprecated
	public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
		return setObject(key,value);
	}

	/**
	 * 缓存基本的对象，Integer、String、实体类等，带过期时间
	 *
	 * @param key      缓存的键
	 * @param value    缓存的值
	 * @param timeout  时间
	 * @param timeUnit 时间颗粒度
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> setObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value, timeout, timeUnit);
		return operation;
	}
	/**
	 * 缓存基本的对象，Integer、String、实体类等，带过期时间
	 * @param key      缓存的键
	 * @param value    缓存的值
	 * @param timeout  时间
	 * @param timeUnit 时间颗粒度
	 * @return 缓存的对象
	 */
	@Deprecated
	public <T> ValueOperations<String, T> setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
		return setObject(key,value,timeout,timeUnit);
	}
	/**
	 * 重置缓存基本的对象，Integer、String、实体类等，带过期时间
	 *
	 * @param key      缓存的键
	 * @param value    缓存的值
	 * @param timeout  时间
	 * @param timeUnit 时间颗粒度
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> resetObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
		if(this.hashKeys(key)) {
			this.deleteObject(key);
		}
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value, timeout, timeUnit);
		return operation;
	}

	/**
	 * 获得缓存的基本对象。
	 *
	 * @param key 缓存的键
	 * @return 缓存的键对应的数据
	 */
	public <T> T getObject(String key) {
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		return operation.get(key);
	}
	/**
	 * 获得缓存的基本对象。
	 * @param key 缓存的键
	 * @return 缓存的键对应的数据
	 */
	@Deprecated
	public <T> T getCacheObject(String key) {
		return getObject(key);
	}

	/**
	 * 删除单个对象
	 *
	 * @param key redis缓存的键
	 */
	public void deleteObject(String key) {
		if(this.hashKeys(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 删除集合对象
	 *
	 * @param collection 集合
	 */
	public void deleteObject(Collection collection) {
		redisTemplate.delete(collection);
	}

	/**
	 * 缓存List数据(Redis本身的List)
	 *
	 * @param key      缓存的键
	 * @param dataList 待缓存的List数据
	 * @return 缓存的对象
	 */
	public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
		ListOperations listOperation = redisTemplate.opsForList();
		if (null != dataList) {
			int size = dataList.size();
			for (int i = 0; i < size; i++) {
				listOperation.leftPush(key, dataList.get(i));
			}
		}
		return listOperation;
	}

	/**
	 * 获得缓存的list对象
	 *
	 * @param key 缓存的键
	 * @return 缓存的键对应的数据
	 */
	public <T> List<T> getCacheList(String key) {
		List<T> dataList = new ArrayList<T>();
		ListOperations<String, T> listOperation = redisTemplate.opsForList();
		Long size = listOperation.size(key);

		for (int i = 0; i < size; i++) {
			dataList.add(listOperation.index(key, i));
		}
		return dataList;
	}

	/**
	 * 缓存Set
	 *
	 * @param key     缓存的键
	 * @param dataSet 缓存的数据
	 * @return 缓存数据的对象
	 */
	public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
		BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
		Iterator<T> it = dataSet.iterator();
		while (it.hasNext()) {
			setOperation.add(it.next());
		}
		return setOperation;
	}

	/**
	 * 获得缓存的set
	 *
	 * @param key
	 * @return Set集合
	 */
	public <T> Set<T> getCacheSet(String key) {
		Set<T> dataSet = new HashSet<T>();
		BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);
		dataSet = operation.members();
		return dataSet;
	}

	/**
	 * 缓存Map
	 *
	 * @param key redis键
	 * @param dataMap map数据
	 * @return 缓存的对象
	 */
	public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap) {
		HashOperations hashOperations = redisTemplate.opsForHash();
		if (null != dataMap) {
			for (Map.Entry<String, T> entry : dataMap.entrySet()) {
				hashOperations.put(key, entry.getKey(), entry.getValue());
			}
		}
		return hashOperations;
	}
	/**
	 * 获得缓存的基本对象。
	 *
	 * @param key 缓存的键
	 * @return 缓存的键对应的数据
	 */
	public int getCacheInt(String key) {
		ValueOperations<String, Integer> operation = redisTemplate.opsForValue();
		return operation.get(key)==null?0:operation.get(key);
	}
	/**
	 * 获得缓存的Map
	 *
	 * @param key redis键
	 * @return 获取的map结果
	 */
	public <T> Map<String, T> getCacheMap(String key) {
		Map<String, T> map = redisTemplate.opsForHash().entries(key);
		return map;
	}

	/**
	 * 获得缓存的基本对象列表
	 * 
	 * @param pattern 字符串前缀
	 * @return 对象列表
	 */
	public Collection<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
	/**
	 * 缓存对象数据，指定时间（分钟）
	 * @param key 缓存的键
	 * @param value 缓存的值
	 * @param minutes 分钟
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> setCacheData(String key, T value,long minutes) {
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value,Duration.ofMinutes(minutes));
		return operation;
	}
	/**
	 * 缓存对象数据，指定时间
	 * @param key 缓存的键
	 * @param value 缓存的值
	 * @param minutes 分钟
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> resetCacheData(String key, T value,long minutes) {
		if(this.hashKeys(key)) {
			this.deleteObject(key);
		}
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value,Duration.ofMinutes(minutes));
		return operation;
	}
	/**
	 * 缓存对象数据，指定时间
	 * @param key 缓存的键
	 * @param value 缓存的值
	 * @return 缓存的对象
	 */
	public <T> ValueOperations<String, T> resetCacheData(String key, T value) {
		if(this.hashKeys(key)) {
			this.deleteObject(key);
		}
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		operation.set(key, value);
		return operation;
	}
	/**
	 * 判断key是否存在
	 * @param key redis键
	 * @return 存在返回true
	 */
	public boolean hashKeys(String key) {
		return redisTemplate.hasKey(key);
	}
	/**
	 * 模糊删除，*keys
	 * @param key redis键后缀
	 */
	public void deleteBySuffix(String key) {
		Set<String> keys=redisTemplate.keys("*"+key);
        redisTemplate.delete(keys);
	}
	/**
	 * <pre>模糊删除，keys*</pre>
	 * @param key redis键前缀
	 */
	public void deleteByPrex(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.delete(keys);
    }
	/**
     *  获取指定前缀的一系列key
     *  使用scan命令代替keys, Redis是单线程处理，keys命令在KEY数量较多时，
     *  操作效率极低【时间复杂度为O(N)】，该命令一旦执行会严重阻塞线上其它命令的正常请求
     * @param keyPrefix redis键关键词
     * @return key集合
     */
    public Set<String> keysPrex(String keyPrefix) {
        String realKey = "*" + keyPrefix + "*";
        try {
            return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
                Set<String> binaryKeys = new HashSet<>();
                Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(realKey).count(Integer.MAX_VALUE).build());
                while (cursor.hasNext()) {
                    binaryKeys.add(new String(cursor.next()));
                }
                return binaryKeys;
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  删除指定前缀的一系列key
     * @param keyPrefix redis键关键词
     * @return 成功返回true
     */
    public boolean removeAll(String keyPrefix) {
        try {
            Set<String> keys = keysPrex(keyPrefix);
            if(keys!=null) {
                redisTemplate.delete(keys);
               }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 获取剩余的过期时间（指定时间单位）
     * @param key redis键
     * @param timeUnit 时间单位
     * @return 按照时间单位进行返回
     */
    public long getExpire(String key,TimeUnit timeUnit) {
    	return redisTemplate.getExpire(key, timeUnit);
    }
    /**
     * 获取剩余的过期时间(分钟)
     * @param key redis键
     * @return 返回以分钟为单位的时间
     */
    public long getExpireMin(String key) {
    	return redisTemplate.getExpire(key, TimeUnit.MINUTES);
    }
    /**
     * 获取剩余的过期时间(秒)
     * @param key redis键
     * @return 返回以秒为单位的时间
     */
    public long getExpireSeconds(String key) {
    	return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    /**
     * 设置过期时间
     *
     * @param key redis键
     * @param timeout 时间
     * @param unit 时间单位
     * @return 操作结果，成功返回true
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key redis键
     * @param date 时间对象
     * @return 操作结果，成功返回true
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }
    /**
	 * 缓存List数据
	 *
	 * @param key      缓存的键
	 * @param list 待缓存的List数据
	 * @return 缓存的对象
	 */
	public <T> Object setListString(String key, List<T> list) {
		if(this.hashKeys(key)) {this.deleteObject(key);}
		
		ValueOperations<String, Object> operation = redisTemplate.opsForValue();
		operation.set(key, Jackson.toJSONString(list));
		return operation;
	}
	/**
	 * 获得缓存的list对象
	 * @param key 缓存的键
	 * @param clazz 对象类型
	 * @return 缓存的键对应的数据
	 */
	public <T> List<T> getListString(String key,Class<T> clazz) {
		if(!this.hashKeys(key)) {return null;}
		ValueOperations<String, Object> operation = redisTemplate.opsForValue();
		Object rsdata = operation.get(key);
		List<T> list = Jackson.toList(rsdata.toString(), clazz);
		return list;
	}
	
	/**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key 缓存的键
     * @param dbIndex 新的redis数据库编号
     * @return  操作结果，成功返回true
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 缓存的键
     * @return 操作结果，成功返回true
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }
    
    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return 随机返回的key
     */
    public String randomKey() {
        return redisTemplate.randomKey().toString();
    }
    
    /**
     * 修改 key 的名称
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }
    
    /**
     * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     * @return 操作结果,bool类型
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }
    
    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 缓存的键
     * @return key对应值的类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }
    
    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key 缓存的键
     * @return  自增长后的值
     */
    public Long incrBy(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }
    
    /**
     * 获取两个键值的交集
     *
     * @param key 缓存的键
     * @param otherKey 缓存的另一个键
     * @return 两个键值的交集
     */
    public Set<String> sIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key 缓存的键
     * @param otherKeys 缓存的另一些键的集合
     * @return key集合与多个集合的交集
     */
    public Set<String> sIntersect(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }
    
    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param key 健
     * @param otherKey 多个健集合
     * @param destKey 新的健
     * @return 操作结果
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @param destKey 新的健
     * @return 操作结果
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @return 操作结果
     */
    public Set<String> sUnion(String key, String otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @return 操作结果
     */
    public Set<String> sUnion(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @param destKey 新的健
     * @return 操作结果
     */
    public Long sUnionAndStore(String key, String otherKeys, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @param destKey 新的健
     * @return 操作结果
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys,
                               String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @return 操作结果
     */
    public Set<String> sDifference(String key, String otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的差集
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @return 操作结果
     */
    public Set<String> sDifference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @param destKey 新的健
     * @return 操作结果
     */
    public Long sDifference(String key, String otherKeys, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,destKey);
    }

    /**
     * key与多个key的差集存储到destKey中
     *
     * @param key 健
     * @param otherKeys 多个健集合
     * @param destKey 新的健
     * @return 操作结果
     */
    public Long sDifference(String key, Collection<String> otherKeys,
                            String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                destKey);
    }
    
    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key 健
     * @param value 值
     * @param score score值
     * @return 操作结果
     */
    public Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }
    
    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param key 健
     * @param value 值
     * @param delta delta值
     * @return 增加后的值
     */
    public Double zIncrementScore(String key, String value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }
    
    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param key 健
     * @param value 值
     * @return 0表示第一位
     */
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param key 健
     * @param value 值
     * @return 排名
     */
    public Long zReverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key 健
     * @param start
     *            开始位置
     * @param end
     *            结束位置, -1查询所有
     * @return 集合的元素
     */
    public Set<String> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }
}
