package net.sudot.fdfs.proto.tracker.internal;

import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.ProtoHead;

/**
 * 获取存储节点请求
 * @author tobato
 */
public class TrackerGetStoreStorageRequest extends FdfsRequest {

    private static final byte WITHOUT_GROUP_CMD = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;

    /**
     * 获取存储节点
     */
    public TrackerGetStoreStorageRequest() {
        super();
        this.head = new ProtoHead(WITHOUT_GROUP_CMD);
    }

}
