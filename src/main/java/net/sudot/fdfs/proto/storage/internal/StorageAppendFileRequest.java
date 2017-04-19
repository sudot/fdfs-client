package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 文件上传命令
 * @author tobato
 * Update by sudot on 2017-04-19 0019.
 */
public class StorageAppendFileRequest extends FdfsRequest {

    /** 文件路径长度 */
    @FdfsColumn(index = 0)
    private long pathSize;
    /** 发送文件长度 */
    @FdfsColumn(index = 1)
    private long fileSize;
    /** 文件路径 */
    @FdfsColumn(index = 2, dynamicField = DynamicFieldType.allRestByte)
    private String path;

    /**
     * 构造函数
     * @param inputStream 文件输入流
     * @param fileSize    文件大小
     * @param path        文件路径(不包含组名)
     */
    public StorageAppendFileRequest(InputStream inputStream, long fileSize, String path) {
        super();
        this.inputFile = inputStream;
        this.fileSize = fileSize;
        this.path = path;
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_APPEND_FILE);
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

    public StorageAppendFileRequest setPathSize(long pathSize) {
        this.pathSize = pathSize;
        return this;
    }

    public String getPath() {
        return path;
    }

    public StorageAppendFileRequest setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    public StorageAppendFileRequest setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    @Override
    public String toString() {
        return "StorageAppendFileRequest [pathSize=" + pathSize + ", fileSize=" + fileSize + ", path=" + path + "]";
    }

}
