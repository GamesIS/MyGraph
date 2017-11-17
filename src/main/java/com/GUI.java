package com;

import org.jgraph.JGraph;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class GUI {
    private static JFrame frame = new JFrame("Math Graph");
    private static MathGraph mathGraph;

    private static JMenuBar menuBar = new JMenuBar();
    private static JMenu fileMenu = new JMenu("Файл");
    private static JMenuItem newGraph = new JMenuItem("Очистить");
    private static JMenuItem saveItem = new JMenuItem("Сохранить");
    private static JMenuItem openItem = new JMenuItem("Открыть");
    private static JMenuItem exitItem = new JMenuItem("Выход");

    private static JMenu graphMenu = new JMenu("Граф");
    private static JMenuItem addVertex = new JMenuItem("Добавить вершину");
    private static JMenuItem delVertex = new JMenuItem("Удалить вершину");
    private static JMenuItem addEdge = new JMenuItem("Добавить ребро");
    private static JMenuItem delEdge = new JMenuItem("Удалить ребро");
    private static JMenuItem showCountEdge = new JMenuItem("Показать количество ребер");
    private static JMenuItem showCountVertex = new JMenuItem("Показать количество вершин");
    private static JMenuItem showWeightEdge = new JMenuItem("Показать вес ребра");

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        });
    }

    public static void createGUI() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Verdana", Font.PLAIN, 11);

        fileMenu.setFont(font);
        graphMenu.setFont(font);


        newGraph.setFont(font);
        newGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("NEW GRAPH");
                frame.getContentPane().removeAll();
                frame.repaint();
                mathGraph = null;
                buttonEnabled(false);
            }
        });


        addVertex.setFont(font);
        addVertex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(
                        frame,
                        "<html><h2>Введите название вершины");
                try {
                    if (result != null && !"".equals(result)) {
                        if (mathGraph == null) {
                            mathGraph = new MathGraph();
                        }
                        if (!mathGraph.getG().vertexSet().contains(result)) {
                            try {
                                mathGraph.addV(new Vertex(result));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame,
                                        ex.getMessage(),
                                        "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            frame.getContentPane().removeAll();
                            if (mathGraph.getG().vertexSet().size() > 0 && !delVertex.isEnabled()) {
                                delVertex.setEnabled(true);
                            }
                            if (mathGraph.getG().vertexSet().size() > 1 && !addEdge.isEnabled()) {
                                addEdge.setEnabled(true);
                            }

                            frame.getContentPane().add(new JScrollPane(mathGraph.getjGraph()));
                            frame.pack();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Неверное название вершины",
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    System.out.println("Ошибка: " + ex.getMessage());
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        graphMenu.add(addVertex);


        delVertex.setEnabled(false);
        delVertex.setFont(font);
        delVertex.addActionListener(new ActionListener() {//ЕСЛИ УДАЛЯЕТСЯ ВЕРШИНА ТО И РЕБРО ДОЛЖНО УДАЛИТЬСЯ
            public void actionPerformed(ActionEvent e) {
                if (mathGraph == null || mathGraph.getjGraph().getSelectionCells().length != 1) {
                    JOptionPane.showMessageDialog(frame,
                            "Выберите 1 вершину",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    for (Object item : mathGraph.getjGraph().getSelectionCells()) {
                        mathGraph.getG().removeVertex(item.toString());
                    }
                    if (mathGraph.getG().vertexSet().size() == 0) delVertex.setEnabled(false);
                    if (mathGraph.getG().vertexSet().size() < 2) addEdge.setEnabled(false);
                }
            }
        });
        graphMenu.add(delVertex);


        addEdge.setFont(font);
        addEdge.setEnabled(false);
        addEdge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mathGraph == null || mathGraph.getjGraph().getSelectionCells().length != 2) {
                    JOptionPane.showMessageDialog(frame,
                            "Выберите две не смежные вершины для добавления ребра",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    String sourceV = mathGraph.getjGraph().getSelectionCells()[0].toString();
                    String targetV = mathGraph.getjGraph().getSelectionCells()[1].toString();
                    if (mathGraph.getG().getEdge(sourceV, targetV) != null) {
                        JOptionPane.showMessageDialog(frame,
                                "Выберите две не смежные вершины для добавления ребра",
                                "Ошибка",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String result = JOptionPane.showInputDialog(
                            frame,
                            "<html><h2>Введите вес ребра");
                    try {
                        if (result != null && !"".equals(result)) {
                            if(sourceV != null && targetV != null){
                                mathGraph.addE(new Edge(new Vertex(sourceV), new Vertex(targetV), Double.valueOf(result)));
                            }

                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Неверное значение веса ребра",
                                    "Ошибка",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        System.out.println("Ошибка: " + ex.getMessage());
                        JOptionPane.showMessageDialog(frame,
                                ex.getMessage(),
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    if (mathGraph.getG().edgeSet().size() > 0) {
                        delEdge.setEnabled(true);
                        showWeightEdge.setEnabled(true);
                    }
                }
            }
        });
        graphMenu.add(addEdge);


        delEdge.setFont(font);
        delEdge.setEnabled(false);
        delEdge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mathGraph == null || mathGraph.getjGraph().getSelectionCells().length != 2) {
                    JOptionPane.showMessageDialog(frame,
                            "Выберите смежные вершины для удаления ребра",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    String sourceV = mathGraph.getjGraph().getSelectionCells()[0].toString();
                    String targetV = mathGraph.getjGraph().getSelectionCells()[1].toString();
                    if (mathGraph.getG().removeEdge(sourceV, targetV) == null) {
                        JOptionPane.showMessageDialog(frame,
                                "Выберите смежные вершины для удаления ребра",
                                "Ошибка",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (mathGraph.getG().edgeSet().size() == 0) {
                        delEdge.setEnabled(false);
                        showWeightEdge.setEnabled(false);
                    }
                }
            }
        });
        graphMenu.add(delEdge);


        showCountEdge.setFont(font);
        showCountEdge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count;
                if (mathGraph != null) {
                    count = mathGraph.getG().edgeSet().size();
                } else count = 0;
                JOptionPane.showMessageDialog(frame,
                        "\"" + count + "\"",
                        "Количество ребер",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        graphMenu.add(showCountEdge);


        showCountVertex.setFont(font);
        showCountVertex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count;
                if (mathGraph != null) {
                    count = mathGraph.getG().vertexSet().size();
                } else count = 0;
                JOptionPane.showMessageDialog(frame,
                        "\"" + count + "\"",
                        "Количество вершин",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        graphMenu.add(showCountVertex);


        showWeightEdge.setEnabled(false);
        showWeightEdge.setFont(font);
        showWeightEdge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mathGraph == null || mathGraph.getjGraph().getSelectionCells().length != 2) {
                    JOptionPane.showMessageDialog(frame,
                            "Выберите смежные вершины для того, чтобы увидеть вес ребра",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    String sourceV = mathGraph.getjGraph().getSelectionCells()[0].toString();
                    String targetV = mathGraph.getjGraph().getSelectionCells()[1].toString();
                    if (mathGraph.getG().getEdge(sourceV, targetV) == null) {
                        JOptionPane.showMessageDialog(frame,
                                "Выберите смежные вершины для того, чтобы увидеть вес ребра",
                                "Ошибка",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double count = mathGraph.getG().getEdgeWeight(mathGraph.getG().getEdge(sourceV, targetV));
                    if (mathGraph.getG().edgeSet().size() == 0) delEdge.setEnabled(false);
                    JOptionPane.showMessageDialog(frame,
                            "\"" + count + "\"",
                            "Вес ребра",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        graphMenu.add(showWeightEdge);


        fileMenu.add(newGraph);

        openItem.setFont(font);
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else {
                            String filename = f.getName().toLowerCase();
                            return filename.endsWith(".txt");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "Text(*.txt)";
                    }
                });
                if (fc.showDialog(frame, "Открыть") == JFileChooser.APPROVE_OPTION) {
                    try (FileReader fr = new FileReader(fc.getSelectedFile())) {
                        Scanner scan = new Scanner(fr);
                        java.util.List<String> fileText = new ArrayList<String>();

                        while (scan.hasNextLine()) {
                            fileText.add(scan.nextLine());
                        }
                        if (fileText.size() < 2) {
                            throw new Exception("строк меньше 2");
                        }

                        frame.getContentPane().removeAll();
                        mathGraph = new MathGraph();
                        mathGraph.setjGraph(convertToGraph(fileText));

                        if (mathGraph.getG().vertexSet().size() > 0) delVertex.setEnabled(true);
                        else delVertex.setEnabled(false);
                        if (mathGraph.getG().vertexSet().size() > 2) addEdge.setEnabled(true);
                        else addEdge.setEnabled(false);
                        if (mathGraph.getG().edgeSet().size() > 0) delEdge.setEnabled(true);
                        else delEdge.setEnabled(false);
                        if (mathGraph.getG().edgeSet().size() > 0) showWeightEdge.setEnabled(true);
                        else showWeightEdge.setEnabled(false);


                        frame.getContentPane().add(new JScrollPane(mathGraph.getjGraph()));
                        frame.pack();

                    } catch (IOException ex) {
                        System.out.println("Всё погибло! " + ex);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Неверный формат файла: " + ex.getMessage(),
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(openItem);


        saveItem.setFont(font);
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else {
                            String filename = f.getName().toLowerCase();
                            return filename.endsWith(".txt");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "Text(*.txt)";
                    }
                });
                if (fc.showDialog(frame, "Сохранить") == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file != null) {
                        String ext = file.getName();
                        if (!ext.endsWith(".txt")) {
                            fc.setSelectedFile(new File(file.getParent() + "\\" + ext + ".txt"));
                        }
                    }
                    try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {

                        String fileText = "" + (mathGraph.getG().edgeSet().size());

                        for (Edge edge : mathGraph.getG().edgeSet()) {
                            fileText = fileText + "\n"
                                    + mathGraph.getG().getEdgeSource(edge)
                                    + " " + mathGraph.getG().getEdgeTarget(edge)
                                    + " " + mathGraph.getG().getEdgeWeight(edge);
                        }
                        fw.write(fileText);

                    } catch (IOException ex) {
                        System.out.println("Всё погибло! " + ex);
                    } catch (Exception ex) {
                        System.out.println("Все очень плохо! " + ex);
                    }
                }
            }
        });
        fileMenu.add(saveItem);
        fileMenu.addSeparator();

        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(graphMenu);

        frame.setJMenuBar(menuBar);

        frame.setPreferredSize(new Dimension(500, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JGraph convertToGraph(java.util.List<String> fileText) throws Exception {
        Set<Vertex> V = new HashSet<>();
        Set<Edge> E = new HashSet<>();
        int countEdge = Integer.valueOf(fileText.get(0).trim());
        if (countEdge != fileText.size() - 1) throw new Exception("Неверно указано количество ребер");
        System.out.println(fileText.size());
        for (int i = 1; i < fileText.size(); i++) {
            String V1 = null;
            String V2 = null;
            double edgeWeight = 0;

            String str = fileText.get(i).replaceAll("[\\s]{2,}", " ").trim();
            char[] strArray = str.toCharArray();
            int index = 0;
            for (int l = 0; l < strArray.length; l++) {
                if (strArray[l] == ' ') {
                    V1 = str.substring(index, l);

                    index = ++l;
                    while (strArray[l] != ' ') {
                        if (l >= strArray.length) throw new Exception();
                        l++;
                    }

                    V2 = str.substring(index, l);

                    index = ++l;
                    if (strArray.length <= l) throw new Exception();
                    edgeWeight = Double.valueOf(str.substring(index, strArray.length));
                }
            }
            Vertex vertex1 = new Vertex(V1);
            if (!V.contains(vertex1)) {
                V.add(vertex1);
            }
            Vertex vertex2 = new Vertex(V2);
            if (!V.contains(vertex2)) V.add(vertex2);

            Edge edge = new Edge(vertex1, vertex2, edgeWeight);
            if (!E.contains(edge)) E.add(edge);
        }
        mathGraph = new MathGraph();
        return mathGraph.create(V, E);
    }

    private static void buttonEnabled(boolean bool) {
        delVertex.setEnabled(bool);
        addEdge.setEnabled(bool);
        delEdge.setEnabled(bool);
        showWeightEdge.setEnabled(bool);
    }
}
