# fdfs-client
java版fastdfs客户端操作工具

######

基于[chulung同学的版本](https://github.com/chulung/FastDFS_Client),根据项目环境针对调整
此项目现已不存在,估计是因为其个人网站从springmvc更换为springboot的原因

This is a java client lib for [FastDFS](https://github.com/happyfish100/fastdfs).

这个版本是我重构了tobato同学的版本，将他使用的spring-boot框架转化为普通spring框架以适应个人项目需要
配置文件方式和部分注解做了修改

[tobato的spring-boot版](https://github.com/tobato/FastDFS_Client)


主要特性

1. 对关键部分代码加入了单元测试，便于理解与服务端的接口交易，提高接口质量
2. 将以前对byte硬解析风格重构为使用 对象+注解 的形式，尽量增强了代码的可读性
3. 支持对服务端的连接池管理(commons-pool2)
4. 支持上传图片时候检查图片格式，并且自动生成缩略图

##运行环境要求

Spirng4
    
JDK环境要求  1.7+

##单元测试

由于工作时间关系与解析原代码的复杂性，单元测试无法完全做到脱离FastDFS服务端，请见谅。

执行单元测试需要配置TestConstants文件当中参数

在Tracker与Storage都在一个机器的环境下

      private static String ip_home = "192.168.1.105";
      public static InetSocketAddress address = new InetSocketAddress(ip_home, FdfsMockSocketServer.PORT);
      public static InetSocketAddress store_address = new InetSocketAddress(ip_home, FdfsMockSocketServer.STORE_PORT);
      
      public static final String DEFAULT_STORAGE_IP = ip_home;
  
      
在Tracker与Storage不在一个机器的环境下      
     
    private static String ip_work = "192.168.174.47";
    private static String ip_work_store = "192.168.174.49";
    public static InetSocketAddress address = new InetSocketAddress(ip_work, FdfsMockSocketServer.PORT);
    public static InetSocketAddress store_address = new InetSocketAddress(ip_work_store, FdfsMockSocketServer.STORE_PORT);
    
    public static final String DEFAULT_STORAGE_IP = ip_work_store;
   

##FastDFS-Client使用方式

###1.在项目Pom当中加入依赖

Maven依赖为

    <dependency>
        <groupId>net.sudot.fdfs</groupId>
        <artifactId>fdfs-client</artifactId>
        <version>1.0.1-SNAPSHOT</version>
    </dependency>


###2.将Fdfs配置引入项目

在spring中配置扫描

	<context:component-scan base-package="net.sudot.fdfs" />
	<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="locations">
			<list>
				<!--fastdfs.properties路径-->
				<value>classpath*:fastdfs.properties</value>
			</list>
		</property>
	</bean>

	//注入storageClient类
	
	@Autowired
	protected FastFileStorageClient storageClient;
    
  

###3.在fastdfs.properties当中配置相关参数

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

###4.使用接口服务对Fdfs服务端进行操作

主要接口包括

1. TrackerClient - TrackerServer接口 
2. GenerateStorageClient - 一般文件存储接口 (StorageServer接口)
3. FastFileStorageClient - 为方便项目开发集成的简单接口(StorageServer接口)
4. AppendFileStorageClient - 支持文件续传操作的接口 (StorageServer接口)



