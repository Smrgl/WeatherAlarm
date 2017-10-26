package main.java.monitoring;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;

public class JsonRequestService {

    public JsonObject requestJsonObject(InputStream input) {

        JsonObject jsonObject;
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(input));

        StringBuilder sJson = new StringBuilder();
        String inputLine;
        try {
            while ((inputLine = bufferedReader.readLine()) != null)
                sJson.append(inputLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonReader jsonReader = Json.createReader(new StringReader(sJson.toString()));
        jsonObject = jsonReader.readObject();

        return jsonObject;
    }
}
