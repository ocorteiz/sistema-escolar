package com.autobahn.sistemaescolar.controller;

import com.autobahn.sistemaescolar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    // Injeta os componentes do FXML (deve ter o mesmo nome do 'fx:id')
    @FXML
    private Button btnSalas;

    @FXML
    private Button btnProfessores;

    @FXML
    private Button btnAlunos;

    @FXML private Button btnRelatorios;

    @FXML
    private AnchorPane contentArea; // A área de conteúdo

    // Este método é chamado automaticamente depois que o FXML é carregado
    @FXML
    public void initialize() {
        System.out.println("MainController inicializado!");
        // Carrega a tela de Salas como padrão ao iniciar
        try {
            handleBtnSalas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos chamados pelos botões (onAction)

    @FXML
    private void handleBtnSalas() throws IOException {
        System.out.println("Carregando tela de Salas...");
        // Carrega o FXML da tela de Salas
        Node view = FXMLLoader.load(Main.class.getResource("/com/autobahn/sistemaescolar/view/salas-view.fxml"));

        // Define a view carregada como o conteúdo da 'contentArea'
        definirConteudo(view);
    }

    @FXML
    private void handleBtnProfessores() throws IOException {
        System.out.println("Carregando tela de Professores...");
        Node view = FXMLLoader.load(Main.class.getResource("/com/autobahn/sistemaescolar/view/professores-view.fxml"));
        definirConteudo(view);
    }

    @FXML
    private void handleBtnAlunos() throws IOException {
        System.out.println("Carregando tela de Alunos...");
        Node view = FXMLLoader.load(Main.class.getResource("/com/autobahn/sistemaescolar/view/alunos-view.fxml"));
        definirConteudo(view);
    }

    @FXML
    private void handleBtnRelatorios() throws IOException {
        System.out.println("Carregando tela de Relatórios...");
        Node view = FXMLLoader.load(getClass().getResource("/com/autobahn/sistemaescolar/view/relatorio-view.fxml"));
        definirConteudo(view);
    }

    /**
     * Método auxiliar para limpar a área de conteúdo e adicionar a nova tela.
     * @param view O Node (AnchorPane, etc.) a ser exibido.
     */
    private void definirConteudo(Node view) {
        contentArea.getChildren().clear(); // Limpa o conteúdo atual
        contentArea.getChildren().add(view); // Adiciona o novo conteúdo

        // Ajusta a view para preencher totalmente o AnchorPane (contentArea)
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
    }

}
