package fr.isen.ticket.app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LecteurJSON {

    public void intitialiserJSON(String filename) throws IOException {
        // Chargement du fichier depuis les ressources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IOException("Le fichier n'a pas pu être trouvé dans les ressources.");
        }

        // Définir le répertoire de sortie
        Path outputPath = Paths.get("output", filename);
        Files.createDirectories(outputPath.getParent()); // Créer le répertoire s'il n'existe pas

        // Copier le fichier des ressources dans le répertoire "output"
        try (InputStream in = inputStream;
             OutputStream out = new FileOutputStream(outputPath.toFile())) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }


    public String lireJSON(String filename) throws IOException {
        Path filePath = Paths.get("output", filename);

        if (!Files.exists(filePath)) {
            throw new IOException("Le fichier n'existe pas dans le répertoire de sortie.");
        }

        return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
    }

    public void ecrireJSON(String filename, String content) throws IOException {
        Path filePath = Paths.get("output", filename);
        Files.createDirectories(filePath.getParent()); // Créer le répertoire s'il n'existe pas
        String fileNameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
        content = "{ \"" + fileNameWithoutExtension + "\": " + content + " }";
        // Écrire le contenu dans le fichier
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }
}
