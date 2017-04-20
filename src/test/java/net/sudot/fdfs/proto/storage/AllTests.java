package net.sudot.fdfs.proto.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({StorageAppendFileCommandTest.class, StorageDeleteFileCommandTest.class,
        StorageQueryFileInfoCommandTest.class, StorageUploadFileCommandTest.class,
        StorageUploadSlaveFileCommandTest.class, StorageSetMetaDataCommandTest.class,
        StorageDownloadCommandTest.class})
public class AllTests {
    // for test
}
