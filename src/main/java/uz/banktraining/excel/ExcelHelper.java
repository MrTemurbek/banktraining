package uz.banktraining.excel;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.entity.Participants;


public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "1";
    private static final String PATH= "./src/main/resources/upload/certificate_";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }


    public static List<Participants> excelToTutorials(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Participants> tutorials = new ArrayList<Participants>();

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
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            participants.setName(currentCell.getStringCellValue());
                            break;

                        case 1:
                            participants.setSurname(currentCell.getStringCellValue());
                            break;

                        case 2:
                            participants.setCourse(currentCell.getStringCellValue());
                            break;

                        case 3:
                            double number =currentCell.getNumericCellValue();
                            participants.setNumber(Double.toString(number));
                            break;
                        case 4:
                            participants.setCertificateID(currentCell.getStringCellValue());
                            break;
                        case 5:
                            participants.setCertificateDate(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }
                    participants.setFileName(PATH+participants.getCertificateID());
                    participants.setCreatedAt(new Date());
                    cellIdx++;
                }

                tutorials.add(participants);
            }

            workbook.close();

            return tutorials;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
