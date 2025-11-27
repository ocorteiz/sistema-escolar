package com.autobahn.sistemaescolar.model;

import java.util.ArrayList;
import java.util.List;

public class Sala {

    private int id;
    private String nome;
    private int capacidade;
    private Professor professor;
    private List<Aluno> alunos;

    public Sala(int id, String nome, int capacidade) { // Atualize o construtor
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.alunos = new ArrayList<>();
    }

    // Construtor auxiliar
    public Sala(String nome, int capacidade) {
        this(0, nome, capacidade);
    }

    /**
     * Associa um aluno a uma sala.
     * @param aluno A sala a ser modificada.
     */
    public void adicionarAluno(Aluno aluno) {
        if (this.alunos.size() < this.capacidade) {
            this.alunos.add(aluno);
        } else {
            System.err.println("Sala " + nome + " está cheia!");
        }
    }
    /**
     * Remove um aluno da sala.
     * @param aluno O aluno a ser removido.
     */
    public void removerAluno(Aluno aluno) {
        this.alunos.remove(aluno);
    }

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor; // [cite: 7]
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    @Override
    public String toString() {
        return getNome();
    }

}
