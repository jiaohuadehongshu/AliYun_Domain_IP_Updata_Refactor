import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Read_Json {
    public ObjectMapper objectMapper = new ObjectMapper();
    private String currentPath;     //当前jar包所在路径

    public Read_Json() {
        currentPath = Read_Json.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        /*
            获取Java程序运行时当前类所在目录
            ReadJson.class：指向类 ReadJson 的 Class 对象，用于访问类的元数据
            getProtectionDomain()：获取该类的保护域信息（ProtectionDomain），这个信息包括了类的代码来源
            getCodeSource()：返回 CodeSource 对象，它包含类的实际源路径（即类文件或 JAR 文件的路径）
            getLocation()：返回类的 URL 表示，通常是 JAR 文件或类文件的路径
            toURI()：将 URL 转换为 URI，以便可以更方便地进行路径操作
            //        currentPath = new File(ReadJson.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            new File(...).getParent()：创建一个 File 对象表示该路径的父目录，然后调用 getParent() 获取父目录的路径
         */
    }

    public Object readJsonIPs(String JsonName,Class<?> divination){
        try {
            return objectMapper.readValue(new File(currentPath+"\\"+JsonName),divination);
        } catch (IOException e) {
            System.out.println("文件读取失败，目录：'"+currentPath+"'中没有该文件"+JsonName+"！！！\n程序结束");
            System.exit(1);
            return null;
        }
    }

}
