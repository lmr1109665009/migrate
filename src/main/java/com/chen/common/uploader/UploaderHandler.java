package com.chen.common.uploader;


import io.minio.errors.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 上传文件接口
 * @user 子华
 * @created 2018/8/2
 */
public interface UploaderHandler {
    //是否允许过期
    public static String KEY_FILE_CAN_EXPIRE="isCanExpire";
    /**
     * 上传文件
     * @param filepath 文件存储路径
     * @param inputStream 文件流
     * @param extraParams 其他参数
     */
    public void upload(String filepath, InputStream inputStream, Map<String,Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;
    public void upload(String filepath, InputStream inputStream) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;
    public void upload(String filename,String dir, InputStream inputStream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;
    public void upload(String filename,String dir, InputStream inputStream,Map<String,Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;

    /**
     * 获取文件地址
     * @param path
     * @return
     */
    public String getFileUrl(String path) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException;
    public String getFileUrl(String path,Map<String,Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, io.minio.errors.InternalException;

    /**
     * 下载文件
     * @param path
     */
    public void download(String path) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;
    public void download(String path,Map<String,Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;
    public void download(String path,boolean isDownload) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;
    public void download(String path,boolean isDownload,Map<String,Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;

    /**
     * 移动文件
     * @param srcPath
     * @param destPath
     * @param extraParams
     */
    public void move(String srcPath, String destPath, Map<String,Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;
    public void move(String srcPath,String destPath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;

    /**
     * 复制文件
     * @param srcPath
     * @param destPath
     * @param extraParams
     */
    public void copy(String srcPath, String destPath, Map<String,Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;
    public void copy(String srcPath, String destPath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;

    /**
     * 删除文件
     * @param path
     * @param extraParams
     */
    public void delete(String path, Map<String,Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException;
    public void delete(String path) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException;

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public boolean isExist(String path);
    public boolean isExist(String path,Map<String,Object> extraParams);
}
