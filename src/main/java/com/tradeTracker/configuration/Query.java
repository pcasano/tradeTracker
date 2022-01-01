package com.tradeTracker.configuration;

public class Query {

    private final String queryId;

    public Query(Configuration configuration, String... runnerType) {
        if(runnerType.length==0) {
            this.queryId = configuration.getBrokerData().getMonthlyQueryId();
        } else {
            switch (runnerType[0]) {
                case "d" -> this.queryId = configuration.getBrokerData().getDailyQueryId();
                case "lm" -> this.queryId = configuration.getBrokerData().getLastMonthQueryId();
                case "m" -> this.queryId = configuration.getBrokerData().getMonthlyQueryId();
                default -> throw new IllegalArgumentException("tradeTracker can run only with d or m parameters");
            }
        }
    }

    public String getQueryId() {
        return queryId;
    }
}
