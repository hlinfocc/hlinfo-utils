# hlinfo-utils是什么？

hlinfo-utils是一些常用的java小工具集合。

# 特点

小巧玲珑，静态封装，快速使用


# 快速使用

### 1.引入pom:

>请前往Maven中央库查找最新版本

``` xml
<dependency>
    <groupId>net.hlinfo</groupId>
    <artifactId>hlinfo-utils</artifactId>
    <version>1.0.5</version>
</dependency>
```


### 2.如何使用？

* Func:静态方法类集合，通过**Func.方法名**直接使用
* Jackson：Jackson常用操作方法，均为静态方法可直接使用，通过**Jackson.方法名**使用（非Spring Boot应用需要引入Jackson依赖）
* HashUtils:封装常用的hash散列杂凑算法，有SM3、sha3等（需要引入依赖org.bouncycastle:bcprov-jdk15to18）
* AESUtils：AES加密/解密工具，含微信平台(小程序/开放平台)加密数据解密（微信平台(小程序/开放平台)加密数据解密需要引入依赖org.bouncycastle:bcprov-jdk15to18）

AESUtils使用示例：

```
//微信平台(小程序/开放平台)加密数据解密
String encryptedData = "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZMQmRzooG2xrDcvSnxIMXFufNstNGTyaGS9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+3hVbJSRgv+4lGOETKUQz6OYStslQ142dNCuabNPGBzlooOmB231qMM85d2/fV6ChevvXvQP8Hkue1poOFtnEtpyxVLW1zAo6/1Xx1COxFvrc2d7UL/lmHInNlxuacJXwu0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn/Hz7saL8xz+W//FRAUid1OksQaQx4CMs8LOddcQhULW4ucetDf96JcR3g0gfRK4PC7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns/8wR2SiRS7MNACwTyrGvt9ts8p12PKFdlqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYVoKlaRv85IfVunYzO0IKXsyl7JCUjCpoG20f0a04COwfneQAGGwd5oa+T8yO5hzuyDb/XcxxmK01EpqOyuxINew==";
String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
String iv = "r7BXXKkLb8qrSNn05n0qiA==";
System.out.println("微信数据解密："+AESUtils.wxDecrypt(encryptedData, sessionKey, iv));
//普通 加密/解密
String key = getKey();
System.out.println("密钥:"+key);
String encData = encrypt("Hello Word", key);
System.out.println("加密密文:"+encData);
System.out.println("解密原文："+decrypt(encData, key));
```

* FfmpegUtils：视频编码获取及转码工具(需要安装ffmpeg及非Spring Boot应用引入Jackson依赖)

FfmpegUtils使用示例：

```java
 FfmpegUtils ffmpegUtils = new FfmpegUtils();
 //获取编码信息：
 ffmpegUtils.initEncodingFormat("/opt/test.mp4");
 System.out.println(ffmpegUtils.getFormatParams().getVideoCodecLongName());
 System.out.println(ffmpegUtils.getFormatParams().getAudioCodecLongName());
 System.out.println(ffmpegUtils.getFormatParams().isMP4H264());
 //转码：
 ffmpegUtils.transcodeH264("/opt/test.mp4", "/opt/out-test.mp4");
```

引入需要的依赖org.bouncycastle:bcprov-jdk15to18（请前往Maven中央库搜索，查找对应JDK的最新版本。）：

``` xml
<dependency>
	<groupId>org.bouncycastle</groupId>
	<artifactId>bcprov-jdk15to18</artifactId>
	<version>1.70</version>
</dependency>
```

非Spring Boot应用引入需要的Jackson依赖

``` xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.13.1</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-annotations</artifactId>
  <version>2.13.1</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.13.1</version>
</dependency>
```

# 参考资料
* Nutz(部分注解)
* 其他网络资源

# 许可证
MIT License 
