package com.chen.common.attachment;

import com.chen.demo.model.SysFile;

import java.io.InputStream;
import java.io.OutputStream;


public interface AttachmentHandler {
	/**
	 * 获取附件处理器类型
	 * @return
	 */
	String getType();
	/**
	 * 删除附件
	 * @param sysFile
	 * @throws Exception
	 */
	void remove(SysFile sysFile) throws Exception;
	/**
	 * 上传附件
	 * @param sysFile
	 * @param inputStream
	 * @throws Exception
	 */
	void upload(SysFile sysFile, InputStream inputStream) throws Exception;
	/**
	 * 下载附件
	 * @param sysFile
	 * @param outStream
	 * @throws Exception
	 */
	void download(SysFile sysFile, OutputStream outStream) throws Exception;
	void download(SysFile sysFile, OutputStream outStream,boolean isCloseOutStream) throws Exception;

	String getFileUrl(SysFile sysFile, Integer expireDate) throws Exception;
}
