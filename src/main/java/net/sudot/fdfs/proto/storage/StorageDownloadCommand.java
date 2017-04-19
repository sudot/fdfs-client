package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.storage.internal.StorageDownloadRequest;
import net.sudot.fdfs.proto.storage.internal.StorageDownloadResponse;

/**
 * 文件下载命令
 * @param <T>
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class StorageDownloadCommand<T> extends AbstractFdfsCommand<T> {

    /**
     * 下载文件
     * <pre>
     * 如果downloadOffset和downloadSize都为零,则表示下载文件所有内容
     * 只有downloadSize为零,则下载所有剩余内容
     * downloadOffset和downloadSize相加之和不能大于文件总大小
     * </pre>
     * @param groupName      组名
     * @param path           文件路径
     * @param downloadOffset 文件下载起始位置
     * @param downloadSize   预期下载文件大小
     * @param callback       文件下载的回调处理
     */
    public StorageDownloadCommand(String groupName, String path, long downloadOffset, long downloadSize,
                                  DownloadCallback<T> callback) {
        super();
        if (downloadOffset < 0) { throw new IllegalArgumentException("downloadOffset必须为正整数.当前值:" + downloadOffset); }
        if (downloadSize < 0) { throw new IllegalArgumentException("downloadSize必须为正整数.当前值:" + downloadSize); }
        this.request = new StorageDownloadRequest(groupName, path, downloadOffset, downloadSize);
        // 输出响应
        this.response = new StorageDownloadResponse<T>(callback);
    }

    /**
     * 下载文件所有内容
     * @param groupName 组名
     * @param path      文件路径
     * @param callback  文件下载的回调处理
     */
    public StorageDownloadCommand(String groupName, String path, DownloadCallback<T> callback) {
        this(groupName, path, 0, 0, callback);
    }
}
