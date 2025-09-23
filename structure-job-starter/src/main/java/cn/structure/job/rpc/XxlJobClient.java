package cn.structure.job.rpc;

import cn.structure.job.dto.XxlJobInfoDTO;
import cn.structure.job.properties.JobProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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


    public ReturnT<String> add(XxlJobInfoDTO jobInfo) {
        String url = jobProperties.getAdminAddress() + "/api/add";
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头为 application/json
        HttpHeaders headers = new HttpHeaders();
        headers.set(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN, jobProperties.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 直接使用 XxlJobInfoDTO 对象作为请求体，让 RestTemplate 自动序列化为 JSON
        HttpEntity<XxlJobInfoDTO> requestEntity = new HttpEntity<>(jobInfo, headers);

        // 发送 POST 请求，直接接收 ReturnT<String> 类型的响应
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String result = response.getBody();
        // 执行添加命令
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    public ReturnT<String> update(XxlJobInfoDTO jobInfo) {
        String url = jobProperties.getAdminAddress() + "/api/update";
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头为 application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.set(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN, jobProperties.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 直接使用 XxlJobInfoDTO 对象作为请求体，让 RestTemplate 自动序列化为 JSON
        HttpEntity<XxlJobInfoDTO> requestEntity = new HttpEntity<>(jobInfo, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String result = response.getBody();
        // 执行添加命令
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    public ReturnT<String> remove(String jobId) {
        String url = jobProperties.getAdminAddress() + "/api/remove";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN, jobProperties.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formData = "id=" + jobId;
        HttpEntity<String> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String result = response.getBody();
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    public ReturnT<String> pause(String jobId) {
        String url = jobProperties.getAdminAddress() + "/api/stop";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN, jobProperties.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formData = "id=" + jobId;
        HttpEntity<String> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String result = response.getBody();
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }

    public ReturnT<String> start(String jobId) {
        String url = jobProperties.getAdminAddress() + "/api/start";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN, jobProperties.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String formData = "id=" + jobId;
        HttpEntity<String> requestEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String result = response.getBody();
        return JSON.parseObject(result, new TypeReference<ReturnT<String>>() {
        });
    }
}
