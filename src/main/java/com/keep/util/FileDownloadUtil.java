package com.keep.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

public class FileDownloadUtil {

	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static final String LIBRARY_TYPE = "library";//文库类型
	public static final String LECTURE_NOTES_TYPE="lectureNotes";//讲议类型
	/**
	 * 执行下载
	 * @param request
	 * @param response
	 * @param zipFile 压缩文件
	 * @throws Exception
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File zipFile) throws Exception {
		String fileName = zipFile.getName();
		fileName = fileName(request,fileName.split("\\.")[0]);
		
		response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/zip");
        response.setHeader("Content-disposition", "attachment;"+fileName+".zip");
		
        BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;
		fis = new FileInputStream(zipFile.getPath());
		bis = new BufferedInputStream(fis);
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);

		int bytesRead = 0;
		byte[] buffer = new byte[5 * 1024];
		while ((bytesRead = fis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
		}
		bos.flush();
		bis.close();
		fos.flush();
		fis.close();
	}
	
	/**
	 * 下载
	 * @param request
	 * @param userId 用户ID
	 * @param dataId 数据ID
	 * @param fileName 文件名
	 * @param callback 回调
	 * @param resultMap 
	 * @param donwType 下载类型
	 * @return
	 * @throws IOException
	 */
	public static Object donwFile(
			HttpServletRequest request,
			Long userId,
			Long dataId, 
			String fileName, 
			String callback, 
			Map<String, Object> resultMap,
			String donwType)
			throws IOException {
		String fileUrl = (String) resultMap.get("message");
		if(fileUrl!=null && (!fileUrl.trim().endsWith(".zip") && !fileUrl.endsWith(".rar"))){
			int l = fileUrl.lastIndexOf(".");
			fileUrl = fileUrl.substring(0, l)+".pdf";
		}
		System.out.println("----------fileUrl:"+fileUrl);
		File _f = new File(FileUtil.rootpath +fileUrl);
		//File _f = new File("D:/work/周报-李明星 .xls");
		System.out.println("------------------_f:"+_f);
		if(_f.exists()){
			String zipPath = _f.getPath();
			File zipFile =null;
			if(!fileUrl.trim().endsWith(".zip") && !fileUrl.endsWith(".rar")){
				if(fileName!=null && fileName.trim().length()>0){
					if(fileName.length()>16){
						fileName = fileName.substring(0,17);
					}
					int len = zipPath.lastIndexOf("/");
					if(len==-1){
						len = zipPath.lastIndexOf("\\");
					}
					zipPath = zipPath.substring(0,len)+"/"+fileName+".zip";
				}else{
					int len = zipPath.lastIndexOf(".");
					zipPath =zipPath.substring(0, len)+".zip";
				}
				zipFile = FileUtil.doZip(_f.getPath(),zipPath);
			}else{
				zipFile = _f;
			}
			System.out.println(zipFile);
		String key = UUID.randomUUID().toString().substring(0,8)+"-"+System.currentTimeMillis();
			/*memCache.set(key, zipFile);*/
			resultMap = new HashMap<String,Object>();
			resultMap.put("success", true);
			resultMap.put("message","下载验证成功");
			resultMap.put("entity","/executeDownload?key="+key+"&userId="+userId+"&dataId="+dataId+"&donwType="+donwType);
			return callback + "(" + gson.toJson(resultMap) + ")";
		}else{
			System.out.println("donwLibrary()--文件不存在");
			resultMap.put("success", false);
			resultMap.put("message", "下载文件不存在");
			return callback + "(" + gson.toJson(resultMap) + ")";
		}
	}
	
	public static String fileName(HttpServletRequest request, String new_filename) {
        String userAgent =getUserAgent(request);
        String rtn = "filename=xteam";
        try {
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf("internet explorer") != -1) {
                    rtn = "filename=\"" + URLEncoder.encode(new_filename, "utf-8") + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf("opera") != -1) {
                    rtn = "filename*=" + new_filename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf("safari") != -1) {
                    rtn = "filename=" + new String(new_filename.getBytes("UTF-8"), "ISO8859-1") + "";
                }
                // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.indexOf("chrome") != -1) {
                    new_filename = new String(new_filename.getBytes("UTF-8"), "ISO8859-1");
                    rtn = "filename=\"" + new_filename + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf("firefox") != -1) {
                    new_filename = MimeUtility.encodeText(new_filename, "UTF8", "B");
                    rtn = "filename*=" + new_filename;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
	
	public static String getUserAgent(HttpServletRequest request) {
    	String uabrow = request.getHeader("User-Agent");//获取浏览器信息
    	UserAgent userAgent =UserAgent.parseUserAgentString(uabrow);
    	Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();
    	return browser.getName().toLowerCase()+";"+os.getName().toLowerCase();
    }
}
