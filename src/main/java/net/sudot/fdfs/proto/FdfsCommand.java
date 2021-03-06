package net.sudot.fdfs.proto;

import net.sudot.fdfs.conn.Connection;

/**
 * Fdfs交易命令抽象
 *
 * @author tobato
 */
public interface FdfsCommand<T> {

    /**
     * 执行交易
     *
     * @param conn 连接对象
     * @return
     */
    public T execute(Connection conn);

}
