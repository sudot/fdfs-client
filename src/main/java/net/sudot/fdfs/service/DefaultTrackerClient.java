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
 * Created by tangjialin on 2017-04-17 0017.
 */
public class DefaultTrackerClient implements TrackerClient {

    private TrackerConnectionManager trackerConnectionManager;

    /**
     * 获取存储节点
     */
    @Override
    public StorageNode getStoreStorage() {
        TrackerGetStoreStorageCommand command = new TrackerGetStoreStorageCommand();
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按组获取存储节点
     */
    @Override
    public StorageNode getStoreStorage(String groupName) {
        TrackerGetStoreStorageCommand command;
        if (Validate.isBlank(groupName)) {
            command = new TrackerGetStoreStorageCommand();
        } else {
            command = new TrackerGetStoreStorageCommand(groupName);
        }

        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 获取源服务器
     */
    @Override
    public StorageNodeInfo getFetchStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, false);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 获取更新服务器
     */
    @Override
    public StorageNodeInfo getUpdateStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, true);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 列出组
     */
    @Override
    public List<GroupState> listGroups() {
        TrackerListGroupsCommand command = new TrackerListGroupsCommand();
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按组列出存储状态
     */
    @Override
    public List<StorageState> listStorages(String groupName) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按ip列出存储状态
     */
    @Override
    public List<StorageState> listStorages(String groupName, String storageIpAddr) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName, storageIpAddr);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 删除存储节点
     */
    @Override
    public void deleteStorage(String groupName, String storageIpAddr) {
        TrackerDeleteStorageCommand command = new TrackerDeleteStorageCommand(groupName, storageIpAddr);
        trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    public TrackerConnectionManager getTrackerConnectionManager() {
        return trackerConnectionManager;
    }

    public DefaultTrackerClient setTrackerConnectionManager(TrackerConnectionManager trackerConnectionManager) {
        this.trackerConnectionManager = trackerConnectionManager;
        return this;
    }
}
