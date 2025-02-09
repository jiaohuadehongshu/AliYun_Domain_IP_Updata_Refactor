import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

import java.util.List;

public class AliYun_Json_Data {
    public String ACCESS_KEY_ID;   //阿里云KEY ID
    public String ACCESS_KEY_SECRET;   //阿里云KEY 密码
    public String DOMAIN_NAME;     //阿里云主域名名称
    public List<String> Sub_Domain_List;   //阿里云子域名名称
    public List<String> NetworkInterfaceName;   //网络接口名称
    public int RefreshTime;

    public boolean correct(){
        if (ACCESS_KEY_ID.isEmpty()){
            System.out.println("阿里云KEY ID 未填写");
        }else if (ACCESS_KEY_SECRET.isEmpty()){
            System.out.println("阿里云KEY 密码 未填写");
        } else if (DOMAIN_NAME.isEmpty()) {
            System.out.println("DNS主域名名称 未填写");
        }else if (Sub_Domain_List.isEmpty()){
            System.out.println("DNS子域名名称 未填写");
        } else if (NetworkInterfaceName.isEmpty()) {
            System.out.println("网卡名称未填写");
        }else if (RefreshTime==0){
            System.out.println("刷新时间未填写");
        } else if (Sub_Domain_List.size()!=NetworkInterfaceName.size()) {
            if (Sub_Domain_List.size()>NetworkInterfaceName.size()){
                System.out.println("子域名数量>网卡列表数量，请检查");
            }else System.out.println("子域名数量<网卡列表数量，请检查");
        } else return true;
        return false;
    }

    public String getRecordId(String sub_Domain) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        // 创建查询请求
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(DOMAIN_NAME);

        // 发送请求
        DescribeDomainRecordsResponse response = client.getAcsResponse(request);
        List<DescribeDomainRecordsResponse.Record> records = response.getDomainRecords();
        for (DescribeDomainRecordsResponse.Record record : records) {
            if (record.getRR().equals(sub_Domain) && record.getType().equals("AAAA")) {
                return record.getRecordId();
            }
        }
        return null;
    }

    //更新DNS
    public boolean updateDNSRecord(String ipv6Address, String RECORD_ID, String sub_Domain) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        // 创建更新请求
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();    //构建一个用于更新域名记录的请求对象
        request.setRecordId(RECORD_ID); //设置要更新的记录的 ID
        request.setRR(sub_Domain);  //设置要更新的子域名
        request.setType("AAAA");    //设置记录类型为 AAAA，即修改 IPv6 地址的解析记录
        request.setValue(ipv6Address);
        request.setTTL(600L); // 设置记录的生存时间（TTL），单位是秒，600秒即 10 分钟

        try {
            // 发送请求
            UpdateDomainRecordResponse response = client.getAcsResponse(request);   //将更新请求发送给阿里云并获取响应。这个响应包含了 API 请求的结果。
            System.out.println("Update Response: " + response.getRequestId());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
