package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

import java.nio.charset.Charset;

/**
 * 文件Truncate命令
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class StorageTruncateRequest extends FdfsRequest {

    /** 文件路径长度 */
    @FdfsColumn(index = 0)
    private long pathSize;
    /** 截取文件长度 */
    @FdfsColumn(index = 1)
    private long truncatedFileSize;
    /** 文件路径 */
    @FdfsColumn(index = 2, dynamicField = DynamicFieldType.ALL_REST_BYTE)
    private String path;

    /**
     * 文件Truncate命令
     * @param path              文件路径
     * @param truncatedFileSize 截取后的文件长度(即文件保留长度)
     */
    public StorageTruncateRequest(String path, long truncatedFileSize) {
        super();
        this.truncatedFileSize = truncatedFileSize;
        this.path = path;
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_TRUNCATE_FILE);
    }

    /**
     * 打包参数
     */
    @Override
    public byte[] encodeParam(Charset charset) {
        // 运行时参数在此计算值
        this.pathSize = path.getBytes(charset).length;
        return super.encodeParam(charset);
    }

    public long getPathSize() {
        return pathSize;
    }

    public StorageTruncateRequest setPathSize(long pathSize) {
        this.pathSize = pathSize;
        return this;
    }

    public String getPath() {
        return path;
    }

    public StorageTruncateRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public long getTruncatedFileSize() {
        return truncatedFileSize;
    }

    public StorageTruncateRequest setTruncatedFileSize(long truncatedFileSize) {
        this.truncatedFileSize = truncatedFileSize;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StorageTruncateRequest{");
        sb.append("pathSize=").append(pathSize);
        sb.append(", truncatedFileSize=").append(truncatedFileSize);
        sb.append(", path='").append(path).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
