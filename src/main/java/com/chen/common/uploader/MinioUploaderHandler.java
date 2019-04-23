package com.chen.common.uploader;

import io.minio.MinioClient;
import io.minio.errors.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xmlpull.v1.XmlPullParserException;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @user 子华
 * @created 2018/8/2
 */
public class MinioUploaderHandler implements UploaderHandler {
    private String defaultBucket;
    private Integer expire;
    @Autowired
    private MinioClient client;

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
        this.defaultBucket=env.getProperty("uploader.minio.bucket");
        this.expire=env.getProperty("uploader.minio.urlExpire",Integer.class);
    }

    @Override
    public void upload(String filepath, InputStream inputStream, Map<String, Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        String bucket=getBucket(extraParams);
        boolean isExist = client.bucketExists(bucket);
        if(!isExist){
            client.makeBucket(bucket);
        }
        String contentType="application/octet-stream";
        if (extraParams!=null&&extraParams.get("contentType")!=null){
            contentType= (String) extraParams.get("contentType");
        }
        client.putObject(bucket,filepath,inputStream,contentType);
        if (inputStream!=null){
            inputStream.close();
        }
    }

    @Override
    public void upload(String filepath, InputStream inputStream) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        upload(filepath,inputStream,null);
    }

    @Override
    public void upload(String filename, String dir, InputStream inputStream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        this.upload(dir+ File.separator+filename,inputStream);
    }

    @Override
    public void upload(String filename, String dir, InputStream inputStream, Map<String, Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        this.upload(dir+ File.separator+filename,inputStream,extraParams);
    }

    @Override
    public String getFileUrl(String path) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidExpiresRangeException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException {
        return this.getFileUrl(path,null);
    }

    @Override
    public String getFileUrl(String path, Map<String, Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, io.minio.errors.InternalException {
        String bucket=getBucket(extraParams);
        boolean isCanExpire=true;
        if (extraParams != null && extraParams.get(UploaderHandler.KEY_FILE_CAN_EXPIRE)!=null){
            isCanExpire= (boolean) extraParams.get(UploaderHandler.KEY_FILE_CAN_EXPIRE);
        }
        if (isCanExpire){
            return client.presignedGetObject(bucket, path, expire);
        }else {
            return client.getObjectUrl(bucket, path);
        }
    }

    @Override
    public void download(String path) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        this.download(path,false);
    }

    @Override
    public void download(String path, Map<String, Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        this.download(path,false,extraParams);
    }

    @Override
    public void download(String path, boolean isDownload) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        this.download(path,isDownload,null);
    }

    @Override
    public void download(String path, boolean isDownload, Map<String, Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        response.setCharacterEncoding("utf-8");
        String bucket=getBucket(extraParams);
        InputStream inputStream=client.getObject(bucket,path);
        String contentType="application/octet-stream";
        if (extraParams!=null&&extraParams.get("contentType")!=null){
            contentType= (String) extraParams.get("contentType");
        }
        response.setContentType(contentType);
        if (isDownload){
            String attachName=path.substring(path.lastIndexOf('/')+1);
            if (extraParams!=null&&extraParams.get("attachName")!=null){
                attachName= (String) extraParams.get("attachName");
            }
            String agent=request.getHeader("User-Agent");
            if (agent.contains("MSIE")) {
                // IE浏览器
                attachName = URLEncoder.encode(attachName, "utf-8");
                attachName = attachName.replace("+", " ");
            } else if (agent.contains("Firefox")) {
                // 火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                attachName = "=?utf-8?B?" + base64Encoder.encode(attachName.getBytes("utf-8")) + "?=";
            } else if(agent.contains("Chrome")){
                attachName=new String(attachName.getBytes("utf-8"),"ISO8859-1");
            } else {
                // 其它浏览器
                attachName = URLEncoder.encode(attachName, "utf-8");
            }

            response.setHeader("Content-Disposition", "attachment;filename=\"" + attachName+"\"");
        }
        IOUtils.copy(inputStream,response.getOutputStream());
        if (inputStream!=null){
            inputStream.close();
        }
    }

    @Override
    public void move(String srcPath, String destPath, Map<String, Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        if (srcPath.equals(destPath)){
            return;
        }
        String srcBucket=defaultBucket;
        String destBucket=defaultBucket;
        if (extraParams!=null&&extraParams.get("srcBucket")!=null){
            srcBucket= (String) extraParams.get("srcBucket");
        }
        if (extraParams!=null&&extraParams.get("destBucket")!=null){
            destBucket= (String) extraParams.get("destBucket");
        }
        client.copyObject(srcBucket,srcPath,destBucket,destPath);
        client.removeObject(srcBucket,srcPath);
    }

    @Override
    public void move(String srcPath, String destPath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        this.move(srcPath,destPath,null);
    }

    @Override
    public void copy(String srcPath, String destPath, Map<String, Object> extraParams) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        if (srcPath.equals(destPath)){
            return;
        }
        String srcBucket=defaultBucket;
        String destBucket=defaultBucket;
        if (extraParams!=null&&extraParams.get("srcBucket")!=null){
            srcBucket= (String) extraParams.get("srcBucket");
        }
        if (extraParams!=null&&extraParams.get("destBucket")!=null){
            destBucket= (String) extraParams.get("destBucket");
        }
        client.copyObject(srcBucket,srcPath,destBucket,destPath);
    }

    @Override
    public void copy(String srcPath, String destPath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        this.copy(srcPath,destPath,null);
    }

    @Override
    public void delete(String path, Map<String, Object> extraParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, io.minio.errors.InvalidArgumentException, io.minio.errors.InternalException {
        String bucket=getBucket(extraParams);
        client.removeObject(bucket,path);
    }

    @Override
    public void delete(String path) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, io.minio.errors.InternalException, io.minio.errors.InvalidArgumentException {
        this.delete(path,null);
    }

    @Override
    public boolean isExist(String path) {
        return this.isExist(path,null);
    }

    @Override
    public boolean isExist(String path, Map<String, Object> extraParams) {
        String bucket=getBucket(extraParams);
        try {
            if (client.statObject(bucket,path)!=null){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获取存储桶
     * @param extraParams
     * @return
     */
    private String getBucket(Map<String, Object> extraParams){
        if (extraParams!=null&&extraParams.get("bucket")!=null){
            return (String) extraParams.get("bucket");
        }
        return defaultBucket;
    }
}
