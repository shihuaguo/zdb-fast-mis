package com.zdb.modules.ueditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdb.common.exception.RRException;
import com.zdb.common.utils.R;
import com.zdb.modules.sys.controller.AbstractController;

/**
 * Ueditor后端配置
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/ueditor")
public class UeditorController extends AbstractController{ 
	private static final Logger logger = LoggerFactory.getLogger(UeditorController.class);
	
	//上传文件的保存路径
	@Value("${ueditor.upload-dir}")
	private String uploadDir;
	
	@Autowired
	private IRichTextInfoService richTextInfoService;
	
	//从文件读取配置
	private static String readFromFile(){
		//URL url = UeditorController.class.getResource("/config.json");
		//logger.info("url={}", url);
		InputStream is = UeditorController.class.getResourceAsStream("/config.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder config = new StringBuilder();
		String line;
		try {
			while((line = br.readLine()) != null){
				config.append(line);
			}
		} catch (IOException e) {
			logger.error("", e);
		}
		return config.toString();
	}

	@ResponseBody
	@RequestMapping(value="/req", method=RequestMethod.GET)
	public String config(@RequestParam Map<String, Object> params){
		logger.info("request ueditor's config, params = {}", params);
		String config = readFromFile();
		logger.info("request ueditor's config = {}", config);
		return config;
	}
	
	
	private Response uploadFile(MultipartFile file) throws IOException {
		String config = readFromFile();
		JSONObject jo = JSON.parseObject(config);
		//logger.info("ueditor's config={}", jo);
		logger.info("upload dir={}", uploadDir);
		
		//获得上传文件的一些信息
		String fileName = file.getOriginalFilename();
		long fileSize = file.getSize();
		String prefix = FilenameUtils.getPrefix(fileName);
		String suffix = "." + FilenameUtils.getExtension(fileName);
		//String baseFileName = FilenameUtils.getBaseName(fileName);
		logger.info("上传文件,OriginalFilename={}, fileSize={}, prefix={}, suffix={}", fileName, fileSize, prefix, suffix);
		
		//解析config.json中的图片保存路径
		String imagePathFormat = jo.getString("imagePathFormat");
		logger.info("before parse, imagePathFormat={}", imagePathFormat);
		//加上文件的后缀
		String imageParsedPath = UeditorUtil.parsePathFormat(imagePathFormat) + suffix;
		logger.info("after parse, imageParsedPath={}", imageParsedPath);
		
		//在服务器上保存完整路径
		String imageFullPath = uploadDir + imageParsedPath;
		logger.info("image full path = {}", imageFullPath);
		File imageFile = new File(imageFullPath);
		//在服务器上保存的目录
		if(!imageFile.getParentFile().exists() && !imageFile.getParentFile().mkdirs()) {
			throw new RRException("create dir " + imageFile.getParent() + " failed");
		}
		
		FileUtils.copyToFile(file.getInputStream(), imageFile);
		logger.info("create image file's path={}", imageFile.getPath());
		
		return new Response(imageFile.getName(),fileName,String.valueOf(fileSize),Response.STATE_SUCCESS,suffix,imageParsedPath);
	}
	
	/**
	 * 上传文件
	 */
	@RequestMapping(value="/req", method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	//@RequiresPermissions("sys:oss:all")
	@ResponseBody
	public void upload(@RequestParam("upfile") MultipartFile file, HttpServletResponse response) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		Response res =  uploadFile(file);
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Type", "text/html");
		response.getWriter().print(JSON.toJSONString(res));
	}
	
	@ResponseBody
	@RequestMapping(value="/query", method=RequestMethod.GET)
	public Object queryRichTextInfo(RichTextInfo rti){
		logger.info("query RichTextInfo, params = {}", rti);
		RichTextInfo info = richTextInfoService.queryByType(rti);
		return R.ok().put("info", info);
	}
	
	@ResponseBody
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public Object saveRichTextInfo(RichTextInfo rti){
		logger.info("save RichTextInfo, params = {}", JSON.toJSONString(rti));
		long userId = getUserId();
		int n;
		if(rti.getId() == null || rti.getId() <= 0) {
			rti.setId(null);
			rti.setCreateBy((int)userId);
			n = richTextInfoService.insertSelective(rti);
		}else {
			rti.setUpdateBy((int)userId);
			n = richTextInfoService.updateByPrimaryKeySelective(rti);
		}
		return n > 0 ? R.ok() : R.error("保存失败");
	}
}
