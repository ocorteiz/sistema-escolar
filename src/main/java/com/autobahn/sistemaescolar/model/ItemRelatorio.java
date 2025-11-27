package com.autobahn.sistemaescolar.model;

public class ItemRelatorio {

    private String categoria; // Ex: "Branca", "Renda < 1000", "Sala 101"
    private int quantidade;   // Ex: 5
    private String alunos;    // Ex: "Carlos, Ana, Pedro"

    public ItemRelatorio(String categoria, int quantidade, String alunos) {
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.alunos = alunos;
    }

    public String getCategoria() { return categoria; }
    public int getQuantidade() { return quantidade; }
    public String getAlunos() { return alunos; }
}