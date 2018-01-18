package com.keep.sys.controller;

import com.keep.util.FileUtil;
import com.keep.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lance
 * on 2016/11/22.
 */
@Controller
public class ImgUploadController {
    private static  final Logger logger = LoggerFactory.getLogger(ImgUploadController.class);
    public static int CUS_PHOTO_WIDTH = 150;
    public static int CUS_PHOTO_HEIGHT = 150;
    public static PropertyUtil propertyUtil = PropertyUtil.getInstance("conf/application");
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static JsonParser jsonParser = new JsonParser();
    /**
     * kindeditor4.x编辑器中上传图片 返回ke中需要的url和error值 注意：同域中本方法直接返回json格式字符即可
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/imgk4", method = RequestMethod.POST)
    public String img(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            logger.info("request path : " + getPath(request));
            String referer = request.getHeader("referer");
            String projectName = request.getParameter("pname");
            Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?", Pattern.CASE_INSENSITIVE);
            Matcher mathcer = p.matcher(referer);
            logger.info("imgk4 referer:" + referer+ " pname " + projectName);
            if (mathcer.find()) {
                String callBackPath = mathcer.group();// 请求来源
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile imgFile = multipartRequest.getFile("imgFile");
                String[] paths = FileUtil.getSavePathByRequest(request);

                JsonObject json = FileUtil.saveImage(imgFile, paths);

                // 编辑器中需要返回完整的路径
                json.addProperty("url", propertyUtil.getProperty("import.root") + json.get("url").getAsString());
                // 同域时直接返回json即可无需redirect
               String url = "redirect:" + callBackPath + "/"+ projectName + "/kindeditor/plugins/image/redirect.html?s=" + json.toString() + "#" + json.toString();

                logger.info("imgk4 ok " + json.toString()+"call url :" + url);
                return url;
            } else {
                logger.info("imgk4 referer not find");
            }
        } catch (Exception e) {

            logger.error("imgk4 error", e);
        }
        return null;
    }

    private String getPath(HttpServletRequest request){

        String path = request.getContextPath();
        int port = request.getServerPort();
        String basePath = null;
        if (port==80){
            basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
        }else{
            basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        }
        return basePath;
    }
    /**
     * kindeditor4 单个按钮上传图片
     * kindeditor3.5中使用call_back.html 单个图片按钮时 上传方法集合，根据参数匹配属性文件 模块提供,返回的是图片的全路径
     * base:项目 param：模块 cusid.用户id
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/gok4", method = RequestMethod.POST)
    public String go(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            String referer = request.getHeader("referer");
            Pattern p = Pattern.compile("([a-z]*:(//[^/?#]+)?)?", Pattern.CASE_INSENSITIVE);
            Matcher mathcer = p.matcher(referer);
            if (mathcer.find()) {
                String callBackPath = mathcer.group();// 请求来源
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile imgFile = multipartRequest.getFile("fileupload");
                String[] paths = FileUtil.getSavePathByRequest(request);
                JsonObject json = FileUtil.saveImage(imgFile, paths);
                // 同域时直接返回json即可无需redirect
                String url = "redirect:" + callBackPath + "/kindeditor/plugins/image/redirect.html?s=" + json.toString() + "#" + json.toString();
                logger.info("++++gok4 return:" + url);
                return url;
            }
        } catch (Exception e) {
            logger.error("gok4 error", e);
        }
        return null;
    }
    /**
     * 删除文件
     *
     * @param files
     *            删除的文件数组["/a/aa.jpg","a/aa01.jpg","/a/aa01.jpg"]
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Map<String, Object> del(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "files") String files) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JsonArray jsonObject = jsonParser.parse(files).getAsJsonArray();
            logger.info("del file:" + jsonObject.toString());
            String types = request.getParameter("types");
            for (int i = 0; i < jsonObject.size(); i++) {
                if(types!=null && types.equals("swf")){
                    map.put(i + "", FileUtil.deleteSwfImage(jsonObject.get(i).getAsString()));
                }else{
                    map.put(i + "", FileUtil.deleteImageFile(jsonObject.get(i).getAsString()));
                }
            }
            return map;
        } catch (Exception e) {
            logger.error("del error", e);
        }
        return null;
    }

}
