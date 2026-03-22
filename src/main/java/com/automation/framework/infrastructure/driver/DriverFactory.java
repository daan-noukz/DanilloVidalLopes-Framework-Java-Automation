package com.automation.framework.infrastructure.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class DriverFactory {

    // Sua ThreadLocal original, para isolar o driver por thread.
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    // NOVO: Uma lista thread-safe para rastrear todas as instâncias de driver ativas.
// Isso é o que permite que nosso "mecanismo de segurança" funcione.
    private static final List<WebDriver> allDrivers = Collections.synchronizedList(new ArrayList<>());

    // NOVO: O "mecanismo de segurança" (Shutdown Hook).
// Este bloco é executado uma única vez quando a classe é carregada.
    static {
        // Registra uma nova thread que será executada quando a JVM estiver prestes a desligar.
        // Isso captura encerramentos abruptos (ex: Ctrl+C no terminal).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Hook acionado: encerrando drivers restantes...");
            for (WebDriver driver : allDrivers) {
                if (driver != null) {
                    try {
                        driver.quit();
                    } catch (Exception e) {
                        System.err.println("Erro ao encerrar driver no shutdown hook: " + e.getMessage());
                    }
                }
            }
        }));
    }

    /**
     * Inicializa o WebDriver para a thread atual com base no navegador especificado.
     * Mantém sua lógica original para Chrome e Edge.
     */
    public static void init(String browser) {
        WebDriver driver; // Variável local para criar a instância

        switch (browser.toLowerCase()) {
            case "edge":
                // Mantendo seu path absoluto para o msedgedriver
                String driverPath = System.getProperty("user.dir") + "/drivers/msedgedriver";
                System.setProperty("webdriver.edge.driver", driverPath);
                driver = new EdgeDriver();
                break;
            default: // "chrome" ou qualquer outro valor
                // Mantendo o gerenciamento automático do chromedriver
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                driver = new ChromeDriver(options);
        }

        // Armazena o driver na ThreadLocal da thread atual
        threadLocalDriver.set(driver);
        // Adiciona o driver à lista global para o Shutdown Hook
        allDrivers.add(driver);
    }

    /**
     * Retorna a instância do WebDriver associada à thread atual.
     */
    public static WebDriver get() {
        return threadLocalDriver.get();
    }

    /**
     * Encerra o WebDriver da thread atual e o remove das listas de rastreamento.
     * DEVE ser chamado no final de cada teste (ex: em um @AfterEach).
     */
    public static void quit() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            try {
                // 1. Encerra o navegador e o processo do driver.
                driver.quit();
            } finally {
                // 2. Remove o driver da ThreadLocal para limpar a thread.
                threadLocalDriver.remove();
                // 3. Remove o driver da lista global, pois ele foi fechado com sucesso.
                // Isso impede que o Shutdown Hook tente fechá-lo novamente.
                allDrivers.remove(driver);
            }
        }
    }
}