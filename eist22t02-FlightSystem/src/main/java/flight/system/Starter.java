package flight.system;

import flight.system.UI.MainFrame;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

public class Starter {
    public static void main(String[] args) {

        var ctx = new SpringApplicationBuilder(MainFrame.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {

            var ex = ctx.getBean(MainFrame.class);
            ex.setVisible(true);
        });
    }
}
