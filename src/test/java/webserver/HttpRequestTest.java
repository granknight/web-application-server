package webserver;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by granknight on 2016. 7. 4..
 */
public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void readHeaderTest() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));

        HttpRequest request = new HttpRequest(in);

        assertEquals(HttpMethod.GET.toString(), request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("granknight", request.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest request = new HttpRequest(in);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParameter("userId"));
    }


}