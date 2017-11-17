package com;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

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

    public JGraph create(Set<Vertex> V, Set<Edge> E) {
        try {
            for (Vertex v : V) {
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

    void addV(Vertex v) {
        Random random = new Random();
        g.addVertex(v.getName());
        positionVertexAt(v.getName(), random.nextInt(400), random.nextInt(400));
    }

    void addE(Edge edge) {
        g.setEdgeWeight(edge, edge.getWeig());
        g.addEdge(edge.getV1().getName(), edge.getV2().getName(), edge);
        System.out.println(g.getEdgeWeight(edge));
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

    public ListenableUndirectedWeightedGraph<String, Edge> getG() {
        return g;
    }

    public void setG(ListenableUndirectedWeightedGraph<String, Edge> g) {
        this.g = g;
    }
}
