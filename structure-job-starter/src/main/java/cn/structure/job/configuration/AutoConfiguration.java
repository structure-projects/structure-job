package cn.structure.job.configuration;

import cn.structure.job.properties.JobProperties;
import cn.structure.job.rpc.XxlJobClient;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 自动调度装配
 *
 * @author cqliut
 * @version 2022.1222
 * @since 1.0.1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(JobProperties.class)
public class AutoConfiguration {

    @Resource
    private JobProperties jobProperties;

    @Bean
    @ConditionalOnMissingBean({JobProperties.class})
    @ConditionalOnProperty(
            name = {"structure.job.enable"},
            havingValue = "true"
    )
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobProperties.getAdminAddress());
        xxlJobSpringExecutor.setAppname(jobProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(jobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(jobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(jobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(jobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobProperties.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(jobProperties.getExecutor().getLogretentiondays());
        return xxlJobSpringExecutor;
    }

    @Bean
    @ConditionalOnMissingBean({JobProperties.class})
    @ConditionalOnProperty(name = {"structure.job.enable"},
            havingValue = "true")
    public XxlJobClient xxlJobClient() {
        log.info(">>>>>>>>>>> xxl-job client init.");
        XxlJobClient xxlJobClient = new XxlJobClient(jobProperties);
        return xxlJobClient;
    }
}
