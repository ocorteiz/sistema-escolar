package com.autobahn.sistemaescolar.controller;

import com.autobahn.sistemaescolar.model.ItemRelatorio;
import com.autobahn.sistemaescolar.service.PdfService;
import com.autobahn.sistemaescolar.service.RelatorioService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {

    @FXML private ComboBox<String> cbTipoRelatorio;
    @FXML private Label lblDescricao;

    @FXML private TableView<ItemRelatorio> tabelaRelatorio;
    @FXML private TableColumn<ItemRelatorio, String> colCategoria;
    @FXML private TableColumn<ItemRelatorio, Integer> colQuantidade;
    @FXML private TableColumn<ItemRelatorio, String> colAlunos;

    @FXML private Button btnExportarPdf; // Botão do FXML

    private RelatorioService relatorioService;
    private PdfService pdfService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.relatorioService = RelatorioService.getInstancia();
        this.pdfService = PdfService.getInstancia(); // Inicializa o serviço de PDF

        configurarTabela();
        carregarOpcoesCombo();

        // Gera o relatório automaticamente ao mudar a opção
        cbTipoRelatorio.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> gerarRelatorio(newVal)
        );
    }

    private void configurarTabela() {
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colAlunos.setCellValueFactory(new PropertyValueFactory<>("alunos"));

        // Centraliza a coluna de quantidade
        colCategoria.setStyle("-fx-alignment: CENTER;");
        colQuantidade.setStyle("-fx-alignment: CENTER;");
        colAlunos.setStyle("-fx-alignment: CENTER;");
    }

    private void carregarOpcoesCombo() {
        cbTipoRelatorio.setItems(FXCollections.observableArrayList(
                "Por Cor / Raça",
                "Por Renda Familiar",
                "Por Turma (Sala)",
                "Por Endereço (Agrupado)"
        ));
    }

    private void gerarRelatorio(String tipo) {
        if (tipo == null) return;

        lblDescricao.setText("Visualizando: " + tipo);
        tabelaRelatorio.getItems().clear();

        switch (tipo) {
            case "Por Cor / Raça":
                tabelaRelatorio.setItems(relatorioService.gerarPorCor());
                break;
            case "Por Renda Familiar":
                tabelaRelatorio.setItems(relatorioService.gerarPorRenda());
                break;
            case "Por Turma (Sala)":
                tabelaRelatorio.setItems(relatorioService.gerarPorTurma());
                break;
            case "Por Endereço (Agrupado)":
                tabelaRelatorio.setItems(relatorioService.gerarPorEndereco());
                break;
        }
    }

    /**
     * AÇÃO DO BOTÃO "Baixar PDF"
     */
    @FXML
    void handleExportarPdf(ActionEvent event) {
        // 1. Verifica se a tabela tem dados
        if (tabelaRelatorio.getItems().isEmpty()) {
            mostrarAlerta("Atenção", "Gere um relatório primeiro (selecione uma opção) para poder exportar.", Alert.AlertType.WARNING);
            return;
        }

        // 2. Configura o FileChooser (Janela de Salvar Arquivo)
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório em PDF");

        // Filtro para mostrar apenas .pdf
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));

        // Nome sugerido do arquivo (ex: Relatorio_Por_Turma.pdf)
        String nomeLimpo = cbTipoRelatorio.getValue().replaceAll("[^a-zA-Z0-9]", "_");
        fileChooser.setInitialFileName("Relatorio_" + nomeLimpo + ".pdf");

        // 3. Mostra a janela e espera o usuário escolher o local
        File arquivoDestino = fileChooser.showSaveDialog(btnExportarPdf.getScene().getWindow());

        // 4. Se o usuário escolheu um arquivo (não clicou em Cancelar)
        if (arquivoDestino != null) {
            try {
                // Chama o serviço para criar o PDF
                pdfService.gerarPdfRelatorio(
                        tabelaRelatorio.getItems(), // Dados
                        cbTipoRelatorio.getValue(), // Título
                        arquivoDestino              // Arquivo
                );

                mostrarAlerta("Sucesso", "PDF gerado com sucesso!\nLocal: " + arquivoDestino.getAbsolutePath(), Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                e.printStackTrace(); // Mostra erro no console se houver
                mostrarAlerta("Erro", "Falha ao gerar PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}