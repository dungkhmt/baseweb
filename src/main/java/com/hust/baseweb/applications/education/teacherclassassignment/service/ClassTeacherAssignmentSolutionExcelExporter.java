package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.entity.TeacherClassAssignmentSolution;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentSolutionModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ClassTeacherAssignmentSolutionExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ClassTeacherAssignmentSolutionModel> solution;

    public ClassTeacherAssignmentSolutionExcelExporter(List<ClassTeacherAssignmentSolutionModel> classTeacherAssignmentSolutionModels){
        this.solution = classTeacherAssignmentSolutionModels;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet = workbook.createSheet("DS phân công");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Mã lớp", style);
        createCell(row, 1, "Mã học phần", style);
        createCell(row, 2, "Tên học phần", style);
        createCell(row, 3, "Giáo viên", style);
        createCell(row, 4, "TKB", style);

    }
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    private void writeDataLines(){
        for(int i = 0; i < solution.size(); i++) {
            ClassTeacherAssignmentSolutionModel s = solution.get(i);
            Row row = sheet.createRow(i+1);

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(false);
            font.setFontHeight(16);
            style.setFont(font);

            createCell(row, 0, s.getClassCode(), style);
            createCell(row, 1, s.getCourseId(), style);
            createCell(row, 2, s.getCourseName(), style);
            createCell(row, 3, s.getTeacherName(), style);
            createCell(row, 4, s.getTimetable(), style);

        }

    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
        System.out.println("export: FINISHED write to response");

    }
    public ByteArrayInputStream toExcel(){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeHeaderLine();
            writeDataLines();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
