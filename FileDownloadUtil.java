
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileDownloadUtil {
	
	private FileDownloadUtil() {}

    public static void download(String fileName, byte[] data, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream();){
        	response.setContentType("application/octet-stream");
        	response.setHeader("Content-Length", String.valueOf(data.length)); 
        	response.setHeader("Content-disposition", "attachment; filename="
        			+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        	
			out.write(data);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
    }
	
}
