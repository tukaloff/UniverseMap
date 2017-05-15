/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universemap.levels;

/**
 *
 * @author tukalov_ev
 */
public class Cluster {
    
    private int[][] map;
    
    public Cluster(int size) {
        this.map = new int[size][size];
    }
    
    public int[][] getMap() {
        return map;
    }
    
    public void setMap(int[][] map) {
        this.map = map;
    }
}
