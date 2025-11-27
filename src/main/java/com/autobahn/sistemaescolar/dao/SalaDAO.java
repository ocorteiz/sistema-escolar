package com.autobahn.sistemaescolar.dao;

import com.autobahn.sistemaescolar.model.Professor;
import com.autobahn.sistemaescolar.model.Sala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {

    private ProfessorDAO professorDAO = new ProfessorDAO();

    public void salvar(Sala sala) throws SQLException {
        String sql = "INSERT INTO sala (nome, capacidade, id_professor) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, sala.getNome());
            stmt.setInt(2, sala.getCapacidade());

            if (sala.getProfessor() != null) stmt.setInt(3, sala.getProfessor().getId());
            else stmt.setNull(3, Types.INTEGER);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sala.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void atualizar(Sala sala) throws SQLException {
        String sql = "UPDATE sala SET nome=?, capacidade=?, id_professor=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sala.getNome());
            stmt.setInt(2, sala.getCapacidade());

            if (sala.getProfessor() != null) stmt.setInt(3, sala.getProfessor().getId());
            else stmt.setNull(3, Types.INTEGER);

            stmt.setInt(4, sala.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(Sala sala) throws SQLException {
        String sql = "DELETE FROM sala WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sala.getId());
            stmt.executeUpdate();
        }
    }

    public List<Sala> listarTodas() throws SQLException {
        List<Sala> lista = new ArrayList<>();
        String sql = "SELECT * FROM sala";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sala s = new Sala(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("capacidade")
                );

                // Busca e define o professor se houver
                int idProf = rs.getInt("id_professor");
                if (idProf > 0) {
                    Professor p = professorDAO.buscarPorId(idProf);
                    s.setProfessor(p);
                }

                lista.add(s);
            }
        }
        return lista;
    }
}