package uz.banktraining.pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

@Component
public class PDFHelper {
    public void pdfCreator(String username, String surname, String ID, String courseDate, String course, String link) throws IOException, DocumentException {

        /* example inspired from "iText in action" (2006), chapter 2 */

//        PdfReader reader = new PdfReader("templates/template.pdf"); // input PDF

        PdfReader reader = new PdfReader("template.pdf"); // input PDF
        PdfStamper stamper = new PdfStamper(reader,
//                new FileOutputStream("src/main/resources/pdf/certificate_"+ID+".pdf")); // output PDF
                new FileOutputStream("certificate_"+ID+".pdf")); // output PDF
        BaseFont bfForAll = BaseFont.createFont(
                BaseFont.TIMES_BOLDITALIC, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        // set font

        //loop on pages (1-based)
        for (int i=1; i<=reader.getNumberOfPages(); i++){

            // get object for writing over the existing content;
            // you can also use getUnderContent for writing in the bottom layer
            PdfContentByte over = stamper.getOverContent(i);

            //CertificateID-done
            over.beginText();
            over.setFontAndSize(bfForAll, 14);
            over.setColorFill(BaseColor.BLACK);
            over.setTextMatrix(450, 315);
            over.newlineShowText(ID);
            over.endText();

            // Name/Surname
            over.beginText();
            over.setFontAndSize(bfForAll, 36);    // set font and size
            over.setRGBColorFill(0, 0, 194);
            over.setTextMatrix(350, 280);
            over.newlineShowText(username+"  "+ surname);
            over.endText();

            //Date
            ColumnText ct1 = new ColumnText(over);
            BaseColor color = new BaseColor(36,79,124);
            Font paragraphFont=new Font(bfForAll,18,-1,color);
            ct1.setSimpleColumn(150, 275, 825, 40);
            Paragraph paragraph2 = new Paragraph(courseDate,paragraphFont);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            ct1.addElement(paragraph2);
            ct1.go();

            //Course-Name
            String end = "mavzusidagi o’quv kursini tugatdi.";
            ColumnText ct = new ColumnText(over);

            ct.setSimpleColumn(150, 250, 825, 40);
            Paragraph paragraph = new Paragraph(course,paragraphFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            Paragraph endParagraph = new Paragraph(end,paragraphFont);
            endParagraph.setAlignment(Element.ALIGN_CENTER);
            ct.addElement(paragraph);
            ct.addElement(endParagraph);
            ct.go();

            //QR-code
            BarcodeQRCode my_code = new BarcodeQRCode(link, 125, 125, null);
            Image image = my_code.getImage();
            image.setAbsolutePosition(675f, 40f);
            over.addImage(image);
        }

        stamper.close();

    }


}

