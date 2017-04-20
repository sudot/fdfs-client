package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.domain.StorageState;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.tracker.internal.TrackerListStoragesRequest;
import net.sudot.fdfs.proto.tracker.internal.TrackerListStoragesResponse;
import net.sudot.fdfs.util.Validate;

import java.util.List;

/**
 * 列出组命令
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class TrackerListStoragesCommand extends AbstractFdfsCommand<List<StorageState>> {

    public TrackerListStoragesCommand(String groupName, String storageIpAddress) {
        Validate.notBlank(groupName, "分组不能为空");
        super.request = new TrackerListStoragesRequest(groupName, storageIpAddress);
        super.response = new TrackerListStoragesResponse();
    }

    public TrackerListStoragesCommand(String groupName) {
        this(groupName, null);
    }

}
