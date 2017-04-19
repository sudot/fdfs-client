package net.sudot.fdfs.proto.storage;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件下载回调接口
 * @param <T>
 * @author tobato
 * @author sudot on 2017-03-17 0017.
 */
public interface DownloadCallback<T> {

    /**
     * 输入响应回调
     * <pre>
     * Q:不能关闭ins?
     * A:如果{@link FdfsInputStream#close()}关闭了其成员变量ins,则此处不能关闭此ins,否则可以关闭
     * 为了方便后期开发人员,防止其错误关闭,故{@link FdfsInputStream#close()}未关闭其成员变量ins
     * </pre>
     * @param ins
     * @return
     * @throws IOException
     */
    T recv(InputStream ins) throws IOException;

}
