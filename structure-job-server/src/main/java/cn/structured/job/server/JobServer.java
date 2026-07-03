package cn.structured.job.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.structured.job.server", "com.xxl.job.admin"})
@MapperScan({"com.xxl.job.admin.business.mapper", "com.xxl.job.admin.framework.mapper"})
public class JobServer {
    public static void main(String[] args) {
        SpringApplication.run(JobServer.class, args);
    }
}