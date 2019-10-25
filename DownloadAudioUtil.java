package com.creditease.honeybot.utils;

import java.util.Base64;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.creditease.honeybot.entity.HuayunRecordEntity;

public class DownloadAudioUtil {

	private static Logger logger = LoggerFactory.getLogger(DownloadAudioUtil.class);
	
	public static void getTalkRecordAudio(JSONObject getRecordParam, String taskId , String url ,CloseableHttpClient client) {

		try {

			if(getRecordParam == null){  //普强下载
                 String recordStr = HttpUtils.getAudio(url, client);
                 if(recordStr.length()==0){  //由于录音生成延迟，延迟30s在下载一次
                	 Thread.sleep(30000);
                	 recordStr = HttpUtils.getAudio(url, client);
                 }
                 logger.info(taskId+"普强获取录音接口返回" + recordStr.length());
                 byte[] fileContent = Base64.getDecoder().decode(recordStr);
                 JSONObject  jsonObj = new JSONObject();
                 jsonObj.put("fileName", taskId+".wav");  //存储文件名
                 jsonObj.put("clientId", taskId);  //clientId batchId 确定文件上传目录 可分别取值
                 jsonObj.put("batchId", taskId);
                 Cm5FileUtil.uploadFile(fileContent, jsonObj);
                 //TODO
//                 TaskAudio taskAudio = new TaskAudio();
//                 taskAudio.setTaskId(taskId);
//                 taskAudio.setAudio(recordStr);
//                 taskAudio.setCreateionDate(new Date());
//                 taskAudio.setUpdateDate(new Date());
//                 taskaudioDao.insert(taskAudio);
			}else{//华云下载
				String getRecordStr = HttpUtils.getPageHTMLByPost(url, getRecordParam.toJSONString(), client);
				HuayunRecordEntity recordEntity = JSONObject.parseObject(getRecordStr, HuayunRecordEntity.class);
				if(recordEntity != null && "0".equals(recordEntity.getStatus()) && recordEntity.getData().length == 0){
					Thread.sleep(30000);
					getRecordStr = HttpUtils.getPageHTMLByPost(url, getRecordParam.toJSONString(), client);
					recordEntity = JSONObject.parseObject(getRecordStr, HuayunRecordEntity.class);
				}
				logger.info(taskId + "华云获取录音接口返回" + recordEntity.toString());
	
				if (recordEntity != null && "0".equals(recordEntity.getStatus())) {
					/* 新版本获取录音开始 */
					String record = Base64.getEncoder().encodeToString(recordEntity.getData());
					/* 新版本获取录音结束 */
//					TaskAudio taskAudio = new TaskAudio();
//					taskAudio.setTaskId(taskId);
//					taskAudio.setAudio(record);
//					taskAudio.setCreateionDate(new Date());
//					taskAudio.setUpdateDate(new Date());
//					taskaudioDao.insert(taskAudio);
				} else {
					logger.error(taskId + "获取录音失败,参数:" + getRecordParam.toJSONString() + ",返回:" + getRecordStr);
					
				}
			}
		} catch (Exception e) {
			logger.error(taskId, e);
		}
	}
	
	
	
}
