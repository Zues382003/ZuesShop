package com.zuesshopbackend.product.Export;

import com.zuesshop.entity.Category;
import com.zuesshop.entity.Product;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class ProductExcelExporter extends AbstractExporter {
    public void export(List<Product> listProduct, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"application/octet-stream",".xlsx","products_");

        writeHeaderLine();
        writeDataLines(listProduct);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ProductExcelExporter(){
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Products");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "Product ID",cellStyle);
        createCell(row, 1, "Product Name",cellStyle);
        createCell(row, 2, "Brand",cellStyle);
        createCell(row, 3, "Category",cellStyle);
        createCell(row, 4, "InStock",cellStyle);
        createCell(row, 5, "Cost($)",cellStyle);
        createCell(row, 6, "Price($)",cellStyle);
        createCell(row, 7, "Discount(%)",cellStyle);

    }

    private void writeDataLines(List<Product> listCategory) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(Product product : listCategory){
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;

            createDataCell(row,columnIndex++,product.getId(),cellStyle);
            createDataCell(row,columnIndex++,product.getName(),cellStyle);
            createDataCell(row,columnIndex++,product.getBrand().getName(),cellStyle);
            createDataCell(row,columnIndex++,product.getCategory().getName(),cellStyle);
            createDataCell(row,columnIndex++,product.isInStock(),cellStyle);
            createDataCell(row,columnIndex++,product.getCost(),cellStyle);
            createDataCell(row,columnIndex++,product.getPrice(),cellStyle);
            createDataCell(row,columnIndex++,product.getDiscountPercent(),cellStyle);
        }
    }

    private void createDataCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }else if(value instanceof Boolean){
            cell.setCellValue((boolean) value);
        } else if (value instanceof Float){
            cell.setCellValue((float) value);
        }else{
            cell.setCellValue((String)value);
        }

        cell.setCellStyle(cellStyle);
    }
}
