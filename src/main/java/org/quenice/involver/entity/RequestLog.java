package org.quenice.involver.entity;

/**
 * @author damon.qiu 2020/10/28 2:25 PM
 */
public class RequestLog {
    /**
     * 请求URL
     */
    private String url;
    /**
     * 明文request
     */
    private String plainRequest;
    /**
     * 密文request
     */
    private String cipherRequest;
    /**
     * 明文response
     */
    private String plainResponse;
    /**
     * 密文response
     */
    private String ciphertResponse;
    /**
     * 参数是否加密
     */
    private boolean encrypt;
    /**
     * http是否成功执行
     */
    private boolean httpSuccess;

    /**
     * 请求参数加密是否成功
     */
    private boolean requestEncodeSuccess;
    /**
     * 响应参数是否加密成功
     */
    private boolean responseEncodeSuccess;
    /**
     * 响应参数是否解密成功
     */
    private boolean responseDecodeSuccess;

    /**
     * 请求时间
     */
    private long requestTime;
    /**
     * 响应时间
     */
    private long responseTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlainRequest() {
        return plainRequest;
    }

    public void setPlainRequest(String plainRequest) {
        this.plainRequest = plainRequest;
    }

    public String getCipherRequest() {
        return cipherRequest;
    }

    public void setCipherRequest(String cipherRequest) {
        this.cipherRequest = cipherRequest;
    }

    public String getPlainResponse() {
        return plainResponse;
    }

    public void setPlainResponse(String plainResponse) {
        this.plainResponse = plainResponse;
    }

    public String getCiphertResponse() {
        return ciphertResponse;
    }

    public void setCiphertResponse(String ciphertResponse) {
        this.ciphertResponse = ciphertResponse;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean isHttpSuccess() {
        return httpSuccess;
    }

    public void setHttpSuccess(boolean httpSuccess) {
        this.httpSuccess = httpSuccess;
    }

    public boolean isRequestEncodeSuccess() {
        return requestEncodeSuccess;
    }

    public void setRequestEncodeSuccess(boolean requestEncodeSuccess) {
        this.requestEncodeSuccess = requestEncodeSuccess;
    }

    public boolean isResponseEncodeSuccess() {
        return responseEncodeSuccess;
    }

    public void setResponseEncodeSuccess(boolean responseEncodeSuccess) {
        this.responseEncodeSuccess = responseEncodeSuccess;
    }

    public boolean isResponseDecodeSuccess() {
        return responseDecodeSuccess;
    }

    public void setResponseDecodeSuccess(boolean responseDecodeSuccess) {
        this.responseDecodeSuccess = responseDecodeSuccess;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
}
