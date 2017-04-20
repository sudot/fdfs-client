package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.MetaData;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import net.sudot.fdfs.proto.storage.enums.StorageMetaDataSetType;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * 文件上传命令测试
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageSetMetaDataCommandTest extends StorageCommandTestBase {

    /**
     * 文件上传测试
     */
    @Test
    public void testStorageSetMetadataCommand() {
        // 上传主文件
        StorePath path = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
        Set<MetaData> firstMetaData = getFirstMateData();

        // 如果第一次设置MetaData使用MERGE后台会报出错误(因为后台要做一次查询)，然后再增加MetaData
        // 所以尽量第一次设置的时候采用OverWrite的方式进行设置
        logger.debug("--第一次做测试OVERWRITE-----");
        StorageSetMetaDataCommand command = new StorageSetMetaDataCommand(path.getGroup(), path.getPath(),
                firstMetaData, StorageMetaDataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        executeStoreCmd(command);
        logger.debug("--设置文件元数据结果-----");

        StorageGetMetaDataCommand getCommand = new StorageGetMetaDataCommand(path.getGroup(), path.getPath());
        Set<MetaData> firstMetaDataResult = executeStoreCmd(getCommand);
        logger.debug("--获取文件元数据结果-----{}", firstMetaDataResult);
        assertEquals(firstMetaData.size(), firstMetaDataResult.size());

        logger.debug("--第二次做测试FLAG_MERGE-----");
        Set<MetaData> mergeMetaData = getMergeMateData();
        StorageSetMetaDataCommand mergeCommand = new StorageSetMetaDataCommand(path.getGroup(), path.getPath(),
                mergeMetaData, StorageMetaDataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        executeStoreCmd(mergeCommand);
        logger.debug("--设置文件元数据结果-----");

        Set<MetaData> mergeMetaDataResult = executeStoreCmd(getCommand);
        logger.debug("--获取文件元数据结果-----{}", mergeMetaDataResult);
        assertEquals(firstMetaData.size() + 1, mergeMetaDataResult.size());

        logger.debug("--第三次做测试OverWrite-----");
        Set<MetaData> overWriteMetaData = getOverWriteMateData();
        StorageSetMetaDataCommand overWriteCommand = new StorageSetMetaDataCommand(path.getGroup(), path.getPath(),
                overWriteMetaData, StorageMetaDataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        executeStoreCmd(overWriteCommand);
        logger.debug("--设置文件元数据结果-----");

        Set<MetaData> overWriteDataResult = executeStoreCmd(getCommand);
        logger.debug("--获取文件元数据结果-----{}", overWriteDataResult);
        assertEquals(overWriteMetaData.size(), overWriteDataResult.size());

        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
    }

    /**
     * 获取初始化MateDate
     * @return
     */
    private Set<MetaData> getFirstMateData() {
        Set<MetaData> metaDataSet = new HashSet<MetaData>();

        metaDataSet.add(new MetaData("width", "800"));
        metaDataSet.add(new MetaData("bgcolor", "FFFFFF"));
        metaDataSet.add(new MetaData("author", "FirstMateData"));
        return metaDataSet;
    }

    /**
     * 获取需要合并的MateDate
     * 没有的条目增加，有则条目覆盖 MERGE
     * @return
     */
    private Set<MetaData> getMergeMateData() {
        Set<MetaData> metaDataSet = new HashSet<MetaData>();

        metaDataSet.add(new MetaData("heigth", "600"));
        metaDataSet.add(new MetaData("author", "MergeMateData"));
        return metaDataSet;
    }

    /**
     * 获取需要覆盖的MateData
     * @return
     */
    private Set<MetaData> getOverWriteMateData() {
        Set<MetaData> metaDataSet = new HashSet<MetaData>();

        metaDataSet.add(new MetaData("width", "700"));
        metaDataSet.add(new MetaData("heigth", "800"));
        metaDataSet.add(new MetaData("bgcolor", "FFFFFF"));
        metaDataSet.add(new MetaData("test", "tobato"));
        metaDataSet.add(new MetaData("author", "OverWriteData"));
        return metaDataSet;
    }

}
