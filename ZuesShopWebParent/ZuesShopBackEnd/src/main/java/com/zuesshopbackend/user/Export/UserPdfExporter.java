package com.zuesshopbackend.user.Export;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.zuesshop.entity.User;
import com.zuesshopbackend.AbstractExporter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf", "user_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.red);

        Paragraph paragraph = new Paragraph("List of User",font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(new Paragraph(paragraph));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(15);
        table.setWidths(new float[] {1.0f, 4.0f, 2.5f, 2.5f, 3.0f, 1.7f });

        writeTableHeader(table);
        writeTableData(table, listUsers);

        document.add(table);

        document.close();
    }

    private void writeTableData(PdfPTable table, List<User> listUsers) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);

        for(User user : listUsers){
            cell.setPhrase(new Phrase(String.valueOf(user.getId())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(user.getEmail()));
            table.addCell(cell);
            cell.setPhrase(new Phrase(user.getFirstName()));
            table.addCell(cell);
            cell.setPhrase(new Phrase(user.getLastName()));
            table.addCell(cell);
            cell.setPhrase(new Phrase(user.getRoles().toString()));
            table.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(user.isEnabled())));
            table.addCell(cell);

        }
    }

    private void writeTableHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.pink);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.white);

        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("E-mail",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("First Name",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Last Name",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Roles",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Enabled",font));
        table.addCell(cell);
    }
}
