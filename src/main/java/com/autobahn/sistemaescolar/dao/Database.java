package com.autobahn.sistemaescolar.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // O arquivo do banco será criado na raiz do projeto
    private static final String URL = "jdbc:sqlite:escola.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Cria as tabelas se elas não existirem
    public static void inicializar() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Tabela Professor
            stmt.execute("CREATE TABLE IF NOT EXISTS professor (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "disciplina TEXT NOT NULL" +
                    ");");

            // Tabela Sala
            // Note que sala tem um id_professor (Chave Estrangeira)
            stmt.execute("CREATE TABLE IF NOT EXISTS sala (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "capacidade INTEGER NOT NULL," +
                    "id_professor INTEGER REFERENCES professor(id) ON DELETE SET NULL" +
                    ");");

            // Tabela Aluno
            // Note que aluno tem um id_sala (Chave Estrangeira)
            stmt.execute("CREATE TABLE IF NOT EXISTS aluno (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "matricula TEXT NOT NULL," +
                    "cor TEXT," +
                    "renda REAL," +
                    "responsaveis TEXT," +
                    "endereco TEXT," +
                    "id_sala INTEGER REFERENCES sala(id) ON DELETE SET NULL" +
                    ");");

            System.out.println("Banco de dados inicializado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}