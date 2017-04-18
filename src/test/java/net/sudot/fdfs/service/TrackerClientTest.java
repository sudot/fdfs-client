package net.sudot.fdfs.service;

import net.sudot.fdfs.FastdfsTestBase;
import net.sudot.fdfs.TestConstants;
import net.sudot.fdfs.domain.GroupState;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorageState;
import net.sudot.fdfs.exception.FdfsServerException;
import net.sudot.fdfs.proto.ErrorCodeConstants;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * unit test for TrackerClientService
 * @author tobato
 * Update by sudot on 2017-04-18 0018.
 */
public class TrackerClientTest extends FastdfsTestBase {
    @Test
    public void testGetStoreStorage() {
        logger.debug("testGetStoreStorage..");
        StorageNode client = trackerClient.getStoreStorage();
        assertNotNull(client.getInetSocketAddress());
        logger.debug("result={}", client);

    }

    @Test
    public void testGetStoreStorageByGroup() {
        logger.debug("testGetStoreStorageByGroup..");
        StorageNode client = trackerClient.getStoreStorage(TestConstants.DEFAULT_GROUP);
        assertNotNull(client.getInetSocketAddress());
        logger.debug("result={}", client);
    }

    @Test
    public void testListGroups() {
        logger.debug("testListGroups..");
        List<GroupState> list = trackerClient.listGroups();
        assertNotNull(list);
        logger.debug("result={}", list);
    }

    @Test
    public void testListStoragesByGroup() {
        logger.debug("testListStoragesByGroup..");
        List<StorageState> list = trackerClient.listStorages(TestConstants.DEFAULT_GROUP);
        assertNotNull(list);
        logger.debug("result={}", list);
    }

    @Test
    public void testListStoragesByGroupAndIp() {
        logger.debug("testListStoragesByGroupAndIp..");
        List<StorageState> list = trackerClient.listStorages(TestConstants.DEFAULT_GROUP,
                TestConstants.STORAGE_HOST);
        assertNotNull(list);
        logger.debug("result={}", list);
    }

    @Test
    public void testDeleteStorage() {
        logger.debug("testDeleteStorage..");
        try {
            trackerClient.deleteStorage(TestConstants.DEFAULT_GROUP,
                    TestConstants.STORAGE_HOST);
            fail("No exception thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof FdfsServerException);
            assertTrue(((FdfsServerException) e).getErrorCode() == ErrorCodeConstants.ERR_NO_EBUSY);
        }
        logger.debug("testDeleteStorage..done");
    }

}
