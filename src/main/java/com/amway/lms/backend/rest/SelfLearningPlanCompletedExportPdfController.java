package com.amway.lms.backend.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.model.CourseSessionExport;
import com.amway.lms.backend.service.LearningDashboardService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class to support for trainee to export pdf list course section completed
 *
 * @author Albert (albert@enclave.vn)
 */
@RestController
@RequestMapping("/api/v1/")
public class SelfLearningPlanCompletedExportPdfController {

    @Autowired
    private LearningDashboardService learningDashboardService;

    @RequestMapping(value = "/self-learning-completed/{userId}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody void exporSelfLearningPdf(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("userId") Integer userId) throws IOException {

        final ServletContext servletContext = request.getSession().getServletContext();
        final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        final String temperotyFilePath = tempDirectory.getAbsolutePath();

        String fileName = "self-learning-completed.pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + fileName);

        try {
            List<CourseSessionExport> courseSessionList = learningDashboardService.getAllCourseSessionExport(userId);

            createPdf(temperotyFilePath + "\\" + fileName, courseSessionList);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos = convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
        } catch (Exception e1) {
            Utils.generateFailureResponseEntity(ErrorCode.CODE_EXPORT_FILE_EXCEPTION,
                    ErrorCode.MSG_EXPORT_FILE_EXCEPTION);
        }
    }

    private static Document createPdf(String file, List<CourseSessionExport> courseSessionList) {
        Document pdfDoc = null;
        try {
            pdfDoc = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream(file));
            pdfDoc.open();
            createTable(pdfDoc, courseSessionList);
            pdfDoc.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return pdfDoc;
    }

    private static void createTable(Document pdfDoc, List<CourseSessionExport> courseSessionList)
            throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[] { 3.0f, 2.0f, 2.0f, 2.0f, 2.0f });
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // define font for table header row
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        BaseColor headerColor = WebColors.getRGBColor("#C2C2C2");
        cell.setBackgroundColor(headerColor);
        cell.setPadding(5);

        // data header table
        cell.setPhrase(new Phrase("Course Name", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Status", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Session", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Start Day", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("End Day", fontHeader));
        table.addCell(cell);

        // data body table
        for (CourseSessionExport course : courseSessionList) {
            if (course.getStatus().equalsIgnoreCase(AmwayEnum.CompletionStatus.COMPLETED.getStrValue())) {
                table.addCell(course.getCourseName());
                table.addCell(course.getStatus());
                table.addCell(course.getSessionName());
                table.addCell(course.getStartTime());
                table.addCell(course.getEndTime());
            }
        }

        pdfDoc.add(table);
    }

    private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {
        InputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos;
    }

}
