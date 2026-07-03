package cn.structured.job.server.openapi;

import cn.structured.job.server.openapi.model.JobRequest;
import com.xxl.job.core.openapi.model.CallbackRequest;
import com.xxl.job.core.openapi.model.RegistryRequest;
import com.xxl.tool.response.Response;

import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:52:49
 */
public interface AdminBiz {

    // ---------------------- callback ----------------------

    /**
     * callback
     *
     * @param callbackRequestList callback request list
     * @return response
     */
    public Response<String> callback(List<CallbackRequest> callbackRequestList);


    // ---------------------- registry ----------------------

    /**
     * registry
     *
     * @param registryRequest registry request
     * @return  response
     */
    public Response<String> registry(RegistryRequest registryRequest);

    /**
     * registry remove
     *
     * @param registryRequest registry request
     * @return  response
     */
    public Response<String> registryRemove(RegistryRequest registryRequest);


    // ---------------------- job operate ----------------------

    /**
     * add job
     *
     * @param jobRequest job request
     * @return response
     */
    public Response<String> add(JobRequest jobRequest);

    /**
     * update job
     *
     * @param jobRequest job request
     * @return response
     */
    public Response<String> update(JobRequest jobRequest);

    /**
     * remove job
     *
     * @param jobRequest job request
     * @return response
     */
    public Response<String> remove(JobRequest jobRequest);

    /**
     * start job
     *
     * @param jobRequest job request
     * @return response
     */
    public Response<String> start(JobRequest jobRequest);

    /**
     * stop job
     *
     * @param jobRequest job request
     * @return response
     */
    public Response<String> stop(JobRequest jobRequest);

    /**
     * trigger job
     *
     * @param jobRequest job request
     * @return response
     */
    public Response<String> trigger(JobRequest jobRequest);

}