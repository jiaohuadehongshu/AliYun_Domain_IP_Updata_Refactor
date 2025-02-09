<h3>目录</h3>
<p>
本程序的功能是用于给普通用户在使用家庭宽带时同步程序所在计算机的<code style="font-size:40px;">IPv6地址</code>到阿里云服务器DNS。</br>
下面是使用时的注意事项：</br>
1、程序在写的时候Java版本为22.0.2</br>
2、在简单的修改Json文件后即可轻松的使用该程序，将本地公网的IPv6地址同步至阿里云的DNS中</br>
3、如果在阿里云没有购买dns服务则emmmmmm不行</br>
4、很重要的一点，现在这个代码在打包成jar包后是无法运行的，原因是由于用maven解决了依赖引入的其他的jar包，其他的jar包带签名文件，这将导致后续签名验证失败导致，需要删除jar包里面 /META-INF目录下的BC20248KE.SF文件，不然会一直报错无法运行。</br>
5、AliYun_Domain_IP_Updata.json 这个文件一定要注意，以防万一这里解释一下</br>
&emsp;&emsp;&emsp;NetworkInterfaceName：网卡名称，程序会去读取这些网卡的地址信息，需要注意的是要填入真实网卡名称，真实网卡名称在第一次运行程序时获得，在有多个子域名的情况下需填多个网卡名称。</br>
&emsp;&emsp;&emsp;RefreshTime：刷新时间，每隔一段时间会重写去获取网卡的IP地址信息，并同步到阿里云上，这里单位是毫秒，填数字即可。</br>
&emsp;&emsp;&emsp;KEY：ID和SECRET都是从阿里云获取。</br>
&emsp;&emsp;&emsp;DOMAIN_NAME：主域名：举个例子 www.bilibili.com 中的 bilibili.com。
&emsp;&emsp;&emsp;Sub_Domain_List：子域名：用上面这个例子就是 www。
</p>
