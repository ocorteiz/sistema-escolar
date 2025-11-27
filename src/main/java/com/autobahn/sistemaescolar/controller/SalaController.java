package com.autobahn.sistemaescolar.controller;

import com.autobahn.sistemaescolar.model.Aluno;
import com.autobahn.sistemaescolar.model.Professor;
import com.autobahn.sistemaescolar.model.Sala;
import com.autobahn.sistemaescolar.service.ProfessorService;
import com.autobahn.sistemaescolar.service.SalaService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Optional; // <-- Nova Importação
import java.util.ResourceBundle;

public class SalaController implements Initializable {

    // --- Componentes do Formulário de Cadastro ---
    @FXML private TextField txtNome;
    @FXML private TextField txtCapacidade;
    @FXML private Label lblStatus;
    @FXML private Button btnCadastrar;

    // --- Componentes da Tabela Principal de Salas ---
    @FXML private TableView<Sala> tabelaSalas;
    @FXML private TableColumn<Sala, String> colNome;
    @FXML private TableColumn<Sala, Integer> colCapacidade;
    @FXML private TableColumn<Sala, Professor> colProfessor;
    @FXML private Button btnDeletarSala; // <-- NOVO BOTÃO

    // --- Componentes do Painel de Detalhes ---
    @FXML private VBox painelDetalhes;
    @FXML private Label lblNomeSalaDetalhe;
    @FXML private ComboBox<Professor> cbProfessor;
    @FXML private Button btnSalvarProfessor;
    @FXML private TableView<Aluno> tabelaAlunosDaSala;
    @FXML private TableColumn<Aluno, String> colAlunoNome;
    @FXML private TableColumn<Aluno, String> colAlunoMatricula;
    @FXML private Button btnRemoverAluno; // <-- NOVO BOTÃO

    // --- Serviços ---
    private SalaService salaService;
    private ProfessorService professorService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.salaService = SalaService.getInstancia();
        this.professorService = ProfessorService.getInstancia();

