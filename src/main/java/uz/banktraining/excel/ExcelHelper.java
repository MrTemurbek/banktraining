package uz.banktraining.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.DocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.entity.Participants;
import uz.banktraining.pdf.PDFHelper;
import uz.banktraining.repo.ParticipantsRepository;


public class ExcelHelper {
    public String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String PATH = "./src/main/resources/upload/certificate_";

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }


    public static List<Participants> excelToTutorials(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Participants> participantsList = new ArrayList<Participants>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Participants participants = new Participants();

                int cellIdx = 0;
                boolean copy = true;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            if (currentCell.getStringCellValue() != null) {
                                participants.setName(currentCell.getStringCellValue());
                            } else {
                                cellIdx = 7;
                            }
                            break;

                        case 1:
                            if (currentCell.getStringCellValue() != null) {
                                participants.setSurname(currentCell.getStringCellValue());
                            } else {
                                cellIdx = 7;
                            }

                            break;

                        case 2:
                            if (currentCell.getStringCellValue() != null) {
                                participants.setCourse(currentCell.getStringCellValue());
                            } else {
                                cellIdx = 7;
                            }
                            break;

                        case 3:
                            double number = currentCell.getNumericCellValue();
                            String id = Double.toString(number);
                            participants.setNumber(id);

                            break;
                        case 4:
                            if (currentCell.getStringCellValue() != null) {

                                participants.setCertificateID(currentCell.getStringCellValue());

                            } else {
                                cellIdx = 7;
                            }
                            break;
                        case 5:
                            if (currentCell.getStringCellValue() != null) {
                                participants.setCertificateDate(currentCell.getStringCellValue());
                                participants.setFileName(PATH + participants.getCertificateID());
                                participants.setCreatedAt(new Date());
                            } else {
                                cellIdx = 7;
                            }
                            break;
                        case 7:
                            copy= false;
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (copy) {
                    new PDFHelper().pdfCreator(participants.getName(), participants.getSurname(), participants.getCertificateID(), participants.getCertificateDate(), participants.getCourse());
                    participantsList.add(participants);
                }
            }

            workbook.close();

            return participantsList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        } catch (DocumentException e) {
            throw new RuntimeException("fail to create pdf file: " + e.getMessage());
        }
    }
}
