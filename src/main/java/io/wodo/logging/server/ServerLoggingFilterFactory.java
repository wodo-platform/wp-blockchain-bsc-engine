package io.wodo.logging.server;


import io.wodo.logging.LoggingProperties;
import io.wodo.logging.server.message.formatter.ReqIdServerFormatter;
import io.wodo.logging.server.message.formatter.ServerMetadataMessageFormatter;
import io.wodo.logging.server.message.formatter.request.CookieServerRequestFormatter;
import io.wodo.logging.server.message.formatter.request.HeaderServerRequestFormatter;
import io.wodo.logging.server.message.formatter.response.CookieServerResponseFormatter;
import io.wodo.logging.server.message.formatter.response.HeaderServerResponseFormatter;
import io.wodo.logging.server.message.logger.DefaultServerRequestLogger;
import io.wodo.logging.server.message.logger.DefaultServerResponseLogger;
import io.wodo.logging.server.message.logger.ServerRequestLogger;
import io.wodo.logging.server.message.logger.ServerResponseLogger;

import java.util.Arrays;
import java.util.List;

public class ServerLoggingFilterFactory {

    public static LoggingFilter defaultFilter(LoggingProperties requestProperties,
                                              LoggingProperties responseProperties) {

        List<ServerMetadataMessageFormatter> requestFormatters = Arrays.asList(
                new ReqIdServerFormatter(),
                new HeaderServerRequestFormatter(),
                new CookieServerRequestFormatter()
        );

        List<ServerMetadataMessageFormatter> responseFormatters = Arrays.asList(
                new ReqIdServerFormatter(),
                new HeaderServerResponseFormatter(),
                new CookieServerResponseFormatter()
        );

        return new LoggingFilter(
                new DefaultServerRequestLogger(requestProperties, requestFormatters),
                new DefaultServerResponseLogger(responseProperties, responseFormatters)
        );
    }

    public static LoggingFilter customFilter(ServerRequestLogger serverRequestLogger,
                                             ServerResponseLogger serverResponseLogger) {
        return new LoggingFilter(serverRequestLogger, serverResponseLogger);
    }
}