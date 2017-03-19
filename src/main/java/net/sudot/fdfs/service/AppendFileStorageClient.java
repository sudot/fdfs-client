package net.sudot.fdfs.service;

import net.sudot.fdfs.domain.StorePath;

import java.io.InputStream;

/**
 * 支持断点续传的文件服务接口
 * <pre>
 * 适合处理大文件，分段传输
 * </pre>
 * @author tobato
 * Update by sudot on 2017-03-19 0019.
 */
public interface AppendFileStorageClient extends GenerateStorageClient {

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

}
