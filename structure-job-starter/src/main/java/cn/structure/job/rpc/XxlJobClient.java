package cn.structure.job.rpc;

import cn.structure.job.dto.XxlJobInfoDTO;
import cn.structure.job.properties.JobProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xxl.job.core.constant.Const;
import com.xxl.tool.response.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * XXL-JOBclient
 *
 * @author chuck
 * @version 1.0
 * @since 1.8
 * @since 2025/9/15-下午4:01
 */
@Slf4j
@AllArgsConstructor
public class XxlJobClient {

    private final JobProperties jobProperties;


    public Response<String> add(XxlJobInfoDTO jobInfo) {
        String url = jobProperties.getAdminAddress() + "/api/add";
        return doPost(url, jobInfo);
    }

    public Response<String> update(XxlJobInfoDTO jobInfo) {
        String url = jobProperties.getAdminAddress() + "/api/update";
        return doPost(url, jobInfo);
    }

    public Response<String> remove(String jobId) {
        String url = jobProperties.getAdminAddress() + "/api/remove";
        XxlJobInfoDTO jobInfo = new XxlJobInfoDTO();
        jobInfo.setId(Integer.parseInt(jobId));
        return doPost(url, jobInfo);
    }

    public Response<String> pause(String jobId) {
        String url = jobProperties.getAdminAddress() + "/api/stop";
        XxlJobInfoDTO jobInfo = new XxlJobInfoDTO();
        jobInfo.setId(Integer.parseInt(jobId));
        return doPost(url, jobInfo);
    }

    public Response<String> start(String jobId) {
        String url = jobProperties.getAdminAddress() + "/api/start";
        XxlJobInfoDTO jobInfo = new XxlJobInfoDTO();
        jobInfo.setId(Integer.parseInt(jobId));
        return doPost(url, jobInfo);
    }

    private Response<String> doPost(String url, XxlJobInfoDTO jobInfo) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(Const.XXL_JOB_ACCESS_TOKEN, jobProperties.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<XxlJobInfoDTO> requestEntity = new HttpEntity<>(jobInfo, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            String result = response.getBody();
            if (result == null) {
                log.warn("XXL-JOB API response body is null for url: {}", url);
                Response<String> resp = new Response<>();
                resp.setCode(-1);
                resp.setMsg("API response is null");
                return resp;
            }
            return JSON.parseObject(result, new TypeReference<Response<String>>() {
            });
        } catch (RestClientException e) {
            log.error("XXL-JOB API request failed for url: {}", url, e);
            Response<String> resp = new Response<>();
            resp.setCode(-1);
            resp.setMsg("API request failed: " + e.getMessage());
            return resp;
        }
    }
}