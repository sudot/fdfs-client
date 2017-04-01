# fdfs-client
java版fastdfs客户端操作工具

此项目代码已通过工具库方式投入正式商业环境使用(2017-03-31)
更新日志详见CHANGELOG.md

主要特性

>1. 增加不同地址对文件的操作.包括文件上传、下载、删除
>2. 增加不同地址路径参数的文件操作方法.包括上传、下载、删除等
>3. 调整socket连接超时参考设置,soTime(连接成功后数据读取或发送时长):3000毫秒,connectTimeout(整个socket的连接超时时长):5000毫秒
>4. 连接池设置配置处理


## 基于[chulung同学的版本](https://github.com/chulung/FastDFS_Client)
此项目现已不存在,估计是因为其个人网站从springmvc更换为springboot的原因
根据项目环境针对调整
>This is a java client lib for [FastDFS](https://github.com/happyfish100/fastdfs).
>
>这个版本是我重构了tobato同学的版本，将他使用的spring-boot框架转化为普通spring框架以适应个人项目需要
>配置文件方式和部分注解做了修改
> 
>[tobato的spring-boot版](https://github.com/tobato/FastDFS_Client)
> 
> 
>主要特性
>
>>1. 对关键部分代码加入了单元测试，便于理解与服务端的接口交易，提高接口质量
>>2. 将以前对byte硬解析风格重构为使用 对象+注解 的形式，尽量增强了代码的可读性
>>3. 支持对服务端的连接池管理(commons-pool2)
>>4. 支持上传图片时候检查图片格式，并且自动生成缩略图

## 运行环境要求

Spirng4
    
JDK环境要求  1.7+

## 单元测试

由于工作时间关系与解析原代码的复杂性，单元测试无法完全做到脱离FastDFS服务端，请见谅。

执行单元测试需要配置TestConstants文件当中参数

在Tracker与Storage都在一个机器的环境下
```java
    private static String ip_work = "192.168.1.101"; // Tracker Host
    private static String ip_work_store = "192.168.1.101"; // Storage Host
    public static InetSocketAddress address = new InetSocketAddress(ip_work, FdfsMockSocketServer.PORT);
    public static InetSocketAddress store_address = new InetSocketAddress(ip_work_store, FdfsMockSocketServer.STORE_PORT);
```
  
      
在Tracker与Storage不在一个机器的环境下      
```java
    private static String ip_work = "192.168.1.101"; // Tracker Host
    private static String ip_work_store = "192.168.1.102"; // Storage Host
    public static InetSocketAddress address = new InetSocketAddress(ip_work, FdfsMockSocketServer.PORT);
    public static InetSocketAddress store_address = new InetSocketAddress(ip_work_store, FdfsMockSocketServer.STORE_PORT);
```


## FastDFS-Client使用方式

### 1.在项目Pom当中加入依赖

Maven依赖为
```xml
    <dependency>
        <groupId>net.sudot.fdfs</groupId>
        <artifactId>fdfs-client</artifactId>
        <version>1.0.1-SNAPSHOT</version>
    </dependency>
```


### 2.将Fdfs配置引入项目

在spring中配置扫描
```xml
    <context:component-scan base-package="net.sudot.fdfs" />
    <bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <!--fastdfs.properties路径-->
                <value>classpath*:fastdfs.properties</value>
            </list>
        </property>
    </bean>
```

```java
    //注入storageClient类
	
    @Resource
    protected FastFileStorageClient storageClient;
```

### 3.在fastdfs.properties当中配置相关参数
```
    # ===================================================================
    # fastdfs配置
    # ===================================================================

    fdfs.soTimeout=1501
    fdfs.connectTimeout=601 

    #缩略图配置
    fdfs.thumbImage.width=150
    fdfs.thumbImage.height=150

    # ,号隔开的host:port列表
    fdfs.trackerList=192.168.1.133:22122

    #无需配置
    fast.webServerUrl=
    
    #连接池设置
    fdfs.pool.maxTotal=2
    #设置为true时,池中无可用连接,borrow时进行阻塞;为false时,当池中无可用连接,抛出NoSuchElementException异常
    fdfs.pool.blockWhenExhausted=true
    #最大等待时间(单位:毫秒),当 blockWhenExhausted 配置为 true 时,此值有效.当需要borrow一个连接时,最大的等待时间,如果超出时间,抛出NoSuchElementException异常,-1为不限制时间
    fdfs.pool.maxWaitMillis=5000
    #空闲时进行连接测试,会启动异步evict线程进行失效检测
    fdfs.pool.testWhileIdle=true
    #资源最小空闲时间(单位:毫秒),视休眠时间超过的对象为过期.需要testWhileIdle为true
    fdfs.pool.minEvictableIdleTimeMillis=600000
    #回收资源线程的执行周期(单位:毫秒),需要testWhileIdle为true
    fdfs.pool.timeBetweenEvictionRunsMillis=600000
```

### 4.使用接口服务对Fdfs服务端进行操作

主要接口包括

1. TrackerClient - TrackerServer接口 
2. GenerateStorageClient - 一般文件存储接口 (StorageServer接口)
3. FastFileStorageClient - 为方便项目开发集成的简单接口(StorageServer接口)
4. AppendFileStorageClient - 支持文件续传操作的接口 (StorageServer接口)



