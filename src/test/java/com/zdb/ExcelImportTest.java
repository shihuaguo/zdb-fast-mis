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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.tomcat.util.file.ConfigFileLoader.getInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelImportTest {
	@SneakyThrows
	@Test
	public void testExcel2003NoModel() {
		File f= new File("C:\\Users\\Administrator\\Desktop\\企业模板.xls") ;
		String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
		InputStream inputStream = new FileInputStream(f);
		try {
			// 解析每行结果在listener中处理
			ExcelListener listener = new ExcelListener();

			ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
//			ReadSheet readSheet1 = EasyExcel.readSheet(0).build();
//			ReadSheet readSheet2 = EasyExcel.readSheet(1).build();
//			ReadSheet readSheet3 = EasyExcel.readSheet(2).build();
//			ReadSheet readSheet4 = EasyExcel.readSheet(3).build();
//			ReadSheet readSheet5 = EasyExcel.readSheet(4).build();
//			ReadSheet readSheet6 = EasyExcel.readSheet(5).build();
//			ReadSheet readSheet7 = EasyExcel.readSheet(6).build();
//			ReadSheet readSheet8 = EasyExcel.readSheet(7).build();
//			ReadSheet readSheet9 = EasyExcel.readSheet(8).build();
//			ReadSheet readSheet10= EasyExcel.readSheet(9).build();
//			ReadSheet readSheet11= EasyExcel.readSheet(10).build();
//			ReadSheet readSheet12 = EasyExcel.readSheet(11).build();
//			excelReader.read(
//					readSheet1,
//					readSheet2,
//					readSheet3,
//					readSheet4,
//					readSheet5,
//					readSheet6,
//					readSheet7,
//					readSheet8,
//					readSheet9,
//					readSheet10,
//					readSheet11,
//					readSheet12
//			);
//
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
