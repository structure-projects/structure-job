package com.structure.example.job.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * demo
 *
 * @author chuck
 * @version 1.0
 * @since 1.8
 * @since 2025/9/23-下午8:28
 */
@Component
public class JobDemo {

    @XxlJob("jobDemo")
    public ReturnT<String> execute(String param) {
        System.out.println("I'm job demo" + param);
        return ReturnT.SUCCESS;
    }

}
