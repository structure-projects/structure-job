package com.structure.example.job.controller;

import cn.structure.job.dto.XxlJobInfoDTO;
import cn.structure.job.enums.ExecutorRouteStrategyEnum;
import cn.structure.job.properties.JobProperties;
import cn.structure.job.rpc.XxlJobClient;
import com.xxl.job.core.constant.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务管理
 *
 * @author chuck
 * @version 1.0
 * @since 1.8
 * @since 2025/9/23-下午8:09
 */
@RestController
@RequestMapping("/job")
public class JobManagerController {

    @Resource
    private XxlJobClient xxlJobClient;

    @Resource
    private JobProperties jobProperties;


    @RequestMapping("/add")
    public String add() {
        System.out.println("add");
        XxlJobInfoDTO jobInfo = getXxlJobInfo();
        Response<String> response = xxlJobClient.add(jobInfo);
        return response.getData();
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable Integer id) {
        System.out.println("update");
        XxlJobInfoDTO jobInfo = getXxlJobInfo();
        jobInfo.setId(id);
        jobInfo.setJobDesc("更新描述");
        Response<String> response = xxlJobClient.update(jobInfo);
        return response.getData();
    }

    @RequestMapping("/remove/{id}")
    public String remove(@PathVariable String id) {
        System.out.println("remove");
        Response<String> response = xxlJobClient.remove(id);
        return response.getData();
    }

    @RequestMapping("/pause/{id}")
    public String pause(@PathVariable String id) {
        System.out.println("pause");
        Response<String> response = xxlJobClient.pause(id);
        return response.getData();
    }

    @RequestMapping("/start/{id}")
    public String resume(@PathVariable String id) {
        System.out.println("resume");
        Response<String> response = xxlJobClient.start(id);
        return response.getData();
    }

    private XxlJobInfoDTO getXxlJobInfo() {
        XxlJobInfoDTO xxlJobInfo = new XxlJobInfoDTO();
        xxlJobInfo.setJobCron("0/5 * * * * ?");
        xxlJobInfo.setAuthor("system user");
        xxlJobInfo.setJobDesc("测试");
        xxlJobInfo.setExecutorHandler("jobDemo");
        xxlJobInfo.setExecutorParam("test=test");
        xxlJobInfo.setJobGroup(1);
        xxlJobInfo.setExecutorRouteStrategy(ExecutorRouteStrategyEnum.FIRST.name());
        xxlJobInfo.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name());
        xxlJobInfo.setExecutorTimeout(3000);
        xxlJobInfo.setExecutorFailRetryCount(1);
        xxlJobInfo.setGlueType(GlueTypeEnum.BEAN.name());
        return xxlJobInfo;
    }
}
