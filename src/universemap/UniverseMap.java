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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import universemap.generators.ClusterGenerator;
import universemap.levels.Cluster;

/**
 *
 * @author tukalov_ev
 */
public class UniverseMap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        System.out.println(Color.BLACK.getRGB());
        System.out.println(Color.WHITE.getRGB());
        ClusterGenerator generator = new ClusterGenerator();
        generator.setRadius(8);
        generator.setCoeffIntencity(4);
        Cluster cluster = generator.getCluster();
        BufferedImage bi;// = Utils.getBufferedImage(cluster.getMap());
        //Utils.saveImage("cluster/Cluster", bi);
        for (int i = 0; i < 50; i++) {
            cluster = generator.generate();
            bi = Utils.getBufferedImage(cluster.getMap());
            Utils.saveImage("cluster/Cluster" + (i < 10 ? "0" : "") + i, bi);
        }
        File[] f = new File("cluster/").listFiles();
        String[] g = new String[f.length + 1];
        for (int i = 0; i < f.length; i++) g[i] = f[i].getAbsolutePath();
        g[g.length - 1] = "cluster.gif";
        System.out.println(g.length + Arrays.toString(g));
        GifSequenceWriter.main(g);
    }
    
}
