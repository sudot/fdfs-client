package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

import java.io.InputStream;

/**
 * 文件上传命令
 * @author tobato
 * Update by sudot on 2017-03-17 0017.
 */
public class StorageUploadFileRequest extends FdfsRequest {

    private static final byte uploadCmd = CmdConstants.STORAGE_PROTO_CMD_UPLOAD_FILE;
    private static final byte uploadAppenderCmd = CmdConstants.STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE;

    /** 存储节点index */
    @FdfsColumn(index = 0)
    private byte storeIndex;
    /** 发送文件长度 */
    @FdfsColumn(index = 1)
    private long fileSize;
    /** 文件扩展名 */
    @FdfsColumn(index = 2, max = OtherConstants.FDFS_FILE_EXT_NAME_MAX_LEN)
    private String fileExtName;

    /**
     * 构造函数
     * @param storeIndex     存储节点
     * @param inputStream    输入流
     * @param fileExtName    文件扩展名
     * @param fileSize       文件大小
     * @param isAppenderFile 是否支持断点续传
     */
    public StorageUploadFileRequest(byte storeIndex, InputStream inputStream, String fileExtName, long fileSize,
                                    boolean isAppenderFile) {
        super();
        this.inputFile = inputStream;
        this.fileSize = fileSize;
        this.storeIndex = storeIndex;
        this.fileExtName = fileExtName;
        if (isAppenderFile) {
            head = new ProtoHead(uploadAppenderCmd);
        } else {
            head = new ProtoHead(uploadCmd);
        }
    }

    public byte getStoreIndex() {
        return storeIndex;
    }

    public StorageUploadFileRequest setStoreIndex(byte storeIndex) {
        this.storeIndex = storeIndex;
        return this;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public StorageUploadFileRequest setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
        return this;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

}
