package devCamp.WebApp;

import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
	private Log log = LogFactory.getLog(CacheConfig.class);

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
    		JedisPoolConfig poolConfig = new JedisPoolConfig();
    		poolConfig.setMaxTotal(5);
    		poolConfig.setTestOnBorrow(true);
    		poolConfig.setTestOnReturn(true);
    		JedisConnectionFactory ob = new JedisConnectionFactory(poolConfig);
    		ob.setUsePool(true);
    		String redishost = System.getenv("REDISCACHE_HOSTNAME");
    		log.info("REDISCACHE_HOSTNAME="+redishost);
    		ob.setHostName(redishost);
    		String redisport = System.getenv("REDISCACHE_PORT");
    		log.info("REDISCACHE_PORT="+redisport);
    		ob.setPort(Integer.parseInt(  redisport));
    		String rediskey = System.getenv("REDISCACHE_PRIMARY_KEY");
    		log.info("REDISCACHE_PRIMARY_KEY="+rediskey);
    		ob.setPassword(rediskey);
    		ob.afterPropertiesSet();
			RedisTemplate<Object,Object> tmp = new RedisTemplate<>();
			tmp.setConnectionFactory(ob);

    		//make sure redis connection is working
    		try {
    			String msg = tmp.getConnectionFactory().getConnection().ping();
        		log.info("redis ping response="+msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return ob;
    	}

    @Bean(name="redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
    	RedisCacheManager manager =new RedisCacheManager(redisTemplate(redisConnectionFactory()));
    	manager.setDefaultExpiration(300);
        return manager;
    }

}
