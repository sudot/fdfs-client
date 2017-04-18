package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.ConnectionManager;

/**
 * FastDFS操作客户端工厂接口
 * Created by tangjialin on 2017-04-14 0014.
 */
public interface FdfsOptionsFactory {

    /**
     * Tracker连接池管理实例
     * @return 返回Tracker连接池管理实例
     */
    ConnectionManager getTrackerConnectionManager();

    /**
     * Storage连接池管理实例
     * @return 返回Storage连接池管理实例
     */
    ConnectionManager getStorageConnectionManager();

    /**
     * 目录服务(Tracker)操作客户端
     * @return 返回目录服务(Tracker)操作客户端实例
     */
    TrackerClient getTrackerClient();

    /**
     *
     * 基本文件存储操作客户端
     * @return 返回文件存储操作客户端实例
     */
    StorageClient getStorageClient();
}
