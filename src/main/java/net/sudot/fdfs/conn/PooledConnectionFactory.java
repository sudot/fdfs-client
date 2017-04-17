package net.sudot.fdfs.conn;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * pooled FdfsSocket factory
 *
 * <pre>
 * 定义了被池化的对象的创建，初始化，激活，钝化以及销毁功能
 * </per>
 *
 * @author tobato
 */
public class PooledConnectionFactory extends BaseKeyedPooledObjectFactory<InetSocketAddress, Connection> {

    /** 默认字符集 */
    private static final String DEFAULT_CHARSET_NAME = "UTF-8";
    /** 读取时间(毫秒) */
    private static final int DEFAULT_SO_TIMEOUT = 3000;
    /** 连接超时时间(毫秒) */
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    /** 读取时间 */
    private int soTimeout = DEFAULT_SO_TIMEOUT;
    /** 连接超时时间 */
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    /** 字符集 */
    private Charset charset;
    /** 设置默认字符集 */
    private String charsetName = DEFAULT_CHARSET_NAME;

    /**
     * 创建连接
     */
    @Override
    public Connection create(InetSocketAddress address) throws Exception {
        // 初始化字符集
        if (null == charset) {
            charset = Charset.forName(charsetName);
        }
        return new DefaultConnection(address, soTimeout, connectTimeout, charset);
    }

    /**
     * 将对象池化pooledObject
     */
    @Override
    public PooledObject<Connection> wrap(Connection conn) {
        return new DefaultPooledObject<Connection>(conn);
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public PooledConnectionFactory setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public PooledConnectionFactory setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public void destroyObject(InetSocketAddress key, PooledObject<Connection> p) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(InetSocketAddress key, PooledObject<Connection> p) {
        return p.getObject().isValid();
    }

    public PooledConnectionFactory setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

}
