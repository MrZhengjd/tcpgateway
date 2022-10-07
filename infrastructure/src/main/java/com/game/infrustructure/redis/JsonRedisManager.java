/**
 * 
 */
package com.game.infrustructure.redis;


import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Getter
@Setter
public class JsonRedisManager {
//	private  JsonRedisManager redisManager = new JsonRedisManager();
	private DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();
//	@Resource
//	private RedisUtil redisUtil ;
	@Resource
	private JedisPool jedisPool ;

	private JsonRedisManager() {
		System.out.println("info");
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

//	public static JsonRedisManager getInstance() {
//		return redisManager;
//	}

//	public void setJedisPool(JedisPool jedisPool) {
//		this.jedisPool = jedisPool;
//
//	}

	public String set(String key, String value) {
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();

			return resource.set(key, value);
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public void incr(String key){
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			resource.incr(key);
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	public Set<String> keys(String pattern) {
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			return resource.keys(pattern);
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public long zadd(String key, double score, String value) {
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			return resource.zadd(key, score, value);
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public Set<String> zrangeByScore(String key, double minScore,
                                     double maxScore) {
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			return resource.zrangeByScore(key, minScore, maxScore);
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public double zincrby(String key, double score, String value) {
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			return resource.zincrby(key, score, value);
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}









	public <T> long setObjectHash1(String key, String m, T t) {
		if (t == null) {
			throw new NullPointerException("保存hash不能为null");
		}
		if (m == null) {
			throw new NullPointerException("保存hash,m不能为null");
		}
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();

			return resource.hset(key.getBytes(), m.getBytes(), serializeUtil.serialize(t));
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public <T> long setObjectHash2(String key, String m, String t) {
		if (t == null) {
			throw new NullPointerException("保存hash不能为null");
		}
		if (m == null) {
			throw new NullPointerException("保存hash,m不能为null");
		}
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			long r = resource.hset(key.getBytes(), m.getBytes(), t.getBytes());
			return r;
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public int getObjectHashLen(String key) throws Exception {
		if (key == null) {
			throw new NullPointerException("key 不能为空");
		}
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			long r = resource.hlen(key);
			return (int) r;
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	public <T> T getObjectHash(String key, String id,Class<?extends T> clazz){
		byte[] data = getObjectHash1(key,id);
		if (data == null){
			return null;
		}
		return serializeUtil.deserialize(data,clazz);
	}

	public byte[] getObjectHash1(String key, String id) {
		if (id == null) {
			throw new RuntimeException("key 不能为空");
		}
		Jedis resource = null;
		try {
//			System.out.println("bytes "+key.getBytes());
			resource = jedisPool.getResource();
			return resource.hget(key.getBytes(), id.getBytes());
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	/**
	 * 创建房间信息
	 * @param keys
	 * @param params
	 * @return
	 */
	public boolean createRoomInfo(List<byte[]> keys, List<byte[]> params){
		Jedis resource = null;
		try {

			resource = jedisPool.getResource();

			Object eval = resource.eval(LuaExpression.CREATE_ROOM_LUA.getBytes(), keys, params);
			System.out.println("result "+eval);
			return false;
//
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	/**
	 * 改变房间信息
	 * @param keys
	 * @param params
	 * @return
	 */
	public boolean changeRoomInfo(List<byte[]> keys, List<byte[]> params) {

		Jedis resource = null;
		try {

			resource = jedisPool.getResource();

			long r = (long) resource.eval(LuaExpression.CHANGE_ROOM_LUA.getBytes(), keys, params);
//			System.out.println("r  00000000---- "+r);
			return r == 1;
//
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	public byte[] getRoomWithServerId(List<byte[]> keys, List<byte[]> params) {

		Jedis resource = null;
		try {

			resource = jedisPool.getResource();

			return (byte[]) resource.eval(LuaExpression.GET_ROOM_LUA.getBytes(), keys, params);
//			return eval.getBytes();
//			return resource.hget(key.getBytes(), id.getBytes());
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	public boolean hashPutIfNotExistSimple(String key,String hashCode,Object data){
		List<byte[]> keys = new ArrayList<>();
		keys.add(key.getBytes());
		List<byte[]> param = new ArrayList<>();
		param.add(hashCode.getBytes());
		param.add(serializeUtil.serialize(data));
		return hashPutIfNotExist(keys,param);
	}
	public boolean hashPutIfNotExist(List<byte[]> keys, List<byte[]> params){
		Integer result = (Integer) executeLua(keys,params, LuaExpression.PUT_IF_NOT_EXISTS,jedisPool.getResource());
		if (result == 1){
			return true;
		}
		return false;
	}
	/**
	 * 运行lua脚本
	 * @param keys
	 * @param params
	 * @param lua
	 * @param resource
	 */
	public Object executeLua(List<byte[]> keys, List<byte[]> params, String lua, Jedis resource){
		try {
			Object eval = resource.eval(lua.getBytes(), keys, params);
			return eval;

//
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	/**
	 * 改变所有房间的服务器信息
	 * @param keys
	 * @param params
	 * @return
	 */
	public boolean changeAllRoomServerInfo(List<String> keys, List<String> params) {

		Jedis resource = null;
		try {

			resource = jedisPool.getResource();

			 resource.eval(LuaExpression.CHANGE_ROOM_SERVER_LUA, keys, params);
			 return false;
//			return eval.getBytes();
//			return resource.hget(key.getBytes(), id.getBytes());
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}
	public byte[] getObjectHashLua(List<String> keys, List<String> params) {

		Jedis resource = null;
		try {

			resource = jedisPool.getResource();

			return (byte[]) resource.eval(LuaExpression.GET_ROOM_LUA, keys, params);
//			return eval.getBytes();
//			return resource.hget(key.getBytes(), id.getBytes());
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public boolean delObjectHash(String key, String id) {
		if (id == null) {
			throw new RuntimeException("key 不能为空");
		}
		Jedis resource = null;
		try {

			resource = jedisPool.getResource();
			return resource.hdel(key.getBytes(), id.getBytes()) == 1 ? true
					: false;
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}

	public boolean delObject(String key) {
		if (key == null) {
			throw new RuntimeException("key 不能为空");
		}
		Jedis resource = null;
		try {

			resource = jedisPool.getResource();
			return resource.del(key) > 0 ? true : false;
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}








	public String get(String key) {
		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			return resource.get(key);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}
	}



	public <T> List<Object> setPipelingZadd(String key, Map<String, Double> m) {

		Jedis resource = null;
		try {
			resource = jedisPool.getResource();
			Pipeline p = resource.pipelined();
			p.zadd(key, m);
			List<Object> results = p.syncAndReturnAll();
			return results;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (resource != null && jedisPool != null) {
				jedisPool.returnResource(resource);
			}
		}

	}


}
