package org.faster.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sqwen
 */
@XmlRootElement(name = "Result")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpResult {

    public static final OpResult SUCCESS = new OpResult(true);

    private boolean success;

    private Integer code;

    private String message;

    private String resourceId;

    public OpResult() {}

    public OpResult(boolean success) {
        this.success = success;
    }

    public OpResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public OpResult(boolean success, Integer code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public OpResult(Integer code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public static final OpResult created(String resourceId) {
        OpResult ret = new OpResult();
        ret.setSuccess(true);
        ret.setResourceId(resourceId);
        return ret;
    }

    public static final OpResult failed(Integer errorCode) {
        return new OpResult(false, errorCode, null);
    }

    public static final OpResult failed(String errorMessage) {
        return new OpResult(false, null, errorMessage);
    }

    public static final OpResult failed(Integer errorCode, String errorMessage) {
        return new OpResult(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
