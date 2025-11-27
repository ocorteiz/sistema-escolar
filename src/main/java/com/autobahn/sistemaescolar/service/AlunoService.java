package com.autobahn.sistemaescolar.service;

import com.autobahn.sistemaescolar.dao.AlunoDAO;
import com.autobahn.sistemaescolar.model.Aluno;
import com.autobahn.sistemaescolar.model.Sala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public class AlunoService {

    private static final AlunoService instancia = new AlunoService();
    private final AlunoDAO dao = new AlunoDAO();

    private AlunoService() {}

    public static AlunoService getInstancia() { return instancia; }

    public ObservableList<Aluno> getAlunos() {
        try {
            return FXCollections.observableArrayList(dao.listarTodos());
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public Aluno cadastrarAluno(String nome, String matricula, String cor, double renda, String responsaveis, String endereco) {
        Aluno novo = new Aluno(nome, matricula, cor, renda, responsaveis, endereco);
        try {
            dao.salvar(novo);
            System.out.println("Aluno salvo no banco: " + nome);
            return novo;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar aluno no banco.");
        }
    }

    public void associarAlunoASala(Aluno aluno, Sala sala) {
        try {
            // Atualiza o ID da sala no registro do aluno
            dao.atualizarSala(aluno, sala.getId());

            // Atualiza a lista em memória da sala (para a visualização atual funcionar sem recarregar tudo)
            sala.adicionarAluno(aluno);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarAluno(Aluno aluno, String nome, String matricula, String cor, double renda, String responsaveis, String endereco) {
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCor(cor);
        aluno.setRendaFamiliar(renda);
        aluno.setResponsaveis(responsaveis);
        aluno.setEndereco(endereco);
        try {
            dao.atualizar(aluno);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarAluno(Aluno aluno) {
        try {
            dao.deletar(aluno);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}