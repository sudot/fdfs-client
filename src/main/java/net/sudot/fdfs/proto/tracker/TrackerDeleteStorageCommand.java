package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.tracker.internal.TrackerDeleteStorageRequest;
import net.sudot.fdfs.util.Validate;

/**
 * 移除存储服务器命令
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class TrackerDeleteStorageCommand extends AbstractFdfsCommand<Void> {

    public TrackerDeleteStorageCommand(String groupName, String storageIpAddress) {
        Validate.notBlank(groupName, "分组不能为空");
        Validate.notBlank(storageIpAddress, "文件路径不能为空");
        super.request = new TrackerDeleteStorageRequest(groupName, storageIpAddress);
        super.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
