package uz.banktraining.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

@Component
public class PDFHelper {
    public void pdfCreator(String username, String surname, String ID, String courseDate, String course) throws IOException, DocumentException {

        /* example inspired from "iText in action" (2006), chapter 2 */

        PdfReader reader = new PdfReader("templates/template.pdf"); // input PDF
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream("src/main/resources/pdf/certificate_"+ID+".pdf")); // output PDF
        BaseFont bf = BaseFont.createFont(
                BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font

        //loop on pages (1-based)
        for (int i=1; i<=reader.getNumberOfPages(); i++){

            // get object for writing over the existing content;
            // you can also use getUnderContent for writing in the bottom layer
            PdfContentByte over = stamper.getOverContent(i);

            // write text
            over.beginText();
            over.setFontAndSize(bf, 36);    // set font and size
            over.setColorFill(ExtendedColor.BLUE);
            over.setTextMatrix(350, 280);

            // set x,y position (0,0 is at the bottom left)
            over.newlineShowText(username+" "+ surname);
            over.endText();

        }

        stamper.close();

    }


}

