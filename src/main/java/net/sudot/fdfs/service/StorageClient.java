package net.sudot.fdfs.service;

import net.sudot.fdfs.domain.FileInfo;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.storage.DownloadCallback;

import java.io.InputStream;
import java.util.Set;

/**
 * 基本文件存储客户端操作
 * @author tobato
 * @author sudot on 2017-03-17 0017.
 */
public interface StorageClient {

    /**
     * 上传文件(不支持断点续传)
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
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
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @param metaDataSet 文件元信息
     * @return 返回文件全路径
     */
    StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet);

    /**
     * 上传包含元信息的文件(不支持断点续传)
     * <pre>
     * 文件上传后不可以修改，如果要修改则删除以后重新上传
     * </pre>
     * @param groupName   文件需要上传的组
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @param metaDataSet 文件元信息
     * @return 返回文件全路径
     */
    StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet);

    /**
     * 上传从文件
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
     * @param masterFullPath 主文件全路径,包含组
     * @param inputStream    主文件路径
     * @param fileSize       从文件流
     * @param suffixName     从文件大小
     * @param fileExtName    从文件后缀(后缀在扩展名前面.[groupName]/[masterPath][suffixName].[fileExtName])
     * @return 返回从文件全路径
     */
    StorePath uploadSlaveFile(String masterFullPath, InputStream inputStream, long fileSize,
                              String suffixName, String fileExtName);

    /**
     * 上传从文件
     * @param masterStorePath 主文件路径信息
     * @param inputStream     主文件路径
     * @param fileSize        从文件流
     * @param suffixName      从文件大小
     * @param fileExtName     从文件后缀(后缀在扩展名前面.[groupName]/[masterPath][suffixName].[fileExtName])
     * @return 返回从文件全路径
     */
    StorePath uploadSlaveFile(StorePath masterStorePath, InputStream inputStream, long fileSize,
                              String suffixName, String fileExtName);

    /**
     * 上传支持断点续传的文件
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @return 返回文件全路径
     */
    StorePath uploadAppenderFile(InputStream inputStream, long fileSize, String fileExtName);

    /**
     * 上传支持断点续传的文件
     * @param groupName   文件需要上传的组
     * @param inputStream 文件流
     * @param fileSize    文件大小
     * @param fileExtName 文件扩展名
     * @return 返回文件全路径
     */
    StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName);

    /**
     * 断点续传文件
     * @param groupName   文件组
     * @param path        文件路径
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     */
    void appendFile(String groupName, String path, InputStream inputStream, long fileSize);

    /**
     * 断点续传文件
     * @param fullPath    文件全路径
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     */
    void appendFile(String fullPath, InputStream inputStream, long fileSize);

    /**
     * 断点续传文件
     * @param storePath   文件路径信息
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     */
    void appendFile(StorePath storePath, InputStream inputStream, long fileSize);

    /**
     * 修改续传文件的内容
     * @param groupName   文件组
     * @param path        文件路径
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     * @param fileOffset  修改文件的起始偏移量
     */
    void modifyFile(String groupName, String path, InputStream inputStream, long fileSize, long fileOffset);

    /**
     * 修改续传文件的内容
     * @param fullPath    文件全路径
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     * @param fileOffset  修改文件的起始偏移量
     */
    void modifyFile(String fullPath, InputStream inputStream, long fileSize, long fileOffset);

    /**
     * 修改续传文件的内容
     * @param storePath   文件路径信息
     * @param inputStream 上传文件流
     * @param fileSize    上传文件大小
     * @param fileOffset  修改文件的起始偏移量
     */
    void modifyFile(StorePath storePath, InputStream inputStream, long fileSize, long fileOffset);

    /**
     * 清除续传类型文件的内容
     * @param groupName         文件组
     * @param path              文件路径
     * @param truncatedFileSize 清除的文件大小
     */
    void truncateFile(String groupName, String path, long truncatedFileSize);

    /**
     * 清除续传类型文件的内容
     * @param fullPath          文件全路径
     * @param truncatedFileSize 清除的文件大小
     */
    void truncateFile(String fullPath, long truncatedFileSize);

    /**
     * 清除续传类型文件的内容
     * @param storePath         文件路径信息
     * @param truncatedFileSize 清除的文件大小
     */
    void truncateFile(StorePath storePath, long truncatedFileSize);

    /**
     * 清除续传类型文件的内容
     * @param groupName 文件组
     * @param path      文件路径
     */
    void truncateFile(String groupName, String path);

    /**
     * 清除续传类型文件的内容
     * @param fullPath 文件全路径
     */
    void truncateFile(String fullPath);

    /**
     * 清除续传类型文件的内容
     * @param storePath 文件路径信息
     */
    void truncateFile(StorePath storePath);

    /**
     * 查看文件的信息
     * @param groupName 文件组
     * @param path      文件路径
     * @return 返回文件信息
     */
    FileInfo queryFileInfo(String groupName, String path);

    /**
     * 查看文件的信息
     * @param fullPath 文件全路径
     * @return 返回文件信息
     */
    FileInfo queryFileInfo(String fullPath);

    /**
     * 查看文件的信息
     * @param storePath 文件路径信息
     * @return 返回文件信息
     */
    FileInfo queryFileInfo(StorePath storePath);

    /**
     * 下载整个文件
     * @param groupName 文件组
     * @param path      文件路径
     * @param callback  文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback);

    /**
     * 下载整个文件
     * @param fullPath 文件全路径
     * @param callback 文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(String fullPath, DownloadCallback<T> callback);

    /**
     * 下载整个文件
     * @param storePath 文件路径信息
     * @param callback  文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(StorePath storePath, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     * @param groupName  文件组
     * @param path       文件路径
     * @param fileOffset 起始位置
     * @param fileSize   下载总大小
     * @param callback   文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     * @param fullPath   文件全路径
     * @param fileOffset 起始位置
     * @param fileSize   下载总大小
     * @param callback   文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(String fullPath, long fileOffset, long fileSize, DownloadCallback<T> callback);

    /**
     * 下载文件片段
     * @param storePath  文件路径信息
     * @param fileOffset 起始位置
     * @param fileSize   下载总大小
     * @param callback   文件下载回调
     * @return 返回下载回调定义对象
     */
    <T> T downloadFile(StorePath storePath, long fileOffset, long fileSize, DownloadCallback<T> callback);

    /**
     * 删除文件
     * @param groupName 文件组
     * @param path      文件路径
     */
    void deleteFile(String groupName, String path);

    /**
     * 删除文件
     * @param fullPath 文件全路径(groupName/path)
     */
    void deleteFile(String fullPath);

    /**
     * 删除文件
     * @param storePath 文件路径信息
     */
    void deleteFile(StorePath storePath);

    /**
     * 修改文件元信息（合并）
     * @param groupName   文件组
     * @param path        文件路径
     * @param metaDataSet 需要合并的元信息
     */
    void mergeMetadata(String groupName, String path, Set<MateData> metaDataSet);

    /**
     * 修改文件元信息（合并）
     * @param fullPath    文件全路径
     * @param metaDataSet 需要合并的元信息
     */
    void mergeMetadata(String fullPath, Set<MateData> metaDataSet);

    /**
     * 修改文件元信息（合并）
     * @param storePath   文件路径信息
     * @param metaDataSet 需要合并的元信息
     */
    void mergeMetadata(StorePath storePath, Set<MateData> metaDataSet);

    /**
     * 修改文件元信息（覆盖）
     * @param groupName   文件组
     * @param path        文件路径
     * @param metaDataSet 需要修改的元信息
     */
    void overwriteMetadata(String groupName, String path, Set<MateData> metaDataSet);

    /**
     * 修改文件元信息（覆盖）
     * @param fullPath    文件全路径
     * @param metaDataSet 需要修改的元信息
     */
    void overwriteMetadata(String fullPath, Set<MateData> metaDataSet);

    /**
     * 修改文件元信息（覆盖）
     * @param storePath   文件路径信息
     * @param metaDataSet 需要修改的元信息
     */
    void overwriteMetadata(StorePath storePath, Set<MateData> metaDataSet);

    /**
     * 获取文件元信息
     * @param groupName 文件组
     * @param path      文件路径
     * @return 返回元信息
     */
    Set<MateData> getMetadata(String groupName, String path);

    /**
     * 获取文件元信息
     * @param fullPath 文件全路径
     * @return 返回元信息
     */
    Set<MateData> getMetadata(String fullPath);

    /**
     * 获取文件元信息
     * @param storePath 文件路径信息
     * @return 返回元信息
     */
    Set<MateData> getMetadata(StorePath storePath);

}
