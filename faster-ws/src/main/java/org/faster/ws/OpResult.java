package org.faster.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sqwen
 */
@XmlRootElement(name = "Result")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpResult {

    public static final OpResult SUCCESS = new OpResult(true);

    @XmlAttribute
    private Integer code;

    @XmlAttribute
    private boolean success;

    @XmlAttribute
    private String message;

    @XmlAttribute
    private Integer count;

    @XmlAttribute
    private String createdId;

    public OpResult() {}

    public OpResult(boolean success) {
        this.success = success;
    }

    public OpResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static final OpResult success() {
        return new OpResult(true);
    }

    public static final OpResult success(String message) {
        return success().message(message);
    }

    public static final OpResult created(String createdId) {
        OpResult ret = new OpResult();
        ret.setSuccess(true);
        ret.setCreatedId(createdId);
        return ret;
    }

    public static final OpResult failed() {
        return new OpResult(false);
    }

    public static final OpResult failed(String errorMessage) {
        return failed().message(errorMessage);
    }

    public OpResult code(Integer code) {
        this.code = code;
        return this;
    }

    public OpResult message(String message) {
        this.message = message;
        return this;
    }

    public OpResult count(Integer count) {
        this.count = count;
        return this;
    }

    public OpResult createdId(String createdId) {
        this.createdId = createdId;
        return this;
    }

    public OpResult createdId(Number createdId) {
        return createdId( String.valueOf(createdId) );
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }
}
