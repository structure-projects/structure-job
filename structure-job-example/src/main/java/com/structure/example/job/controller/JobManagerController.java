package com.structure.example.job.controller;

import cn.structure.job.dto.XxlJobInfoDTO;
import cn.structure.job.enums.ExecutorRouteStrategyEnum;
import cn.structure.job.properties.JobProperties;
import cn.structure.job.rpc.XxlJobClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
        ReturnT<String> returnT = xxlJobClient.add(jobInfo);
        return returnT.getContent();
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable Integer id) {
        System.out.println("update");
        XxlJobInfoDTO jobInfo = getXxlJobInfo();
        jobInfo.setId(id);
        jobInfo.setJobDesc("更新描述");
        ReturnT<String> returnT = xxlJobClient.update(jobInfo);
        return returnT.getContent();
    }

    @RequestMapping("/remove/{id}")
    public String remove(@PathVariable String id) {
        System.out.println("remove");
        ReturnT<String> returnT = xxlJobClient.remove(id);
        return returnT.getContent();
    }

    @RequestMapping("/pause/{id}")
    public String pause(@PathVariable String id) {
        System.out.println("pause");
        ReturnT<String> returnT = xxlJobClient.pause(id);
        return returnT.getContent();
    }

    @RequestMapping("/start/{id}")
    public String resume(@PathVariable String id) {
        System.out.println("resume");
        ReturnT<String> returnT = xxlJobClient.start(id);
        return returnT.getContent();
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