        configurarTabelaPrincipal();
        configurarTabelaAlunos();
        carregarDadosTabelaPrincipal();
        carregarProfessoresNoComboBox();
        configurarListenerSelecaoTabela();
    }

    // --- Configuração Inicial (sem mudanças) ---

    private void configurarTabelaPrincipal() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        colProfessor.setCellValueFactory(new PropertyValueFactory<>("professor"));

        colNome.setStyle("-fx-alignment: CENTER;");
        colCapacidade.setStyle("-fx-alignment: CENTER;");
        colProfessor.setStyle("-fx-alignment: CENTER;");
    }

    private void configurarTabelaAlunos() {
        colAlunoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAlunoMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        colAlunoNome.setStyle("-fx-alignment: CENTER;");
        colAlunoMatricula.setStyle("-fx-alignment: CENTER;");
    }

    private void carregarDadosTabelaPrincipal() {
        tabelaSalas.setItems(salaService.getSalas());
    }

    private void carregarProfessoresNoComboBox() {
        cbProfessor.setItems(professorService.getProfessores());
    }

    private void configurarListenerSelecaoTabela() {
        tabelaSalas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> exibirDetalhesSala(newValue)
        );
    }


    // --- Métodos de Ação (Handlers) ---

    @FXML
    void handleCadastrar(ActionEvent event) {
        // (Sem mudanças)
        String nome = txtNome.getText();
        String capacidadeStr = txtCapacidade.getText();
        int capacidade = 0;

        if (nome == null || nome.trim().isEmpty()) {
            exibirMensagem("O nome não pode estar vazio.", true);
            return;
        }
        try {
            capacidade = Integer.parseInt(capacidadeStr);
            if (capacidade <= 0) {
                exibirMensagem("A capacidade deve ser um número positivo.", true);
                return;
            }
        } catch (NumberFormatException e) {
            exibirMensagem("A capacidade deve ser um número válido.", true);
            return;
        }

        try {
            salaService.cadastrarSala(nome, capacidade);
            exibirMensagem("Sala cadastrada com sucesso!", false);

            carregarDadosTabelaPrincipal();

            limparCamposCadastro();
        } catch (Exception e) {
            exibirMensagem("Erro ao cadastrar sala: " + e.getMessage(), true);
        }
    }

    @FXML
    void handleSalvarProfessor(ActionEvent event) {
        // (Sem mudanças)
        Sala salaSelecionada = tabelaSalas.getSelectionModel().getSelectedItem();
        Professor professorSelecionado = cbProfessor.getValue();

        if (salaSelecionada == null) {
            exibirMensagem("Nenhuma sala selecionada na tabela.", true);
            return;
        }
        if (professorSelecionado == null) {
            exibirMensagem("Nenhum professor selecionado.", true);
            return;
        }

        try {
            salaService.associarProfessorASala(salaSelecionada, professorSelecionado);

            carregarDadosTabelaPrincipal();

            exibirMensagem("Professor associado com sucesso!", false);
        } catch (Exception e) {
            exibirMensagem("Erro ao associar professor: " + e.getMessage(), true);
        }
    }

    /**
     * NOVO MÉTODO: Chamado ao clicar em 'btnDeletarSala'
     */
    @FXML
    void handleDeletarSala(ActionEvent event) {
        // 1. Pega a sala selecionada na tabela principal
        Sala salaSelecionada = tabelaSalas.getSelectionModel().getSelectedItem();

        if (salaSelecionada == null) {
            exibirMensagem("Selecione uma sala para deletar.", true);
            return;
        }

        // 2. Cria um Alerta de Confirmação
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmação de Exclusão");
        alerta.setHeaderText("Você tem certeza que deseja deletar a sala?");
        alerta.setContentText("Sala: " + salaSelecionada.getNome() +
                "\n\nTodos os alunos e professores associados perderão o vínculo.");

        // 3. Exibe o alerta e espera a resposta do usuário
        Optional<ButtonType> resultado = alerta.showAndWait();

        // 4. Se o usuário clicou em "OK"
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // Chama o serviço para deletar
                salaService.deletarSala(salaSelecionada);
                // (A tabela principal atualiza sozinha, pois usa ObservableList)
                exibirMensagem("Sala deletada com sucesso!", false);

                carregarDadosTabelaPrincipal(); // Tabela da esquerda
                exibirDetalhesSala(null); // Limpa o painel da direita

            } catch (Exception e) {
                exibirMensagem("Erro ao deletar sala: " + e.getMessage(), true);
            }
        } else {
            exibirMensagem("Exclusão cancelada.", false);
        }
    }

    /**
     * NOVO MÉTODO: Chamado ao clicar em 'btnRemoverAluno'
     */
    @FXML
    void handleRemoverAluno(ActionEvent event) {
        // 1. Pega a sala principal (para saber "de onde" remover)
        Sala salaPai = tabelaSalas.getSelectionModel().getSelectedItem();

        // 2. Pega o aluno selecionado na tabela de detalhes
        Aluno alunoSelecionado = tabelaAlunosDaSala.getSelectionModel().getSelectedItem();

        if (salaPai == null || alunoSelecionado == null) {
            exibirMensagem("Selecione uma sala e, em seguida, um aluno para remover.", true);
            return;
        }

        // 3. Cria o Alerta de Confirmação
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmação de Remoção");
        alerta.setHeaderText("Remover aluno da sala?");
        alerta.setContentText("Aluno: " + alunoSelecionado.getNome() + "\nSala: " + salaPai.getNome());

        Optional<ButtonType> resultado = alerta.showAndWait();

        // 4. Se o usuário clicou em "OK"
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // Chama o serviço
                salaService.removerAlunoDaSala(alunoSelecionado, salaPai);

                // ATUALIZAÇÃO MANUAL: Remove o aluno da tabela de detalhes
                // (Isso não atualiza sozinho, pois a lista de alunos
                // da sala não é "observável" por padrão)
                tabelaAlunosDaSala.getItems().remove(alunoSelecionado);

                exibirMensagem("Aluno removido com sucesso!", false);
            } catch (Exception e) {
                exibirMensagem("Erro ao remover aluno: " + e.getMessage(), true);
            }
        } else {
            exibirMensagem("Remoção cancelada.", false);
        }
    }


    // --- Métodos Auxiliares (sem mudanças) ---

    private void exibirDetalhesSala(Sala sala) {
        if (sala != null) {
            painelDetalhes.setDisable(false);
            lblNomeSalaDetalhe.setText(sala.getNome());
            cbProfessor.setValue(sala.getProfessor());
            ObservableList<Aluno> alunosDaSala = FXCollections.observableArrayList(sala.getAlunos());
            tabelaAlunosDaSala.setItems(alunosDaSala);
        } else {
            painelDetalhes.setDisable(true);
            lblNomeSalaDetalhe.setText("Selecione uma sala");
            cbProfessor.setValue(null);
            tabelaAlunosDaSala.setItems(FXCollections.emptyObservableList());
        }
    }

    private void limparCamposCadastro() {
        txtNome.clear();
        txtCapacidade.clear();
    }

    private void exibirMensagem(String mensagem, boolean isErro) {
        lblStatus.setText(mensagem);
        lblStatus.setTextFill(isErro ? Color.RED : Color.GREEN);
    }
}