package com.xy.it.xysms.repository.config.impl;


import com.xy.it.xysms.repository.config.IRepositoryConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * redis仓储配置实现类
 * Created by liamjung on 2018/1/23.
 */

@Data
@Component
@ConfigurationProperties(prefix = "redis")
@PropertySource("classpath:config/redis-db.properties")
public class RedisRepositoryConfig implements IRepositoryConfig {

    /**
     * redis所在主机ip和端口
     * 格式：ip:port
     * 默认值：127.0.0.1:6379
     */
    private String host;

    /**
     * redis密码，若无密码时，不需要配置
     * 默认值：null
     */
    private String pass ;

    /**
     * redis数据库，不建议修改默认值
     * 默认值：2
     */
    private int db;
}
