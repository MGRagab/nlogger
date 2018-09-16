package org.naqla.nlogger;

import android.util.Log;


public class Logger {

    public enum LogType {
        ERROR,
        INFO,
        DEBUG,
        WARN,
        VERBOSE
    }


    private Logger() {
        throw new RuntimeException("Private constructor cannot be accessed");
    }

    private static LogType logType = LogType.DEBUG;
    private static boolean isLoggable = true;
    private static boolean isKotlin = false;
    private static String TAG = "NLOG";

    private static void init(Builder builder) {
        Logger.logType = builder.getLogType();
        Logger.TAG = builder.getTag();
        Logger.isLoggable = builder.isIsLoggable();
        Logger.isKotlin = builder.isIsKotlin();
    }


    public static void log(Object message) {
        if (isLoggable) {
            String body = "| " + makeLog(message, "log");
            switch (logType) {
                case INFO:
                    Log.i(TAG, body);
                    break;
                case DEBUG:
                    Log.d(TAG, body);
                    break;
                case ERROR:
                    Log.e(TAG, body);
                    break;
                case WARN:
                    Log.w(TAG, body);
                    break;
                case VERBOSE:
                    Log.v(TAG, body);
                    break;
            }
        }
    }

    private static String makeLog(Object message, String calledMethodName) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo(calledMethodName) == 0) {
                currentIndex = ++i;
                break;
            }
        }

        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[currentIndex];
        String fullClassName = traceElement.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = traceElement.getMethodName();
        int lineNumber = traceElement.getLineNumber();
        String logMessage = message == null ? null : message.toString();
        String postFix = isKotlin ? ".kt:" : ".java:";
        return logMessage + " | (" + className + postFix + lineNumber + ")";
    }

    public static class Builder {
        private static LogType logType = LogType.INFO;
        private static boolean isLoggable = true;
        private static boolean isKotlin = false;
        private static String tag = "Logger";

        public Builder logType(LogType logType) {
            Builder.logType = logType;
            return this;
        }

        public Builder isLoggable(boolean isLoggable) {
            Builder.isLoggable = isLoggable;
            return this;
        }

        public Builder tag(String tag) {
            Builder.tag = tag;
            return this;
        }

        public Builder setIsKotlin(boolean isKotlin) {
            Builder.isKotlin = isKotlin;
            return this;
        }

        public boolean isIsKotlin() {
            return isKotlin;
        }

        public void build() {
            init(this);
        }

        LogType getLogType() {
            return logType;
        }

        boolean isIsLoggable() {
            return isLoggable;
        }

        String getTag() {
            return tag;
        }
    }

}
