package devCamp.WebApp.configurations;

import com.microsoft.azure.storage.CloudStorageAccount;
import devCamp.WebApp.properties.ApplicationProperties;
import devCamp.WebApp.properties.AzureStorageAccountProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
@EnableConfigurationProperties(value = {
        ApplicationProperties.class,
        AzureStorageAccountProperties.class
})
@EnableCaching
@ComponentScan
public class ApplicationConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    private static String AZURE_STORAGEACCOUNT_KEY = "AZURE_STORAGE_ACCESS_KEY";

    @Autowired
    private Environment environment;

    @Autowired
    private AzureStorageAccountProperties azureStorageAccountProperties;

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostConstruct
    protected void postConstruct() throws IOException {
        LOG.info(applicationProperties.toString());
        LOG.info(azureStorageAccountProperties.toString());
    }

    @Bean
    CloudStorageAccount getStorageAccount() throws InvalidKeyException, URISyntaxException {
        String cs = String.format("DefaultEndpointsProtocol=http;AccountName=%s;AccountKey=%s",
                azureStorageAccountProperties.getName(),
                environment.getProperty(AZURE_STORAGEACCOUNT_KEY));
        return CloudStorageAccount.parse(cs);
    }

    @Bean
    RestTemplate getRestTemplate(){
        //create/configure REST template class here and autowire where needed
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        JedisConnectionFactory ob = new JedisConnectionFactory(poolConfig);
        ob.setUsePool(true);
        String redishost = applicationProperties.getRedisHost(); //System.getenv("REDISCACHE_HOSTNAME");
        LOG.info("REDISCACHE_HOSTNAME={}", redishost);
        ob.setHostName(redishost);
        String redisport = applicationProperties.getRedisPort().toString(); //System.getenv("REDISCACHE_PORT");
        LOG.info("REDISCACHE_PORT= {}", redisport);
        try {
            ob.setPort(Integer.parseInt(  redisport));
        } catch (NumberFormatException e1) {
            // if the port is not in the ENV, use the default
            ob.setPort(6379);
        }
        String rediskey = applicationProperties.getPrimaryKey(); //System.getenv("REDISCACHE_PRIMARY_KEY");
        LOG.info("REDISCACHE_PRIMARY_KEY= {}", rediskey);
        ob.setPassword(rediskey);
        ob.afterPropertiesSet();
        RedisTemplate<Object,Object> tmp = new RedisTemplate<>();
        tmp.setConnectionFactory(ob);

        //make sure redis connection is working
        try {
            String msg = tmp.getConnectionFactory().getConnection().ping();
            LOG.info("redis ping response="+msg);
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
        RedisCacheManager manager = new RedisCacheManager(redisTemplate(redisConnectionFactory()));
        manager.setDefaultExpiration(300);
        return manager;
    }
}
