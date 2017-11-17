package com;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    private String name;
    private String v1;
    private String v2;
    private double weig;

    public Edge(){
        super();
    }

    public Edge(String v1, String v2, double weig) {
        this.v1 = v1;
        this.v2 = v2;
        this.weig = weig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public double getWeig() {
        return weig;
    }

    public void setWeig(double weig) {
        this.weig = weig;
    }
}
