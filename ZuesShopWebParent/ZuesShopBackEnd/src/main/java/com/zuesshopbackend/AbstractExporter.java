package com.zuesshopbackend;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {
    public void setResponseHeader(HttpServletResponse response, String contentType, String extension, String title) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = title + timestamp + extension;

        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        //Điều này sẽ cho trình duyệt biết rằng phản hồi trả về là một tệp CSV.

        String headerKey = "Content-Disposition";// để tải file về máy còn Content-Type là để xem trên web
        String headerValue = "attachment; filename=" + fileName;
        response.addHeader(headerKey, headerValue);
    }
}
