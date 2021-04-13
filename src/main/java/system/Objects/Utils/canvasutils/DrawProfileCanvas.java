package system.Objects.Utils.canvasutils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import system.Objects.Utils.profileconfigutils.ProfileBuilder;
import system.Objects.Utils.levelUtils.LevelsCalculations;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Random;

public class DrawProfileCanvas extends Canvas implements LevelsCalculations {

    private BufferedImage bufferedImage;
    private int ruko;
    private int level;
    private int achievements;

    public DrawProfileCanvas(User user, Guild guild, Role role, int ruko, int level, int achievements) {
        try {
            Random random = new Random();
            int nextNumber = random.nextInt(12);
            this.bufferedImage = ImageIO.read(new File("Image/canvas/id_"+nextNumber+".png"));
            Graphics2D graphics = this.bufferedImage.createGraphics();
            this.ruko = ruko;
            this.level = level;
            this.achievements = achievements;

            paint(graphics, user, guild, role);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRuko() {
        return ruko;
    }

    public void paint(Graphics2D g, User user, Guild guild, Role role) {

        try {

            ProfileBuilder profile = new ProfileBuilder(user);

            Graphics2D inform = (Graphics2D) bufferedImage.getGraphics();

            BufferedImage[] filter = {
                    null,
                    ImageIO.read(new File("Image/Filters/PngItem_5099664.png")),
                    ImageIO.read(new File("Image/Filters/PngItem_5236248.png")),
                    ImageIO.read(new File("Image/Filters/pinpng.com-transparent-text-tumblr-png-6860503.png")),
                    ImageIO.read(new File("Image/Filters/pngkey.com-ears-png-1184858.png")),
                    ImageIO.read(new File("Image/Filters/287540521015211.png")),
                    ImageIO.read(new File("Image/Filters/SeekPng.com_heart-filter-png_632052.png")),
                    ImageIO.read(new File("Image/Filters/SeekPng.com_snapchat-filters-png_90572.png")),
                    ImageIO.read(new File("Image/Filters/pngkey.com-dog-png-100069.png"))
            };

            String username = user.getName() + "#" + user.getDiscriminator();

            URLConnection url = new URL(Objects.requireNonNull(user.getAvatarUrl())).openConnection();
            url.addRequestProperty("User-Agent", null);
            BufferedImage avatar = ImageIO.read(url.getInputStream());

            URLConnection urlg = new URL(Objects.requireNonNull(guild.getIconUrl())).openConnection();
            urlg.addRequestProperty("User-Agent", null);
            BufferedImage avatarg = ImageIO.read(urlg.getInputStream());
            
            Font custom = null;

            if (username.chars().count() >= 16) {
                custom = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Lexend-Regular.ttf")).deriveFont(Font.BOLD, 20f);
            } else {
                custom = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Lexend-Regular.ttf")).deriveFont(Font.BOLD, 24f);
            }

            g.setFont(custom);
            inform.setFont(custom.deriveFont(24f));

            FontMetrics metrics = inform.getFontMetrics(custom);
            int xtext = (getBufferedImage().getWidth() / 2 - metrics.stringWidth(username) / 2);
            int x = (getBufferedImage().getWidth() / 2 - metrics.stringWidth("") / 2);
            int y = (getBufferedImage().getHeight() / 2 - metrics.getHeight() / 2);

            g.setColor(role.getColor());
            g.drawString(username, xtext + 70, y - 50);

            inform.setColor(new Color(255, 204, 108));
            inform.drawString("Rukos: " + ruko, x - 35, y + 20);
            inform.setColor(new Color(35, 145, 229));
            inform.drawString("Level: " + level, x - 35, y + 60);
            inform.setColor(new Color(132, 35, 229));
            inform.drawString("Achievements: " + achievements, x - 35, y + 100);

            custom = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Lexend-Regular.ttf")).deriveFont(14f);
            g.setFont(custom);

            g.setClip(new Ellipse2D.Float(x - 210, y - 90, avatar.getWidth(), avatar.getHeight()));
            g.drawImage(avatar, x - 210, y - 90, null);

            g.setClip(new Ellipse2D.Float(x - 220, y + 30, 30, 30));
            g.drawImage(avatarg, x - 220, y + 30, 30, 30, null);

            inform.setColor(role.getColor());
            inform.setStroke(new BasicStroke(6f));
            inform.drawRoundRect(x - 210, y - 90, avatar.getWidth(), avatar.getHeight(), avatar.getWidth(), avatar.getHeight());
            inform.setStroke(new BasicStroke(0f));

            if (profile.getFilter() == 0) {

            } else if (profile.getFilter() == 1) {
                inform.drawImage(filter[1], x - 210, y - 95, avatar.getWidth(), avatar.getHeight(), null);
            } else if (profile.getFilter() == 2) {
                inform.drawImage(filter[2], x - 218, y, 150, 20, null);
            } else if (profile.getFilter() == 3) {
                inform.drawImage(filter[3], x - 177, y+5, avatar.getWidth()-70, avatar.getHeight()-70, null);
            } else if (profile.getFilter() == 4) {
                inform.drawImage(filter[4], x - 210, y-90, avatar.getWidth(), avatar.getHeight()/4, null);
            } else if (profile.getFilter() == 5) {
                inform.drawImage(filter[5], x - 250, y-180, avatar.getWidth()+80, avatar.getHeight()+80, null);
            } else if (profile.getFilter() == 6) {
                inform.drawImage(filter[6], x - 230, y-150, avatar.getWidth()+40, avatar.getHeight()+40-20, null);
            } else if (profile.getFilter() == 7) {
                inform.drawImage(filter[7], x - 215, y-120, avatar.getWidth()+10, avatar.getHeight()+20, null);
            }  else if (profile.getFilter() == 8) {
                inform.drawImage(filter[8], x - 220, y-110, avatar.getWidth()+20, avatar.getHeight(), null);
            }


        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
