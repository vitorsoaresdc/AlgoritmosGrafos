import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InterfaceGrafo extends JFrame {
    private Grafo grafo;
    private JTextArea textArea;
    private JTextField numeroNosField, origemField, destinoField, pesoField;
    private JComboBox<String> tipoGrafoComboBox;
    private JButton criarGrafoButton, adicionarArestaButton, imprimirGrafoButton, dijkstraButton, aEstrelaButton;

    public InterfaceGrafo() {
        setTitle("Interface do Grafo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        grafo = null;

        JPanel panelConfig = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelConfig.add(new JLabel("Número de Nós:"), gbc);

        gbc.gridx = 1;
        numeroNosField = new JTextField(10);
        panelConfig.add(numeroNosField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelConfig.add(new JLabel("Tipo de Grafo:"), gbc);

        gbc.gridx = 1;
        tipoGrafoComboBox = new JComboBox<>(new String[]{"Direcional não ponderado", "Não direcional ponderado"});
        panelConfig.add(tipoGrafoComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        criarGrafoButton = new JButton("Criar Grafo");
        panelConfig.add(criarGrafoButton, gbc);

        // Painel para adicionar arestas e imprimir grafo
        JPanel panelArestas = new JPanel(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelArestas.add(new JLabel("Nó Origem:"), gbc);

        gbc.gridx = 1;
        origemField = new JTextField(10);
        panelArestas.add(origemField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelArestas.add(new JLabel("Nó Destino:"), gbc);

        gbc.gridx = 1;
        destinoField = new JTextField(10);
        panelArestas.add(destinoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelArestas.add(new JLabel("Peso (se aplicável):"), gbc);

        gbc.gridx = 1;
        pesoField = new JTextField(10);
        panelArestas.add(pesoField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        adicionarArestaButton = new JButton("Adicionar Aresta");
        panelArestas.add(adicionarArestaButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        imprimirGrafoButton = new JButton("Imprimir Grafo");
        imprimirGrafoButton.setForeground(Color.decode("#1e90ff"));
        panelArestas.add(imprimirGrafoButton, gbc);

        // Painel para execução de algoritmos
        JPanel panelAlgoritmos = new JPanel(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        dijkstraButton = new JButton("Executar Dijkstra");
        panelAlgoritmos.add(dijkstraButton, gbc);

        gbc.gridx = 1;
        aEstrelaButton = new JButton("Executar A*");
        panelAlgoritmos.add(aEstrelaButton, gbc);

        // Adicionando painéis ao JFrame
        add(panelConfig, BorderLayout.NORTH);
        add(panelArestas, BorderLayout.WEST);
        add(panelAlgoritmos, BorderLayout.SOUTH);

        // Área de texto para exibir o grafo e resultados
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Adicionando listeners aos botões
        criarGrafoButton.addActionListener(e -> criarGrafo());
        adicionarArestaButton.addActionListener(e -> adicionarAresta());
        imprimirGrafoButton.addActionListener(e -> imprimirGrafo());
        dijkstraButton.addActionListener(e -> executarDijkstra());
        aEstrelaButton.addActionListener(e -> executarAEstrela());
    }

    private void criarGrafo() {
        if (grafo == null) {
            try {
                int numeroNos = Integer.parseInt(numeroNosField.getText());
                int tipoGrafo = tipoGrafoComboBox.getSelectedIndex();
                boolean direcional = tipoGrafo == 0;
                boolean ponderado = tipoGrafo == 1;
                grafo = new Grafo(direcional, ponderado, numeroNos);
                textArea.append("Grafo criado com " + numeroNos + " nós.\n");
            } catch (NumberFormatException e) {
                textArea.append("Número de nós inválido.\n");
            }
        } else {
            textArea.append("Grafo já criado.\n");
        }
    }

    private void adicionarAresta() {
        if (grafo != null) {
            try {
                int origem = Integer.parseInt(origemField.getText()) - 1;
                int destino = Integer.parseInt(destinoField.getText()) - 1;
                int peso = grafo.ponderado ? Integer.parseInt(pesoField.getText()) : 1;
                grafo.adicionarAresta(origem, destino, peso);
                textArea.append("Aresta adicionada: " + origem + " -> " + destino + " (Peso: " + peso + ")\n");
            } catch (NumberFormatException e) {
                textArea.append("Dados da aresta inválidos.\n");
            }
        } else {
            textArea.append("Por favor, crie um grafo primeiro.\n");
        }
    }

    private void imprimirGrafo() {
        if (grafo != null) {
            textArea.setText("");
            textArea.append("Grafo criado:\n");
            textArea.append(grafo.getGrafoAsString());
        } else {
            textArea.append("Por favor, crie um grafo primeiro.\n");
        }
    }

    private void executarDijkstra() {
        if (grafo != null) {
            try {
                int inicio = Integer.parseInt(JOptionPane.showInputDialog("Digite o nó de início para Dijkstra (1 a " + grafo.numeroNos + "):")) - 1;
                int fim = Integer.parseInt(JOptionPane.showInputDialog("Digite o nó de fim para Dijkstra (1 a " + grafo.numeroNos + "):")) - 1;
                textArea.append("\nExecutando o Algoritmo de Dijkstra (RESPOSTA NO TERMINAL):\n");
                grafo.dijkstra(inicio, fim);
            } catch (NumberFormatException e) {
                textArea.append("Dados de entrada inválidos para Dijkstra.\n");
            }
        } else {
            textArea.append("Por favor, crie um grafo primeiro.\n");
        }
    }

    private void executarAEstrela() {
        if (grafo != null) {
            try {
                double[][] coordenadas = new double[grafo.numeroNos][2];
                for (int i = 0; i < grafo.numeroNos; i++) {
                    coordenadas[i][0] = Double.parseDouble(JOptionPane.showInputDialog("Digite a coordenada X do nó " + (i + 1) + ":"));
                    coordenadas[i][1] = Double.parseDouble(JOptionPane.showInputDialog("Digite a coordenada Y do nó " + (i + 1) + ":"));
                }
                int inicio = Integer.parseInt(JOptionPane.showInputDialog("Digite o nó de início para A* (1 a " + grafo.numeroNos + "):")) - 1;
                int fim = Integer.parseInt(JOptionPane.showInputDialog("Digite o nó de fim para A* (1 a " + grafo.numeroNos + "):")) - 1;
                textArea.append("\nExecutando o Algoritmo A* (RESPOSTA NO TERMINAL):\n");
                grafo.aEstrela(inicio, fim, coordenadas);
            } catch (NumberFormatException e) {
                textArea.append("Dados de entrada inválidos para A*.\n");
            }
        } else {
            textArea.append("Por favor, crie um grafo primeiro.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceGrafo().setVisible(true));
    }
}