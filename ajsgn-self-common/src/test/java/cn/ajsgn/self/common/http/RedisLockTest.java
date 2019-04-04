package cn.ajsgn.self.common.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import cn.ajsgn.self.common.redis.lock.RedisLock;

public class RedisLockTest {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public void test() {
		RedisLock redisLock = new RedisLock(redisTemplate, "LOCK:ORDER_LOCK:".concat("XXXXXX"), 90, 5000);
		try {
			if (redisLock.lock()) {
				// do something
				redisLock.unlock();
			} else {
				//获取锁失败 - 快速失败
			}
		} catch (Exception e) {
			// log exception
		}
	}
	
}
