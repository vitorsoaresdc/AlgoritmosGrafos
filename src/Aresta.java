class Aresta {
    int destino;
    int peso;
    Aresta proxima;

    Aresta(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
        this.proxima = null;
    }
}