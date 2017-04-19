package net.sudot.fdfs.proto.storage.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

/**
 * 查询文件信息命令
 * @author tobato
 * Update by sudot on 2017-04-19 0019.
 */
public class StorageGetMetadataRequest extends FdfsRequest {

    /** 组名 */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 路径名 */
    @FdfsColumn(index = 1, dynamicField = DynamicFieldType.allRestByte)
    private String path;

    /**
     * 删除文件命令
     * @param groupName 文件存储组
     * @param path      文件路径
     */
    public StorageGetMetadataRequest(String groupName, String path) {
        super();
        this.groupName = groupName;
        this.path = path;
        this.head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_GET_METADATA);
    }

    public String getGroupName() {
        return groupName;
    }

    public StorageGetMetadataRequest setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getPath() {
        return path;
    }

    public StorageGetMetadataRequest setPath(String path) {
        this.path = path;
        return this;
    }

}
