package net.sudot.fdfs.proto.storage.enums;

/**
 * 元数据设置方式
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public enum StorageMetaDataSetType {

    /** 覆盖:新的条目集合覆盖旧的条目集合(类似删除后重建) */
    STORAGE_SET_METADATA_FLAG_OVERWRITE((byte) 'O'),
    /** 合并:没有的条目增加,有则条目覆盖 */
    STORAGE_SET_METADATA_FLAG_MERGE((byte) 'M');

    private byte type;

    StorageMetaDataSetType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

}
