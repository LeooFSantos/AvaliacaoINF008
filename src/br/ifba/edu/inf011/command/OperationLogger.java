package br.ifba.edu.inf011.command;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class OperationLogger {

    private static final String LOG_DIRECTORY = "logs";
    private final String logFile;
    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private List<String> registros;

    
    public OperationLogger(String documentoNumero) {
        this.registros = new ArrayList<>();

        String safe = (documentoNumero == null || documentoNumero.isBlank())
                ? "sem_numero"
                : documentoNumero.replaceAll("[^a-zA-Z0-9._-]", "_");
        this.logFile = LOG_DIRECTORY + "/doc_" + safe + ".log";
    }

    
    public void registrarExecucao(Command comando) {
        String registro = String.format("[%s] EXECUTE - %s",
                LocalDateTime.now().format(FORMATTER),
                comando.getDescricao()
        );
        registros.add(registro);
        escreverNoArquivo(registro);
    }

    
    public void registrarUndo(Command comando) {
        String registro = String.format("[%s] UNDO - %s",
                LocalDateTime.now().format(FORMATTER),
                comando.getDescricao()
        );
        registros.add(registro);
        escreverNoArquivo(registro);
    }

    
    public void registrarRedo(Command comando) {
        String registro = String.format("[%s] REDO - %s",
                LocalDateTime.now().format(FORMATTER),
                comando.getDescricao()
        );
        registros.add(registro);
        escreverNoArquivo(registro);
    }

    
    public void registrarConsolidacao(int operacoesDescartadas) {
        String registro = String.format("[%s] CONSOLIDATE - %d operações descartadas do histórico",
                LocalDateTime.now().format(FORMATTER),
                operacoesDescartadas
        );
        registros.add(registro);
        escreverNoArquivo(registro);
    }

    
    private void escreverNoArquivo(String registro) {
        try {
            
            java.nio.file.Path dirPath = java.nio.file.Paths.get(LOG_DIRECTORY);
            if (!java.nio.file.Files.exists(dirPath)) {
                java.nio.file.Files.createDirectories(dirPath);
            }

            
            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(registro);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }

    
    public List<String> obterRegistros() {
        return new ArrayList<>(registros);
    }

    
    public void limpar() {
        registros.clear();
    }
}
