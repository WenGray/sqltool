package xin.ryven.sqltool.tool;

import xin.ryven.sqltool.bean.TableProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 自动生成Mybatis文件，sql内容
 *
 * @author Administrator
 */
public class MyBatisGenerator {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("请配置文件地址");
        }
        //设置对象的java文件
        String filepath = args[0];
        if (!new File(filepath).exists()) {
            throw new RuntimeException("文件不存在");
        }
        //读取文件
        String content = readFile(filepath);
        TableProperties properties = getTableProperties(content);
    }

    /**
     * 从private 属性中获取
     * @param content
     * @return
     */
    private static TableProperties getTableProperties(String content) {
        return null;
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

    private static void generateInsertWithAll(Object object) {

    }

}
