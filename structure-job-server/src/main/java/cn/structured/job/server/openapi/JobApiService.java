package cn.structured.job.server.openapi;

import cn.structured.job.server.openapi.model.JobRequest;
import com.xxl.job.admin.business.constant.TriggerStatus;
import com.xxl.job.admin.business.mapper.XxlJobGroupMapper;
import com.xxl.job.admin.business.mapper.XxlJobInfoMapper;
import com.xxl.job.admin.business.mapper.XxlJobLogGlueMapper;
import com.xxl.job.admin.business.mapper.XxlJobLogMapper;
import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.cron.CronExpression;
import com.xxl.job.admin.business.scheduler.misfire.MisfireStrategyEnum;
import com.xxl.job.admin.business.scheduler.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.business.scheduler.thread.JobScheduleHelper;
import com.xxl.job.admin.business.scheduler.trigger.TriggerTypeEnum;
import com.xxl.job.admin.business.scheduler.type.ScheduleTypeEnum;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.core.constant.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JobApiService {

    private static final Logger logger = LoggerFactory.getLogger(JobApiService.class);

    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;

    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;

    @Resource
    private XxlJobLogGlueMapper xxlJobLogGlueMapper;

    public Response<String> add(JobRequest jobRequest) {
        XxlJobInfo jobInfo = convertToXxlJobInfo(jobRequest);

        XxlJobGroup group = xxlJobGroupMapper.load(jobInfo.getJobGroup());
        if (group == null) {
            return Response.ofFail(I18nUtil.getString("system_please_choose") + I18nUtil.getString("jobinfo_field_jobgroup"));
        }
        if (StringTool.isBlank(jobInfo.getJobDesc())) {
            return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc"));
        }
        if (StringTool.isBlank(jobInfo.getAuthor())) {
            return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author"));
        }

        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        if (scheduleTypeEnum == null) {
            return Response.ofFail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_invalid"));
        }
        if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
            if (jobInfo.getScheduleConf() == null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
                return Response.ofFail("Cron" + I18nUtil.getString("system_invalid"));
            }
        }

        if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_gluetype") + I18nUtil.getString("system_invalid"));
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType()) && StringTool.isBlank(jobInfo.getExecutorHandler())) {
            return Response.ofFail(I18nUtil.getString("system_please_input") + "JobHandler");
        }

        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_invalid"));
        }
        if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
            return Response.ofFail(I18nUtil.getString("misfire_strategy") + I18nUtil.getString("system_invalid"));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_invalid"));
        }

        jobInfo.setAddTime(new Date());
        jobInfo.setUpdateTime(new Date());
        jobInfo.setGlueUpdatetime(new Date());
        if (jobInfo.getExecutorHandler() != null) {
            jobInfo.setExecutorHandler(jobInfo.getExecutorHandler().trim());
        }
        xxlJobInfoMapper.save(jobInfo);
        if (jobInfo.getId() < 1) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_add") + I18nUtil.getString("system_fail"));
        }

        logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}", "api", "jobinfo-save", jobInfo.getId());

        return Response.ofSuccess(String.valueOf(jobInfo.getId()));
    }

    public Response<String> update(JobRequest jobRequest) {
        XxlJobInfo jobInfo = convertToXxlJobInfo(jobRequest);

        if (jobInfo.getId() <= 0) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_invalid"));
        }

        XxlJobInfo existsJobInfo = xxlJobInfoMapper.loadById(jobInfo.getId());
        if (existsJobInfo == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_not_found"));
        }

        if (StringTool.isBlank(jobInfo.getJobDesc())) {
            return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc"));
        }
        if (StringTool.isBlank(jobInfo.getAuthor())) {
            return Response.ofFail(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author"));
        }

        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        if (scheduleTypeEnum == null) {
            return Response.ofFail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_invalid"));
        }
        if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
            if (jobInfo.getScheduleConf() == null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
                return Response.ofFail("Cron" + I18nUtil.getString("system_invalid"));
            }
        }

        XxlJobGroup jobGroup = xxlJobGroupMapper.load(jobInfo.getJobGroup());
        if (jobGroup == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_field_jobgroup") + I18nUtil.getString("system_invalid"));
        }

        long nextTriggerTime = existsJobInfo.getTriggerNextTime();
        boolean scheduleDataNotChanged = jobInfo.getScheduleType() != null && jobInfo.getScheduleType().equals(existsJobInfo.getScheduleType())
                && jobInfo.getScheduleConf() != null && jobInfo.getScheduleConf().equals(existsJobInfo.getScheduleConf());
        if (existsJobInfo.getTriggerStatus() == TriggerStatus.RUNNING.getValue() && !scheduleDataNotChanged) {
            try {
                Date nextValidTime = scheduleTypeEnum.getScheduleType().generateNextTriggerTime(jobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
                if (nextValidTime == null) {
                    return Response.ofFail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_invalid"));
                }
                nextTriggerTime = nextValidTime.getTime();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return Response.ofFail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_invalid"));
            }
        }

        existsJobInfo.setJobGroup(jobInfo.getJobGroup());
        existsJobInfo.setJobDesc(jobInfo.getJobDesc());
        existsJobInfo.setAuthor(jobInfo.getAuthor());
        existsJobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
        existsJobInfo.setScheduleType(jobInfo.getScheduleType());
        existsJobInfo.setScheduleConf(jobInfo.getScheduleConf());
        existsJobInfo.setMisfireStrategy(jobInfo.getMisfireStrategy());
        existsJobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
        if (jobInfo.getExecutorHandler() != null) {
            existsJobInfo.setExecutorHandler(jobInfo.getExecutorHandler().trim());
        }
        existsJobInfo.setExecutorParam(jobInfo.getExecutorParam());
        existsJobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        existsJobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
        existsJobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
        existsJobInfo.setChildJobId(jobInfo.getChildJobId());
        existsJobInfo.setTriggerNextTime(nextTriggerTime);
        existsJobInfo.setUpdateTime(new Date());

        xxlJobInfoMapper.update(existsJobInfo);

        logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}", "api", "jobinfo-update", jobInfo.getId());

        return Response.ofSuccess();
    }

    public Response<String> remove(Integer id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
        if (xxlJobInfo == null) {
            return Response.ofSuccess();
        }

        xxlJobInfoMapper.delete(id);
        xxlJobLogMapper.delete(id);
        xxlJobLogGlueMapper.deleteByJobId(id);

        logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}", "api", "jobinfo-remove", id);

        return Response.ofSuccess();
    }

    public Response<String> start(Integer id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
        if (xxlJobInfo == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
        }

        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(), ScheduleTypeEnum.NONE);
        if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
            return Response.ofFail(I18nUtil.getString("schedule_type_none_limit_start"));
        }

        long nextTriggerTime = 0;
        try {
            Date nextValidTime = scheduleTypeEnum.getScheduleType().generateNextTriggerTime(xxlJobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            if (nextValidTime == null) {
                return Response.ofFail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_invalid"));
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Response.ofFail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_invalid"));
        }

        xxlJobInfo.setTriggerStatus(TriggerStatus.RUNNING.getValue());
        xxlJobInfo.setTriggerLastTime(0);
        xxlJobInfo.setTriggerNextTime(nextTriggerTime);
        xxlJobInfo.setUpdateTime(new Date());

        xxlJobInfoMapper.update(xxlJobInfo);

        logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}", "api", "jobinfo-start", id);

        return Response.ofSuccess();
    }

    public Response<String> stop(Integer id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
        if (xxlJobInfo == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
        }

        xxlJobInfo.setTriggerStatus(TriggerStatus.STOPPED.getValue());
        xxlJobInfo.setTriggerLastTime(0);
        xxlJobInfo.setTriggerNextTime(0);
        xxlJobInfo.setUpdateTime(new Date());

        xxlJobInfoMapper.update(xxlJobInfo);

        logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}", "api", "jobinfo-stop", id);

        return Response.ofSuccess();
    }

    public Response<String> trigger(Integer jobId, String executorParam, String addressList) {
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(jobId);
        if (xxlJobInfo == null) {
            return Response.ofFail(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
        }

        if (executorParam == null) {
            executorParam = "";
        }

        XxlJobAdminBootstrap.getInstance().getJobTriggerPoolHelper().trigger(jobId, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);

        logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}", "api", "jobinfo-trigger", jobId);

        return Response.ofSuccess();
    }

    private XxlJobInfo convertToXxlJobInfo(JobRequest jobRequest) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setId(jobRequest.getId() != null ? jobRequest.getId() : 0);
        xxlJobInfo.setJobGroup(jobRequest.getJobGroup() != null ? jobRequest.getJobGroup() : 0);
        xxlJobInfo.setJobDesc(jobRequest.getJobDesc());
        xxlJobInfo.setAuthor(jobRequest.getAuthor());
        xxlJobInfo.setAlarmEmail(jobRequest.getAlarmEmail());
        xxlJobInfo.setScheduleType(jobRequest.getScheduleType() != null ? jobRequest.getScheduleType() : ScheduleTypeEnum.CRON.name());
        xxlJobInfo.setScheduleConf(jobRequest.getScheduleConf() != null ? jobRequest.getScheduleConf() : jobRequest.getJobCron());
        xxlJobInfo.setMisfireStrategy(jobRequest.getMisfireStrategy() != null ? jobRequest.getMisfireStrategy() : "DO_NOTHING");
        xxlJobInfo.setExecutorRouteStrategy(jobRequest.getExecutorRouteStrategy());
        xxlJobInfo.setExecutorHandler(jobRequest.getExecutorHandler());
        xxlJobInfo.setExecutorParam(jobRequest.getExecutorParam());
        xxlJobInfo.setExecutorBlockStrategy(jobRequest.getExecutorBlockStrategy());
        xxlJobInfo.setExecutorTimeout(jobRequest.getExecutorTimeout() != null ? jobRequest.getExecutorTimeout() : 0);
        xxlJobInfo.setExecutorFailRetryCount(jobRequest.getExecutorFailRetryCount() != null ? jobRequest.getExecutorFailRetryCount() : 0);
        xxlJobInfo.setGlueType(jobRequest.getGlueType());
        xxlJobInfo.setGlueSource(jobRequest.getGlueSource());
        xxlJobInfo.setGlueRemark(jobRequest.getGlueRemark());
        xxlJobInfo.setChildJobId(jobRequest.getChildJobId());
        return xxlJobInfo;
    }
}