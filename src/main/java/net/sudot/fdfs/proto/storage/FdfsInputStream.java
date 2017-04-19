package net.sudot.fdfs.proto.storage;

import java.io.IOException;
import java.io.InputStream;

/**
 * FdfsInputStream包装类
 * @author tobato
 * Update by sudot on 2017-04-19 0019.
 */
public class FdfsInputStream extends InputStream {

    private final InputStream ins;
    private final long size;
    private long remainByteSize;

    public FdfsInputStream(InputStream ins, long size) {
        this.ins = ins;
        this.size = size;
        remainByteSize = size;
    }

    @Override
    public int read() throws IOException {
        return ins.read();
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (remainByteSize == 0) {
            return -1;
        }
        int byteSize = ins.read(b, off, len);
        if (remainByteSize < byteSize) {
            throw new IOException("协议长度" + size + "与实际长度不符");
        }

        remainByteSize -= byteSize;
        return byteSize;
    }

    /**
     * 关闭输入流
     * <pre>
     * 注意:
     * 此处不能关闭ins
     * 因为socket是"全双工"通信
     * 其中任意一个流(包含输入流和输出流)关闭
     * 都会导致socket连接断掉,引发socket io异常
     * </pre>
     * @throws IOException
     * @see java.net.ServerSocket
     * @see net.sudot.socket.client.ClientDemo#main(java.lang.String[])
     */
    @Override
    public void close() throws IOException {
        super.close();
        // do nothing
    }

    /**
     * 是否已完成读取
     * @return
     */
    public boolean isReadCompleted() {
        return remainByteSize == 0;
    }

}
