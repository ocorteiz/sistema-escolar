package com.autobahn.sistemaescolar.service;

import com.autobahn.sistemaescolar.model.ItemRelatorio;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PdfService {

    private static final PdfService instancia = new PdfService();

    private PdfService() {}

    public static PdfService getInstancia() {
        return instancia;
    }

    /**
     * Gera o PDF baseado na lista de itens do relatório.
     * * @param dados A lista de dados que está na tabela.
     * @param tituloRelatorio O título para colocar no topo.
     * @param destino O arquivo onde o PDF será salvo.
     */
    public void gerarPdfRelatorio(List<ItemRelatorio> dados, String tituloRelatorio, File destino) throws FileNotFoundException {

        // 1. Inicializa o escritor de PDF (Writer -> PdfDocument -> Document)
        PdfWriter writer = new PdfWriter(destino);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 2. Adiciona Título do Documento
        Paragraph titulo = new Paragraph("Relatório Escolar: " + tituloRelatorio)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);

        document.add(titulo);

        // 3. Cria a Tabela (3 colunas com larguras relativas)
        // Larguras: Categoria(30%), Qtd(10%), Alunos(60%)
        float[] largurasColunas = {3, 1, 6};
        Table table = new Table(UnitValue.createPercentArray(largurasColunas));
        table.setWidth(UnitValue.createPercentValue(100)); // Ocupa 100% da página

        // 4. Cria o Cabeçalho da Tabela
        adicionarCabecalho(table, "Categoria / Grupo");
        adicionarCabecalho(table, "Qtd.");
        adicionarCabecalho(table, "Alunos Listados");

        // 5. Preenche com os Dados da lista
        for (ItemRelatorio item : dados) {
            // Coluna 1: Categoria
            table.addCell(new Cell().add(new Paragraph(item.getCategoria())));

            // Coluna 2: Quantidade (Centralizada)
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantidade())))
                    .setTextAlignment(TextAlignment.CENTER));

            // Coluna 3: Lista de Nomes
            table.addCell(new Cell().add(new Paragraph(item.getAlunos())));
        }

        // 6. Adiciona a tabela ao documento
        document.add(table);

        // 7. Adiciona um rodapé simples
        document.add(new Paragraph("\nGerado automaticamente pelo Sistema Escolar")
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.RIGHT));

        // 8. Fecha o documento (Salva o arquivo)
        document.close();

        System.out.println("PDF gerado com sucesso em: " + destino.getAbsolutePath());
    }

    // Método auxiliar para formatar células de cabeçalho (Cinza e Negrito)
    private void adicionarCabecalho(Table table, String texto) {
        Cell cell = new Cell();
        cell.add(new Paragraph(texto).setBold());
        cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
    }
}