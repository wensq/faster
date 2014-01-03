package org.faster.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sqwen
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpResult {

    public static final OpResult OK = ok();

    @XmlAttribute
    private boolean success;

    @XmlAttribute
    private Integer code;

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

    public static final OpResult ok() {
        return success().code(200).message("OK");
    }

    public static final OpResult created(String createdId) {
        return success().code(201).message("Created").createdId(createdId);
    }

    public static final OpResult accepted() {
        return success().code(202).message("Accepted");
    }

    public static final OpResult noContent() {
        return success().code(204).message("No Content");
    }

    public static final OpResult failed() {
        return new OpResult(false);
    }

    public static final OpResult failed(String errorMessage) {
        return failed().message(errorMessage);
    }

    public static final OpResult badRequest() {
        return failed().code(400).message("Bad Request");
    }

    public static final OpResult unauthorized() {
        return failed().code(401).message("Unauthorized");
    }

    public static final OpResult forbidden() {
        return failed().code(403).message("Forbidden");
    }

    public static final OpResult notFound() {
        return failed().code(404).message("Not Found");
    }

    public static final OpResult methodNotAllowed() {
        return failed().code(405).message("Method Not Allowed");
    }

    public static final OpResult gone() {
        return failed().code(410).message("Gone");
    }

    public static final OpResult unsupportedMediaType() {
        return failed().code(415).message("Unsupported Media Type");
    }

    public static final OpResult unprocessableEntity() {
        return failed().code(422).message("Unprocessable Entity");
    }

    public static final OpResult tooManyRequests() {
        return failed().code(429).message("Too Many Requests");
    }

    public static final OpResult internalServerError(String message) {
        return failed().code(500).message(message);
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
