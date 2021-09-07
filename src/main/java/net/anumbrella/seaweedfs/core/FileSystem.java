package net.anumbrella.seaweedfs.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import net.anumbrella.seaweedfs.core.file.FileHandleStatus;

public class FileSystem {

    private final Logger logger = LoggerFactory.getLogger(FileSystem.class);

    private FileSource fileSource = null;
    private ConnectionProperties connectionProperties = null;

    public FileSystem(String host, int masterPort, int maxConnection) {

        connectionProperties = new ConnectionProperties.Builder().host(host).port(masterPort)
                .maxConnection(maxConnection).build();

        fileSource = new FileSource();
        fileSource.setProperties(connectionProperties);
        fileSource.startup();
    }

    public void stop() {
        if (fileSource != null) {
            fileSource.shutdown();
        }
    }

    public FileHandleStatus saveFile(File file) {

        return this.saveFile(file, null, null, null, null, null, null, 0, 0, 0, null);
    }

    /**
     * 设定保存问的有效期
     * 
     * @param file
     * @param ttl  - examples, 3m: 3 minutes, 4h: 4 hours, 5d: 5 days, 6w: 6 weeks,
     *             7M: 7 months, 8y: 8 years
     * @return
     */
    public FileHandleStatus saveFile(File file, String ttl) {

        return this.saveFile(file, null, ttl, null, null, null, null, 0, 0, 0, null);
    }

