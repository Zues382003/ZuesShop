package com.zuesshopbackend.brand.Export;

import com.zuesshop.entity.Brand;
import com.zuesshop.entity.Category;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class BrandCsvExporter extends AbstractExporter {
    public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv",".csv","brands_");

        ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Brand ID", "Brand Name", "Categories"};
        String[] fieldName = {"id","name","categories"};

        writer.writeHeader(csvHeader);

        for(Brand brand : listBrands){
            writer.write(brand,fieldName);
        }

        writer.close();
    }
}
