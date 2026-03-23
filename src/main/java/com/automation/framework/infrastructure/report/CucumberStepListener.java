package com.automation.framework.infrastructure.report;

import com.automation.framework.infrastructure.driver.DriverFactory;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class CucumberStepListener implements EventListener {

    private static final ThreadLocal<PdfEvidenceManager> evidenceManagerContext = new ThreadLocal<>();

    // --- DIAGNÓSTICO: Sinal de vida da classe ---
// Se o Cucumber estiver carregando este plugin, esta mensagem DEVE aparecer no console.
    public CucumberStepListener() {
        System.out.println("===================================================================");
        System.out.println("[DIAGNÓSTICO] CONSTRUTOR: CucumberStepListener foi instanciado.");
        System.out.println("===================================================================");
    }
// ---------------------------------------------

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        PdfEvidenceManager manager = new PdfEvidenceManager();
        evidenceManagerContext.set(manager);
        try {
            String scenarioName = event.getTestCase().getName();
            manager.startReport(scenarioName);
            System.out.println("[LISTENER] Relatório iniciado para o cenário: " + scenarioName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        PdfEvidenceManager pdfManager = evidenceManagerContext.get();
        WebDriver driver = DriverFactory.get();

        if (event.getTestStep() instanceof PickleStepTestStep && pdfManager != null && driver != null) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            String stepText = pickleStep.getStep().getKeyword() + pickleStep.getStep().getText();
            try {
                pdfManager.addScreenshot(driver, stepText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        PdfEvidenceManager pdfManager = evidenceManagerContext.get();
        if (pdfManager != null) {
            pdfManager.addFinalStatus(event.getResult().getStatus());
            pdfManager.endReport();
            evidenceManagerContext.remove();
            System.out.println("[LISTENER] Relatório finalizado.");
        }
    }
}