package com.creditease.honeybot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.creditease.honeybot.vo.FileContentHolder;

public class ExcelUtil {
	
	private ExcelUtil() {}

	public static FileContentHolder read(String fileName, InputStream bytes) {
		List<String> titleRow = new ArrayList<>();
		List<List<String>> dataRows = new ArrayList<>();

		Workbook workbook = null;
		
		// 读取excel
		try {
			if(fileName.endsWith(".xls")){
				workbook = new HSSFWorkbook(bytes);
			} else if(fileName.endsWith(".xlsx")){
//				workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
				workbook = new XSSFWorkbook(bytes);

			} else{
				throw new RuntimeException("错误的文件类型：" + fileName);
			}
			
			Sheet sheet = workbook.getSheetAt(0);

			Row firstRow = sheet.getRow(0);
			int colCount = firstRow.getPhysicalNumberOfCells();
			for(int i=0; i<colCount; i++) {
				titleRow.add(getCellValue(firstRow, i));
			}
			
			int totalRowCount = sheet.getPhysicalNumberOfRows();
			for(int i=1; i<totalRowCount; i++) {
				Row row = sheet.getRow(i);
				if(row==null) continue;
				List<String> rowData = new ArrayList<>();
				for(int j=0; j<colCount; j++) {
					rowData.add(getCellValue(row, j));
				}
				if(allEmpty(rowData)) {
					continue;
				}
				dataRows.add(rowData);
			}
		} catch (IOException e) {
			throw new RuntimeException("读取excel异常", e);
		} finally {
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
		
		FileContentHolder fch = new FileContentHolder();
		fch.setFileName(fileName);
		fch.setTitleRow(titleRow);
		fch.setDataRow(dataRows);
		fch.setTotal(dataRows.size());
		
		return fch;
	}
	
	private static boolean allEmpty(List<String> rowData) {
		return rowData.stream().allMatch(val -> StringUtils.isBlank(val));
	}

	private static String getCellValue(Row row, int cellnum) {
		Cell cell = row.getCell(cellnum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		String value = "";
		switch (cell.getCellType()) {
		case STRING:
			value = cell.getStringCellValue(); // 文本
			break;
		case NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)){
                value = new SimpleDateFormat("yyyy-MM-dd").format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())); // 日期
            } else {
            	value = String.valueOf(cell.getNumericCellValue()); // 金额
            }
			break;
		case BLANK:
			value = "";
			break;
		default:
			value = "";
			break;
			// throw new RuntimeException("非法的 celltype, celltype=" +
			// currentCell.getCellType());
		}
		if(value.endsWith(".0") ) {
			value = value.replace(".0", "");
		}
		return value;
	}
	
	
	public static byte[] write(List<String> title, Collection<List<String>> rowList) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Sheet1");
			int rowNum = 0;
			
			rowNum = writeRow(sheet, rowNum, title, createHeaderStyle(workbook));
			
			for (List<String> cols : rowList) {
			    rowNum = writeRow(sheet, rowNum, cols, null);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			workbook.close();
			
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
	}

	private static CellStyle createHeaderStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short)10);
		font.setBold(true);
		style.setFont(font);
		return style;
	}

	private static int writeRow(XSSFSheet sheet, int rowNum, List<String> cols, CellStyle style) {
		Row row = sheet.createRow(rowNum++);
		int colNum = 0;
		for (String val : cols) {
		    Cell cell = row.createCell(colNum++);
		    cell.setCellValue(val);
		    cell.setCellStyle(style);
		}
		return rowNum;
	}

}
