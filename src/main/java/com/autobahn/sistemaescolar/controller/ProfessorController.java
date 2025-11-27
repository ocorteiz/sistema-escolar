package com.autobahn.sistemaescolar.controller;

import com.autobahn.sistemaescolar.model.Professor;
import com.autobahn.sistemaescolar.service.ProfessorService;
import com.autobahn.sistemaescolar.service.SalaService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfessorController implements Initializable{

    // --- Componentes FXML ---
    @FXML private TextField txtNome;
    @FXML private TextField txtDisciplina;
    @FXML private Label lblStatus;
    @FXML private TableView<Professor> tabelaProfessores;
    @FXML private TableColumn<Professor, String> colNome;
    @FXML private TableColumn<Professor, String> colDisciplina;
    @FXML private Button btnCadastrar;
    @FXML private Button btnCancelar;
    @FXML private Button btnDeletarProfessor; // <-- NOVO BOTÃO FXML

    // --- Serviços ---
    private ProfessorService professorService;
    private SalaService salaService; // <-- NOVO SERVIÇO

    // --- Controle de Estado ---
    private Professor professorSelecionado;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.professorService = ProfessorService.getInstancia();
        this.salaService = SalaService.getInstancia(); // <-- INICIALIZA O SERVIÇO

        configurarTabela();
        carregarDadosTabela();
        configurarListenerSelecaoTabela();
        limparCampos();
    }

    // --- Configuração (sem mudanças) ---

    private void configurarTabela() {
        // Vincula as colunas aos dados
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("disciplina"));

        // --- NOVO: Centralizar o texto das células ---
        // Isso fará com que os dados fiquem no meio, alinhados com o título
        colNome.setStyle("-fx-alignment: CENTER;");
        colDisciplina.setStyle("-fx-alignment: CENTER;");
    }

    private void carregarDadosTabela() {
        ObservableList<Professor> professores = professorService.getProfessores();
        tabelaProfessores.setItems(professores);
    }

    private void configurarListenerSelecaoTabela() {
        tabelaProfessores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> exibirDetalhesProfessor(newValue)
        );
    }

    // --- Métodos de Ação (Handlers) ---

    @FXML
    void handleCadastrar(ActionEvent event) {
        // (Sem mudanças da Parte 8)
        String nome = txtNome.getText();
        String disciplina = txtDisciplina.getText();

        if (nome == null || nome.trim().isEmpty()) {
            exibirMensagem("O nome não pode estar vazio.", true);
            return;
        }
        if (disciplina == null || disciplina.trim().isEmpty()) {
            exibirMensagem("A disciplina não pode estar vazia.", true);
            return;
        }

        try {
            if (professorSelecionado == null) {
                professorService.cadastrarProfessor(nome, disciplina);
                exibirMensagem("Professor(a) cadastrado(a) com sucesso!", false);
            } else {
                professorService.editarProfessor(professorSelecionado, nome, disciplina);
                exibirMensagem("Professor(a) atualizado(a) com sucesso!", false);
            }

            carregarDadosTabela();

            limparCampos();
        } catch (Exception e) {
            exibirMensagem("Erro: " + e.getMessage(), true);
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        limparCampos();
    }

    /**
     * NOVO MÉTODO: Chamado ao clicar em 'btnDeletarProfessor'
     */
    @FXML
    void handleDeletarProfessor(ActionEvent event) {
        // 1. Pega o professor selecionado na tabela
        Professor professorParaDeletar = tabelaProfessores.getSelectionModel().getSelectedItem();

        if (professorParaDeletar == null) {
            exibirMensagem("Selecione um professor na tabela para deletar.", true);
            return;
        }

        // 2. Cria um Alerta de Confirmação
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmação de Exclusão");
        alerta.setHeaderText("Você tem certeza que deseja DELETAR este professor?");
        alerta.setContentText("Professor: " + professorParaDeletar.getNome() +
                "\n\nEsta ação é permanente e o removerá de todas as salas.");

        // 3. Exibe o alerta e espera a resposta
        Optional<ButtonType> resultado = alerta.showAndWait();

        // 4. Se o usuário clicou em "OK"
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // A ORDEM É IMPORTANTE:
                // 1. Desassociar o professor das salas (enquanto ele ainda existe)
                salaService.desassociarProfessorDeTodasSalas(professorParaDeletar);

                // 2. Deletar o professor do sistema
                professorService.deletarProfessor(professorParaDeletar);

                exibirMensagem("Professor deletado com sucesso!", false);

                carregarDadosTabela();

                limparCampos(); // Reseta o formulário

            } catch (Exception e) {
                exibirMensagem("Erro ao deletar professor: " + e.getMessage(), true);
            }
        } else {
            exibirMensagem("Exclusão cancelada.", false);
        }
    }


    // --- Métodos Auxiliares (sem mudanças) ---

    private void exibirDetalhesProfessor(Professor professor) {
        if (professor != null) {
            this.professorSelecionado = professor;
            txtNome.setText(professor.getNome());
            txtDisciplina.setText(professor.getDisciplina());
            btnCadastrar.setText("Salvar Alterações");
            btnCancelar.setVisible(true);
        } else {
            limparCampos();
        }
    }

    private void limparCampos() {
        this.professorSelecionado = null;
        txtNome.clear();
        txtDisciplina.clear();
        tabelaProfessores.getSelectionModel().clearSelection();
        btnCadastrar.setText("Cadastrar");
        btnCancelar.setVisible(false);
        lblStatus.setText("");
    }

    private void exibirMensagem(String mensagem, boolean isErro) {
        lblStatus.setText(mensagem);
        lblStatus.setTextFill(isErro ? Color.RED : Color.GREEN);
    }

}
