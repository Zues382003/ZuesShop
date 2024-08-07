package com.zuesshopbackend.user.Export;

import com.zuesshop.entity.User;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv",".csv","user_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        //response.getWriter() trả về một đối tượng PrintWriter để ghi dữ liệu trực tiếp vào phản hồi HTTP.
        //CsvPreference để định cấu hình cho quá trình ghi CSV. STANDARD_PREFERENCE đại diện cho một tập hợp các thiết lập tiêu chuẩn cho việc ghi CSV, bao gồm ký tự phân cách, ký tự trích dẫn và các cài đặt khác.

        String[] csvHeader = {"User ID","E-mail","First Name","Last Name","Roles","Enable"};
        String[] fieldName = {"id","email","firstName","lastName","roles","enabled"};

        csvBeanWriter.writeHeader(csvHeader);

        for(User user: listUsers){
            csvBeanWriter.write(user,fieldName);
        }

        csvBeanWriter.close();
    }
}
