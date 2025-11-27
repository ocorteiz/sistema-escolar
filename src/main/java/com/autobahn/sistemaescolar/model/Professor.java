package com.autobahn.sistemaescolar.model;

public class Professor {

    private int id;
    private String nome;
    private String disciplina;

    public Professor(int id, String nome, String disciplina) { // Atualize o construtor
        this.id = id;
        this.nome = nome;
        this.disciplina = disciplina;
    }

    // Construtor sem ID (para quando for criar novo antes de salvar no banco)
    public Professor(String nome, String disciplina) {
        this.nome = nome;
        this.disciplina = disciplina;
    }

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

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public String toString() {
        return getNome() + " - " + getDisciplina();
    }

}
