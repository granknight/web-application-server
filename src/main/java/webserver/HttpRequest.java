package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by granknight on 2016. 7. 4..
 */
public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);


    private Map<String, String> header = new HashMap<>();

    private Map<String, String> param;

    public String method;

    public String path;

    public String body;
    public String params;

    public HttpRequest(InputStream in) {

        BufferedReader br = null;
        String line = null;
        int contentLength = 0;
        boolean logined = false;

        try {
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            line = br.readLine();
            if (line == null) {
                return;
            }

            String[] tokens = line.split(" ");

            this.method = tokens[0];

//            log.debug("method : " + method);

            String requestUrl = tokens[1];

//            log.debug("requestUrl : " + requestUrl);

            int index = requestUrl.indexOf('?');

            this.params = requestUrl.substring(index + 1);

            this.param = HttpRequestUtils.parseQueryString(this.params);

            this.path = requestUrl.substring(0, index);

//            log.debug("path : " + path);

            line = br.readLine();

            while (!line.equals("")) {

//                log.debug("header : {}", line);
                String[] headerLineArray = line.split(":");

                header.put(headerLineArray[0], headerLineArray[1].trim());

                line = br.readLine();

                if(line == null){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doGet() {

    }

    public void doPost() {

    }


    public String getHeader(String key) {
        return this.header.get(key);
    }

    public String getParameter(String key) {
        return this.param.get(key);
    }


    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }


    public String getPath() {
        return path;
    }
}
