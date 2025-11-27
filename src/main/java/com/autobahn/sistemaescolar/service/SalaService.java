package com.autobahn.sistemaescolar.service;

import com.autobahn.sistemaescolar.dao.AlunoDAO;
import com.autobahn.sistemaescolar.dao.SalaDAO;
import com.autobahn.sistemaescolar.model.Aluno;
import com.autobahn.sistemaescolar.model.Professor;
import com.autobahn.sistemaescolar.model.Sala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;

public class SalaService {

    private static final SalaService instancia = new SalaService();
    private final SalaDAO salaDAO = new SalaDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    private SalaService() {}

    public static SalaService getInstancia() { return instancia; }

    public ObservableList<Sala> getSalas() {
        try {
            List<Sala> salas = salaDAO.listarTodas();

            // MÁGICA: Para cada sala, busca os alunos dela no banco
            for (Sala sala : salas) {
                List<Aluno> alunosDaSala = alunoDAO.listarPorSala(sala.getId());
                sala.getAlunos().addAll(alunosDaSala);
            }

            return FXCollections.observableArrayList(salas);
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public void cadastrarSala(String nome, int capacidade) {
        Sala nova = new Sala(nome, capacidade);
        try {
            salaDAO.salvar(nova);
            System.out.println("Sala salva no banco: " + nome);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarSala(Sala sala) {
        try {
            // No banco, configuramos "ON DELETE SET NULL", então os alunos ficam sem sala automaticamente
            salaDAO.deletar(sala);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void associarProfessorASala(Sala sala, Professor professor) {
        sala.setProfessor(professor);
        try {
            salaDAO.atualizar(sala); // Salva a alteração do professor no banco
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desassociarProfessorDeTodasSalas(Professor professor) {
        // No banco isso é manual se não tiver cascade, vamos fazer via código para garantir
        try {
            // Carrega todas as salas e verifica
            for (Sala s : getSalas()) {
                if (professor.equals(s.getProfessor())) {
                    s.setProfessor(null);
                    salaDAO.atualizar(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferirAluno(Aluno aluno, Sala salaNova) {
        // Apenas atualiza o ID da sala no registro do aluno
        try {
            alunoDAO.atualizarSala(aluno, salaNova.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerAlunoDaSala(Aluno aluno, Sala sala) {
        try {
            // Passar 0 ou -1 define a sala como NULL no banco
            alunoDAO.atualizarSala(aluno, 0);
            sala.removerAluno(aluno); // Remove da lista visual
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desassociarAlunoDeTodasSalas(Aluno aluno) {
        try {
            alunoDAO.atualizarSala(aluno, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}