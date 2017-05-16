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
        ClusterGenerator generator = new ClusterGenerator();
        generator.setRadius(16);
        generator.setCoeffIntencity(2);
        Cluster cluster;
        BufferedImage bi;
        long start, stop;
        for (int i = 0; i < 30; i++) {
            start = System.currentTimeMillis();
            cluster = generator.generate();
            stop = System.currentTimeMillis();
            System.out.print(i + " -> " + (stop - start) + ":\t");
            bi = Utils.getBufferedImage(cluster.getMap());
            Utils.saveImage("cluster/Cluster" + (i < 10 ? "0" : "") + i, bi);
        }
        for (int i = 1; i < 512 / 16 / 2; i++) {
            cluster = generator.reinforce(1);
            bi = Utils.getBufferedImage(cluster.getMap());
            Utils.saveImage("reinforced/reinforced" + (i < 10?"0":"") + i, bi);
        }
        File[] f = new File("cluster/").listFiles();
        String[] g = new String[f.length + 1];
        for (int i = 0; i < f.length; i++) g[i] = f[i].getAbsolutePath();
        g[g.length - 1] = "cluster.gif";
        GifSequenceWriter.main(g);
        
        f = new File("reinforced/").listFiles();
        g = new String[f.length + 1];
        for (int i = 0; i < f.length; i++) g[i] = f[i].getAbsolutePath();
        g[g.length - 1] = "reinforced.gif";
        GifSequenceWriter.main(g);
    }
    
}
