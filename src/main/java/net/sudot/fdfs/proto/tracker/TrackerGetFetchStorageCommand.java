package net.sudot.fdfs.proto.tracker;

import net.sudot.fdfs.domain.StorageNodeInfo;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.tracker.internal.TrackerGetFetchStorageRequest;
import net.sudot.fdfs.util.Validate;

/**
 * 获取源服务器
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class TrackerGetFetchStorageCommand extends AbstractFdfsCommand<StorageNodeInfo> {

    public TrackerGetFetchStorageCommand(String groupName, String path, boolean toUpdate) {
        Validate.notBlank(groupName, "分组不能为空");
        Validate.notBlank(path, "文件路径不能为空");
        super.request = new TrackerGetFetchStorageRequest(groupName, path, toUpdate);
        super.response = new FdfsResponse<StorageNodeInfo>() {
            // default response
        };
    }

}
