package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.GroupState;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorageState;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.proto.ErrorCodeConstants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * unit test for TrackerClientService
 * @author tobato
 */
public class TrackerClientTest extends FastdfsTestBase {

    /** 日志 */
    private static Logger LOGGER = LoggerFactory.getLogger(TrackerClientTest.class);

    @Resource
    private TrackerClient trackerClient;

    @Test
    public void testGetStoreStorage() {
        LOGGER.debug("testGetStoreStorage..");
        StorageNode client = trackerClient.getStoreStorage();
        assertNotNull(client.getInetSocketAddress());
        LOGGER.debug("result={}", client);

    }

    @Test
    public void testGetStoreStorageByGroup() {
        LOGGER.debug("testGetStoreStorageByGroup..");
        StorageNode client = trackerClient.getStoreStorage(TestConstants.DEFAULT_GROUP);
        assertNotNull(client.getInetSocketAddress());
        LOGGER.debug("result={}", client);
    }

    @Test
    public void testListGroups() {
        LOGGER.debug("testListGroups..");
        List<GroupState> list = trackerClient.listGroups();
        assertNotNull(list);
        LOGGER.debug("result={}", list);
    }

    @Test
    public void testListStoragesByGroup() {
        LOGGER.debug("testListStoragesByGroup..");
        List<StorageState> list = trackerClient.listStorages(TestConstants.DEFAULT_GROUP);
        assertNotNull(list);
        LOGGER.debug("result={}", list);
    }

    @Test
    public void testListStoragesByGroupAndIp() {
        LOGGER.debug("testListStoragesByGroupAndIp..");
        List<StorageState> list = trackerClient.listStorages(TestConstants.DEFAULT_GROUP,
                TestConstants.DEFAULT_STORAGE_IP);
        assertNotNull(list);
        LOGGER.debug("result={}", list);
    }

    @Test
    public void testDeleteStorage() {
        LOGGER.debug("testDeleteStorage..");
        try {
            trackerClient.deleteStorage(TestConstants.DEFAULT_GROUP,
                    TestConstants.DEFAULT_STORAGE_IP);
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsServerException);
            assertTrue(((FdfsServerException) e).getErrorCode() == ErrorCodeConstants.ERR_NO_EBUSY);
        }
        LOGGER.debug("testDeleteStorage..done");
    }

}
