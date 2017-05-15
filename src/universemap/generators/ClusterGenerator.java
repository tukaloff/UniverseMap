/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universemap.generators;

import universemap.levels.Cluster;

/**
 *
 * @author tukalov_ev
 */
public class ClusterGenerator {
    
    private Cluster cluster;
    private int size;
    private int radius;
    private int coeffIntencity;
    
    public ClusterGenerator() {
        this(512);
    }
    
    public ClusterGenerator(int size) {
        this.size = size;
        this.cluster = new Cluster(size);
        randomGenerate(cluster.getMap());
    }
    
    public Cluster getCluster() {
        return cluster;
    }
    
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    public void setCoeffIntencity(int coeff) {
        this.coeffIntencity = coeff;
    }
    
    public Cluster generate() {
        this.cluster.setMap(normalize(filterOne(cluster.getMap())));
        return this.cluster;
    }
    
    private int[][] randomGenerate(int[][] map) {
        for (int[] row : map)
            for (int j = 0; j < row.length; j++)
                row[j] = (int) (255 * Math.random());
        return map;
    }
    
    private int[][] filterOne(int[][] map) {
        int[][] newMap = new int[map.length][map[0].length];
        int intencity = (int) Math.pow(radius * coeffIntencity, 2);
        for (int i = radius; i < map.length - radius; i++)
            for (int j = radius; j < map[i].length - radius; j++) {
                int cur = map[i][j];
                int avg = 0;
                for (int m = 0; m < radius * 2 + 1; m++)
                    for (int n = 0; n < radius * 2 + 1; n++) {
                        if (m == radius && n == radius) continue;
                        avg += map[i - radius + m][j - radius + n];
                    }
                if (cur > avg) {
                    newMap[i][j] = Math.min(cur + avg / intencity, 255);
                } else if (cur < avg) {
                    newMap[i][j] = Math.max(cur - avg / intencity, 0);
                } else {
                    newMap[i][j] = map[i][j];
                }
            }
        return newMap;
    }
    
    private int[][] normalize(int map[][]) {
        int max = 0;
        for (int[] row : map)
            for (int j = 0; j < row.length; j++)
                max = row[j] > max ? row[j] : max;
        double k = 255f / max;
        for (int[] row : map)
            for (int j = 0; j < row.length; j++)
                row[j] *= k;
        return map;
    }
}
