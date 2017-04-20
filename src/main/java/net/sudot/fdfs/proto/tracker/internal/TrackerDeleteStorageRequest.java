package net.sudot.fdfs.proto.tracker.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

/**
 * 移除存储服务器
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class TrackerDeleteStorageRequest extends FdfsRequest {

    /** 组名 */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 存储ip */
    @FdfsColumn(index = 1, max = OtherConstants.FDFS_IPADDR_SIZE - 1)
    private String storageIpAddress;

    /**
     * 获取文件源服务器
     * @param groupName
     * @param storageIpAddress
     */
    public TrackerDeleteStorageRequest(String groupName, String storageIpAddress) {
        this.groupName = groupName;
        this.storageIpAddress = storageIpAddress;
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE);
    }

    public String getGroupName() {
        return groupName;
    }

    public String getStorageIpAddress() {
        return storageIpAddress;
    }

}
