package xin.ryven.sqltool.tool;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Gray
 */
public class FileUtils {

    public static String readResourceFile(String filename) {
        URL resource = FileUtils.class.getClassLoader().getResource(filename);
        if (resource == null) {
            throw new RuntimeException("文件不存在");
        }
        return readFile(resource.getFile());
    }

    public static void writeToFile(String folder, String filename, String msg) {
        if (folder == null) {
            //默认使用用户的根目录
            folder = System.getProperty("user.home") + File.separator + ".xml_temp";
        }
        File folderFile = new File(folder);
        if (folderFile.exists() || folderFile.mkdirs()) {
            File newFile = new File(folder, filename);
            BufferedWriter bufferedWriter = null;
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "utf-8"));
                bufferedWriter.write(msg);
                bufferedWriter.flush();
                //window打开文件夹
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    WindowsTool.openDirectory(folder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static String readFile(String filepath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filepath));
            FileChannel fc = fis.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
            int length;
            StringBuilder builder = new StringBuilder();
            while ((length = fc.read(byteBuffer)) != -1) {
                byteBuffer.clear();
                byte[] now = new byte[length];
                System.arraycopy(byteBuffer.array(), 0, now, 0, length);
                builder.append(new String(now));
            }
            fc.close();
            return builder.toString();
        } catch (IOException e) {
            System.out.println("error -> " + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
