package uz.banktraining.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import uz.banktraining.entity.Participants;


@Component
public class ExcelHelper {
    private static final String PATH = "./pdf/certificate_";
    private static final String LINK = "banktraining.uz/userCertificates/";

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
                            if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().isBlank()) {
                                participants.setName(currentCell.getStringCellValue());
                            } else {
                                copy = false;
                            }
                            break;

                        case 1:
                            try {
                                int number = (int) currentCell.getNumericCellValue();
                                String id = Integer.toString(number);
                                if (!id.isBlank()) {
                                    participants.setNumber(id);
                                } else {
                                    copy = false;
                                }
                            } catch (Exception e) {
                                String id = currentCell.getStringCellValue();
                                if (!id.isEmpty() && !id.isBlank()) {
                                    participants.setNumber(id);
                                } else {
                                    copy = false;
                                }
                            }
                            break;
                        case 2:
                            if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().isBlank()) {
                                participants.setCertificateID(currentCell.getStringCellValue());
                                participants.setPath(PATH + participants.getCertificateID());
                                participants.setLink("http://" + LINK + participants.getCertificateID());
                                participants.setCreatedAt(new Date());
                            } else {
                                copy = false;
                            }
                            break;
                    }
                    cellIdx++;
                }
                if (copy) {
                    participantsList.add(participants);
                }
            }

            workbook.close();

            return participantsList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }


    public static List<String> excelGetIds(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<String> participantsIds = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                for (Cell currentCell : currentRow) {
                    if (!currentCell.getStringCellValue().isEmpty() && !currentCell.getStringCellValue().isBlank()) {
                        participantsIds.add(currentCell.getStringCellValue());
                    }
                }
            }

            workbook.close();

            return participantsIds;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file v2: " + e.getMessage());
        }
    }
}
