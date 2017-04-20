package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

/**
 * 文件下载请求
 * @author tobato
 * @author sudot on 2017-04-19 0019.
 */
public class StorageDownloadRequest extends FdfsRequest {

    /** 开始位置 */
    @FdfsColumn(index = 0)
    private long downloadOffset;
    /** 读取文件长度 */
    @FdfsColumn(index = 1)
    private long downloadSize;
    /** 组名 */
    @FdfsColumn(index = 2, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 文件路径 */
    @FdfsColumn(index = 3, dynamicField = DynamicFieldType.ALL_REST_BYTE)
    private String path;

    /**
     * 文件下载请求
     * @param groupName      文件存储组
     * @param path           文件路径
     * @param downloadOffset 文件下载偏移量(文件下载的起始位置)
     * @param downloadSize   预期下载文件大小
     */
    public StorageDownloadRequest(String groupName, String path, long downloadOffset, long downloadSize) {
        super();
        this.groupName = groupName;
        this.path = path;
        this.downloadSize = downloadSize;
        this.downloadOffset = downloadOffset;
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_DOWNLOAD_FILE);

    }

    public long getDownloadOffset() {
        return downloadOffset;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPath() {
        return path;
    }
}
