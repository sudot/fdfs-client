package net.sudot.fdfs.proto.tracker;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.sudot.fdfs.domain.GroupState;
import org.junit.Test;

import net.sudot.fdfs.proto.CommandTestBase;

/**
 * 列举存储目录分组情况
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsCommandTest  extends  CommandTestBase {

    @Test
    public void test() {
        List<GroupState> list = executeTrackerCmd(new TrackerListGroupsCommand());
        assertTrue(list.size() > 0);
        LOGGER.debug("-----列举存储服务器分组状态处理结果-----");
        LOGGER.debug(list.toString());
    }

}