    public FileHandleStatus saveFile(File file, String fileName, String ttl, String operation, String dataCenter,
            String rack, String collection,

            int replicateOnDiffDataCenterCount, int replicateOnDiffRackCount, int replicateOnSameRackCount,
            Integer maxChunkSize) {

        String fileName2 = StrUtil.isNotEmpty(fileName) ? fileName : FileUtil.getName(file);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            Connection connection = fileSource.getConnection();
            FileTemplate fileTemplate = new FileTemplate(connection);

            if (StrUtil.isNotEmpty(ttl)) {
                fileTemplate.setTimeToLive(ttl);
            }

            if (StrUtil.isNotEmpty(operation)) {
                fileTemplate.setOperation(operation);
            }

            if (StrUtil.isNotEmpty(dataCenter)) {
                fileTemplate.setDataCenter(dataCenter);
            }

            if (StrUtil.isNotEmpty(collection)) {
                fileTemplate.setCollection(collection);
            }

            if (replicateOnSameRackCount != 0) {
                fileTemplate.setSameRackCount(replicateOnSameRackCount);
            }

            if (replicateOnDiffDataCenterCount != 0) {
                fileTemplate.setDiffDataCenterCount(replicateOnDiffDataCenterCount);
            }

            if (replicateOnDiffRackCount != 0) {
                fileTemplate.setSameRackCount(replicateOnSameRackCount);
            }

            if (StrUtil.isNotEmpty(rack)) {
                fileTemplate.setRack(rack);
            }

            if (maxChunkSize != null) {
                fileTemplate.setMaxChunkSize(maxChunkSize);
            }

            FileHandleStatus fStatus = fileTemplate.saveFileByStream(fileName2, fileInputStream);

            fileInputStream.close();

            this.logger.info("File {} has been saved to File System", fileName2);
            this.logger.info("File ID: {} \n FileName: {} \n, FileURL: {}, \n File Size: {} \n", fStatus.getFileId(),
                    fStatus.getFileName(), fStatus.getFileUrl(), fStatus.getSize());
            this.logger.info("TTL: {}\n Operation: {}\n Data Center: {}\n Collection: {}\n Replication: {}", ttl,
                    operation, dataCenter, collection,
                    "" + replicateOnDiffDataCenterCount + replicateOnDiffRackCount + replicateOnSameRackCount, rack);
            return fStatus;

        } catch (Exception e) {
            this.logger.error("An error happened when saving file {} to File System.", fileName2);
            this.logger.error("{}", e.getLocalizedMessage());
            return null;
        }
    }

    public void deleteFile(String fileId) {

        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);

        try {
            fileTemplate.deleteFile(fileId);
            this.logger.info("File with id {} has been deleted.", fileId);
        } catch (IOException e) {
            this.logger.error("Error happened when deleting file {}", fileId);
            this.logger.error("{}", e.getLocalizedMessage());
        }
    }

    public void deleteFiles(ArrayList<String> fileIdList) {

        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);

        try {
            fileTemplate.deleteFiles(fileIdList);
            this.logger.info("Files with id list {} have been deleted.", ArrayUtil.toString(fileIdList));
        } catch (IOException e) {
            this.logger.error("Error happened when deleting files {}", ArrayUtil.toString(fileIdList));
            this.logger.error("{}", e.getLocalizedMessage());
        }
    }

    public void deleteFileByPath(String url) {

        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);

        try {
            fileTemplate.deleteFileByFiler(url);
            this.logger.info("File with url {} have been deleted.", url);
        } catch (IOException e) {
            this.logger.error("Error happened when deleting file {}", url);
            this.logger.error("{}", e.getLocalizedMessage());
        }
    }

    public File getFileWithOriginalName(String fileId) {

        return this.getFile(fileId, null, null, true);
    }

    public File getFile(String fileId) {

        return this.getFile(fileId, null, null, null);
    }

    public File getFile(String fileId, String newName) {

        return this.getFile(fileId, newName, null, null);
    }

    public File getFile(String fileId, Boolean nameWithDate) {

        return this.getFile(fileId, null, nameWithDate, null);
    }

    public File getFile(String fileId, String newName, Boolean nameWithDate) {

        return this.getFile(fileId, newName, nameWithDate, null);
    }

    public File getFile(String fileId, String newName, Boolean nameWithDate, Boolean useOriginalName) {

        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);

        try (InputStream is = fileTemplate.getFileStream(fileId).getInputStream()) {

            FileHandleStatus fhs = this.getFileStatus(fileId);
            File tempFile;

            if (useOriginalName != null) {
                tempFile = new File(FileUtil.getTmpDirPath() + "/" + fhs.getFileName());
            } else {

                if (StrUtil.isNotEmpty(newName)) {
                    if (nameWithDate != null) {
                        tempFile = new File(FileUtil.getTmpDirPath() + "/" + newName + "_"
                                + DateUtil.format(new Date(), "yyyyMMddHHmm") + fhs.getExtension());
                    } else {
                        tempFile = new File(FileUtil.getTmpDirPath() + "/" + newName + fhs.getExtension());
                    }
                } else {
                    if (nameWithDate != null) {
                        tempFile = File.createTempFile(DateUtil.format(new Date(), "yyyyMMddHHmm"), fhs.getFileName());
                    } else {
                        tempFile = File.createTempFile(RandomUtil.randomString(4), fhs.getFileName());
                    }
                }
            }

            FileUtil.writeFromStream(is, tempFile);

            this.logger.info("File with id {} has been retrieved.", fileId);
            this.logger.info("File Handler Status: {}", fhs.toString());

            return tempFile;

        } catch (Exception e) {
            this.logger.error("Error happened when retrieving file {}", fileId);
            this.logger.error("{}", e.getLocalizedMessage());
            return null;
        }

    }

    public FileHandleStatus getFileStatus(String fileId) {

        Connection connection = fileSource.getConnection();
        FileTemplate fileTemplate = new FileTemplate(connection);

        try {

            FileHandleStatus fhs = fileTemplate.getFileStatus(fileId);

            this.logger.info("File Handler Status with id {} has been retrieved.", fileId);
            this.logger.info("File Handler Status: {}", fhs.toString());

            return fhs;
        } catch (Exception e) {

            this.logger.error("Error happened when retrieving file {}", fileId);
            this.logger.error("{}", e.getLocalizedMessage());
            return null;
        }

    }
}
