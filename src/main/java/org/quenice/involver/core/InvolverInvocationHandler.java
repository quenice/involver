package org.quenice.involver.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quenice.involver.compatibility.GenericTypeResolver;
import org.springframework.context.ApplicationContext;
import org.quenice.involver.entity.FinalConfig;
import org.quenice.involver.entity.RequestLog;
import org.quenice.involver.exception.CodecException;
import org.quenice.involver.exception.InvolverException;
import org.quenice.involver.handler.CodecHandler;
import org.quenice.involver.helper.ConfigResolver;

import java.lang.reflect.*;

/**
 * 动态代理实例的调用处理器
 *
 * @author damon.qiu 2020/10/26 4:53 PM
 */
public class InvolverInvocationHandler implements InvocationHandler {

    private ObjectMapper objectMapper;

    private ApplicationContext applicationContext;

    public InvolverInvocationHandler() {
        this.initial();
    }

    public InvolverInvocationHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.initial();
    }

    /**
     * 初始化
     */
    private void initial() {
        this.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RequestLog requestLog = null;
        FinalConfig config = null;
        try {
            // 获得配置
            config = ConfigResolver.resolve(method, args, applicationContext);

            // 获得请求参数
            Object param = config.getParam();

            requestLog = new RequestLog();
            requestLog.setUrl(config.getUrl());

            // 处理请求参数
            String finalRequest = this.handleRequest(config, param, requestLog);

            // 发起请求
            String response = this.execute(config, finalRequest, requestLog);

            // 处理响应参数
            String finalResponse = this.handleResponse(config, response, requestLog);

            if (finalResponse == null) return null;

            // 如果是String类型，直接返回
            if (method.getReturnType() == String.class) {
                return finalResponse;
            }

            // json转为复杂对象
            return objectMapper.readValue(finalResponse, objectMapper.getTypeFactory().constructType(GenericTypeResolver.resolveType(method.getGenericReturnType(), method.getDeclaringClass())));
        } catch (Exception e) {
            throw new InvolverException(e);
        } finally {
            // 处理log
            if (config != null && config.getLogHandler() != null && requestLog != null) {
                config.getLogHandler().handle(requestLog, config.getExposedConfig() != null ? config.getExposedConfig().clone() : null);
            }
        }

    }


    /**
     * 处理请求参数
     * // TODO - 考虑参数是string的情况，不需要转换为JSON
     *
     * @param config
     * @param param
     * @param requestLog
     * @return
     */
    private String handleRequest(FinalConfig config, Object param, RequestLog requestLog) {
        String plainRequest = null;
        String cipherRequest = null;
        try {
            if (param == null) return null;
            if (param instanceof String) {
                plainRequest = param.toString();
            } else {
                plainRequest = objectMapper.writeValueAsString(param);
            }

            if (!config.isEncrypt()) return plainRequest;

            CodecHandler codecHandler = config.getCodecHandler();

            // 当@Http.codec = Flag.TRUE时，codecHandler必须配置
            if (codecHandler == null) throw new CodecException("codecHandler is required when codec = Flag.TRUE");

            if (config.getCodecHandler() != null) {
                cipherRequest = config.getCodecHandler().encode(plainRequest, config.getExposedConfig().clone());
                requestLog.setRequestEncodeSuccess(true);
            }
            return cipherRequest != null ? cipherRequest : plainRequest;
        } catch (Exception e) {
            requestLog.setRequestEncodeSuccess(false);
            throw new CodecException("Handle request params Error.", e);
        } finally {
            requestLog.setRequestTime(config.getTimeHandler().getTime());
            requestLog.setPlainRequest(plainRequest);
            requestLog.setCipherRequest(cipherRequest);
        }
    }

    /**
     * 处理http执行
     *
     * @param config
     * @param request
     * @param requestLog
     * @return
     */
    private String execute(FinalConfig config, String request, RequestLog requestLog) {
        try {
            if (config.getRequestHandler() == null) {
                requestLog.setHttpSuccess(false);
                return null;
            }

            requestLog.setHttpSuccess(true);
            return config.getRequestHandler().handle(config.getUrl(), request, config.getMethod(), config.getExposedConfig().clone());
        } catch (Exception e) {
            requestLog.setHttpSuccess(false);
            throw new InvolverException("Do http request Error.", e);
        } finally {
            requestLog.setResponseTime(config.getTimeHandler().getTime());
        }
    }


    /**
     * 处理响应参数
     *
     * @param config
     * @param response
     * @param requestLog
     * @return
     */
    private String handleResponse(FinalConfig config, String response, RequestLog requestLog) {
        String plainResponse = null;
        String cipherResponse = null;
        try {
            if (response == null) return null;

            if (!config.isEncrypt()) {
                plainResponse = response;
                return plainResponse;
            }

            cipherResponse = response;
            plainResponse = config.getCodecHandler().decode(cipherResponse, config.getExposedConfig().clone());
            requestLog.setResponseDecodeSuccess(true);
            return plainResponse;
        } catch (Exception e) {
            requestLog.setResponseDecodeSuccess(false);
            throw new CodecException("Handle response params Error.", e);
        } finally {
            requestLog.setPlainResponse(plainResponse);
            requestLog.setCiphertResponse(cipherResponse);
        }
    }
}
