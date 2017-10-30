package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.ConnectionManager;
import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadCallback;

import java.io.InputStream;
import java.util.Set;

/**
 * 基本文件存储客户端操作
 *
 * @author tobato
 * @author sudot on 2017-03-17 0017.
 */
public interface StorageClient {

    /**
     * 上传文件(不支持断点续传)
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @return 返回文件全路径
     */
    StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName);

    /**
     * 上传文件(不支持断点续传)
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param groupName   文件需要上传的组
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @return 返回文件全路径
     */
    StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName);

    /**
     * 上传包含元信息的文件(不支持断点续传)
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @param metaDataSet 文件元信息
     * @return 返回文件全路径
     */
    StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet);

    /**
     * 上传包含元信息的文件(不支持断点续传)
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     *
     * @param groupName   文件需要上传的组
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @param metaDataSet 文件元信息
     * @return 返回文件全路径
     */
    StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet);

    /**
     * 上传支持断点续传的文件
     *
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @return 返回文件全路径
     */
    StorePath uploadAppenderFile(InputStream inputStream, long fileSize, String fileExtName);

    /**
     * 上传支持断点续传的文件
     *
     * @param groupName   文件需要上传的组
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @return 返回文件全路径
     */
    StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName);

    /**
     * 上传支持断点续传的文件
     *
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @param metaDataSet 文件元信息
     * @return 返回文件全路径
     */
    StorePath uploadAppenderFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet);

    /**
     * 上传支持断点续传的文件
     *
     * @param groupName   文件需要上传的组
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @param metaDataSet 文件元信息
     * @return 返回文件全路径
     */
    StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet);

    /**
     * 断点续传文件
     *
     * @param groupName   文件组
     * @param path        文件路径
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     */
    void appendFile(String groupName, String path, InputStream inputStream, long fileSize);

    /**
     * 断点续传文件
     *
     * @param fullPath    文件全路径
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     */
    void appendFile(String fullPath, InputStream inputStream, long fileSize);

    /**
     * 断点续传文件
     *
     * @param storePath   文件路径信息
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     */
    void appendFile(StorePath storePath, InputStream inputStream, long fileSize);

    /**
     * 上传从文件
     *
     * @param groupName   主文件组
     * @param masterPath  主文件路径
     * @param inputStream 从文件流
     * @param fileSize    从文件大小
     * @param suffixName  从文件后缀(后缀在扩展名前面.[groupName]/[masterPath][suffixName].[fileExtName])
     * @param fileExtName 从文件扩展名
     * @return 返回从文件全路径
     */
    StorePath uploadSlaveFile(String groupName, String masterPath, InputStream inputStream, long fileSize,
                              String suffixName, String fileExtName);

    /**
     * 上传从文件
     *
     * @param masterFullPath 主文件全路径,包含组
     * @param inputStream    从文件流
     * @param fileSize       从文件大小
     * @param suffixName     从文件后缀(后缀在扩展名前面.[groupName]/[masterPath][suffixName].[fileExtName])
     * @param fileExtName    文件扩展名
     * @return 返回从文件全路径
     */
    StorePath uploadSlaveFile(String masterFullPath, InputStream inputStream, long fileSize,
                              String suffixName, String fileExtName);

    /**
     * 上传从文件
     *
     * @param masterStorePath 主文件路径信息
     * @param inputStream     从文件流
     * @param fileSize        从文件大小
     * @param suffixName      从文件后缀(后缀在扩展名前面.[groupName]/[masterPath][suffixName].[fileExtName])
     * @param fileExtName     文件扩展名
     * @return 返回从文件全路径
     */
    StorePath uploadSlaveFile(StorePath masterStorePath, InputStream inputStream, long fileSize,
                              String suffixName, String fileExtName);

    /**
     * 修改续传文件的内容
     *
     * @param groupName    文件组
     * @param path         文件路径
     * @param inputStream  上传文件流
     * @param modifySize   替换内容的大小(其大小不可大于输入流实际大小)
     * @param modifyOffset 文件修改的起始位置(原文件内容索引值,从0开始计数)
     */
    void modifyFile(String groupName, String path, InputStream inputStream, long modifySize, long modifyOffset);

    /**
     * 修改续传文件的内容
     *
     * @param fullPath     文件全路径
     * @param inputStream  上传文件流
     * @param modifySize   替换内容的大小(其大小不可大于输入流实际大小)
     * @param modifyOffset 文件修改的起始位置(原文件内容索引值,从0开始计数)
     */
    void modifyFile(String fullPath, InputStream inputStream, long modifySize, long modifyOffset);

    /**
     * 修改续传文件的内容
     *
     * @param storePath    文件路径信息
     * @param inputStream  上传文件流
     * @param modifySize   替换内容的大小(其大小不可大于输入流实际大小)
     * @param modifyOffset 文件修改的起始位置(原文件内容索引值,从0开始计数)
     */
    void modifyFile(StorePath storePath, InputStream inputStream, long modifySize, long modifyOffset);

    /**
     * 删减续传类型文件的内容(从文件起始部分删减)
     *
     * @param groupName         文件组
     * @param path              文件路径
     * @param truncatedFileSize 删减后的文件大小(需要保留的文件大小)
     */
    void truncateFile(String groupName, String path, long truncatedFileSize);

    /**
     * 删减续传类型文件的内容(从文件起始部分删减)
     *
     * @param fullPath          文件全路径
     * @param truncatedFileSize 删减后的文件大小(需要保留的文件大小)
     */
    void truncateFile(String fullPath, long truncatedFileSize);

    /**
     * 删减续传类型文件的内容(从文件起始部分删减)
     *
     * @param storePath         文件路径信息
     * @param truncatedFileSize 删减后的文件大小(需要保留的文件大小)
     */
    void truncateFile(StorePath storePath, long truncatedFileSize);

    /**
     * 删减续传类型文件的全部内容
     *
     * @param groupName 文件组
     * @param path      文件路径
     */
    void truncateFile(String groupName, String path);

    /**
     * 删减续传类型文件的全部内容
     *
     * @param fullPath 文件全路径
     */
    void truncateFile(String fullPath);

    /**
     * 删减续传类型文件的全部内容
     *
     * @param storePath 文件路径信息
     */
    void truncateFile(StorePath storePath);

    /**
     * 查看文件的信息
     *
     * @param groupName 文件组
     * @param path      文件路径
     * @return 返回文件信息
     */
    FileInfo queryFileInfo(String groupName, String path);

    /**
     * 查看文件的信息
     *
     * @param fullPath 文件全路径
     * @return 返回文件信息
     */
    FileInfo queryFileInfo(String fullPath);

    /**
     * 查看文件的信息
     *
     * @param storePath 文件路径信息
     * @return 返回文件信息
     */
    FileInfo queryFileInfo(StorePath storePath);

    /**
     * 下载整个文件
     *
     * @param groupName 文件组
     * @param path      文件路径
     * @param callback  文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback);

    /**
     * 下载整个文件
     *
     * @param fullPath 文件全路径
     * @param callback 文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(String fullPath, DownloadCallback<T> callback);

    /**
     * 下载整个文件
     *
     * @param storePath 文件路径信息
     * @param callback  文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(StorePath storePath, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     *
     * @param groupName      文件组
     * @param path           文件路径
     * @param downloadOffset 文件下载起始位置
     * @param downloadSize   预期下载文件大小
     * @param callback       文件下载回调
     * @return 返回下载回调定义对象
     * @see net.sudot.fdfs.proto.storage.StorageDownloadCommand#StorageDownloadCommand(java.lang.String, java.lang.String, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    <T> T downloadFile(String groupName, String path, long downloadOffset, long downloadSize, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     *
     * @param fullPath       文件全路径
     * @param downloadOffset 文件下载起始位置
     * @param downloadSize   预期下载文件大小
     * @param callback       文件下载回调
     * @return 返回下载回调定义对象
     * @see net.sudot.fdfs.proto.storage.StorageDownloadCommand#StorageDownloadCommand(java.lang.String, java.lang.String, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    <T> T downloadFile(String fullPath, long downloadOffset, long downloadSize, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     *
     * @param storePath      文件路径信息
     * @param downloadOffset 文件下载起始位置
     * @param downloadSize   预期下载文件大小
     * @param callback       文件下载回调
     * @return 返回下载回调定义对象
     * @see net.sudot.fdfs.proto.storage.StorageDownloadCommand#StorageDownloadCommand(java.lang.String, java.lang.String, long, long, net.sudot.fdfs.proto.storage.DownloadCallback)
     */
    <T> T downloadFile(StorePath storePath, long downloadOffset, long downloadSize, DownloadCallback<T> callback);

    /**
     * 删除文件
     *
     * @param groupName 文件组
     * @param path      文件路径
     */
    void deleteFile(String groupName, String path);

    /**
     * 删除文件
     *
     * @param fullPath 文件全路径(groupName/path)
     */
    void deleteFile(String fullPath);

    /**
     * 删除文件
     *
     * @param storePath 文件路径信息
     */
    void deleteFile(StorePath storePath);

    /**
     * 修改文件元信息（合并）
     *
     * @param groupName   文件组
     * @param path        文件路径
     * @param metaDataSet 需要合并的元信息
     */
    void mergeMetaData(String groupName, String path, Set<MetaData> metaDataSet);

    /**
     * 修改文件元信息（合并）
     *
     * @param fullPath    文件全路径
     * @param metaDataSet 需要合并的元信息
     */
    void mergeMetaData(String fullPath, Set<MetaData> metaDataSet);

    /**
     * 修改文件元信息（合并）
     *
     * @param storePath   文件路径信息
     * @param metaDataSet 需要合并的元信息
     */
    void mergeMetaData(StorePath storePath, Set<MetaData> metaDataSet);

    /**
     * 修改文件元信息（覆盖）
     *
     * @param groupName   文件组
     * @param path        文件路径
     * @param metaDataSet 需要修改的元信息
     */
    void overwriteMetaData(String groupName, String path, Set<MetaData> metaDataSet);

    /**
     * 修改文件元信息（覆盖）
     *
     * @param fullPath    文件全路径
     * @param metaDataSet 需要修改的元信息
     */
    void overwriteMetaData(String fullPath, Set<MetaData> metaDataSet);

    /**
     * 修改文件元信息（覆盖）
     *
     * @param storePath   文件路径信息
     * @param metaDataSet 需要修改的元信息
     */
    void overwriteMetaData(StorePath storePath, Set<MetaData> metaDataSet);

    /**
     * 获取文件元信息
     *
     * @param groupName 文件组
     * @param path      文件路径
     * @return 返回元信息
     */
    Set<MetaData> getMetaData(String groupName, String path);

    /**
     * 获取文件元信息
     *
     * @param fullPath 文件全路径
     * @return 返回元信息
     */
    Set<MetaData> getMetaData(String fullPath);

    /**
     * 获取文件元信息
     *
     * @param storePath 文件路径信息
     * @return 返回元信息
     */
    Set<MetaData> getMetaData(StorePath storePath);

    /**
     * 获取目录服务(Tracker)客户端接口
     *
     * @return 返回目录服务(Tracker)客户端接口
     */
    TrackerClient getTrackerClient();

    /**
     * 获取原始连接管理对象
     *
     * @return 返回连接管理对象
     */
    ConnectionManager getConnectionManager();
}
