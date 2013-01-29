/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.faster.commons.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 *
 * @author calvin
 * @author sqwen
 */
public class Exceptions {

    /**
     * 封装异常到自定义的应用异常
     */
    public static ApplicationException wrap(Throwable t) {
        return wrap(t, null);
    }

    public static ApplicationException wrap(Throwable exception, ErrorCode errorCode) {
        if (exception instanceof ApplicationException) {
            ApplicationException se = (ApplicationException) exception;
            if (errorCode != null && errorCode != se.getErrorCode()) {
                return new ApplicationException(exception.getMessage(), exception, errorCode);
            }
            return se;
        }

        return new ApplicationException(exception.getMessage(), exception, errorCode);
    }

    /**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

}
