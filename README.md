# fdfs-client
java版fastdfs客户端操作工具
此项目代码已通过工具库方式投入正式商业环境使用(2017-03-31)
已更新至2.0.0(2017-04-21)
更新日志详见CHANGELOG.md

>#### 基于[chulung同学的版本](https://github.com/chulung/FastDFS_Client)
>chulung同学的版本现已不存在
>估计是因为其个人网站从springmvc更换为springboot的原因直接使用了[tobato的spring-boot版](https://github.com/tobato/FastDFS_Client)
>chulung同学的版本是当前项目的基板,所以单独切出分支保留.[点此查看](https://github.com/sudot/fdfs-client/tree/FastDFS_Client_chulung)

## 主要特性

>1. 修改了所有包名
>2. 取消对spring的依赖,默认的使用方式是通过单例实现
>3. 取消了对commons-pool2和slf4j以外的所以依赖
>4. 取消了图片缩略图处理,但在单元测试中有使用JDK原生API实现的对应功能.详见[net.sudot.fdfs.proto.storage.StorageUploadSlaveFileCommandTest#testStorageSlaveUploadFileCommand](https://github.com/sudot/fdfs-client/blob/master/src/test/java/net/sudot/fdfs/proto/storage/StorageUploadSlaveFileCommandTest.java)
>5. 修复modifyFile,truncateFile,downloadFile含偏移位置和操作文件大小的重载函数无法正常使用
>6. 常用的连接池配置抽出通过外置文件配置(后续会将有必要的所有参数都抽取出来)
>7. 修复连接池在运行时出现突然连接不上的情况
>8. 解决连接在遇到服务器异常之后将此连接关闭导致此连接失效.(解决办法是当出现此类异常,检查连接情况并根据检查结果销毁连接)
>9. Storage操作的所有接口全部移动到StorageClient类中,并增加了一些操作接口

## 运行环境要求

JDK环境要求  1.7+

## 单元测试

单元测试需依赖FastDFS服务端
执行单元测试需配置fastdfs.properties文件当中参数
>(fdfs-client/src/test/resources/fastdfs.properties)
>
在Tracker与Storage都在一个机器的环境下
```java
fdfs.test.trackerHost=127.0.0.1
fdfs.test.trackerPort=23100
fdfs.test.storageHost=127.0.0.1
fdfs.test.storagePort=23200
```
在Tracker与Storage不在一个机器的环境下
```java
fdfs.test.trackerHost=127.0.0.1
fdfs.test.trackerPort=23100
fdfs.test.storageHost=127.0.0.2
fdfs.test.storagePort=23200
```


## FastDFS-Client使用方式

### 1.在项目Pom当中加入依赖

Maven依赖为
```xml
    <dependency>
        <groupId>net.sudot.fdfs</groupId>
        <artifactId>fdfs-client</artifactId>
        <version>2.0.0</version>
    </dependency>
```


### 2.将Fdfs配置引入项目
使用DefaultFdfsOptionsFactory工厂方法获得所有操作接口
```java
public static final FdfsOptionsFactory FDFS_OPTIONS_FACTORY = new DefaultFdfsOptionsFactory();
protected TrackerClient trackerClient = FDFS_OPTIONS_FACTORY.getTrackerClient();
protected StorageClient storageClient = FDFS_OPTIONS_FACTORY.getStorageClient();
protected ConnectionManager storageConnectionManager = FDFS_OPTIONS_FACTORY.getStorageConnectionManager();
protected TrackerConnectionManager trackerConnectionManager = (TrackerConnectionManager) FDFS_OPTIONS_FACTORY.getTrackerConnectionManager();
```

### 3.在fastdfs.properties当中配置相关参数
可参考fdfs-client/src/main/resources/fastdfs.properties.template
```shell
# =========== #
# fastdfs配置 #
# =========== #

#host:port列表.分隔符(英文标点下的):空格,逗号,分号,换行符,制表符
fdfs.trackerList=192.168.11.234:23100
#文件读取超时时长(单位:毫秒)
fdfs.soTimeout=3000
#请求连接超时时长(单位:毫秒)
fdfs.connectTimeout=5000
#字符编码
fdfs.charsetName=UTF-8

#连接池设置
fdfs.pool.maxTotal=2
#设置为true时,池中无可用连接,borrow时进行阻塞;
#为false时,当池中无可用连接,抛出NoSuchElementException异常
fdfs.pool.blockWhenExhausted=true
#最大等待时间(单位:毫秒),当 blockWhenExhausted配置为 true 时,此值有效.
#当需要borrow一个连接时,最大的等待时间,如果超出时间,抛出NoSuchElementException异常,
#-1为不限制时间
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
2. StorageClient - 为方便项目开发集成的简单接口 (StorageServer接口)
