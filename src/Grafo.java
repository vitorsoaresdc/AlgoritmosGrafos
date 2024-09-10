import java.util.*;

public class Grafo {
    static final int MAX_NODES = 100;
    No[] nos;
    boolean direcional;
    boolean ponderado;
    int numeroNos;
    int[] visitado;
    int[] descoberta;
    int[] finalizacao;
    int tempo;

    Grafo(boolean direcional, boolean ponderado, int numeroNos) {
        this.direcional = direcional;
        this.ponderado = ponderado;
        this.numeroNos = numeroNos;
        this.tempo = 0;
        this.nos = new No[numeroNos];
        this.visitado = new int[numeroNos];
        this.descoberta = new int[numeroNos];
        this.finalizacao = new int[numeroNos];

        for (int i = 0; i < numeroNos; i++) {
            this.nos[i] = new No((char) ('A' + i));
            this.visitado[i] = 0;
            this.descoberta[i] = 0;
            this.finalizacao[i] = 0;
        }
    }

    void adicionarAresta(int origem, int destino, int peso) {
        Aresta novaAresta = new Aresta(destino, peso);
        novaAresta.proxima = this.nos[origem].arestas;
        this.nos[origem].arestas = novaAresta;

        if (!this.direcional) {
            Aresta novaArestaReversa = new Aresta(origem, peso);
            novaArestaReversa.proxima = this.nos[destino].arestas;
            this.nos[destino].arestas = novaArestaReversa;
        }
    }

    void imprimirGrafo() {
        for (int i = 0; i < this.numeroNos; i++) {
            System.out.print(this.nos[i].nome + " ");
            Aresta aresta = this.nos[i].arestas;
            while (aresta != null) {
                if (this.ponderado) {
                    System.out.print("(" + this.nos[aresta.destino].nome + ": " + aresta.peso + ") ");
                } else {
                    System.out.print("(-> " + this.nos[aresta.destino].nome + ") ");
                }
                aresta = aresta.proxima;
            }
            System.out.println();
        }
    }

    void dfs(int no, char[] ordem, int[] indexOrdem) {
        this.visitado[no] = 1;
        this.tempo++;
        this.descoberta[no] = this.tempo;

        Aresta aresta = this.nos[no].arestas;
        while (aresta != null) {
            if (this.visitado[aresta.destino] == 0) {
                dfs(aresta.destino, ordem, indexOrdem);
            }
            aresta = aresta.proxima;
        }

        this.tempo++;
        this.finalizacao[no] = this.tempo;
        ordem[indexOrdem[0]--] = this.nos[no].nome;
    }

    void ordenacaoTopologica() {
        char[] ordem = new char[MAX_NODES];
        int[] indexOrdem = {this.numeroNos - 1};

        for (int i = 0; i < this.numeroNos; i++) {
            if (this.visitado[i] == 0) {
                dfs(i, ordem, indexOrdem);
            }
        }

        System.out.println("\nOrdenacao Topologica: ");
        for (int i = 0; i < this.numeroNos; i++) {
            System.out.print(ordem[i] + " ");
        }
        System.out.println();
    }

    void imprimirDescobertaFinalizacao() {
        System.out.println("\nOrdem de Descoberta/Finalizacao:");
        for (int i = 0; i < this.numeroNos; i++) {
            System.out.println(this.nos[i].nome + " (" + this.descoberta[i] + "/" + this.finalizacao[i] + ")");
        }
    }

    Grafo transporGrafo() {
        Grafo grafoT = new Grafo(this.direcional, this.ponderado, this.numeroNos);
        for (int i = 0; i < this.numeroNos; i++) {
            Aresta aresta = this.nos[i].arestas;
            while (aresta != null) {
                grafoT.adicionarAresta(aresta.destino, i, aresta.peso);
                aresta = aresta.proxima;
            }
        }
        return grafoT;
    }

    void encontrarComponentesFortementeConectados() {
        char[] ordem = new char[MAX_NODES];
        int[] indexOrdem = {this.numeroNos - 1};

        for (int i = 0; i < this.numeroNos; i++) {
            this.visitado[i] = 0;
        }

        for (int i = 0; i < this.numeroNos; i++) {
            if (this.visitado[i] == 0) {
                dfs(i, ordem, indexOrdem);
            }
        }
        Grafo grafoT = transporGrafo();

        System.out.println("\nComponentes Fortemente Conectados:");
        for (int i = 0; i < grafoT.numeroNos; i++) {
            grafoT.visitado[i] = 0;
        }

        for (int i = this.numeroNos - 1; i >= 0; i--) {
            int no = ordem[i] - 'A';
            if (grafoT.visitado[no] == 0) {
                System.out.print("{ ");
                int[] indexOrdemReverso = {grafoT.numeroNos - 1};
                dfsComponentes(grafoT, no);
                System.out.println("}");
            }
        }
    }

