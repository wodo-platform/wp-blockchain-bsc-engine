package io.wodo.logging.provider;


import io.wodo.logging.LoggingProperties;
import io.wodo.logging.LoggingUtils;

public final class ReqIdProvider {

    public String createFromLogPrefix(String logPrefix, LoggingProperties properties) {
        return properties.isLogRequestId()
                ? create(formatReqId(logPrefix), properties.getRequestIdPrefix())
                : LoggingUtils.EMPTY_MESSAGE;
    }


    public String createFromLogId(String logId, LoggingProperties properties) {
        return properties.isLogRequestId()
                ? create(logId, properties.getRequestIdPrefix()).trim()
                : LoggingUtils.EMPTY_MESSAGE;
    }


    private String create(String reqId, String reqIdPrefix) {
        if (reqIdPrefix != null) {
            reqId = (reqIdPrefix + "_") + reqId;
        }

        return (" REQ-ID: [ " + reqId) + " ]";
    }

    private String formatReqId(String logPrefix) {
        return logPrefix.replaceFirst("\\[", "").replaceFirst("]", "").replaceAll("\\s", "");
    }
}