package tech.oxymen.seaweedfs.core.file;

import java.io.Serializable;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import cn.hutool.core.util.StrUtil;

public class FileHandleStatus implements Serializable{

    private String fileId;
    private long lastModified;
    private String fileName;
    private String contentType;
    private long size;
    private String fileUrl;
    private String extension;
    




    public FileHandleStatus(String fileId, long lastModified, String fileName, String contentType, long size) {
        this.fileId = fileId;
        this.lastModified = lastModified;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;


        if (StrUtil.isNotEmpty(this.contentType)) {
            MimeType type;
            try {
                type = MimeTypes.getDefaultMimeTypes().forName(this.contentType);

                this.extension = type.getExtension();
            } catch (MimeTypeException e) {
                e.printStackTrace();
            }
            
        }
    }

    public FileHandleStatus(String fileId, long size) {
        this.fileId = fileId;
        this.size = size;
    }


    

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public FileHandleStatus(long size) {
        this.size = size;
    }

    public FileHandleStatus(String fileId, long size,String publicUrl) {
        this.fileId = fileId;
        this.size = size;
        this.fileUrl = publicUrl+"/"+fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public long getSize() {
        return size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "FileHandleStatus [contentType=" + contentType + ", extension=" + extension + ", fileId=" + fileId
                + ", fileName=" + fileName + ", fileUrl=" + fileUrl + ", lastModified=" + lastModified + ", size="
                + size + "]";
    }

    
    
}
