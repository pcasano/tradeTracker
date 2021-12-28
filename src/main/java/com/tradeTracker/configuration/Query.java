package com.tradeTracker.configuration;

public class Query {

    private final String queryId;

    public Query(Configuration configuration, String... runnerType) {
        if(runnerType.length==0) {
            this.queryId = configuration.getBrokerData().getMonthlyQueryId();
        } else {
            if(runnerType[0].equals("d")){
                this.queryId = configuration.getBrokerData().getDailyQueryId();
            }
            else if(runnerType[0].equals("m")){
                this.queryId = configuration.getBrokerData().getMonthlyQueryId();
            }else{
                throw new IllegalArgumentException("tradeTracker can run only with d or m parameters");
            }
        }
    }

    public String getQueryId() {
        return queryId;
    }
}
