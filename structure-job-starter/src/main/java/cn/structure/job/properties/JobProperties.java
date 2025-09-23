package cn.structure.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * xxl job 配置类
 *
 * @author chuck
 * @version 2022.1222
 * @since 1.0.1
 */
@Data
@Configuration
@ConfigurationProperties(
        prefix = "structure.job"
)
public class JobProperties {

    /**
     * 是否启用
     */
    private Boolean enable = Boolean.FALSE;
    /**
     * 管理端配置
     */
    private String adminAddress;
    /**
     * token
     */
    private String accessToken = "job.structure.cn";

    /**
     * 执行器配置
     */
    private Executor executor;

    @Data
    public static class Executor {
        /**
         * 应用名称
         */
        private String appname;
        /**
         * 地址
         */
        private String address;
        /**
         * ip
         */
        private String ip;
        /**
         * 端口
         */
        private Integer port = 7778;
        /**
         * 日志存储天数
         */
        private Integer logretentiondays = 30;
        /**
         * 日志存储路径
         */
        private String logpath = "/app/logs";
    }
}
