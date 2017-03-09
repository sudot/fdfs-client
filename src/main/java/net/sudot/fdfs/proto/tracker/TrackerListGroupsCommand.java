package net.sudot.fdfs.proto.tracker;

import java.util.List;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.tracker.internal.TrackerListGroupsRequest;
import net.sudot.fdfs.proto.tracker.internal.TrackerListGroupsResponse;
import net.sudot.fdfs.domain.GroupState;

/**
 * 列出组命令
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsCommand extends AbstractFdfsCommand<List<GroupState>> {

    public TrackerListGroupsCommand() {
        super.request = new TrackerListGroupsRequest();
        super.response = new TrackerListGroupsResponse();
    }

}
