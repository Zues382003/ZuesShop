package com.zuesshopbackend.brand.Export;

import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class BrandExcelExporter extends AbstractExporter {
    public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"application/octet-stream",".xlsx","brands_");

        writeHeaderLine();
        writeDataLines(listBrands);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public BrandExcelExporter(){
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Brands");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "Brand ID",cellStyle);
        createCell(row, 1, "Brand Name",cellStyle);
        createCell(row, 2, "Categories",cellStyle);
    }

    private void writeDataLines(List<Brand> listBrands) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(Brand brand : listBrands){
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;

            createDataCell(row,columnIndex++,brand.getId(),cellStyle);
            createDataCell(row,columnIndex++,brand.getName(),cellStyle);
            createDataCell(row,columnIndex++,brand.getCategories(),cellStyle);
        }
    }

    private void createDataCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }else if (value instanceof Set) {
            Set<Category> categorySet = (Set<Category>) value;
            String categoryString = categorySet.stream()
                    .map(Category::getName)
                    .collect(Collectors.joining(", "));
            cell.setCellValue(categoryString);
        } else {
            cell.setCellValue((String)value);
        }

        cell.setCellStyle(cellStyle);
    }
}
