import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkAdapter {
    //判断是不是IPv4/6地址
    //正则表达式
    public static boolean isIPv4(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}" + "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";
        return ip.matches(ipv4Pattern);
    }

    public static boolean isIPv4(InetAddress address){
        return address instanceof Inet4Address;
    }

    public static boolean isIPv6(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        String ipv6Pattern = "([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|"
                + "([0-9a-fA-F]{1,4}:){1,7}:|"
                + "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|"
                + "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|"
                + "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|"
                + "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|"
                + "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|"
                + "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|"
                + ":((:[0-9a-fA-F]{1,4}){1,7}|:)|"
                + "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|"
                + "::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|"
                + "(2[0-4][0-9])|(1[0-9][0-9])|[1-9]?[0-9])\\.){3,3}"
                + "(25[0-5]|(2[0-4][0-9])|(1[0-9][0-9])|[1-9]?[0-9])|"
                + "([0-9a-fA-F]{1,4}:){1,4}:"
                + "((25[0-5]|(2[0-4][0-9])|(1[0-9][0-9])|"
                + "[1-9]?[0-9])\\.){3,3}(25[0-5]|"
                + "(2[0-4][0-9])|(1[0-9][0-9])|[1-9]?[0-9])";

        return ip.matches(ipv6Pattern);
    }

    public static boolean isIPv6(InetAddress address){
        return address instanceof Inet6Address;
    }

    //返回IP版本
    public static int return_IP_Version(String address){
        if (address==null||address.isEmpty()){
            System.out.println("checkAddress调用的address地址有误");
            return 0;
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            if (inetAddress instanceof Inet4Address){
                return 4;
            }
            if (inetAddress instanceof Inet6Address){
                return 6;
            }
        } catch (UnknownHostException e) {
            System.out.println("checkAddress调用的address地址有误");
        }
        System.out.println("checkAddress调用的address地址有误");
        return 0;
    }

    //返回IP版本
    public static int return_IP_Version(InetAddress inetAddress){
        switch (inetAddress) {
            case Inet4Address inet4Address -> {
                return 4;
            }
            case Inet6Address inet6Address -> {
                return 6;
            }
            default -> {
                System.out.println("checkAddress调用的address地址有误");
                return 0;
            }
        }
    }

    //判断是不是局域网IPv4地址
    public static boolean isLocal4(String ip) {
        if (!isIPv4(ip)){
            return false;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            byte[] addressBytes = inetAddress.getAddress();

            int firstOctet = Byte.toUnsignedInt(addressBytes[0]);
            int secondOctet = Byte.toUnsignedInt(addressBytes[1]);

            // 检查私网地址范围
            // 10.0.0.0 - 10.255.255.255
            if (firstOctet == 10) {
                return true;
            }
            // 172.16.0.0 - 172.31.255.255
            if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) {
                return true;
            }
            // 192.168.0.0 - 192.168.255.255
            if (firstOctet == 192 && secondOctet == 168) {
                return true;
            }

        } catch (UnknownHostException e) {
            System.out.println("判断IPv4时发生异常");
        }

        return false;
    }

    public static boolean isLocal4(InetAddress inetAddress) {
        if (inetAddress==null||!isIPv4(inetAddress)){
            return false;
        }

        byte[] addressBytes = inetAddress.getAddress();

        int firstOctet = Byte.toUnsignedInt(addressBytes[0]);
        int secondOctet = Byte.toUnsignedInt(addressBytes[1]);

        // 检查私网地址范围
        // 10.0.0.0 - 10.255.255.255
        if (firstOctet == 10) {
            return true;
        }
        // 172.16.0.0 - 172.31.255.255
        if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) {
            return true;
        }
        // 192.168.0.0 - 192.168.255.255
        if (firstOctet == 192 && secondOctet == 168) {
            return true;
        }

        return false;
    }

    //判断是不是局域网IPv6地址
    public static boolean isLocal6(String ip) {
        if (!isIPv6(ip)){
            return false;
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            byte[] addressBytes = inetAddress.getAddress();

            // 检查链路本地地址 (fe80::/10)
            if ((addressBytes[0] & 0xff) == 0xfe && (addressBytes[1] & 0xc0) == 0x80) {
                return true;
            }

            // 检查唯一本地地址 (fc00::/7)
            if ((addressBytes[0] & 0xfe) == 0xfc) {
                return true;
            }

        } catch (UnknownHostException e) {
            System.out.println("判断IPv6时发生异常");
        }
        return false;
    }

    public static boolean isLocal6(InetAddress inetAddress) {
        if (inetAddress==null||!isIPv6(inetAddress)){
            return false;
        }

        byte[] addressBytes = inetAddress.getAddress();

        // 检查链路本地地址 (fe80::/10)
        if ((addressBytes[0] & 0xff) == 0xfe && (addressBytes[1] & 0xc0) == 0x80) {
            return true;
        }

        // 检查唯一本地地址 (fc00::/7)
        if ((addressBytes[0] & 0xfe) == 0xfc) {
            return true;
        }

        return false;
    }

    //获取网卡的全部IP地址
    public static List<String> getNetworkAdapter_All_IP(String adapter){
        NetworkInterface networkInterface = null;
        List<String> list = new ArrayList<>();
        try {
            networkInterface = NetworkInterface.getByName(adapter);
        } catch (SocketException e) {
            System.out.println("网卡获取失败");
            throw new RuntimeException(e);
        }
        for (InterfaceAddress ip:networkInterface.getInterfaceAddresses()){
            list.add(ip.getAddress().getHostAddress());
        }
        System.out.println(list);
        return list;
    }

    public static List<InetAddress> getNetworkAdapter_All_IP(NetworkInterface adapter){
        if (adapter==null){
            System.out.println("传入的网卡为空");
            return null;
        }
        List<InetAddress> ips = new ArrayList<>();
        Enumeration<InetAddress> list = adapter.getInetAddresses();
        while (list.hasMoreElements()){
            ips.add(list.nextElement());
        }
        return ips;
    }

}
