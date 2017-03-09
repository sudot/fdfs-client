package net.sudot.fdfs.proto.tracker.internal;

import net.sudot.fdfs.proto.FdfsRequest;
import net.sudot.fdfs.proto.CmdConstants;
import net.sudot.fdfs.proto.ProtoHead;

/**
 * 列出分组命令
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsRequest extends FdfsRequest {

    public TrackerListGroupsRequest() {
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
    }
}
