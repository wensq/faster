package org.faster.commons.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sqwen
 */
public class ApplicationException extends RuntimeException {

    private ErrorCode errorCode;

    private Map<String, Object> attributes;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String s) {
        super(s);
    }

    public ApplicationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApplicationException(Throwable throwable) {
        super(throwable);
    }

    public ApplicationException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ApplicationException(String s, ErrorCode errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public ApplicationException(String s, Throwable throwable, ErrorCode errorCode) {
        super(s, throwable);
        this.errorCode = errorCode;
    }

    public ApplicationException(Throwable throwable, ErrorCode errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ApplicationException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public ApplicationException setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public Object getAttribute(String name) {
        return attributes == null ? null : attributes.get(name);
    }

    public ApplicationException setAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        attributes.put(name, value);
        return this;
    }

    @Override
    public String getMessage() {
        if (errorCode == null && attributes == null) {
            return super.getMessage();
        }

        StringBuilder sb = new StringBuilder(super.getMessage() == null ? "" : super.getMessage() + " ");
        sb.append("{");
        if (errorCode != null) {
            sb.append("ErrorCode=")
                    .append(errorCode)
                    .append("(")
                    .append(errorCode.getNumber())
                    .append("), ");
        }
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

}
