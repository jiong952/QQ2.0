package com.zjh.common;

import java.io.Serializable;

/**
 * @author 张俊鸿
 * @since 2022-05-08 15:01
 * @description: 文件类 这里做了简化，不需要写双方电脑的本地路径，涉及隐私
 */
public class FileMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] fileBytes;
    private int fileLen;
    private String fileName;
    /**
     * 文件发送者的本地路径
     */
    private String formPath;

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getFormPath() {
        return formPath;
    }

    public void setFormPath(String formPath) {
        this.formPath = formPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
