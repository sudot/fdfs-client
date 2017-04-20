package net.sudot.fdfs.domain;

import net.sudot.fdfs.proto.OtherConstants;
import net.sudot.fdfs.proto.mapper.FdfsColumn;

/**
 * fastdfs中group的状态信息
 * @author yuqih
 * @author tobato
 * @author sudot on 2017-04-20 0020.
 */
public class GroupState {

    /** name of this group */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN + 1)
    String groupName;
    /** total disk storage in MB */
    @FdfsColumn(index = 1)
    long totalMB;
    /** free disk space in MB */
    @FdfsColumn(index = 2)
    long freeMB;
    /** trunk free space in MB */
    @FdfsColumn(index = 3)
    long trunkFreeMB;
    /** storage server count */
    @FdfsColumn(index = 4)
    int storageCount;
    /** storage server port */
    @FdfsColumn(index = 5)
    int storagePort;
    /** storage server HTTP port */
    @FdfsColumn(index = 6)
    int storageHttpPort;
    /** active storage server count */
    @FdfsColumn(index = 7)
    int activeCount;
    /** current storage server index to upload file */
    @FdfsColumn(index = 8)
    int currentWriteServer;
    /** store base path count of each storage server */
    @FdfsColumn(index = 9)
    int storePathCount;
    /** sub dir count per store path */
    @FdfsColumn(index = 10)
    int subdirCountPerPath;
    /**
     * current trunk file id
     */
    @FdfsColumn(index = 11)
    int currentTrunkFileId;

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the totalMB
     */
    public long getTotalMB() {
        return totalMB;
    }

    /**
     * @param totalMB the totalMB to set
     */
    public void setTotalMB(long totalMB) {
        this.totalMB = totalMB;
    }

    /**
     * @return the freeMB
     */
    public long getFreeMB() {
        return freeMB;
    }

    /**
     * @param freeMB the freeMB to set
     */
    public void setFreeMB(long freeMB) {
        this.freeMB = freeMB;
    }

    /**
     * @return the trunkFreeMB
     */
    public long getTrunkFreeMB() {
        return trunkFreeMB;
    }

    /**
     * @param trunkFreeMB the trunkFreeMB to set
     */
    public void setTrunkFreeMB(long trunkFreeMB) {
        this.trunkFreeMB = trunkFreeMB;
    }

    /**
     * @return the storageCount
     */
    public int getStorageCount() {
        return storageCount;
    }

    /**
     * @param storageCount the storageCount to set
     */
    public void setStorageCount(int storageCount) {
        this.storageCount = storageCount;
    }

    /**
     * @return the storagePort
     */
    public int getStoragePort() {
        return storagePort;
    }

    /**
     * @param storagePort the storagePort to set
     */
    public void setStoragePort(int storagePort) {
        this.storagePort = storagePort;
    }

    /**
     * @return the storageHttpPort
     */
    public int getStorageHttpPort() {
        return storageHttpPort;
    }

    /**
     * @param storageHttpPort the storageHttpPort to set
     */
    public void setStorageHttpPort(int storageHttpPort) {
        this.storageHttpPort = storageHttpPort;
    }

    /**
     * @return the activeCount
     */
    public int getActiveCount() {
        return activeCount;
    }

    /**
     * @param activeCount the activeCount to set
     */
    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    /**
     * @return the currentWriteServer
     */
    public int getCurrentWriteServer() {
        return currentWriteServer;
    }

    /**
     * @param currentWriteServer the currentWriteServer to set
     */
    public void setCurrentWriteServer(int currentWriteServer) {
        this.currentWriteServer = currentWriteServer;
    }

    /**
     * @return the storePathCount
     */
    public int getStorePathCount() {
        return storePathCount;
    }

    /**
     * @param storePathCount the storePathCount to set
     */
    public void setStorePathCount(int storePathCount) {
        this.storePathCount = storePathCount;
    }

    /**
     * @return the subdirCountPerPath
     */
    public int getSubdirCountPerPath() {
        return subdirCountPerPath;
    }

    /**
     * @param subdirCountPerPath the subdirCountPerPath to set
     */
    public void setSubdirCountPerPath(int subdirCountPerPath) {
        this.subdirCountPerPath = subdirCountPerPath;
    }

    /**
     * @return the currentTrunkFileId
     */
    public int getCurrentTrunkFileId() {
        return currentTrunkFileId;
    }

    /**
     * @param currentTrunkFileId the currentTrunkFileId to set
     */
    public void setCurrentTrunkFileId(int currentTrunkFileId) {
        this.currentTrunkFileId = currentTrunkFileId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupState{");
        sb.append("groupName='").append(groupName).append('\'');
        sb.append(", totalMB=").append(totalMB);
        sb.append(", freeMB=").append(freeMB);
        sb.append(", trunkFreeMB=").append(trunkFreeMB);
        sb.append(", storageCount=").append(storageCount);
        sb.append(", storagePort=").append(storagePort);
        sb.append(", storageHttpPort=").append(storageHttpPort);
        sb.append(", activeCount=").append(activeCount);
        sb.append(", currentWriteServer=").append(currentWriteServer);
        sb.append(", storePathCount=").append(storePathCount);
        sb.append(", subdirCountPerPath=").append(subdirCountPerPath);
        sb.append(", currentTrunkFileId=").append(currentTrunkFileId);
        sb.append('}');
        return sb.toString();
    }

}
