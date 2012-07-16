import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Estatisticas {
	TocadorMusica tocador = new TocadorMusica("sound/ificouldfly.mp3");
	private Controladora c;
	private Jogo jogo;
	private BufferedImage fundo;
	private String titulo = "Estatísticas";
	private String[] elemento = new String[20];
	private String[] prox = new String[20];
	private int numElementos = 0;
	private int localAtual = 6;
	private int numElementosPlayer;
	
	public Estatisticas(Controladora c, Jogo jogo) {
		this.c = c;
		this.jogo = jogo;
		try {
			fundo = ImageIO.read(new File("img/fundo_menu.png")); // AQUI
	    } catch (IOException e) {}
		setEstatisticas();
		tocador.start();
	}
	private void adicionarElemento(String texto, String seguinte){
		prox[numElementos] = seguinte;
		elemento[numElementos++] = texto;
	}
	private void setEstatisticas() {
		setPlayer(0);
		if(jogo.isMultiplayer())
			setPlayer(1);
		adicionarElemento("Sair", null);
		adicionarElemento("Novo Jogo", null);
	}
	private void setPlayer(int i) {
		if(i == 1)
			localAtual = 11;
		numElementosPlayer = 5;
		adicionarElemento("Jogador " + jogo.getNome(i), null);
		adicionarElemento("Tempo Total: " + Double.toString(((double)(jogo.getTempo()))/1000f), null);
		adicionarElemento("Tempo Médio: " + jogo.getTempoMedioCarro(i), null);
		adicionarElemento("Melhor Tempo: " + jogo.getTempoMelhorVoltaCarro(i), null);
		adicionarElemento("Número de Voltas: " + (jogo.getVoltaCarro(i) - 1), null);
	}
	public int getNumElementosPlayer(){
		return numElementosPlayer;
	}
	public boolean isMultiplayer(){
		return jogo.isMultiplayer();
	}
	public BufferedImage getFundo(){
		return fundo;
	}

	public int getNumElementos() {
		return numElementos ;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getElemento(int i) {
		return elemento[i];
	}
	public int getLocalAtual() {
		return localAtual;
	}
	public void elementoEsquerda() {
		if(localAtual == 6)
			localAtual = 5;
		else if(localAtual == 11)
			localAtual = 10;
	}
	public void elementoDireita() {
		if(localAtual == 5)
			localAtual = 6;
		else if(localAtual == 10)
			localAtual = 11;
	}
	public void menuSeguinte() {
		if(elemento[localAtual].equalsIgnoreCase("Sair"))
			System.exit(0);
		else{
			tocador.terminarMusica();
			c.setMenu();
		}
	}
}