package com.zuesshopbackend.product.Export;

import com.zuesshop.entity.Category;
import com.zuesshop.entity.Product;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class ProductCsvExporter extends AbstractExporter {

    public void export(List<Product> listProduct, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv",".csv","products_");

        ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Product ID", "Product Name", "Brand", "Category", "InStock", "Cost($)", "Price($)", "Discount(%)"};
        String[] fieldName = {"id", "name", "brand", "category", "inStock", "cost", "price", "discountPercent"};

        writer.writeHeader(csvHeader);

        for(Product product : listProduct){
            writer.write(product,fieldName);
        }

        writer.close();
    }
}
