

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;


public class Base64Utils {

    /**
     * 文件转为Base64
     *
     * @param filePath
     * @return
     */
    public static String filePath2ToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return encoder(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Base64转为文件
     *
     * @param base64
     * @param filePath
     * @return
     */
    public static String base642FilePath(String base64, String filePath) {
        if (base64 == null && filePath == null) {
            return "生成文件失败，请给出相应的数据。";
        }
        try {
            Files.write(Paths.get(filePath), decode(base64), StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "指定路径下生成文件成功！";
    }

    /**
     * byte[]转base64
     *
     * @param b
     * @return
     */
    public static String encoder(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }


    /**
     * base64字符串转byte[]
     *
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str) {
        return Base64.getDecoder().decode(base64Str);
    }

}
