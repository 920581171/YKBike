package com.yk.bike.response;

public class MobResponse {

    /**
     * detail : 验证失败3次，验证码失效，请重新请求
     * description : 校验验证码请求频繁
     * httpStatus : 400
     * status : 467
     * error : Every captcha can verify 3 times within 5 minutes.
     */

    private String detail;
    private String description;
    private int httpStatus;
    private int status;
    private String error;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
