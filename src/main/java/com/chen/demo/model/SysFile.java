/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: SysFile
 * Author:   lmr
 * Date:     2018/9/17 14:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.chen.demo.model;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 对象功能:附件 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:csx
 * 创建时间:2011-11-26 18:19:16
 */
@SuppressWarnings("serial")
@XmlRootElement(name="sysFile")
@XmlAccessorType(XmlAccessType.NONE)
public class SysFile {

    /**
     * 文件已经删除 [value=1]
     */
    public static Short FILE_DEL = 1;

    /**
     * 文件存在[value=0]
     */
    public static Short FILE_NOT_DEL = 0;

    /**
     * 匿名用户
     */
    public static String FILE_UPLOAD_UNKNOWN = "unknown";

    /**
     * 其他分类
     */
    public static String FILETYPE_OTHERS="others";


    public static final String ATTACHMENT_DIRECTORY="attachFiles";

    // fileId
    @XmlAttribute
    protected Long fileId;
    // 分类ID
    @XmlAttribute
    protected Long typeId;
    // 文件名
    @XmlAttribute
    protected String fileName;
    // 文件路径
    @XmlAttribute
    protected String filePath;
    // 创建时间
    @XmlAttribute
    protected java.util.Date createtime;
    // 扩展名
    @XmlAttribute
    protected String ext;
    // 附件类型
    @XmlAttribute
    protected String fileType;
    // 说明
    @XmlAttribute
    protected String note;
    // creatorId
    @XmlAttribute
    protected Long creatorId;
    // 上传者
    @XmlAttribute
    protected String creator;
    // totalBytes
    @XmlAttribute
    protected Long totalBytes;
    // 1=已删除
    @XmlAttribute
    protected Short delFlag;
    // 附件内容
    @XmlAttribute
    protected byte[]  fileBlob;

    protected String typeName;

    protected String bucket;

    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    /**
     * 返回 fileId
     * @return
     */
    public Long getFileId() {
        return fileId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    /**
     * 返回 分类ID
     * @return
     */
    public Long getTypeId() {
        return typeId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * 返回 文件名
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    /**
     * 返回 文件路径
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    public void setCreatetime(java.util.Date createtime) {
        this.createtime = createtime;
    }
    /**
     * 返回 创建时间
     * @return
     */
    public java.util.Date getCreatetime() {
        return createtime;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
    /**
     * 返回 扩展名
     * @return
     */
    public String getExt() {
        return ext;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    /**
     * 返回 附件类型
     * @return
     */
    public String getFileType() {
        return fileType;
    }

    public void setNote(String note) {
        this.note = note;
    }
    /**
     * 返回 说明
     * @return
     */
    public String getNote() {
        return note;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
    /**
     * 返回 creatorId
     * @return
     */
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     * 返回 上传者
     * @return
     */
    public String getCreator() {
        return creator;
    }

    public void setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
    }
    /**
     * 返回 totalBytes
     * @return
     */
    public Long getTotalBytes() {
        return totalBytes;
    }

    public void setDelFlag(Short delFlag) {
        this.delFlag = delFlag;
    }
    /**
     * 返回 1=已删除
     * @return
     */
    public Short getDelFlag() {
        return delFlag;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * 返回附件二进制流
     * @return
     */
    public byte[]  getFileBlob()
    {
        return fileBlob;
    }
    public void setFileBlob(byte[] fileBlob)
    {
        this.fileBlob = fileBlob;
    }


}