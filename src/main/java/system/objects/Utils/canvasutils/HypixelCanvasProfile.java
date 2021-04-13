package system.objects.Utils.canvasutils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HypixelCanvasProfile extends Canvas {

    private final String name;
    private final String rank;

    private final Color rankColor;

    private BufferedImage image;

    public HypixelCanvasProfile(String name, String rank, Color rankColor) {
        image = new BufferedImage((int) (name.chars().count() + rank.chars().count())*400, 400, BufferedImage.TYPE_INT_ARGB);

        this.name = name;
        this.rank = rank;

        this.rankColor = rankColor;

        Graphics2D graphics2D = image.createGraphics();
        paint(graphics2D);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void paint(Graphics2D g) {
        //Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Minecraft.ttf")).deriveFont(24f);

        Font font = new Font(Font.SANS_SERIF , Font.BOLD, 300);

        FontMetrics metrics = g.getFontMetrics(font);
        int x = (getImage().getWidth()/2 - metrics.stringWidth(this.rank + this.name)/2);
        int y = (getImage().getHeight() - metrics.getHeight()/2);

        g.setFont(font);
        g.setColor(this.rankColor);
        g.drawString("[" +this.rank + "] " + this.name, x, y);
    }
}
