## 2.0.0 (2017-04-20)

Features:

  - 脱离Spring
  - 剔除出slf4j和commons-pool以外的第三方依赖
  - 修复原作者modify,truncate,download部分接口无法正常使用
  - 增加断点续传接口支持同时设置元数据
  - 优化命令执行效率和容错率
  - 增加命令执行部分异常导致连接失效后对连接销毁处理
  - 整理接口注释

## 1.1.0 (2017-03-31)

Features:

  - 增加不同地址路径参数的文件操作方法.包括上传、下载、删除等
  - 调整连接池参考超时设置,soTime(连接成功后数据读取或发送时长):3000毫秒,connectTimeout(整个socket的连接超时时长):5000毫秒
  - 调整连接池默认最大连接为20

## 1.1.1 (2017-04-01)

Features:

  - 连接池配置外置

## 1.1.0 (2017-03-31)

Features:

  - 增加不同地址路径参数的文件操作方法.包括上传、下载、删除等
  - 调整连接池参考超时设置,soTime(连接成功后数据读取或发送时长):3000毫秒,connectTimeout(整个socket的连接超时时长):5000毫秒
  - 调整连接池默认最大连接为20

## 1.0.1 (2017-03-13)

Bugfixes:

  - 修正ObjectMateData.praseFieldList解析字段顺序和FdfsColumn.indeex保持顺序一致性

#以下是前作者更新日志:

## 1.0.0 (2016-09-01)

Features:

  - 优化无法连接后端服务器时候的错误输出，输出连接配置地址

Bugfixes:

  - 修正打包时候将application.yml打包的问题 (#1,@wmz7year)
  - 修正设置MetaData错误 (#5, @yuck0419)
