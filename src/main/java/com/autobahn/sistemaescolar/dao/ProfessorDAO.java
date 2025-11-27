package com.autobahn.sistemaescolar.dao;

import com.autobahn.sistemaescolar.model.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    public void salvar(Professor professor) throws SQLException {
        String sql = "INSERT INTO professor (nome, disciplina) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDisciplina());
            stmt.executeUpdate();

            // Pega o ID gerado pelo banco e coloca no objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    professor.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void atualizar(Professor professor) throws SQLException {
        String sql = "UPDATE professor SET nome = ?, disciplina = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDisciplina());
            stmt.setInt(3, professor.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Professor professor) throws SQLException {
        String sql = "DELETE FROM professor WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professor.getId());
            stmt.executeUpdate();
        }
    }

    public List<Professor> listarTodos() throws SQLException {
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT * FROM professor";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Professor p = new Professor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("disciplina")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    // Método auxiliar para buscar um professor específico pelo ID
    public Professor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM professor WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Professor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("disciplina")
                    );
                }
            }
        }
        return null;
    }
}