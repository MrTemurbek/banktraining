package uz.banktraining.pdf;

import java.io.IOException;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;

@Component
public class PDFHelper {
    public void pdfCreator(String name, String ID, String link){
        try {

            PdfDocument pdf = new PdfDocument(new PdfReader("template.pdf"), new PdfWriter("pdf/certificate_"+ID+".pdf"));
            Document document = new Document(pdf);

            //Certificate ID
            Paragraph p = new Paragraph(ID).setFontSize(14f).setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLDITALIC)).setUnderline();
            p.setFixedPosition(445f, 311.2f, 1000);
            document.add(p);

            //USER's NAME
            Paragraph paragraphName = new Paragraph(name).setFontSize(36f).setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLDITALIC));
            paragraphName.setFixedPosition(350, 265, 1000);
            paragraphName.setFontColor(new DeviceRgb(0, 0, 199));
            document.add(paragraphName);


            //QR CODE SUCCESS
            BarcodeQRCode my_code = new BarcodeQRCode(link);
            PdfFormXObject barcodeObject = my_code.createFormXObject(ColorConstants.BLACK, pdf);
            Image barcodeImage = new Image(barcodeObject).setWidth(125f).setHeight(125f);
            barcodeImage.setFixedPosition(675f, 40f);
            document.add(barcodeImage);
            document.close();
        }
        catch (IOException e){
            System.err.println("Error while creating pdf : "+e);
            throw new RuntimeException("PDF");
        }
    }

    public void changeNameAndLocation(String id, String name, String location){
        try {
            String filename = ("changed/"+id+" "+name);
            PdfDocument pdf = new PdfDocument(new PdfReader(location.substring(2)+".pdf"), new PdfWriter(filename+".pdf"));
            Document document = new Document(pdf);
            document.close();
        }
        catch (Exception e){
            System.err.println("Error while creating pdf v2: "+e);
            throw new RuntimeException("PDF");
        }
    }


}

