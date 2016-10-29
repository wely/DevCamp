package devCamp.WebApp.properties;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String incidentApiUrl;
    private String incidentResourcePath;
    private String redisHost;
    private Integer redisPort;
    private String primaryKey;
    private Integer redisSslPort;

    public String getIncidentApiUrl() {
        return incidentApiUrl;
    }

    public void setIncidentApiUrl(String incidentApiUrl) {
        this.incidentApiUrl = incidentApiUrl;
    }

    public String getIncidentResourcePath() {
        return incidentResourcePath;
    }

    public void setIncidentResourcePath(String incidentResourcePath) {
        this.incidentResourcePath = incidentResourcePath;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Integer getRedisSslPort() {
        return redisSslPort;
    }

    public void setRedisSslPort(Integer redisSslPort) {
        this.redisSslPort = redisSslPort;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
