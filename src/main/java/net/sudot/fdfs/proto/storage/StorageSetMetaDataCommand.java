package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.proto.AbstractFdfsCommand;
import net.sudot.fdfs.proto.FdfsResponse;
import net.sudot.fdfs.proto.storage.enums.StorageMetaDataSetType;
import net.sudot.fdfs.proto.storage.internal.StorageSetMetaDataRequest;
import net.sudot.fdfs.util.Validate;

import java.util.Set;

/**
 * 设置文件标签
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class StorageSetMetaDataCommand extends AbstractFdfsCommand<Void> {

    /**
     * 设置文件标签(元数据)
     * @param groupName   组名
     * @param path        文件路径
     * @param metaDataSet 元数据信息
     * @param type        元数据操作方式
     */
    public StorageSetMetaDataCommand(String groupName, String path, Set<MetaData> metaDataSet,
                                     StorageMetaDataSetType type) {
        Validate.notBlank(groupName, "分组不能为空");
        Validate.notBlank(path, "文件路径不能为空");
        Validate.notEmpty(metaDataSet, "元数据不能为空");
        Validate.notNull(type, "标签设置方式不能为空");
        this.request = new StorageSetMetaDataRequest(groupName, path, metaDataSet, type);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
