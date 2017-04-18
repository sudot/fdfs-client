package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.storage.internal.StorageDownloadRequest;
import net.sudot.fdfs.proto.storage.internal.StorageDownloadResponse;

/**
 * 文件下载命令
 * @param <T>
 * @author tobato
 */
public class StorageDownloadCommand<T> extends AbstractFdfsCommand<T> {

    /**
     * 下载文件
     * @param groupName  组名
     * @param path       文件路径
     * @param fileOffset 文件下载起始位置
     * @param fileSize   预期下载文件大小
     * @param callback   文件下载的回调处理
     */
    public StorageDownloadCommand(String groupName, String path, long fileOffset, long fileSize,
                                  DownloadCallback<T> callback) {
        super();
        this.request = new StorageDownloadRequest(groupName, path, fileOffset, fileSize);
        // 输出响应
        this.response = new StorageDownloadResponse<T>(callback);
    }

    /**
     * 下载文件
     * @param groupName 组名
     * @param path      文件路径
     * @param callback  文件下载的回调处理
     */
    public StorageDownloadCommand(String groupName, String path, DownloadCallback<T> callback) {
        super();
        this.request = new StorageDownloadRequest(groupName, path, 0, 0);
        // 输出响应
        this.response = new StorageDownloadResponse<T>(callback);
    }
}
