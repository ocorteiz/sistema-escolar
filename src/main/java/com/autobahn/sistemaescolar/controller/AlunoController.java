package com.autobahn.sistemaescolar.controller;

import com.autobahn.sistemaescolar.model.Aluno;
import com.autobahn.sistemaescolar.model.Sala;
import com.autobahn.sistemaescolar.service.AlunoService;
import com.autobahn.sistemaescolar.service.SalaService; // <-- Importação nova!
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; // <-- Lembre-se disso!
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Optional; // <-- Importação necessária
import java.util.ResourceBundle;

public class AlunoController implements Initializable {

    // --- Componentes FXML ---
    @FXML private TextField txtNome;
    @FXML private TextField txtMatricula;

    // Novos Campos
    @FXML private ComboBox<String> cbCor;
    @FXML private TextField txtRenda;
    @FXML private TextField txtResponsaveis;
    @FXML private TextField txtEndereco;

    @FXML private ComboBox<Sala> cbSala;
    @FXML private Label lblStatus;
    @FXML private TableView<Aluno> tabelaAlunos;

    // Colunas
    @FXML private TableColumn<Aluno, String> colNome;
    @FXML private TableColumn<Aluno, String> colMatricula;
    @FXML private TableColumn<Aluno, String> colResponsaveis; // Nova coluna

    @FXML private Button btnCadastrar;
    @FXML private Button btnCancelar;
    @FXML private Button btnDeletarAluno;

    private AlunoService alunoService;
    private SalaService salaService;
    private Aluno alunoSelecionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.alunoService = AlunoService.getInstancia();
        this.salaService = SalaService.getInstancia();

        configurarTabela();
        carregarDadosTabela();
        carregarSalasNoComboBox();

        // Inicializar Opções de Cor/Raça
        cbCor.getItems().addAll("Branca", "Preta", "Parda", "Amarela", "Indígena", "Não Declarada");

        configurarListenerSelecaoTabela();
        limparCampos();
    }

    private void configurarTabela() {
        // Vincula as colunas aos dados
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colResponsaveis.setCellValueFactory(new PropertyValueFactory<>("responsaveis"));

        // --- NOVO: Centralizar o texto das células ---
        // Isso fará com que os dados fiquem no meio, alinhados com o título
        colNome.setStyle("-fx-alignment: CENTER;");
        colMatricula.setStyle("-fx-alignment: CENTER;");
        colResponsaveis.setStyle("-fx-alignment: CENTER;");
    }

    private void carregarDadosTabela() {
        tabelaAlunos.setItems(alunoService.getAlunos());
    }

    private void carregarSalasNoComboBox() {
        cbSala.setItems(salaService.getSalas());
    }

    private void configurarListenerSelecaoTabela() {
        tabelaAlunos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> exibirDetalhesAluno(newValue)
        );
    }

    @FXML
    void handleCadastrar(ActionEvent event) {
        String nome = txtNome.getText();
        String matricula = txtMatricula.getText();
        String cor = cbCor.getValue();
        String responsaveis = txtResponsaveis.getText();
        String endereco = txtEndereco.getText();
        Sala salaSelecionada = cbSala.getValue();

        // Tratamento da Renda (Parse Double)
        double renda = 0.0;
        try {
            renda = Double.parseDouble(txtRenda.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            exibirMensagem("Renda inválida. Digite apenas números.", true);
            return;
        }

        // Validação Básica
        if (nome.isEmpty() || matricula.isEmpty() || cor == null || responsaveis.isEmpty()) {
            exibirMensagem("Preencha todos os campos obrigatórios.", true);
            return;
        }
        if (salaSelecionada == null) {
            exibirMensagem("Selecione uma sala.", true);
            return;
        }

        try {
            if (alunoSelecionado == null) {
                // --- CADASTRO ---
                Aluno novo = alunoService.cadastrarAluno(nome, matricula, cor, renda, responsaveis, endereco);
                alunoService.associarAlunoASala(novo, salaSelecionada);
                exibirMensagem("Aluno cadastrado com sucesso!", false);
            } else {
                // --- EDIÇÃO ---
                alunoService.editarAluno(alunoSelecionado, nome, matricula, cor, renda, responsaveis, endereco);
                salaService.transferirAluno(alunoSelecionado, salaSelecionada);
                exibirMensagem("Aluno atualizado com sucesso!", false);
            }

            carregarDadosTabela();

            limparCampos();
        } catch (Exception e) {
            exibirMensagem("Erro: " + e.getMessage(), true);
        }
    }

    @FXML
    void handleDeletarAluno(ActionEvent event) {
        Aluno alunoParaDeletar = tabelaAlunos.getSelectionModel().getSelectedItem();
        if (alunoParaDeletar == null) {
            exibirMensagem("Selecione um aluno para deletar.", true);
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Excluir");
        alerta.setHeaderText("Deletar " + alunoParaDeletar.getNome() + "?");
        alerta.setContentText("Isso removerá o aluno do sistema permanentemente.");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            salaService.desassociarAlunoDeTodasSalas(alunoParaDeletar);
            alunoService.deletarAluno(alunoParaDeletar);
            exibirMensagem("Aluno deletado.", false);

            carregarDadosTabela();

            limparCampos();
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        limparCampos();
    }

    private void exibirDetalhesAluno(Aluno aluno) {
        if (aluno != null) {
            this.alunoSelecionado = aluno;

            // Preencher campos
            txtNome.setText(aluno.getNome());
            txtMatricula.setText(aluno.getMatricula());
            cbCor.setValue(aluno.getCor());
            txtRenda.setText(String.valueOf(aluno.getRendaFamiliar()));
            txtResponsaveis.setText(aluno.getResponsaveis());
            txtEndereco.setText(aluno.getEndereco());

            // Selecionar Sala
            cbSala.setDisable(false);
            Optional<Sala> salaAtualOpt = salaService.getSalas().stream()
                    .filter(s -> s.getAlunos().contains(aluno))
                    .findFirst();
            cbSala.setValue(salaAtualOpt.orElse(null));

            btnCadastrar.setText("Salvar Alterações");
            btnCancelar.setVisible(true);
        } else {
            limparCampos();
        }
    }

    private void limparCampos() {
        this.alunoSelecionado = null;
        txtNome.clear();
        txtMatricula.clear();
        txtRenda.clear();
        txtResponsaveis.clear();
        txtEndereco.clear();
        cbCor.setValue(null);
        cbSala.setValue(null);
        cbSala.setDisable(false);

        tabelaAlunos.getSelectionModel().clearSelection();
        btnCadastrar.setText("Cadastrar e Associar");
        btnCancelar.setVisible(false);
        lblStatus.setText("");
    }

    private void exibirMensagem(String msg, boolean erro) {
        lblStatus.setText(msg);
        lblStatus.setTextFill(erro ? Color.RED : Color.GREEN);
    }

}
