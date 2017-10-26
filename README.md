# WeatherAlarmMonitor


## Description


This app gets is requesting weather data from openweathermap.org. Requested period is 5 days with 3 hour periods.

App is configured in resources/monitoring.json. 
Variables to define in monitoring.json :

```
checking_period - pause between requests to server
appid - token that is used to access openweathermap.org
cities - list of city objects
name - name of the checked city
id -  id of the checked city. ID is used to find correct city. Other IDs can be found in info/city_list.txt.
temp_low, temp_high, wind_high - limits for weather conditions. When exeeded, alarm is triggered.
```

Response is analysed and written to logs/monitoring.log file. If any severe weather condition noticed, corresponding alert is appended to the record in log. 

For each city in list, if some weather alert is present for checked period (5 days), it will show up as HTML page on localhost:8080.



### Limitations

Fetching data for one city requires one api request. Maximum 60 requests per minute are allowed. So don't add too many cities for monitoring.

