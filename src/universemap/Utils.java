/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universemap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tukalov_ev
 */
public class Utils {
    
    public static BufferedImage getBufferedImage(int[][] map) {
        BufferedImage bi = new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_INT_ARGB);
        int[] perc = new int[4];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                int x = map[i][j];
                bi.setRGB(i, j, new Color(x, x, x).getRGB());
                switch(x / (255 / 4)) {
                    case 0:
                        perc[0]++;
                        break;
                    case 1:
                        perc[1]++;
                        break;
                    case 2:
                        perc[2]++;
                        break;
                    case 3:
                        perc[3]++;
                        break;
                }
            }
        }
        for (int i : perc) {
            System.out.printf("%1$2.2f%%\t", i / ((float)(map.length * map.length)) * 100);
        }
        System.out.println("");
        return bi;
    }
    
    public static void saveImage(String name, BufferedImage bi) throws IOException {
        File outputfile = new File(name + ".png");
        ImageIO.write(bi, "png", outputfile);
    }
    
    public static void saveGif(String name) throws Exception {
        File[] f = new File(name + "/").listFiles();
        String[] g = new String[f.length + 1];
        for (int i = 0; i < f.length; i++) g[i] = f[i].getAbsolutePath();
        g[g.length - 1] = name + ".gif";
        GifSequenceWriter.main(g);
    }
}
