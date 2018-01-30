package xin.ryven.sqltool.tool;

import java.io.IOException;

public class WindowsTool {

    public static void openDirectory(String dic) throws IOException {
        String[] cmd = new String[5];
        cmd[0] = "cmd";
        cmd[1] = "/c";
        cmd[2] = "start";
        cmd[3] = " ";
        cmd[4] = dic;
        Runtime.getRuntime().exec(cmd);
    }
}
