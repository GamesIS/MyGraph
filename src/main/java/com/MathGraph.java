package com;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MathGraph {
    private JGraph jGraph;
    private JGraphModelAdapter m_jgAdapter;
    private ListenableUndirectedWeightedGraph<String, Edge> g =
            new ListenableUndirectedWeightedGraph<String, Edge>(Edge.class);

    public MathGraph() {
        m_jgAdapter = new JGraphModelAdapter(g);
        jGraph = new JGraph(m_jgAdapter);
    }

    public JGraph create(Set<String> V, Set<Edge> E) {
        try {
            for (String v : V) {
                addV(v);
            }
            for (Edge e : E) {
                addE(e);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
        return jGraph;
    }

    void addV(String v) {
        Random random = new Random();
        g.addVertex(v);
        positionVertexAt(v, random.nextInt(400), random.nextInt(400));
    }

    void addE(Edge edge) {
        g.setEdgeWeight(edge, edge.getWeig());
        g.addEdge(edge.getV1(), edge.getV2(), edge);
        System.out.println(g.getEdgeWeight(edge));
    }

    boolean contains(String vertex){
        return g.containsVertex(vertex);
    }

    boolean contains(Edge edge){
        return g.containsEdge(edge);
    }

    private void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        Map attr = cell.getAttributes();

        GraphConstants.setBounds(attr, new Rectangle(x, y, 50, 60));

        Map cellAttr = new HashMap();
        cellAttr.put(cell, attr);
        m_jgAdapter.edit(cellAttr, null, null, null);
    }

    public JGraph getjGraph() {
        return jGraph;
    }

    public void setjGraph(JGraph jGraph) {
        this.jGraph = jGraph;
    }

    public int getCountEdge(){
        return g.edgeSet().size();
    }

    public int getCountVertex(){
        return g.vertexSet().size();
    }

    public double getEdgeWeight(Edge edge){
        return g.getEdgeWeight(edge);
    }

    public boolean removeVertex(String vertex){
        return g.removeVertex(vertex);
    }

    public Edge removeEdge(String source, String target){
        return g.removeEdge(source, target);
    }
    public Edge getEdge(String source, String target){
        return g.getEdge(source, target);
    }

    @Override
    public String toString() {
        String str = "" + (this.getCountEdge());
        for (Edge edge : g.edgeSet()) {
            str = str + "\n"
                    + g.getEdgeSource(edge)
                    + " " + g.getEdgeTarget(edge)
                    + " " + g.getEdgeWeight(edge);
        }
        return str;
    }
}
