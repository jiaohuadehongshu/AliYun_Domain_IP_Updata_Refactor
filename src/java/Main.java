import com.aliyuncs.exceptions.ClientException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException, ClientException {
        System.out.println("嗨~~~~我是程序员-jiaohuadehongshu-，本程序的功能是用于给普通用户在使用家庭宽带时同步程序所在计算机的IPv6地址到阿里云服务器DNS上");
        System.out.println("本程序已在git hub上进行开源，有不懂的可以去瞧瞧：https://github.com/jiaohuadehongshu/AliYun_Domain_IP_Updata_Refactor");
        System.out.println("写这个程序的目的是为了更好的让大家在21世纪享受到IPv6普及所带来的便利，为IPv6的推广献上自己的一点点绵薄之力，嘿嘿嘿 OvO");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("下面将打印全部具有网卡信息，如果您是第一次使用，需注意该步骤");
        System.out.println("------------------------------------------------------------------------");
        printNetworkAdapterIPv6();
        System.out.println("\n网卡昵称：相当于网卡的外号，好记，但不是真名，在填写json文件时请填入网卡真名");
        System.out.println("\n程序准备读取Json文件，如果你是第一次运行，尚未填写Json文件，稍后程序将会终止");

        LogFileWrite logFileWrite = new LogFileWrite();
        try {
            logFileWrite.write_log("程序开始运行，开始读取Json文件");
        }catch (Exception e){
            System.out.println("日志写入失败？？？？？");
        }


        //二、读取json文件参数
        AliYun_Json_Data aliYunJsonData = (AliYun_Json_Data) new Read_Json().readJsonIPs("AliYun_Domain_IP_Updata.json", AliYun_Json_Data.class);
        //检查json文件是否有错误内容
        if (!aliYunJsonData.correct()){
            System.out.println("程序结束");
        }

        logFileWrite.write_log("程序读取Json文件成功");

        boolean tem = true;     //控制将第一次获取的IP写入日志中

        do{
            //创建栈，将该网卡下的IPv6地址丢到栈里面
            Map<String,Stack<String>> networkAddressList = new HashMap<>();

            //填充IPv6地址
            for (String nin:aliYunJsonData.NetworkInterfaceName){
                NetworkInterface networkInterface = NetworkInterface.getByName(nin);
                if (networkInterface==null){
                    System.out.println("没有找到该-"+nin+"-网卡，请检查网卡名称是否填写正确");
                    logFileWrite.write_log("没有找到该-"+nin+"-网卡，请检查网卡名称是否填写正确");
                    Thread.sleep(200000);
                }else {
                    if (!networkAddressList.isEmpty())
                        if(!(networkAddressList.get(nin)==null))
                            if (!networkAddressList.get(nin).isEmpty())
                                break;
                    Enumeration<InetAddress> ips = networkInterface.getInetAddresses();
                    Stack<String> ip = new Stack<>();
                    while (ips.hasMoreElements()){
                        InetAddress inetAddress = ips.nextElement();
                        if (NetworkAdapter.isIPv6(inetAddress)&&!NetworkAdapter.isLocal6(inetAddress)){
                            if (tem) {
                                System.out.println("网卡："+nin+" 地址："+inetAddress.getHostAddress());
                                logFileWrite.write_log("网卡：" + nin + " 地址：" + inetAddress.getHostAddress());
                            }
                            ip.add(inetAddress.getHostAddress());
                        }
                    }
                    if (!ip.isEmpty()){
                        if (tem) {
                            System.out.println("\n成功获取网卡 " + nin + " 的IPv6地址\n");
                        }
                        networkAddressList.put(nin,ip);
                    }
                }
            }

            for (int i=0; i<aliYunJsonData.Sub_Domain_List.size(); ++i){
                if (networkAddressList.get(aliYunJsonData.NetworkInterfaceName.get(i))==null||networkAddressList.get(aliYunJsonData.NetworkInterfaceName.get(i)).isEmpty())
                    break;
                String RECORD_ID = aliYunJsonData.getRecordId(aliYunJsonData.Sub_Domain_List.get(i));
                String temIPv6 = networkAddressList.get(aliYunJsonData.NetworkInterfaceName.get(i)).peek();
                if (aliYunJsonData.updateDNSRecord(networkAddressList.get(aliYunJsonData.NetworkInterfaceName.get(i)).pop(),RECORD_ID,aliYunJsonData.Sub_Domain_List.get(i))){
                    logFileWrite.write_log("-- 更新 -- 域名："+aliYunJsonData.Sub_Domain_List.get(i)+"."+aliYunJsonData.DOMAIN_NAME+" IPv6地址:"+temIPv6);
                    System.out.println("--成功更新-- 域名："+aliYunJsonData.Sub_Domain_List.get(i)+"."+aliYunJsonData.DOMAIN_NAME+" IPv6地址:"+temIPv6);
                }
            }
            tem = false;
            Thread.sleep(aliYunJsonData.RefreshTime);
        }while (true);
    }


    public static void printNetworkAdapterIPv6() throws SocketException {
        //打印网卡中有IPv6地址的网卡信息
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            System.out.println("网卡昵称："+networkInterface.getDisplayName());
            System.out.println("网卡真名："+networkInterface.getName());

            List<InterfaceAddress> addressList = networkInterface.getInterfaceAddresses();
            if (!addressList.isEmpty()){
                for (InterfaceAddress address:addressList){
                    System.out.println("  地址: " + address.getAddress());
                    System.out.println("  广播地址: " + address.getBroadcast());
                    System.out.println("  子网掩码长度: " + address.getNetworkPrefixLength());
                }
            }
            System.out.println("------------------------");
        }
    }
}
