import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFileWrite {
    private String currentPath;
    private String logFileName;

    public LogFileWrite() {
        currentPath = new File(Read_Json.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        //为什么要加上new File().getParent() 原因是现在获取的是class文件的路径，但是当打包成jar包后就变成获取jar包的路径了，会带上文件名称，而不是单纯的路径信息了
    }

    public String getTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getTimeFileName(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss"));
    }

    public void write_log(String log) throws IOException {
        boolean judge = true;
        if (logFileName==null||logFileName.isEmpty()){
            judge = false;
            logFileName = getTimeFileName()+" AliYun_Domain_Update.log";
        }

        File file = new File(currentPath+"\\"+logFileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

        if (judge) writer.newLine();

        writer.write(getTime()+"  :  "+log);
        writer.close();
    }

}
