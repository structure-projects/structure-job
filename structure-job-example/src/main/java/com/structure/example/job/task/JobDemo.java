package com.structure.example.job.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.stereotype.Component;

@Component
public class JobDemo {

    @XxlJob("jobDemo")
    public void execute() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("I'm job demo: {}", param);
    }

}