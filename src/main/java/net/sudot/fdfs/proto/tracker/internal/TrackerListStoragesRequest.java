package net.sudot.fdfs.proto.tracker.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.ProtoHead;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

/**
 * 列出存储状态
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class TrackerListStoragesRequest extends FdfsRequest {

    /** 组名 */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 存储服务器ip地址 */
    @FdfsColumn(index = 1, max = OtherConstants.FDFS_IPADDR_SIZE - 1, dynamicField = DynamicFieldType.NULLABLE)
    private String storageIpAddress;

    private TrackerListStoragesRequest() {
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE);
    }

    /**
     * 列举存储服务器状态
     * @param groupName
     * @param storageIpAddress
     */
    public TrackerListStoragesRequest(String groupName, String storageIpAddress) {
        this();
        this.groupName = groupName;
        this.storageIpAddress = storageIpAddress;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getStorageIpAddress() {
        return storageIpAddress;
    }

}
