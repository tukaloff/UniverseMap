/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universemap;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
        ClusterGenerator generator = new ClusterGenerator(16);
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
        generator.getCluster().setMap(generator.crop(generator.getCluster().getMap()));
        for (int i = 1; i <= 512 / 16; i*=2) {
            start = System.currentTimeMillis();
            cluster = generator.reinforce(i);
            stop = System.currentTimeMillis();
            System.out.print(i + " -> " + (stop - start) + ":\t");
            bi = Utils.getBufferedImage(cluster.getMap());
            Utils.saveImage("reinforced/reinforced" + (i < 10?"0":"") + i, bi);
        }
        Utils.saveGif("cluster");
        Utils.saveGif("reinforced");
    }
    
}
