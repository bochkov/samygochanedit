package samygo.util;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ResourceContent {

    private final String resource;

    public ResourceContent(String resource) {
        this.resource = resource;
    }

    public ImageIcon asIcon() {
        URL url = ResourceContent.class.getResource(resource);
        if (url == null)
            throw new IllegalArgumentException(fileNotFound());
        return new ImageIcon(url);
    }

    public ImageIcon asIcon(int size) {
        Image icon = asIcon().getImage();
        Image newImage = icon.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    public URL asUrl() {
        return ResourceContent.class.getResource(resource);
    }

    public String asText() {
        return asText(false);
    }

    public String asText(boolean keepFormatting) {
        List<String> lines = new ArrayList<>();
        InputStream is = ResourceContent.class.getResourceAsStream(resource);
        if (is == null)
            throw new IllegalArgumentException(fileNotFound());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(keepFormatting ? line : line.trim());
            }
        } catch (IOException ex) {
            LOG.warn(ex.getMessage(), ex);
        }
        return String.join("\n", lines);
    }

    private String fileNotFound() {
        return String.format("file '%s' not found", resource);
    }
}
