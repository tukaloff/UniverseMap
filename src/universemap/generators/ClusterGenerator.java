/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universemap.generators;

import java.util.ArrayList;
import java.util.List;
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
    
    public ClusterGenerator(int radius) {
        this(512, radius);
    }
    
    public ClusterGenerator(int size, int radius) {
        this.radius = radius;
        this.size = size + this.radius * 4;
        this.cluster = new Cluster(this.size);
        randomGenerate(cluster.getMap());
    }
    
    public Cluster getCluster() {
        return cluster;
    }
    
    public void setCoeffIntencity(int coeff) {
        this.coeffIntencity = coeff;
    }
    
    public Cluster generate() {
        this.cluster.setMap(normalize(filterOne(cluster.getMap())));
        return this.cluster;
    }
    
    public Cluster reinforce(int x) {
        this.cluster.setMap(normalize(filterTwo(cluster.getMap(), radius * x)));
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
                if (i < radius || i > map.length - radius
                        || j < radius || i > map[i].length - radius)
                    newMap[i][j] = 0;
                int cur = map[i][j];
                int avg = 0;
                for (int m = 0; m < radius * 2 + 1; m++)
                    for (int n = 0; n < radius * 2 + 1; n++) {
                        if (m == radius && n == radius) continue;
                        avg += map[i - radius + m][j - radius + n];// * (1.0/(Math.abs(m - radius) + Math.abs(n - radius)));
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
    
    private int[][] filterTwo(int[][] map, int blockSize) {
        int[][] newMap = new int[map.length][map[0].length];
        List<List<int[][]>> blocks = this.getBlocks(map, blockSize);
        blocks.parallelStream().forEach((blockRow)->{
            blockRow.parallelStream().forEach((block)-> {
                List<int[]> brightest = new ArrayList<>();
                for (int i = 0; i < block.length; i++) {
                    for (int j = 0; j < block[i].length; j++) {
                        if (block[i][j] > 255 * (.8 - 1f / (blockSize / radius == 1 ? 2 : blockSize / radius)))
                                //&& block[i][j] < (255 - 255 * (1 - 1/blockSize)))
                            brightest.add(new int[]{i, j});
                    }
                }
                try {
                brightest.sort((int[] t, int[] t1) -> {
                    int x = (t[0] + t[1]) - (t1[0] + t1[1]);
                    if (x != 0) return x;
                    else {
                        if (t[0] > t[1]) return 1;
                        else return -1;
                    }
                });
                } catch(IllegalArgumentException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                List<Line> lines = new ArrayList<>();
                if (brightest.size() <= 1) return;
                for (int i = 0; i < brightest.size(); i++) 
                    for (int j = 0; j < brightest.size(); j++) {
                        if (i >= j) continue;
                        lines.add(new Line(brightest.get(i)[0], brightest.get(i)[1]
                                , brightest.get(j)[0], brightest.get(j)[1]));
                    }
                //for (Line l : lines) if (l.p1.x == l.p2.x && l.p1.y == l.p2.y) lines.remove(l);
                lines.forEach((line) -> {
                    drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y, block, 
                            (block[line.p1.x][line.p1.y] + block[line.p2.x][line.p2.y])/2);
                    /*
                    for (int i = 0; i < brightest.size(); i++) {
                    for (int j = 0; j < brightest.size(); j++) {
                    if (i == j) continue;
                    if (i > j) continue;
                    int[] b1 = brightest.get(i);
                    int[] b2 = brightest.get(j);
                    int len = (int) Math.sqrt(Math.pow(b2[0] - b1[0], 2) + Math.pow(b2[1] - b1[1], 2));
                    if (len == 0) continue;
                    for (int m = b1[0]; m < b2[0]; m++) {
                    for (int n = b1[1]; n < b2[1]; n++) {
                    if (((m - b1[0]) * (b2[1] - b1[1]) - (b2[0] - b1[0]) * (n - b1[1])) == 0) {
                    int len1 = (int) Math.sqrt(Math.pow(m - b1[0], 2) + Math.pow(n - b1[1], 2));
                    len1++;
                    if (len * .66 > len1) {
                    int newBright = (int) (block[b1[0]][b1[1]] * (1 - len1 / (len * .66)));
                    block[m][n] = block[m][n] > newBright ? block[m][n] : newBright;
                    }
                    }
                    }
                    }
                    }
                    }
                    for (int i = brightest.size() - 1; i >= 0; i--) {
                    for (int j = brightest.size() - 1; j >= 0; j--) {
                    if (i == j) continue;
                    if (i < j) continue;
                    int[] b1 = brightest.get(i);
                    int[] b2 = brightest.get(j);
                    int len = (int) Math.sqrt(Math.pow(b2[0] - b1[0], 2) + Math.pow(b2[1] - b1[1], 2));
                    if (len == 0) continue;
                    for (int m = b1[0] - 1; m >= b2[0]; m--) {
                    for (int n = b1[1] - 1; n >= b2[1]; n--) {
                    if (((m - b1[0]) * (b2[1] - b1[1]) - (b2[0] - b1[0]) * (n - b1[1])) == 0) {
                    int len1 = (int) Math.sqrt(Math.pow(m - b1[0], 2) + Math.pow(n - b1[1], 2));
                    len1++;
                    if (len / 3.0 * 2 > len1) {
                    int newBright = (int) (block[b1[0]][b1[1]] * (1 - len1 / (len / 3.0 * 2)));
                    block[m][n] = block[m][n] > newBright ? block[m][n] : newBright;
                    }
                    }
                    }
                    }
                    }
                    }
                    */
                });
            });
        });
        return this.assembleBlocks(blocks, newMap);
    }
    
    private List<List<int[][]>> getBlocks(int[][] map, int blockSize) {
        List<List<int[][]>> blocks = new ArrayList<>();
        for (int i = 0; i < map.length / (blockSize); i++) {
            List<int[][]> blockRow = new ArrayList<>();
            for (int j = 0; j < map[i * blockSize].length / (blockSize); j++) {
                int[][] block = new int[blockSize][blockSize];
                for (int m = 0; m < block.length; m++) {
                    for (int n = 0; n < block[m].length; n++) {
                        block[m][n] = map[i * blockSize + m][j * blockSize + n];
                    }
                }
                blockRow.add(block);
            }
            blocks.add(blockRow);
        }
        return blocks;
    }
    
    private int[][] assembleBlocks(List<List<int[][]>> blocks, int[][] newMap) {
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).size(); j++) {
                int[][] block = blocks.get(i).get(j);
                for (int m = 0; m < block.length; m++) {
                    for (int n = 0; n < block[m].length; n++) {
                        newMap[i * block.length + m][j * block[m].length + n] = block[m][n];
                    }
                }
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

    public int[][] crop(int[][] map) {
        int[][] newMap = new int[map.length - radius * 4][map[0].length - radius * 4];
        for (int i = radius * 2; i < map.length - radius * 2; i++) 
            for (int j = radius * 2; j < map[i].length - radius * 2; j++)
                newMap[i - radius * 2][j - radius * 2] = map[i][j];
        return newMap;
    }
    
    private int[][] drawLine(int x1, int y1, int x2, int y2, int[][] map, int rgb) {
        boolean steep = false;
        int w = map.length;
        int h = map[0].length;
        int halfW = w / 2;
        int halfH = h / 2;
        if (Math.abs(x2 - x1) < Math.abs(y2 - y1)) {
            int tmp = x1;
            x1 = y1;
            y1 = tmp;
            tmp = x2;
            x2 = y2;
            y2 = tmp;
            steep = true;
        }
        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        float d = (float) 1 / (x2 - x1);
        for (int x = x1; x <= x2; x++) {
            float t = (x - x1) * d;
            int y = (int) ((y1 + 0.005) * (1.0 - t) + (y2 + 0.005) * t);
            if (steep) {
                int rx = halfW + y;
                int ry = h - (halfH + x);
                if (!(rx < 0 || rx >= w || ry < 0 || ry >= h)) {
                    map[rx][ry] = rgb;
                }
            } else {
                int rx = halfW + x;
                int ry = h - (halfH + y);
                if (!(rx < 0 || rx >= w || ry < 0 || ry >= h)) {
                    map[rx][ry] = rgb;
                }
            }
        }
        return map;
    }
}
