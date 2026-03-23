package com.automation.framework.presentation.pages.exemple;

import com.automation.framework.infrastructure.driver.DriverFactory;
import com.automation.framework.infrastructure.wrappers.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GooglePage {

    private final WebActions actions;

    // --- Mapeamento de Elementos (Locators) ---
    private final By searchBoxByName = By.name("q");

    // Exemplo de localizador por ID. (ID hipotético, pois a página real do Google usa IDs dinâmicos)
    // Usamos 'hplogo' como um exemplo clássico que já existiu.
    private final By googleLogoById = By.id("hplogo");

    // Exemplo de localizador por XPath para o link "Imagens" no topo da página.
    private final By imagesLinkByXpath = By.xpath("//a[text()='Imagens']");

    /**
     * Construtor da página. Inicia a classe de ações (wrapper).
     * @param driver A instância do WebDriver para a sessão atual.
     */
    public GooglePage(WebDriver driver) {
        this.actions = new WebActions(driver, 10);
    }

    /**
     * Navega para a página inicial do Google.
     */
    public void navigateToGoogle() {
        DriverFactory.get().get("https://www.google.com");
    }

    /**
     * Digita um texto na caixa de busca e pressiona Enter para pesquisar.
     * @param text O texto a ser pesquisado.
     */
    public void search(String text) {
        actions.sendKeys(searchBoxByName, text);
        actions.pressEnter(searchBoxByName);
    }

    /**
     * Método de exemplo que clica no logo do Google usando um localizador por ID.
     */
    public void clickGoogleLogo() {
        // Este método demonstra o uso do localizador 'googleLogoById'.
        // Em um teste real, poderia ser usado para voltar à página inicial.
        actions.click(googleLogoById);
    }

    /**
     * Método de exemplo que clica no link "Imagens" usando um localizador por XPath.
     */
    public void clickImagesLink() {
        // Este método demonstra o uso do localizador 'imagesLinkByXpath'.
        // Em um teste real, navegaria para a busca de imagens do Google.
        actions.click(imagesLinkByXpath);
    }
}