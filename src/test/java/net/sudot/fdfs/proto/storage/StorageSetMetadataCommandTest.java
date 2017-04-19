package net.sudot.fdfs.proto.storage;

import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.MateData;
import net.sudot.fdfs.domain.StorePath;
import net.sudot.fdfs.proto.StorageCommandTestBase;
import net.sudot.fdfs.proto.storage.enums.StorageMetdataSetType;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * 文件上传命令测试
 * @author tobato
 * @author sudot on 2017-04-18 0018.
 */
public class StorageSetMetadataCommandTest extends StorageCommandTestBase {

    /**
     * 文件上传测试
     */
    @Test
    public void testStorageSetMetadataCommand() {
        // 上传主文件
        StorePath path = execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
        Set<MateData> firstMateData = getFirstMateData();

        // 如果第一次设置MataData使用MERGE后台会报出错误(因为后台要做一次查询)，然后再增加MataData
        // 所以尽量第一次设置的时候采用OverWrite的方式进行设置
        logger.debug("--第一次做测试OVERWRITE-----");
        StorageSetMetadataCommand command = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                firstMateData, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        executeStoreCmd(command);
        logger.debug("--设置文件元数据结果-----");

        StorageGetMetadataCommand getCommand = new StorageGetMetadataCommand(path.getGroup(), path.getPath());
        Set<MateData> firstMateDataResult = executeStoreCmd(getCommand);
        logger.debug("--获取文件元数据结果-----{}", firstMateDataResult);
        assertEquals(firstMateData.size(), firstMateDataResult.size());

        logger.debug("--第二次做测试FLAG_MERGE-----");
        Set<MateData> mergeMateData = getMergeMateData();
        StorageSetMetadataCommand mergeCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                mergeMateData, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        executeStoreCmd(mergeCommand);
        logger.debug("--设置文件元数据结果-----");

        Set<MateData> mergeMateDataResult = executeStoreCmd(getCommand);
        logger.debug("--获取文件元数据结果-----{}", mergeMateDataResult);
        assertEquals(firstMateData.size() + 1, mergeMateDataResult.size());

        logger.debug("--第三次做测试OverWrite-----");
        Set<MateData> overWriteMateData = getOverWriteMateData();
        StorageSetMetadataCommand overWriteCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                overWriteMateData, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        executeStoreCmd(overWriteCommand);
        logger.debug("--设置文件元数据结果-----");

        Set<MateData> overWriteDataResult = executeStoreCmd(getCommand);
        logger.debug("--获取文件元数据结果-----{}", overWriteDataResult);
        assertEquals(overWriteMateData.size(), overWriteDataResult.size());

        executeStoreCmd(new StorageDeleteFileCommand(path.getGroup(), path.getPath()));
    }

    /**
     * 获取初始化MateDate
     * @return
     */
    private Set<MateData> getFirstMateData() {
        Set<MateData> metaDataSet = new HashSet<MateData>();

        metaDataSet.add(new MateData("width", "800"));
        metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
        metaDataSet.add(new MateData("author", "FirstMateData"));
        return metaDataSet;
    }

    /**
     * 获取需要合并的MateDate
     * 没有的条目增加，有则条目覆盖 MERGE
     * @return
     */
    private Set<MateData> getMergeMateData() {
        Set<MateData> metaDataSet = new HashSet<MateData>();

        metaDataSet.add(new MateData("heigth", "600"));
        metaDataSet.add(new MateData("author", "MergeMateData"));
        return metaDataSet;
    }

    /**
     * 获取需要覆盖的MateData
     * @return
     */
    private Set<MateData> getOverWriteMateData() {
        Set<MateData> metaDataSet = new HashSet<MateData>();

        metaDataSet.add(new MateData("width", "700"));
        metaDataSet.add(new MateData("heigth", "800"));
        metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
        metaDataSet.add(new MateData("test", "tobato"));
        metaDataSet.add(new MateData("author", "OverWriteData"));
        return metaDataSet;
    }

}
