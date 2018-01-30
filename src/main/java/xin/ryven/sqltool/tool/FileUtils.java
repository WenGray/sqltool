package xin.ryven.sqltool.tool;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Gray
 */
public class FileUtils {

    public static String readResourceFile(String filename) {
        InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(filename);
        if (resourceAsStream == null) {
            throw new RuntimeException("文件不存在");
        }
        return readStream(resourceAsStream);

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

    private static String readFile(FileInputStream fis) {
        try {
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

    private static String readStream(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder b = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                b.append(line).append("\n");
            }
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
