package net.sudot.fdfs.proto.tracker;

import java.util.List;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.tracker.internal.TrackerListStoragesRequest;
import net.sudot.fdfs.proto.tracker.internal.TrackerListStoragesResponse;
import net.sudot.fdfs.domain.StorageState;

/**
 * 列出组命令
 * 
 * @author tobato
 *
 */
public class TrackerListStoragesCommand extends AbstractFdfsCommand<List<StorageState>> {

    public TrackerListStoragesCommand(String groupName, String storageIpAddr) {
        super.request = new TrackerListStoragesRequest(groupName, storageIpAddr);
        super.response = new TrackerListStoragesResponse();
    }

    public TrackerListStoragesCommand(String groupName) {
        super.request = new TrackerListStoragesRequest(groupName);
        super.response = new TrackerListStoragesResponse();
    }

}