    void dfsComponentes(Grafo grafo, int no) {
        grafo.visitado[no] = 1;
        System.out.print(grafo.nos[no].nome + " ");

        Aresta aresta = grafo.nos[no].arestas;
        while (aresta != null) {
            if (grafo.visitado[aresta.destino] == 0) {
                dfsComponentes(grafo, aresta.destino);
            }
            aresta = aresta.proxima;
        }
    }

    void dijkstra(int inicio, int fim) {
        final int INF = Integer.MAX_VALUE;
        int[] distancias = new int[this.numeroNos];
        Arrays.fill(distancias, INF);
        Integer[] predecessores = new Integer[this.numeroNos];
        boolean[] visitado = new boolean[this.numeroNos];
        Arrays.fill(predecessores, null);

        distancias[inicio] = 0;
        PriorityQueue<Integer> fila = new PriorityQueue<>(Comparator.comparingInt(i -> distancias[i]));
        for (int i = 0; i < this.numeroNos; i++) {
            fila.add(i);
        }

        while (!fila.isEmpty()) {
            int u = fila.poll();
            if (u == fim) {
                break;
            }

            visitado[u] = true;
            Aresta aresta = this.nos[u].arestas;
            while (aresta != null) {
                int v = aresta.destino;
                if (!visitado[v]) {
                    int novaDistancia = distancias[u] + aresta.peso;
                    if (novaDistancia < distancias[v]) {
                        distancias[v] = novaDistancia;
                        predecessores[v] = u;
                        fila.add(v);
                    }
                }
                aresta = aresta.proxima;
            }

            imprimirTabelaControle(distancias, predecessores, u);
        }

        imprimirCaminhoMinimo(predecessores, inicio, fim);
    }

    void imprimirTabelaControle(int[] distancias, Integer[] predecessores, int u) {
        System.out.println("\nTabela após visitação do nó " + u + ":");
        System.out.print("u ");
        for (int i = 0; i < this.numeroNos; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.print("dist ");
        for (int i = 0; i < this.numeroNos; i++) {
            System.out.print((distancias[i] == Integer.MAX_VALUE ? "INF" : distancias[i]) + " ");
        }
        System.out.println();
        System.out.print("predecessor ");
        for (int i = 0; i < this.numeroNos; i++) {
            System.out.print((predecessores[i] == null ? "-" : predecessores[i]) + " ");
        }
        System.out.println();
    }

    void imprimirCaminhoMinimo(Integer[] predecessores, int inicio, int fim) {
        if (predecessores[fim] == null) {
            System.out.println("Não há caminho do nó " + inicio + " ao nó " + fim);
            return;
        }

        Stack<Integer> caminho = new Stack<>();
        for (Integer no = fim; no != null; no = predecessores[no]) {
            caminho.push(no);
        }

        System.out.print("Caminho mínimo de " + inicio + " a " + fim + ": ");
        while (!caminho.isEmpty()) {
            System.out.print(caminho.pop() + " ");
        }
        System.out.println();
    }

    void aEstrela(int inicio, int fim, double[][] coordenadas) {
        final int INF = Integer.MAX_VALUE;
        double[] distancias = new double[this.numeroNos];
        Arrays.fill(distancias, INF);
        double[] heuristicas = new double[this.numeroNos];
        Integer[] predecessores = new Integer[this.numeroNos];
        boolean[] visitado = new boolean[this.numeroNos];
        Arrays.fill(predecessores, null);

        PriorityQueue<Integer> fila = new PriorityQueue<>(Comparator.comparingDouble(i -> distancias[i] + heuristicas[i]));
        distancias[inicio] = 0;
        fila.add(inicio);

        for (int i = 0; i < this.numeroNos; i++) {
            heuristicas[i] = heuristica(coordenadas[i], coordenadas[fim]);
        }

        while (!fila.isEmpty()) {
            int u = fila.poll();
            if (u == fim) {
                break;
            }

            visitado[u] = true;
            Aresta aresta = this.nos[u].arestas;
            while (aresta != null) {
                int v = aresta.destino;
                if (!visitado[v]) {
                    double novaDistancia = distancias[u] + aresta.peso;
                    if (novaDistancia < distancias[v]) {
                        distancias[v] = novaDistancia;
                        predecessores[v] = u;
                        fila.add(v);
                    }
                }
                aresta = aresta.proxima;
            }
        }

        imprimirCaminhoMinimo(predecessores, inicio, fim);
    }

    double heuristica(double[] coordenadaA, double[] coordenadaB) {
        double dx = coordenadaA[0] - coordenadaB[0];
        double dy = coordenadaA[1] - coordenadaB[1];
        return Math.sqrt(dx * dx + dy * dy);
    }


    public String getGrafoAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.numeroNos; i++) {
            sb.append(this.nos[i].nome).append(" ");
            Aresta aresta = this.nos[i].arestas;
            while (aresta != null) {
                if (this.ponderado) {
                    sb.append("(").append(this.nos[aresta.destino].nome).append(": ").append(aresta.peso).append(") ");
                } else {
                    sb.append("(-> ").append(this.nos[aresta.destino].nome).append(") ");
                }
                aresta = aresta.proxima;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}