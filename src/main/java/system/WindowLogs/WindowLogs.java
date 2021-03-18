package system.WindowLogs;

import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class WindowLogs {
    private static JFrame frame;

    public static void createWindow() {
        /* Frame */
        frame = new JFrame();

        /* # To make window in the screen center */
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);

        /* Program icon */
        ImageIcon icon = new ImageIcon("src//main//resources//7005_MCbakedpotato.png");

        /* Frame options */
        frame.setSize(400, 600);
        frame.setLocation(new Point(x, y));
        frame.setTitle("Ryuko BOT");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setEnabled(true);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        drawText("Ryuko bot is activated");
        drawButtom("Close");
    }

    public static void drawText(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setText(text);
        label.setForeground(Color.BLACK);
        label.setLayout(new BorderLayout());
        label.setSize(0, 0);
        frame.add(label, BorderLayout.CENTER);
    }

    public static void drawButtom(String text) {
        JButton CloseButton = new JButton(text);

        CloseButton.addActionListener((action) -> {
            try {
                String operatingSystem = System.getProperty("os.name");

                Runtime runtime = Runtime.getRuntime();

                if (operatingSystem.toLowerCase().contains("windows")) {
                    runtime.exec("rundll32.exe user32.dll, LockWorkStation");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        CloseButton.setLayout(new BorderLayout());
        CloseButton.setLocation((int) ((frame.getSize().getWidth())-115)/2,(int) (frame.getSize().getHeight() - frame.getHeight()) / 2);
        CloseButton.setSize(100, 20);
        CloseButton.setBackground(Color.getHSBColor(135, 135, 135));
        CloseButton.setForeground(Color.WHITE);
        frame.add(CloseButton, new FlowLayout());
    }
}
