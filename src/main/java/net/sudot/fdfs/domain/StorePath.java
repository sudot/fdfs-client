package net.sudot.fdfs.domain;

import net.sudot.fdfs.exception.FdfsUnsupportStorePathException;
import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.mapper.DynamicFieldType;
import net.sudot.fdfs.proto.mapper.FdfsColumn;
import org.apache.commons.lang3.Validate;

/**
 * 存储文件的路径信息
 * @author yuqih
 * Update by sudot on 2017-03-17 0017.
 */
public class StorePath {

    /** 解析路径 */
    private static final String SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR = "/";
    /** group */
    private static final String SPLIT_GROUP_NAME = "group";
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    /** 文件分组 */
    private String group;
    @FdfsColumn(index = 1, dynamicField = DynamicFieldType.allRestByte)
    /** 文件路径 */
    private String path;

    /**
     * 存储文件路径
     */
    public StorePath() {
        super();
    }

    /**
     * 存储文件路径
     * @param group
     * @param path
     */
    public StorePath(String group, String path) {
        super();
        this.group = group;
        this.path = path;
    }

    /**
     * 从Url当中解析存储路径对象
     * @param filePath 有效的路径样式为(groupName/path)或者
     *                 (http://ip/groupName/path),路径地址必须包含group
     * @return 返回路径信息
     */
    public static StorePath parseFullPath(String filePath) {
        Validate.notNull(filePath, "解析文件路径不能为空");
        // 获取group起始位置
        int groupStartPos = getGroupStartPos(filePath);
        String groupAndPath = filePath.substring(groupStartPos);

        int pos = groupAndPath.indexOf(SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR);
        if ((pos <= 0) || (pos == groupAndPath.length() - 1)) {
            throw new FdfsUnsupportStorePathException("解析文件路径错误,有效的路径样式为(groupName/path) 而当前解析路径为".concat(filePath));
        }

        String group = groupAndPath.substring(0, pos);
        String path = groupAndPath.substring(pos + 1);
        return new StorePath(group, path);
    }

    /**
     * 获得group起始位置
     * @param filePath
     */
    private static int getGroupStartPos(String filePath) {
        int pos = filePath.indexOf(SPLIT_GROUP_NAME);
        if (pos == -1) {
            throw new FdfsUnsupportStorePathException("解析文件路径错误,被解析路径url没有group,当前解析路径为".concat(filePath));
        }
        return pos;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public StorePath setGroup(String group) {
        this.group = group;
        return this;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public StorePath setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * 获取文件全路径
     * @return
     */
    public String getFullPath() {
        return this.group.concat(SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR).concat(this.path);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StorePath [group=" + group + ", path=" + path + "]";
    }

}
