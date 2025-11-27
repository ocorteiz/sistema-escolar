package com.autobahn.sistemaescolar.service;

import com.autobahn.sistemaescolar.model.Aluno;
import com.autobahn.sistemaescolar.model.ItemRelatorio;
import com.autobahn.sistemaescolar.model.Sala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioService {

    private static final RelatorioService instancia = new RelatorioService();
    private final AlunoService alunoService;
    private final SalaService salaService;

    private RelatorioService() {
        this.alunoService = AlunoService.getInstancia();
        this.salaService = SalaService.getInstancia();
    }

    public static RelatorioService getInstancia() { return instancia; }

    // 1. Relatório por COR/RAÇA
    public ObservableList<ItemRelatorio> gerarPorCor() {
        // Agrupa os alunos pela cor
        Map<String, List<Aluno>> agrupado = alunoService.getAlunos().stream()
                .collect(Collectors.groupingBy(a -> a.getCor() == null ? "Não Informado" : a.getCor()));

        return converterMapaParaLista(agrupado);
    }

    // 2. Relatório por ENDEREÇO (Agrupa quem mora no mesmo lugar)
    public ObservableList<ItemRelatorio> gerarPorEndereco() {
        Map<String, List<Aluno>> agrupado = alunoService.getAlunos().stream()
                .collect(Collectors.groupingBy(a -> a.getEndereco() == null ? "Sem Endereço" : a.getEndereco()));

        return converterMapaParaLista(agrupado);
    }

    // 3. Relatório por TURMA (SALA)
    public ObservableList<ItemRelatorio> gerarPorTurma() {
        ObservableList<ItemRelatorio> relatorio = FXCollections.observableArrayList();

        for (Sala sala : salaService.getSalas()) {
            List<Aluno> alunosNaSala = sala.getAlunos();
            String nomes = alunosNaSala.stream().map(Aluno::getNome).collect(Collectors.joining(", "));

            relatorio.add(new ItemRelatorio(sala.getNome(), alunosNaSala.size(), nomes));
        }
        return relatorio;
    }

    // 4. Relatório por RENDA FAMILIAR (Faixas)
    public ObservableList<ItemRelatorio> gerarPorRenda() {
        List<Aluno> todos = alunoService.getAlunos();

        List<Aluno> faixa1 = new ArrayList<>(); // Até 1500
        List<Aluno> faixa2 = new ArrayList<>(); // 1501 a 3000
        List<Aluno> faixa3 = new ArrayList<>(); // 3001 a 5000
        List<Aluno> faixa4 = new ArrayList<>(); // Acima de 5000

        for (Aluno a : todos) {
            double r = a.getRendaFamiliar();
            if (r <= 1500) faixa1.add(a);
            else if (r <= 3000) faixa2.add(a);
            else if (r <= 5000) faixa3.add(a);
            else faixa4.add(a);
        }

        ObservableList<ItemRelatorio> lista = FXCollections.observableArrayList();
        adicionarSeNaoVazio(lista, "Até R$ 1.500", faixa1);
        adicionarSeNaoVazio(lista, "R$ 1.501 a R$ 3.000", faixa2);
        adicionarSeNaoVazio(lista, "R$ 3.001 a R$ 5.000", faixa3);
        adicionarSeNaoVazio(lista, "Acima de R$ 5.000", faixa4);

        return lista;
    }

    // --- Métodos Auxiliares ---

    private ObservableList<ItemRelatorio> converterMapaParaLista(Map<String, List<Aluno>> mapa) {
        ObservableList<ItemRelatorio> lista = FXCollections.observableArrayList();

        mapa.forEach((chave, listaAlunos) -> {
            String nomes = listaAlunos.stream().map(Aluno::getNome).collect(Collectors.joining(", "));
            lista.add(new ItemRelatorio(chave, listaAlunos.size(), nomes));
        });
        return lista;
    }

    private void adicionarSeNaoVazio(List<ItemRelatorio> lista, String categoria, List<Aluno> alunos) {
        if (!alunos.isEmpty()) {
            String nomes = alunos.stream().map(Aluno::getNome).collect(Collectors.joining(", "));
            lista.add(new ItemRelatorio(categoria, alunos.size(), nomes));
        }
    }
}