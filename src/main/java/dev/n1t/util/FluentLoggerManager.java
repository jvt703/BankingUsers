package dev.n1t.util;

import org.fluentd.logger.FluentLogger;

import java.util.Map;

public class FluentLoggerManager {
    private static FluentLogger LOG = FluentLogger.getLogger("test.**");

    public void doApplicationLogic(String tag, Map<String, Object> data) {
        LOG.log(tag, data);
    }
}
