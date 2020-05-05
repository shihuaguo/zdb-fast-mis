package com.zdb;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

import static org.apache.tomcat.util.file.ConfigFileLoader.getInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelImportTest {
	@SneakyThrows
	@Test
	public void testExcel2003NoModel() throws FileNotFoundException {
		File f= new File("C:\\Users\\Administrator\\Desktop\\企业模板.xls") ;
		String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
		InputStream inputStream = new FileInputStream(f);
		try {
			// 解析每行结果在listener中处理
			ExcelListener listener = new ExcelListener();

			ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
			excelReader.readAll();
			excelReader.finish();
		} catch (Exception e) {

		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
