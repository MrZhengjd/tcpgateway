package com.game.infrustructure.redis;

/**
 * @author zheng
 */
public class LuaExpression {

    public static final String PUT_IF_NOT_EXISTS = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1])\n" +
            "if (exist == 1) then\n" +
            "    return 0;\n" +
            "end\n" +
            "redis.call('hset',KEYS[1],ARGV[2]);\n" +
            "return 1;";
    /**
     * 获取房间信息脚本
     */
    public static final String GET_ROOM_LUA = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1])\n" +
            "if (exist == 0) then\n" +
            "    return nil;\n" +
            "end\n" +
            "local redisServerId = redis.call('hget',KEYS[1],ARGV[1]);\n" +
            "local innerServerId = ARGV[2];\n" +
            "if (redisServerId == innerServerId ) then\n" +
            "    return redis.call('hget',KEYS[2],ARGV[1]);\n" +
            "end;\n" +
            "return nil;";

    /**
     * 改变房间信息脚本
     * 把服务器id传过来和redis里面的数据对比，一样才可以修改数据
     */
//    public static final String CHANGE_ROOM_LUA =
//            "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1])\n" +
//            "if (exist == 0) then\n" +
//            "    return 0;\n" +
//            "end\n" +
//            "local id = redis.call('hget', KEYS[1], ARGV[1]);\n" +
//            "local serverId = (ARGV[2]);\n" +
//            "if (id == serverId) then\n" +
//            "    redis.call('hset', KEYS[2],ARGV[1], ARGV[3]);\n" +
//            " return 1;"+
//            "else\n" +
//            "    return 0;\n" +
//            "end";

    public static final String CHANGE_ROOM_LUA = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1])\n"+
            "if (exist == 0) then\n"+
            "    return 0;\n"+
            "end\n"+
            "local id = redis.call('hget', KEYS[1], ARGV[1]);\n"+
            "local serverId = (ARGV[2]);\n"+
            "local exit = redis.call('HEXISTS',KEYS[3],ARGV[1]);\n"+
            "if (exit == 1) then\n"+
            "    local operate = redis.call('hget', KEYS[3], ARGV[1]);\n"+
            "    redis.call('hset', KEYS[3],ARGV[1], operate + 1);\n"+
            "else\n"+
            "    redis.call('hset', KEYS[3],ARGV[1], 1);\n"+
            "end\n"+
            "if (id == serverId) then\n"+
            "    redis.call('hset', KEYS[2],ARGV[1], ARGV[3]);\n"+
            "    return 1;\n"+
            "else\n"+
            "    return 0;\n"+
            "end";
    /**
     * 根据房间id修改房间对应的serverId 数据一致才可以修改,并绑定到新的serverId
     */
    public static final String CHANGE_ROOM_SERVER_ID_LUA = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1]);\n" +
            "if (exist == 0) then\n" +
            "	return 0;\n" +
            "end;\n" +
            "local id = redis.call('hget', KEYS[1], ARGV[1]);\n" +
            "local serverId = (ARGV[2]);\n" +
            "redis.log(redis.LOG_DEBUG,id);\n" +
            "if (id == serverId) then\n" +
            "	redis.call('hset', KEYS[2],ARGV[1], ARGV[3]);\n" +
            "	return 1;\n" +
            "else\r\n" +
            "	return 0;\n" +
            "end;";

    /**
     * 创建房间信息脚本
     */
    public static final String CREATE_ROOM_LUA = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1]);\n" +
            "if(exist ~= 1) then\n" +
            "    redis.call('hset', KEYS[1],ARGV[1], ARGV[2]);\n" +
            "    redis.call('hset', KEYS[2],ARGV[1], ARGV[3]);\n" +
            "    redis.call('sadd', ARGV[2],ARGV[1]);\n" +
            "    return 1;\n" +
            "else\n" +
            "    local room = redis.call('hget',KEYS[1],ARGV[1]);\n" +
            "    if (room ~=nil) then\n" +
            "        return 0\n" +
            "    end\n" +
            "    redis.call('hset', KEYS[1],ARGV[1], ARGV[2]);\n" +
            "    redis.call('hset', KEYS[2],ARGV[1], ARGV[3]);\n" +
            "    redis.call('sadd', ARGV[2],ARGV[1]);\n" +
            "    return 1;\n" +
            "end";
    public static final String GET_ALL_ROOM_ID_BY_SERVER_ID = "return redis.call('SMEMBERS',ARGV[1]);";
    /**
     * 把服务器id对应roomId转移到新的服务器id
     */
    public static final String CHANGE_ROOM_SERVER_LUA =
            "local data = redis.call('SMEMBERS', ARGV[1]);\n" +
                    "if data == nil then\n" +
                    "    return 0;\n" +
                    "end\n" +
                    "\n" +
                    "for k, v in pairs(data) do\n" +
                    "    redis.call('hset', KEYS[1],v, ARGV[2]);\n" +
                    "    redis.call('SMOVE',ARGV[1],ARGV[2],v);\n" +
                    "end\n" +
                    "return 1;";
    /**
     *删除房间信息
     */
    public static final String REMOVE_ROOM_LUA = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1])\n" +
            "if (exist == 0) then\n" +
            "    return 0;\n" +
            "end\n" +
            "local id = redis.call('hget',KEYS[1],ARGV[1]);\n" +
            "if(id ~= nil) then\n" +
            "    redis.call('HDEL',KEYS[1],ARGV[1]);\n" +
            "    redis.call('HDEL',KEYS[2],ARGV[1]);\n" +
            "    redis.call('SREM',id,ARGV[1]);\n" +
            "    return 1;\n" +
            "else\n" +
            "    return 0;\n" +
            "end;";
    public static final String ROOM_SERVER_ID_IS_EQUALS = "local exist = redis.call('HEXISTS',KEYS[1],ARGV[1])\n" +
            "if (exist == 0) then\n" +
            "    return 0;\n" +
            "end\n" +
            "local redisServerId = redis.call('hget',KEYS[1],ARGV[1]);\n" +
            "local innerServerId = ARGV[2];\n" +
            "if (redisServerId == innerServerId) then\n" +
            "	return 1;\n" +
            "end;\n" +
            "return 0;";
}
