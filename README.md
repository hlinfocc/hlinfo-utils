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
    <version>1.0.2</version>
</dependency>
```


### 2.如何使用？

* Func:静态方法类集合，通过**Func.方法名**直接使用
* Jackson：Jackson常用操作方法，均为静态方法可直接使用，通过**Jackson.方法名**使用（非Spring Boot应用需要引入Jackson依赖）
* HashUtils:封装常用的hash散列杂凑算法，有SM3、sha3等（需要引入依赖org.bouncycastle:bcprov-jdk15to18）
* AESUtils：AES加密/解密工具，含微信平台(小程序/开放平台)加密数据解密（微信平台(小程序/开放平台)加密数据解密需要引入依赖org.bouncycastle:bcprov-jdk15to18）

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
