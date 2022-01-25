package samygo;

import java.awt.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import samygo.frm.Main;
import samygo.infra.AppProps;
import samygo.ui.Images;

@SpringBootApplication
@ConfigurationPropertiesScan
public class App {

    @Bean
    @ConfigurationProperties("samygo")
    public AppProps props() {
        return new AppProps();
    }

    @Bean
    public JLabel modeLabel() {
        return new JLabel();
    }

    @Bean
    public JLabel statusLabel() {
        return new JLabel();
    }

    @Bean
    public JTable mainTable() {
        return new JTable();
    }

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        FlatLightLaf.setup();
        new SpringApplicationBuilder(App.class)
                .headless(false)
                .run(args)
                .getBean(Main.class)
                .setVisible(true);
    }
}
