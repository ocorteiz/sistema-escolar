package com.autobahn.sistemaescolar.dao;

import com.autobahn.sistemaescolar.model.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void salvar(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO aluno (nome, matricula, cor, renda, responsaveis, endereco) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherStatement(stmt, aluno);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    aluno.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE aluno SET nome=?, matricula=?, cor=?, renda=?, responsaveis=?, endereco=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preencherStatement(stmt, aluno);
            stmt.setInt(7, aluno.getId());
            stmt.executeUpdate();
        }
    }

    // Método especial para atualizar APENAS a sala do aluno
    public void atualizarSala(Aluno aluno, int idSala) throws SQLException {
        String sql = "UPDATE aluno SET id_sala = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (idSala > 0) stmt.setInt(1, idSala);
            else stmt.setNull(1, Types.INTEGER); // Se idSala for 0 ou menor, remove da sala

            stmt.setInt(2, aluno.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Aluno aluno) throws SQLException {
        String sql = "DELETE FROM aluno WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aluno.getId());
            stmt.executeUpdate();
        }
    }

    public List<Aluno> listarTodos() throws SQLException {
        return buscarAlunos("SELECT * FROM aluno");
    }

    // Busca apenas os alunos de uma sala específica
    public List<Aluno> listarPorSala(int idSala) throws SQLException {
        return buscarAlunos("SELECT * FROM aluno WHERE id_sala = " + idSala);
    }

    // Método auxiliar para evitar repetição de código
    private List<Aluno> buscarAlunos(String sql) throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aluno a = new Aluno(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("matricula"),
                        rs.getString("cor"),
                        rs.getDouble("renda"),
                        rs.getString("responsaveis"),
                        rs.getString("endereco")
                );
                lista.add(a);
            }
        }
        return lista;
    }

    private void preencherStatement(PreparedStatement stmt, Aluno aluno) throws SQLException {
        stmt.setString(1, aluno.getNome());
        stmt.setString(2, aluno.getMatricula());
        stmt.setString(3, aluno.getCor());
        stmt.setDouble(4, aluno.getRendaFamiliar());
        stmt.setString(5, aluno.getResponsaveis());
        stmt.setString(6, aluno.getEndereco());
    }
}