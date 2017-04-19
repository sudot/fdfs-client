package net.sudot.fdfs.conn;

/**
 * Storage连接池管理
 * @author sudot on 2017-04-18 0018.
 */
public class StorageConnectionManager extends ConnectionManager {
    /**
     * 构造函数
     */
    public StorageConnectionManager() {
        super();
    }

    /**
     * 构造函数
     * @param pool 连接池
     */
    public StorageConnectionManager(FdfsConnectionPool pool) {
        super(pool);
    }
}
