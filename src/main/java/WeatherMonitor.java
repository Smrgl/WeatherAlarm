package main.java;

import main.java.monitoring.*;

class WeatherMonitor {
    public static void main(String[] args){

        OnlineAlertStatus onlineAlertStatus = new OnlineAlertStatus();
        new Thread(onlineAlertStatus).start();

        AlertService alertService = new AlertService();
        DataFilter dataFilter = new DataFilter();
        LoggingService loggingService = new LoggingService(alertService,dataFilter);
        JsonRequestService jsonRequestService = new JsonRequestService();
        MonitoringService mService = new MonitoringService(jsonRequestService,loggingService);

        mService.initiateMonitoring();

    }
}
