package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		//log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		//log.debug(connection.getInetAddress().getAddress().toString());
		//log.debug(connection.getInetAddress().getHostName());



		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

			String header = getHeaderString(in);
			if (header == null) return;

			log.debug("HEADER : " + header);

			String[] headerElement = header.split("\r\n");
			String contentType = "";
			for(String element : headerElement){
				if(element.contains("Accept")){
					contentType = element.split(":")[1].split(",")[0].trim();
					break;
				}
			}

			String url = headerElement[0].split(" ")[1];

			//String file = url.replace("/", "");

			String getParam = "";
			//log.debug("Accept contentType : "+ contentType);
			//log.debug("url  : " + url);
			//log.debug("fileName : " + file);

			if(url.contains("?")){
				String[] urlArray = url.split(":?");

				url = urlArray[0];
				getParam = urlArray[1];

			}

			File targetFile = new File("./webapp" + url);

			//String fileName = targetFile.getName();

			byte[] fileBytes = Files.readAllBytes(targetFile.toPath());
			//if(!fileName.equals("favicon.ico")) {
			DataOutputStream dos = new DataOutputStream(out);


			//byte[] body = "Hello World".getBytes();
			response200Header(dos, targetFile.length(), contentType);
			responseBody(dos, fileBytes);
			//}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private String getHeaderString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String header = "\r\n";
		String line = reader.readLine();
		while(!"".equals(line)){
            if(line == null){
				return null;
            }

            header += line;
            header +="\r\n";
            line = reader.readLine();
        }
		return header;
	}

	private void response200Header(DataOutputStream dos, long lengthOfBodyContent,String contentType) {
		try {
			//log.debug(contentType);
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			if(contentType.equals("text/html")) {
				dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			}else if(contentType.contains("css")){
				dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			}else if(contentType.contains("js")){
				dos.writeBytes("Content-Type: text/javascript;charset=utf-8\r\n");
			}
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			//log.debug(String.valueOf(body.length));
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
