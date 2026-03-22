// File: com/automation/framework/infrastructure/report/PdfEvidenceManager.java

package com.automation.framework.infrastructure.report;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import io.cucumber.plugin.event.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfEvidenceManager {

    private static final ThreadLocal<Document> pdfDocument = new ThreadLocal<>();
    private Path reportPath;

    // --- ALTERAÇÃO AQUI ---
// O método agora aceita uma String simples, tornando-o mais flexível.
    public void startReport(String scenarioName) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String sanitizedScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
        String folderName = sanitizedScenarioName + "_" + timestamp;

        reportPath = Paths.get("target/evidencias-pdf/", folderName);
        Files.createDirectories(reportPath);
        Path pdfFile = reportPath.resolve("Test_Evidence.pdf");

        PdfWriter writer = new PdfWriter(pdfFile.toString());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Test Evidence Report")
                .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        // Usa o nome do cenário recebido como parâmetro.
        document.add(new Paragraph("Scenario: " + scenarioName)
                .setTextAlignment(TextAlignment.CENTER).setItalic());
        document.add(new Paragraph("Executed on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss")))
                .setTextAlignment(TextAlignment.CENTER).setFontSize(8));
        document.add(new Paragraph("\n"));

        pdfDocument.set(document);
    }

    public void addScreenshot(WebDriver driver, String stepText) throws MalformedURLException {
        Document document = pdfDocument.get();
        if (document == null || driver == null) return;

        document.add(new Paragraph(stepText).setBold());

        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Image screenshotImage = new Image(ImageDataFactory.create(screenshot));
        screenshotImage.setAutoScale(true);

        document.add(screenshotImage);
        document.add(new Paragraph("\n"));
    }

    public void endReport() {
        Document document = pdfDocument.get();
        if (document != null) {
            document.close();
            pdfDocument.remove();
        }
    }

    // --- ALTERAÇÃO AQUI ---
// O método agora aceita o tipo de Status do EventListener.
    public void addFinalStatus(Status status) {
        Document document = pdfDocument.get();
        if (document == null) return;

        document.add(new Paragraph("\n"));
        Paragraph statusParagraph = new Paragraph("Final Status: " + status.toString())
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER);

        statusParagraph.setFontColor(status == Status.PASSED ? ColorConstants.GREEN : ColorConstants.RED);
        document.add(statusParagraph);
    }
}