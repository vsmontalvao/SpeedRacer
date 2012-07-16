import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Controladora {
	public static Controladora instance = new Controladora();
	
	private Display display;
	private int width, height;
	private Mapa[] mapa = new Mapa[100];
	private int numMapas = 0;
	private ModeloCarro[] modelo = new ModeloCarro[100];
	private int numModelos = 0;
	private String tela;
	private Jogo jogo;
	private Menu menu;
	private Estatisticas stats;
	private BufferedImage logo;
	private boolean multiplayer = false;
	private boolean pause = false;
	private Jogador[] jogador = new Jogador[2];
	private JoystickListener joysticks[] = new JoystickListener[2];
	private int numJoysticks = 0;

	private double tempoLimite;

	private boolean possuiTempoLimite = false;

	private int limiteVoltas;

	private boolean possuiLimiteVoltas = false;

	private int controle;
	
	public Controladora(){
		width = 1366;
		height = 709;
		try{
			logo = ImageIO.read(new File("img/logo.png"));	
		}catch(Exception e){};
		
		setMenu();
		display = new Display(this);
	}
	public BufferedImage getLogo(){
		return logo;
	}
	public void setMenu(){
		stats = null;
		setSingleplayer();
		menu = new Menu(this);
		tela = "menu";
	}
	public void adicionarMapa(String novoMapa, String novaImagem){
		try {
			mapa[numMapas++] = new Mapa(novoMapa, ImageIO.read(new File(novaImagem)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void adicionarMapa(String novoMapa, String novaImagem, String icon, String mini){
		BufferedImage im = null, icone = null, mMapa = null;
		try {
			im = ImageIO.read(new File(novaImagem));
			icone = ImageIO.read(new File(icon));
			mMapa = ImageIO.read(new File(mini));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		mapa[numMapas++] = new Mapa(novoMapa, im, icone, mMapa);
	}
	public void adicionarCarro(String novoModelo, String novaCor, String novaImagem){
		boolean modeloExiste = false;
		int i;
		for(i=0; i<numModelos; i++)
			if(modelo[i].getModelo().equalsIgnoreCase(novoModelo)){
				modeloExiste = true;
				break;
			}
		if(!modeloExiste){
			modelo[numModelos] = new ModeloCarro(novoModelo);
			i = numModelos++;
		}
		try {
			modelo[i].addCor(novaCor, ImageIO.read(new File(novaImagem)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed(int keyCode) {
		Integer iJogador = null;
		Tecla keyType = jogador[0].tipoTecla(keyCode);
		if(keyType != null)
			iJogador = 0;
		else if(jogador[1] != null){
			keyType = jogador[1].tipoTecla(keyCode);
			iJogador = 1;
		}		
		
		if(keyType == null){
			if(tela.equalsIgnoreCase("menu"))
					switch(keyCode){
						case KeyEvent.VK_HOME:
							menu.moverHome();
							break;
						case KeyEvent.VK_PAGE_UP:
							menu.moverCima();
							break;
						case KeyEvent.VK_END:
							menu.moverEnd();
							break;
						case KeyEvent.VK_PAGE_DOWN:
							menu.moverBaixo();
							break;
					}
			else if(tela.equalsIgnoreCase("jogo"))
				switch(keyCode){
					case KeyEvent.VK_1:
						if(isMultiplayer())
							display.setFoco(0);
						break;
					case KeyEvent.VK_2:
						if(isMultiplayer())
							display.setFoco(1);
						break;
				}
		}
		else
			acaoPressionou(keyType, iJogador);
	}
	
	public void acaoPressionou(Tecla keyType, Integer iJogador) {
		if(tela.equalsIgnoreCase("menu")){
			controle = iJogador+1;
			switch(keyType){
				case ACELERA:
					menu.moverCima();
					break;
				case FREA:
					menu.moverBaixo();
					break;
				case ESQUERDA:
					menu.elementoEsquerda();
					break;
				case DIREITA:
					menu.elementoDireita();
					break;
				case CONTINUAR:
					menu.menuSeguinte();
					break;
				case VOLTAR:
					menu.voltar();
					break;
			default:
				break;
			}
		}
		else if(tela.equalsIgnoreCase("jogo"))
			switch(keyType){
				case SAIR:
					jogo.getTocador().terminarMusica();
					setMenu();
					break;
				case PAUSE:
					if(jogo.estaPausado())
						jogo.continuar();
					else
						jogo.pausar();
					break;
				case ACELERA:
					jogo.Acelerar(iJogador);
					break;
				case FREA:
					jogo.Frear(iJogador);
					break;
				case ESQUERDA:
					jogo.Esquerda(iJogador);
					break;
				case DIREITA:
					jogo.Direita(iJogador);
					break;
				case VISAO:
					if(isMultiplayer())
						display.mudaModoVisual();
					break;
				case BRECAR:
					jogo.Brecar(iJogador);
					break;
				case BUZINA:
					jogo.tocarBuzina();
					break;
				case MUSICA:
					jogo.passarMusica();
					break;
				case NITRO:
					jogo.Nitro(iJogador);
					break;
			default:
				break;
			}
		else if(tela.equalsIgnoreCase("estatisticas"))
			switch(keyType){
			case ESQUERDA:
				stats.elementoEsquerda();
				break;
			case DIREITA:
				stats.elementoDireita();
				break;
			case CONTINUAR:
				stats.menuSeguinte();
				break;
			default:
				break;
		}
	}
	public void keyReleased(int keyCode) {
		Integer iJogador = null;
		Tecla keyType = jogador[0].tipoTecla(keyCode);
		if(keyType != null)
			iJogador = 0;
		else if(jogador[1] != null){
			keyType = jogador[1].tipoTecla(keyCode);
			iJogador = 1;
		}
		if(keyType != null && tela.equalsIgnoreCase("jogo"))
			acaoSoltou(keyType, iJogador);
	}

	public void acaoSoltou(Tecla keyType, Integer iJogador) {
		if(tela.equalsIgnoreCase("jogo"))
			switch(keyType){
			case ACELERA:
				jogo.soltouCima(iJogador);
				break;
			case FREA:
				jogo.soltouBaixo(iJogador);
				break;
			case ESQUERDA:
				jogo.soltouEsquerda(iJogador);
				break;
			case DIREITA:
				jogo.soltouDireita(iJogador);
				break;
			case BRECAR:
				jogo.soltouBrecar(iJogador);
				break;
			case NITRO:
				jogo.soltouNitro(iJogador);
				break;
			default:
				break;
			}
	}
	public Dimension getDimension() {
		return new Dimension(width, height);
	}
 
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public Display getDisplay() {
		return display;
	}

	public String nomeMapa(int numMapa) {
		return mapa[numMapa].getNome();
	}

	public String periodoMapa(int numMapa) {
		return mapa[numMapa].getPeriodo();
	}

	public String modeloCarro(int numCarro) {
		return modelo[numCarro].getModelo();
	}

	public String getCorCarro(int numCarro, int numCor) {
		return modelo[numCarro].getCor(numCor);
	}

	public int getNumMapas() {
		return numMapas;
	}

	public int getNumCarros() {
		return numModelos;
	}

	public void setPeriodoMapa(int numMapa, int i) {
		mapa[numMapa].setPeriodo(i);
	}
	public String getTela(){
		return tela;
	}

	public void jogar() {
		menu.getTocador().terminarMusica();
		jogo = new Jogo(this, mapa[menu.getNumMapa()]);
		jogo.adicionarCarro(modelo[menu.getNumCarro()].getNewInstance(jogo, menu.getNumCor(), 0));
		if(isMultiplayer())
			jogo.adicionarCarro(modelo[menu.getNumCarro2()].getNewInstance(jogo, menu.getNumCor2(), 1));
		if(possuiTempoLimite)
			jogo.setTempoLimite(tempoLimite);
		if(possuiLimiteVoltas)
			jogo.setLimiteVoltas(limiteVoltas);
		display.setJogo(jogo);
		tela = "jogo";
		jogo.start();
	}
	public Jogo getJogo(){
		return jogo;
	}
	public Menu getMenu() {
		return menu;
	}
	public Image getIconMapa(int numMapa) {
		return mapa[numMapa].getIcon();
	}
	public Image getImagemCarro(int numCarro, int numCor) {
		return modelo[numCarro].getIcon(numCor);
	}
	public void setSingleplayer() {
		jogador[0] = new Jogador(new Controles(0, 1), 1);
		jogador[1] = null;
		multiplayer = false;
	}
	public void setMultiplayer() {
		jogador[0] = new Jogador(new Controles(0, 2), 1);
		jogador[1] = new Jogador(new Controles(1, 2), 2);
		multiplayer = true;
	}
	public boolean isMultiplayer() {
		return multiplayer;
	}
	public boolean isPaused(){
		return pause;
	}
	public void setCorLado(int lado) {
		
	}
	public int getNumCores(int numCarro) {
		return modelo[numCarro].getNumCores();
	}
	
	// Método copiado da internet para converter Image para BufferedImage
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Determine if the image has transparent pixels; for this method's
	    // implementation, see Determining If an Image Has Transparent Pixels
	    boolean hasAlpha = hasAlpha(image);

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;
	        if (hasAlpha) {
	            transparency = Transparency.BITMASK;
	        }

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        if (hasAlpha) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}
	public static boolean hasAlpha(Image image) {
	    // If buffered image, the color model is readily available
	    if (image instanceof BufferedImage) {
	        BufferedImage bimage = (BufferedImage)image;
	        return bimage.getColorModel().hasAlpha();
	    }

	    // Use a pixel grabber to retrieve the image's color model;
	    // grabbing a single pixel is usually sufficient
	     PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
	    try {
	        pg.grabPixels();
	    } catch (InterruptedException e) {
	    }

	    // Get the image's color model
	    ColorModel cm = pg.getColorModel();
	    return cm.hasAlpha();
	}
	public Paint getFundoMapa(int numMapa) {
		return mapa[numMapa].getFundo();
	}
	public void setMapaInicios(String string, int[] xy) {
		setMapaInicios(string, 0, xy);
	}
	public void setMapaInicios(String string, double angulo, int[] xy) {
		int i;
		for(i=0; i<numMapas; i++)
			if(mapa[i].getNome().equalsIgnoreCase(string))
				mapa[i].setInicios(angulo, xy);
	}
	public boolean isModoJogo(){
		return tela.equalsIgnoreCase("jogo");
	}
	public void addJoystick(JoystickListener joystickListener) {
		joysticks[numJoysticks] = joystickListener;
		joysticks[numJoysticks++].start();
		//System.out.println(numJoysticks +" - Numero:"+ joystickListener.getNumber());
		//jogador
	}
	public void jogoTerminou() {
		if(tela.equalsIgnoreCase("jogo")){
			jogo.getTocador().terminarMusica();
			stats = new Estatisticas(this, jogo);
			tela = "estatisticas";
		}
	}
	public Estatisticas getEstatisticas() {
		return stats;
	}
	public String getNome(int i) {
		return jogador[i].getNome();
	}
	public void setTempoLimite(int i) {
		possuiTempoLimite = true;
		tempoLimite = i;
	}
	public void setLimiteVoltas(int i) {
		possuiLimiteVoltas  = true;
		limiteVoltas = i;
	}
	public String getElementoTempo() {
		if(!possuiTempoLimite)
			return "Tempo Ilimitado";
		else if(tempoLimite >= 60)
			return (int) (tempoLimite/60) + " min";
		else
			return (int) (tempoLimite) + " s";
	}
	public String getElementoVoltas() {
		if(!possuiLimiteVoltas)
			return "Voltas Ilimitadas";
		else
			return limiteVoltas + " Voltas";
	}
	public void setTempo(int lado) {
		if(lado > 0 && !possuiTempoLimite)
			possuiTempoLimite = true;
		if(possuiTempoLimite || lado > 0)
			tempoLimite += lado * 5 * 60;
		if(tempoLimite <= 0){
			tempoLimite = 0;
			possuiTempoLimite = false;
		}
	}
	public void setVoltas(int lado) {
		if(lado > 0 && !possuiLimiteVoltas)
			possuiLimiteVoltas = true;
		if(possuiLimiteVoltas || lado > 0)
			limiteVoltas += lado * 10;
		if(limiteVoltas <= 0){
			limiteVoltas = 0;
			possuiLimiteVoltas = false;
		}
	}
	public int getControle(){
		return controle;
	}
	public void setWidth(int w){
		width = w;
	}
	public void setHeight(int h){
		height = h;
	}
	
}
