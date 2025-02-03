package xyz.zzzoozo.enx.ide.elements;

import java.awt.*;
import java.io.*;
import javax.swing.*;

/**
 * The class {@code Saving} saves the text in a specified file.
 */
public class Saving {

    private String textToSave;

    private String path;

    public Saving(String path, String textToSave) {
        this.path = path;
        this.textToSave = textToSave;
    }

    /**
     * @throws IOException if an error happens during the writing
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            // Écrire les lignes que vous souhaitez
            writer.write(textToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
