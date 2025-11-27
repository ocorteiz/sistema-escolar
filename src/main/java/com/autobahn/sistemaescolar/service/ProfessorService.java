package com.autobahn.sistemaescolar.service;

import com.autobahn.sistemaescolar.dao.ProfessorDAO;
import com.autobahn.sistemaescolar.model.Professor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public class ProfessorService {

    private static final ProfessorService instancia = new ProfessorService();
    private final ProfessorDAO dao = new ProfessorDAO(); // <--- O DAO

    private ProfessorService() {}

    public static ProfessorService getInstancia() { return instancia; }

    public ObservableList<Professor> getProfessores() {
        try {
            // Busca do banco e converte para ObservableList
            return FXCollections.observableArrayList(dao.listarTodos());
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList(); // Retorna lista vazia em caso de erro
        }
    }

    public void cadastrarProfessor(String nome, String disciplina) {
        Professor novo = new Professor(nome, disciplina);
        try {
            dao.salvar(novo); // Salva no banco (o ID é gerado lá)
            System.out.println("Professor salvo no banco: " + nome);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarProfessor(Professor professor, String novoNome, String novaDisciplina) {
        professor.setNome(novoNome);
        professor.setDisciplina(novaDisciplina);
        try {
            dao.atualizar(professor);
            System.out.println("Professor atualizado no banco.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletarProfessor(Professor professor) {
        try {
            dao.deletar(professor);
            System.out.println("Professor deletado do banco.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}