package com.autobahn.sistemaescolar.model;

public class Aluno {

    private int id;
    private String nome;
    private String matricula;

    // Novos Campos
    private String cor;
    private double rendaFamiliar;
    private String responsaveis;
    private String endereco;

    public Aluno(int id, String nome, String matricula, String cor, double rendaFamiliar, String responsaveis, String endereco) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.cor = cor;
        this.rendaFamiliar = rendaFamiliar;
        this.responsaveis = responsaveis;
        this.endereco = endereco;
    }

    // Construtor SEM ID (para novos cadastros)
    public Aluno(String nome, String matricula, String cor, double rendaFamiliar, String responsaveis, String endereco) {
        this(0, nome, matricula, cor, rendaFamiliar, responsaveis, endereco);
    }

    // --- Getters e Setters ---


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public double getRendaFamiliar() { return rendaFamiliar; }
    public void setRendaFamiliar(double rendaFamiliar) { this.rendaFamiliar = rendaFamiliar; }

    public String getResponsaveis() { return responsaveis; }
    public void setResponsaveis(String responsaveis) { this.responsaveis = responsaveis; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return getNome() + " (" + getMatricula() + ")";
    }

}
