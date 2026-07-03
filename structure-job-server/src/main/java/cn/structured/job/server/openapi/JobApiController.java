package cn.structured.job.server.openapi;

import cn.structured.job.server.openapi.model.JobRequest;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.core.constant.Const;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JobApiController {

    @Resource
    private JobApiService jobApiService;

    @PostMapping("/add")
    public Response<String> add(@RequestBody JobRequest jobInfo,
                                @RequestHeader(value = Const.XXL_JOB_ACCESS_TOKEN, required = false) String accesstoken) {
        if (!validateToken(accesstoken)) {
            return Response.ofFail("The access token is wrong.");
        }
        return jobApiService.add(jobInfo);
    }

    @PostMapping("/update")
    public Response<String> update(@RequestBody JobRequest jobInfo,
                                   @RequestHeader(value = Const.XXL_JOB_ACCESS_TOKEN, required = false) String accesstoken) {
        if (!validateToken(accesstoken)) {
            return Response.ofFail("The access token is wrong.");
        }
        return jobApiService.update(jobInfo);
    }

    @PostMapping("/remove")
    public Response<String> remove(@RequestBody JobRequest jobInfo,
                                   @RequestHeader(value = Const.XXL_JOB_ACCESS_TOKEN, required = false) String accesstoken) {
        if (!validateToken(accesstoken)) {
            return Response.ofFail("The access token is wrong.");
        }
        if (jobInfo.getId() == null) {
            return Response.ofFail("Job id is required");
        }
        return jobApiService.remove(jobInfo.getId());
    }

    @PostMapping("/stop")
    public Response<String> stop(@RequestBody JobRequest jobInfo,
                                 @RequestHeader(value = Const.XXL_JOB_ACCESS_TOKEN, required = false) String accesstoken) {
        if (!validateToken(accesstoken)) {
            return Response.ofFail("The access token is wrong.");
        }
        if (jobInfo.getId() == null) {
            return Response.ofFail("Job id is required");
        }
        return jobApiService.stop(jobInfo.getId());
    }

    @PostMapping("/start")
    public Response<String> start(@RequestBody JobRequest jobInfo,
                                  @RequestHeader(value = Const.XXL_JOB_ACCESS_TOKEN, required = false) String accesstoken) {
        if (!validateToken(accesstoken)) {
            return Response.ofFail("The access token is wrong.");
        }
        if (jobInfo.getId() == null) {
            return Response.ofFail("Job id is required");
        }
        return jobApiService.start(jobInfo.getId());
    }

    private boolean validateToken(String accesstoken) {
        String expectedToken = XxlJobAdminBootstrap.getInstance().getAccessToken();
        return StringTool.isBlank(expectedToken) || expectedToken.equals(accesstoken);
    }
}