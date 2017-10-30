package net.sudot.fdfs.service;

/**
 * FastDFS操作客户端工厂接口
 *
 * @author sudot on 2017-04-14 0014.
 */
public interface FdfsOptionsFactory {

    /**
     * 目录服务(Tracker)操作客户端
     *
     * @return 返回目录服务(Tracker)操作客户端实例
     */
    TrackerClient getTrackerClient();

    /**
     * 基本文件存储操作客户端
     *
     * @return 返回文件存储操作客户端实例
     */
    StorageClient getStorageClient();
}
