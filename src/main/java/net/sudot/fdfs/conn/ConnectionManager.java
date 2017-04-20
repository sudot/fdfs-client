package net.sudot.fdfs.conn;

import net.sudot.fdfs.exception.FdfsException;
import net.sudot.fdfs.exception.FdfsIOException;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.exception.FdfsUnavailableException;
import net.sudot.fdfs.proto.FdfsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 连接池管理
 *
 * <pre>
 * 负责借出连接，在连接上执行业务逻辑，然后归还连
 * </pre>
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public abstract class ConnectionManager {
    /** 日志 */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /** 连接池 */
    private FdfsConnectionPool pool;

    /**
     * 构造函数
     */
    public ConnectionManager() {
        super();
    }

    /**
     * 构造函数
     * @param pool 连接池
     */
    public ConnectionManager(FdfsConnectionPool pool) {
        super();
        setPool(pool);
    }

    /**
     * 获取连接并执行交易
     * @param address
     * @param command
     * @return
     */
    public <T> T executeFdfsCmd(InetSocketAddress address, FdfsCommand<T> command) {
        if (logger.isDebugEnabled()) { logger.debug("对地址[{}]发出交易请求[{}]", address, command.getClass().getSimpleName()); }
        Connection conn = null;
        try {
            // 获取连接
            conn = getConnection(address);
            // 执行交易
            return execute(conn, command);
        } catch (FdfsServerException | FdfsIOException e) {
            invalidateObject(address, conn);
            throw e;
        } catch (FdfsException e) {
            throw e;
        } catch (Exception e) {
            throw new FdfsException("execute fdfs command error", e);
        } finally {
            returnObject(address, conn);
        }
    }

    /**
     * 执行交易
     * @param conn
     * @param command
     * @return
     */
    protected <T> T execute(Connection conn, FdfsCommand<T> command) {
        return command.execute(conn);
    }

    /**
     * 获取连接
     * @param address
     * @return
     */
    protected Connection getConnection(InetSocketAddress address) {
        try {
            return pool.borrowObject(address);
        } catch (Exception e) {
            throw new FdfsUnavailableException("Unable to borrow buffer from pool", e);
        }
    }

    /**
     * 回收连接
     * @param address 连接所属地址
     * @param conn    回收的连接
     * @param <C>     ConnectionManager及其子类泛型
     * @return 返回ConnectionManager及其子类
     */
    protected <C extends ConnectionManager> C returnObject(InetSocketAddress address, Connection conn) {
        try {
            if (address != null && null != conn && !conn.isClosed()) {
                pool.returnObject(address, conn);
            }
        } catch (Exception e) {
            logger.error("return pooled connection error", e);
        }
        return (C) this;
    }

    /**
     * 销毁连接
     * @param address 连接所属地址
     * @param conn    销毁的连接
     * @param <C>     ConnectionManager及其子类泛型
     * @return 返回ConnectionManager及其子类
     */
    protected <C extends ConnectionManager> C invalidateObject(InetSocketAddress address, Connection conn) {
        try {
            if (address != null && null != conn && !conn.isValid()) {
                logger.warn("连接因异常被销毁:{} {}", address, conn);
                pool.invalidateObject(address, conn);
            }
        } catch (Exception e) {
            logger.error("invalidate pooled connection error", e);
        }
        return (C) this;
    }

    public FdfsConnectionPool getPool() {
        return pool;
    }

    public <C extends ConnectionManager> C setPool(FdfsConnectionPool pool) {
        this.pool = pool;
        return (C) this;
    }

}
