package com.zuesshopbackend.category.Export;

import com.zuesshop.entity.Category;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class CategoryExcelExporter extends AbstractExporter {
    public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"application/octet-stream",".xlsx","categories_");

        writeHeaderLine();
        writeDataLines(listCategory);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public CategoryExcelExporter(){
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Categories");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "Category ID",cellStyle);
        createCell(row, 1, "Category Name",cellStyle);
    }

    private void writeDataLines(List<Category> listCategory) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(Category category : listCategory){
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;

            createDataCell(row,columnIndex++,category.getId(),cellStyle);
            createDataCell(row,columnIndex++,category.getName(),cellStyle);
        }
    }

    private void createDataCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }else{
            cell.setCellValue((String)value);
        }

        cell.setCellStyle(cellStyle);
    }
}
