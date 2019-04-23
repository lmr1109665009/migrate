/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: c
 * Author:   lmr
 * Date:     2018/9/17 14:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.chen.common.config;



import com.chen.common.uploader.LocalFsUploaderHandler;
import com.chen.common.uploader.MinioUploaderHandler;
import com.chen.common.uploader.UploaderHandler;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @user 子华
 * @created 2018/8/2
 */
@Configuration
public class UploaderAutoConfigure {
    private String url;
    private Integer port;
    private String accessKey;
    private String secretKey;
    private String type;

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
        this.url=env.getProperty("uploader.minio.url");
        this.port=env.getProperty("uploader.minio.port",Integer.class);
        this.accessKey=env.getProperty("uploader.minio.accessKey");
        this.secretKey=env.getProperty("uploader.minio.secretKey");
        this.type=env.getProperty("uploader.type");
    }

    /**
     * 注册minio客户端
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    @Bean
    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(url, port, accessKey, secretKey);
    }

    /**
     * 注册上传handler
     * @return
     */
    @Bean
    public UploaderHandler getUploaderHandler(){
        switch (type){
            case "minio":
                return new MinioUploaderHandler();
            default:
                return new LocalFsUploaderHandler();
        }
    }
}
