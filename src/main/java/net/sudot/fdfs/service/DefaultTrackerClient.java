package net.sudot.fdfs.service;

import net.sudot.fdfs.conn.TrackerConnectionManager;
import net.sudot.fdfs.domain.GroupState;
import net.sudot.fdfs.domain.StorageNode;
import net.sudot.fdfs.domain.StorageNodeInfo;
import net.sudot.fdfs.domain.StorageState;
import net.sudot.fdfs.proto.tracker.TrackerDeleteStorageCommand;
import net.sudot.fdfs.proto.tracker.TrackerGetFetchStorageCommand;
import net.sudot.fdfs.proto.tracker.TrackerGetStoreStorageCommand;
import net.sudot.fdfs.proto.tracker.TrackerListGroupsCommand;
import net.sudot.fdfs.proto.tracker.TrackerListStoragesCommand;
import net.sudot.fdfs.util.Validate;

import java.util.List;

/**
 * 目录服务客户端实现
 *
 * @author sudot on 2017-04-17 0017.
 */
public class DefaultTrackerClient implements TrackerClient {

    private TrackerConnectionManager connectionManager;

    @Override
    public StorageNode getStoreStorage() {
        TrackerGetStoreStorageCommand command = new TrackerGetStoreStorageCommand();
        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public StorageNode getStoreStorage(String groupName) {
        TrackerGetStoreStorageCommand command;
        if (Validate.isBlank(groupName)) {
            command = new TrackerGetStoreStorageCommand();
        } else {
            command = new TrackerGetStoreStorageCommand(groupName);
        }

        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public StorageNodeInfo getFetchStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, false);
        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public StorageNodeInfo getUpdateStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, true);
        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public List<GroupState> listGroups() {
        TrackerListGroupsCommand command = new TrackerListGroupsCommand();
        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public List<StorageState> listStorages(String groupName) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName);
        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public List<StorageState> listStorages(String groupName, String storageIpAddr) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName, storageIpAddr);
        return connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public void deleteStorage(String groupName, String storageIpAddr) {
        TrackerDeleteStorageCommand command = new TrackerDeleteStorageCommand(groupName, storageIpAddr);
        connectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public TrackerConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public TrackerClient setConnectionManager(TrackerConnectionManager trackerConnectionManager) {
        this.connectionManager = trackerConnectionManager;
        return this;
    }
}
