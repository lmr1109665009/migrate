/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DocFileApiController
 * Author:   lmr
 * Date:     2018/9/17 11:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.chen.demo.controller;

import com.chen.common.attachment.impl.FtpAttachmentHandler;
import com.chen.common.component.ResponseMessage;
import com.chen.common.uploader.UploaderHandler;
import com.chen.common.utils.StringUtil;
import com.chen.demo.model.DocFile;
import com.chen.demo.model.SysFile;
import com.chen.demo.service.DocFileService;
import io.minio.errors.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author lmr
 * @create 2018/9/17
 * @since 1.0.0
 */
@RestController
@RequestMapping("/me/newDocFile/")
public class DocFileApiController {
    private static final Logger log = LogManager.getLogger(DocFileApiController.class);
        @Value("${fs.docFile.groupId}")
        private Long groupId;

        @Autowired
        private DocFileService docFileService;

        @Autowired
        private UploaderHandler uploaderHandler;

        /**路径类型， 文件柜 **/
        public static final String PATHTYPE_FILING_CABINET="filingcabinet";

        /**路径类型， 流程文件 **/
        public static final String PATHTYPE_PROCESS_DOCUMENT="processdocument";

        /**路径类型， 归档文件 **/
        public static final String PATHTYPE_ARCHIVED_FILE="archivedfile";

        /**minio云存储标识 **/
        public static final String MINIO_SAVETYPE = "minio.saveType";

        /**ftp 存储标识 **/
        public static final String FTP_SAVETYPE = "file.saveType";

        /**minio默认的村粗桶 **/
        public static final String MINIO_BUCKET = "minio.bucket";
        @Autowired
        private FtpAttachmentHandler ftpAttachmentHandler;
        @Autowired
        private com.chen.common.attachment.impl.CloudAttachmentHandler cloudAttachmentHandler;
        @RequestMapping(value = "migrate")
        public ResponseMessage migrate(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
            List<DocFile> docFileList = docFileService.listAll();
            String path="";
            try {

                for (DocFile docFile:docFileList) {
                    if(docFile.getPath()!=null&&!(docFile.getPath().contains(PATHTYPE_FILING_CABINET)||docFile.getPath().contains(PATHTYPE_PROCESS_DOCUMENT)||docFile.getPath().contains(PATHTYPE_ARCHIVED_FILE))){
                        String sqlfileName = docFile.getName();
                        String eid = docFile.getEid();
                        if (StringUtils.isBlank(sqlfileName))
                            return null;
                        // imgPath为原文件名
                        int idx = sqlfileName.lastIndexOf(".");
                        // 文件后缀
                        String extention = sqlfileName.substring(idx);
                        Date date = new java.util.Date(System.currentTimeMillis());
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        String time = fmt.format(date);
                        String pathType="";
                        // 新的文件名(日期+后缀)
                        sqlfileName = time + extention;
                        if(docFile.getClassify()==40){
                            pathType=PATHTYPE_ARCHIVED_FILE;
                        }else{
                            pathType= PATHTYPE_FILING_CABINET;
                        }
                        path = eid+"/"+pathType+"/"+sqlfileName;

                        this.downAndUp(path,docFile);
                        docFileService.updatePath(path,docFile.getId());
                    }
                }
            }catch (Exception e){
                return ResponseMessage.fail("失败"+e);
            }
            return ResponseMessage.success("成功");
        }

        public void downAndUp(String path,DocFile docFile) throws IOException {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            InputStream inputStream=null;
            try {

                String fileName = docFile.getName();
                String filePath = docFile.getPath();
                filePath = URLDecoder.decode(filePath, "UTF-8");
                // 如果是Linux,所有反斜杠转成斜杠
                filePath = filePath.replace("\\", System.getProperty("file.separator"));

                if (StringUtils.isBlank(fileName)) {
                    fileName = filePath.substring(filePath.lastIndexOf(System.getProperty("file.separator")) + 1);
                } else {
                    // 判断名称是否有文件类型，没有就加上
                    if (fileName.split("\\.").length < 2) {
                        String[] ds = filePath.split("\\.");
                        fileName = fileName + "." + ds[ds.length - 1];
                    }
                }

                /*
                 * if (vers.indexOf("Chrome") != -1 && vers.indexOf("Mobile") != -1)
                 * { fileName = fileName.toString(); } else {
                 */
                fileName = StringUtil.encodingString(fileName, "utf-8", "ISO-8859-1");
                // }
                SysFile sysFile = new SysFile();
                sysFile.setFilePath(filePath);
                ftpAttachmentHandler.download(sysFile, swapStream);
                inputStream = this.parse(swapStream);
                uploaderHandler.upload(path, inputStream);
                // response.setContentLength(download(filePath, outStream));
            } catch (Exception e) {
                log.error("获取文件失败".getBytes("utf-8"));
            } finally {
                if (swapStream != null) {
                    swapStream.close();
                    swapStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            }
        }

    /**
     * OutputStream 转为InputStream
     * @param out
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream parse(OutputStream out) throws Exception
    {
        ByteArrayOutputStream   baos=new ByteArrayOutputStream();
        baos=(ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }
}