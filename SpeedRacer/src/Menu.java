import java.awt.Image;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Menu {
	private Controladora c;
	private String[] elemento = new String[20];
	private String[] prox = new String[20];
	private String[] seq = new String[20];
	private int numElementos = 0, numSeq = 0;
	private int localAtual;
	private String titulo;
	private BufferedImage fundo;
	int numMapa, numCarro, numCarro1 = 0, numCarro2 = 0, numCor, numCor1 = 0, numCor2 = 0;
	boolean menuCarro2 = false;
	
	TocadorMusica tocador = new TocadorMusica("sound/jungle.mp3");
	
	public Menu(Controladora c){
		this.c = c;
		try {
//	        fundo = ImageIO.read(new File("img/fundo1366x900.png"));
			fundo = ImageIO.read(new File("img/fundo_menu.png")); // AQUI
//	        fundo = ImageIO.read(new File("img/2011-lexus-lfa-rain-race-HD_wallpapers.png")); // AQUI
	    } catch (IOException e) {}
		setMenuInicio();
		
		tocador.start();
	}
	
	private void setMenuInicio(){
		numSeq = 0;
		localAtual = 0;
		c.setSingleplayer();
		exibeMenuInicio();
	}
	private void exibeMenuInicio(){
		seq[numSeq] = "inicio"; 
		titulo = "Menu Principal";
		numElementos = 4;
		elemento[0] = "Single Player";
		prox[0] = "mapa";
		elemento[1] = "MultiPlayer";
		prox[1] = "mapaMulti";
		elemento[2] = "Configurações";
		prox[2] = "configuracoes";
		elemento[3] = "Sair do Jogo";
		prox[3] = "sair";
	}
	private void setMenuConfiguracoes(){
		localAtual = 0;
		exibeMenuConfiguracoes();
	}
	private void exibeMenuConfiguracoes(){
		seq[numSeq] = "configuracoes";
		titulo = "Configurações";
		numElementos = 3;
		elemento[0] = "Controles";
		prox[0] = "teclas";
		elemento[1] = "Opções de Som";
		prox[1] = "som";
		elemento[2] = "Voltar";
		prox[2] = "voltar";
	}

	private void setMenuMapa() {
		numMapa = 0;
		localAtual = 0;
		exibeMenuMapa();
	}
	private void exibeMenuMapa(){
		seq[numSeq] = "mapa";
		titulo = "Mapa";
		numElementos = 6;
		
		if(c.getNumMapas() == 0){
			numElementos = 2;
			elemento[0] = "Voltar";
			prox[0] = "voltar";
			elemento[1] = "Sem Mapas";
			prox[1] = null;

		}
		else{
			elemento[0] = "Próximo";
			prox[0] = "carro";
			elemento[1] = "<< " + c.nomeMapa(numMapa) + " >>";
			prox[1] = "ladoNome";
			elemento[2] = "<< " + c.periodoMapa(numMapa) + " >>";
			prox[2] = "ladoPeriodo";
			elemento[3] = "<< " + c.getElementoTempo() + " >>";
			prox[3] = "tempo";
			elemento[4] = "<< " + c.getElementoVoltas() + " >>";
			prox[4] = "voltas";
			elemento[5] = "Voltar";
			prox[5] = "voltar";
		}
	}

	private void setMenuCarro() {
		localAtual = 0;
		seq[numSeq] = "carro";	
		if(menuCarro2){
			numCarro = numCarro2;
			numCor = numCor2;
		}
		else{
			numCarro = numCarro1;
			numCor = numCor1;
		}
		exibeMenuCarro();
	}
	private void exibeMenuCarro(){		
		if(c.isMultiplayer()){
			if (menuCarro2)
				titulo = "Carro do Jogador 2";
			else
				titulo = "Carro do Jogador 1";
		}
		else
			titulo = "Carro";
		if(c.getNumCarros() == 0){
			numElementos = 2;
			elemento[0] = "Voltar";
			prox[0] = "voltar";
			elemento[1] = "Sem Carros";
			prox[1] = null;

		}
		else{
			numElementos = 4;
			if (menuCarro2 || !c.isMultiplayer()){
				elemento[0] = "Iniciar Jogo";
				prox[0] = "jogo"; // Chamar o Jogo
			}
			else{
				elemento[0] = "Próximo";
				prox[0] = "carro"; // Chamar o Jogo
			}
			elemento[1] = "<< " + c.modeloCarro(numCarro) + " >>";
			prox[1] = "ladoModelo";
			elemento[2] = "<< " + c.getCorCarro(numCarro, numCor) + " >>";
			prox[2] = "ladoCor";
			elemento[3] = "Voltar";
			prox[3] = "voltar";
		}	
	}
	private void setMenuTeclas() {
		localAtual = 0;
		exibeMenuTeclas();
	}
	private void exibeMenuTeclas(){
		seq[numSeq] = "teclas";
		titulo = "Controles";
		numElementos = 0;
		Controles c1 = new Controles(0, 1);
		Controles c2 = new Controles(0, 2);
		Controles c3 = new Controles(1, 2);
		prox[numElementos] = null;
		elemento[numElementos++] = "Acelerar:      R1                 "+c1.getTecla(Tecla.ACELERA)+
				"                                             "
				+c2.getTecla(Tecla.ACELERA)+"                                 "+c3.getTecla(Tecla.ACELERA);
		prox[numElementos] = null;
		elemento[numElementos++] = "Ré:               R2                 "+c1.getTecla(Tecla.FREA)+
				"                                              "
				+c2.getTecla(Tecla.FREA)+"                                 "+c3.getTecla(Tecla.FREA);
		prox[numElementos] = null;
		elemento[numElementos++] = "Direita:         >                  "+c1.getTecla(Tecla.DIREITA)+
				"                                             "
				+c2.getTecla(Tecla.DIREITA)+"                                 "+c3.getTecla(Tecla.DIREITA);
		prox[numElementos] = null;
		elemento[numElementos++] = "Esquerda:      <                 "+c1.getTecla(Tecla.ESQUERDA)+
				"                                          "
				+c2.getTecla(Tecla.ESQUERDA)+"                                 "+c3.getTecla(Tecla.ESQUERDA);
		prox[numElementos] = null;
		elemento[numElementos++] = "Breque:          3                     "+c1.getTecla(Tecla.BRECAR)+
				"                                                  "
				+c2.getTecla(Tecla.BRECAR)+"                                 "+c3.getTecla(Tecla.BRECAR);
		prox[numElementos] = null;
		elemento[numElementos++] = "Nitro:       L1 ou L2                "+c1.getTecla(Tecla.NITRO)+
				"                                             "
				+c2.getTecla(Tecla.NITRO)+"                                 "+c3.getTecla(Tecla.NITRO);
		prox[numElementos] = null;
		elemento[numElementos++] = "Buzina:         4                        "+c1.getTecla(Tecla.BUZINA)+
				"                                                 "
				+c2.getTecla(Tecla.BUZINA)+"                                    "+c3.getTecla(Tecla.BUZINA);
		prox[numElementos] = null;
		elemento[numElementos++] = "Música:         ^                       "+c1.getTecla(Tecla.MUSICA)+
				"                                                 "
				+c2.getTecla(Tecla.MUSICA)+"                                    "+c3.getTecla(Tecla.MUSICA);
		
		prox[numElementos] = "voltar";
		elemento[numElementos++] = "Voltar";
	}
	private void setMenuSom() {
		localAtual = 0;
		exibeMenuSom();
	}
	private void exibeMenuSom(){
		seq[numSeq] = "som";
		titulo = "Opções de Som";
		numElementos = 2;
		elemento[0] = "Sem opções por enquanto"; //fazer dps
		prox[0] = null;
		elemento[1] = "Voltar";
		prox[1] = "voltar";		
	}
	public void elementoLateral(int lado){
		if(prox[localAtual] != null){
			if(prox[localAtual].equalsIgnoreCase("ladoNome")){
				numMapa = (numMapa + lado + c.getNumMapas())%c.getNumMapas();
				exibeMenuMapa();
			}
			else if(prox[localAtual].equalsIgnoreCase("ladoPeriodo")){
				c.setPeriodoMapa(numMapa, lado);
				exibeMenuMapa();
			}
			else if(prox[localAtual].equalsIgnoreCase("ladoModelo")){
				numCarro = (numCarro + lado + c.getNumCarros()) % c.getNumCarros();
				numCor = 0;
				exibeMenuCarro();
			}
			else if(prox[localAtual].equalsIgnoreCase("ladoCor")){
				numCor = (numCor + lado + c.getNumCores(numCarro)) % c.getNumCores(numCarro);
				exibeMenuCarro();
			}
			else if(prox[localAtual].equalsIgnoreCase("tempo")){
				//numCor = (numCor + lado + c.getNumCores(numCarro)) % c.getNumCores(numCarro);
				c.setTempo(lado);
				exibeMenuMapa();
			}
			else if(prox[localAtual].equalsIgnoreCase("voltas")){
				//numCor = (numCor + lado + c.getNumCores(numCarro)) % c.getNumCores(numCarro);
				c.setVoltas(lado);
				exibeMenuMapa();
			}
		}
	}
	public void elementoDireita(){
		elementoLateral(1);
	}
	public void elementoEsquerda(){
		elementoLateral(-1);
	}
	public void menuSeguinte(){
		if (prox[localAtual] == null){}
		else if(prox[localAtual].equalsIgnoreCase("voltar")){
				voltar();
		}
		else if(prox[localAtual].equalsIgnoreCase("tempo")||prox[localAtual].equalsIgnoreCase("voltas"))
		{}
		else if (!prox[localAtual].contains("lado")) {
			++numSeq;
			if(prox[localAtual].equalsIgnoreCase("mapa")){
				c.setSingleplayer();
				setMenuMapa();
			}
			else if(prox[localAtual].equalsIgnoreCase("mapaMulti")){
				c.setMultiplayer();
				setMenuMapa();
			}
			else if(prox[localAtual].equalsIgnoreCase("carro")){
				if(seq[numSeq-1].equalsIgnoreCase("carro")){
					numCarro1 = numCarro;
					numCor1 = numCor;
					menuCarro2 = true;
				}
				else
					menuCarro2 = false;
				setMenuCarro();				
			}
			else if(prox[localAtual].equalsIgnoreCase("configuracoes")){
				setMenuConfiguracoes();
			}
			else if(prox[localAtual].equalsIgnoreCase("teclas")){
				setMenuTeclas();
			}
			else if(prox[localAtual].equalsIgnoreCase("som")){
				setMenuSom();
			}
			else if(prox[localAtual].equalsIgnoreCase("sair")){
				System.exit(0);
				//tocador.terminarMusica();
			}
			else if(prox[localAtual].equalsIgnoreCase("jogo")){
				if(menuCarro2){
					numCarro2 = numCarro;
					numCor2 = numCor;
				}
				else{
					numCarro1 = numCarro;
					numCor1 = numCor;
				}
				c.jogar();
			}
			else{}
		}
	}

	protected BufferedImage getFundo(){
		return fundo;
	}
	protected int getNumMapa(){
		return numMapa;
	}
	protected int getNumCarro(){
		return numCarro1;
	}
	protected int getNumCarro2(){
		return numCarro2;
	}
	protected String getTitulo(){
		return titulo;
	}
	protected int getNumElementos() {
		return numElementos;
	}

	public int getLocalAtual() {
		return localAtual;
	}

	protected String getElemento(int i) {
		return elemento[i];
	}

	public void moverCima() {
		if(localAtual > 0)
			localAtual--;
	}

	public void moverBaixo() {
		if(localAtual < numElementos -1)
			localAtual++;
	}

	public void voltar() {
		if(!seq[numSeq].equalsIgnoreCase("inicio")){
			numSeq--;
			if(seq[numSeq].equalsIgnoreCase("inicio"))
				setMenuInicio();
			else if(seq[numSeq].equalsIgnoreCase("configuracoes"))
				setMenuConfiguracoes();
			else if(seq[numSeq].equalsIgnoreCase("mapa"))
				setMenuMapa();
			if(seq[numSeq].equalsIgnoreCase("carro")){
				menuCarro2 = false;
				numCor = numCor1;
				exibeMenuCarro();
			}
		}
	}

	public Image getImagem() {
		//
		if(seq[numSeq] == null)
			System.out.println("NULL: "+numSeq);
		else
		//
		if(seq[numSeq].equalsIgnoreCase("mapa"))
			return c.getIconMapa(numMapa);
		else if(seq[numSeq].equalsIgnoreCase("carro"))
			return c.getImagemCarro(numCarro, numCor);
		return null;
	}
	public TocadorMusica getTocador(){
		return tocador;
	}
	
	public int getNumCor() {
		return numCor1;
	}
	public int getNumCor2() {
		return numCor2;
	}

	public Paint getFundoIcon() {
		return c.getFundoMapa(numMapa);
	}

	public void moverHome() {
		localAtual = 0;
	}

	public void moverEnd() {
		localAtual = numElementos -1;
	}
}
