package samygo;

import javax.swing.*;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import samygo.frm.Main;
import samygo.infra.AppProps;

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
        System.setProperty("apple.awt.application.name", "SamyGO Channel Editor");
        System.setProperty( "apple.awt.application.appearance", "system" );
        FlatInterFont.install();
        FlatLaf.setPreferredFontFamily(FlatInterFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatInterFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatInterFont.FAMILY_SEMIBOLD);
        FlatLightLaf.setup();
        new SpringApplicationBuilder(App.class)
                .headless(false)
                .run(args)
                .getBean(Main.class)
                .setVisible(true);
    }
}
