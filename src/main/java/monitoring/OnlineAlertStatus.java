package main.java.monitoring;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class OnlineAlertStatus extends AbstractHandler implements Runnable{

    @Override
    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws
            ServletException
    {
        response.setContentType("text/html; charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);

        try {
            response.getWriter()
                    .println("<html><body>\n"+
                            "<h1>Alert monitoring status</h1>\n" +
                            "<p>Showing if there are any weather alerts during next 5 days</p>\n" +
                            "<br>");
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonRequestService jsonRequestService = new JsonRequestService();
        Path alertsPath = Paths.get("./logs/alerts");

        while (!alertsPath.toFile().canRead()){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(alertsPath.toString())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String inputLine;
        try {
            assert bufferedReader != null;
            while ((inputLine = bufferedReader.readLine()) != null){
                JsonObject jo = jsonRequestService.requestJsonObject(new ByteArrayInputStream(inputLine.getBytes(StandardCharsets.UTF_8.name())));

                Iterator<?> keys = jo.keySet().iterator();

                while( keys.hasNext() ) {
                    String key = (String)keys.next();
                    String value = jo.getJsonString(key).getString();
                    response.getWriter().println(String.format("<h2>%s</h2>",value));
                }

                response.getWriter().println("<br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            response.getWriter().println("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }

        baseRequest.setHandled(true);
    }

    @Override
    public void run() {
        Server server = new Server(8080);
        server.setHandler(this);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
