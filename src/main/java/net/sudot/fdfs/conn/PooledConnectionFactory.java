package net.sudot.fdfs.conn;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * pooled FdfsSocket factory
 * 
 * <pre>
 * 定义了被池化的对象的创建，初始化，激活，钝化以及销毁功能
 * </per>
 * 
 * @author tobato
 *
 */
@Component
public class PooledConnectionFactory extends BaseKeyedPooledObjectFactory<InetSocketAddress, Connection> {

    /** 读取时间 */
    private int soTimeout;
    /** 连接超时时间 */
    private int connectTimeout;
    /** 字符集 */
    private Charset charset;
    /** 默认字符集 */
    private static final String DEFAULT_CHARSET_NAME = "UTF-8";
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
    
    @Value("${fdfs.soTimeout}")
    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Value("${fdfs.connectTimeout}")
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
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

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

}
