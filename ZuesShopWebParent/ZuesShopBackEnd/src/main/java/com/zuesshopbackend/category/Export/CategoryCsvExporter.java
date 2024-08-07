package com.zuesshopbackend.category.Export;

import com.zuesshop.entity.Category;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {

    public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv",".csv","categories_");

        ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID", "Category Name"};
        String[] fieldName = {"id","name"};

        writer.writeHeader(csvHeader);

        for(Category category : listCategory){
            writer.write(category,fieldName);
        }

        writer.close();
    }
}
