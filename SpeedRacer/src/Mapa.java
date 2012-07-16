import java.awt.Color;
import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.apache.batik.ext.awt.geom.Polygon2D;


public class Mapa {
	private BufferedImage imagem, miniMapa;
	private Image icon;
	private String nome;
	private int periodo = 0;
	private static int iconsize = 450;
	private int[] inicios = new int[200];
	private Polygon2D polLargada;
	private Line2D frenteLargada, trazLargada;
	private double anguloInicial = 0;
	private int numInicios = 0;
	//private int width, height;
	private String[] tiposPeriodo = {"Dia", "Noite", "Terra"};
	
	public Mapa(String novoMapa, BufferedImage imagem, BufferedImage icon, BufferedImage mMapa){		
		for(int i=0; i<4; i++)
			inicios[i] = 0;
		nome = novoMapa;
		this.imagem = imagem;
		//width = imagem.getWidth();
		//height = imagem.getHeight();
		this.icon = icon;
		miniMapa = mMapa;
	}
	
	public Mapa(String novoMapa, BufferedImage imagem) {
		this(novoMapa, imagem, Controladora.toBufferedImage(imagem.getScaledInstance(iconsize, iconsize, 0)), Controladora.toBufferedImage(imagem.getScaledInstance(200, 200, 0)));
	}

	public Paint getFundo(){
		if(periodo == 1)
			return new Color(0,80,0);
		else if(periodo == 2)
			return new Color(190,150,30);
		else
			return new Color(10,120,10);
	}
	public BufferedImage getImagem(){
		return imagem;
	}
	public Image getIcon(){
		return icon;
	}
	public void setPeriodo(int i){
		periodo = (periodo + i + tiposPeriodo.length) % tiposPeriodo.length;
	}
	public String getPeriodo(){
		return tiposPeriodo[periodo];
	}
	public String getNome(){
		return nome;
	}

	public int getWidth() {
		return imagem.getWidth();
	}
	public int getHeight() {
		return imagem.getHeight();
	}
	public int getInicioX(int n){
		return inicios[2*n];
	}
	public int getInicioY(int n){
		return inicios[2*n + 1];
	}
	public int getNumInicios(){
		return numInicios;
	}

	public void setInicios(double angulo, int[] xy) {
		anguloInicial = angulo * Math.PI/180;
		for(numInicios=0; numInicios < xy.length/2; numInicios++){
			inicios[2*numInicios] = xy[2*numInicios];
			inicios[2*numInicios+1] = xy[2*numInicios+1];
		}
		Point2D[] largada = new Point2D[4];
	
		largada[0] = getBorda(anguloInicial - Math.PI/2, (int)(xy[0] + 3*Math.cos(anguloInicial)), (int)(xy[1] +  3*Math.sin(anguloInicial)));
		largada[1] = getBorda(anguloInicial + Math.PI/2, (int)(xy[0] + 3*Math.cos(anguloInicial)), (int)(xy[1] +  3*Math.sin(anguloInicial)));
		largada[2] = getBorda(anguloInicial + Math.PI/2, xy[0], xy[1]);
		largada[3] = getBorda(anguloInicial - Math.PI/2, xy[0], xy[1]);
		
		polLargada = new Polygon2D();
		for(int i=0; i<4; i++)
			polLargada.addPoint(largada[i]);
		frenteLargada = new Line2D.Double(largada[0], largada[1]);
		trazLargada = new Line2D.Double(largada[2], largada[3]);
	}
	private Point2D getBorda(double angscan, int x, int y){
		int i = x, j = y;
		double razao, cos = Math.cos(angscan), sin = Math.sin(angscan),
				idb = i, jdb = j;
		if(Math.abs(cos) > Math.abs(sin))
			razao = 1/Math.abs(cos);
		else
			razao = 1/Math.abs(sin);

		while(!estaNaBarreira(i, j)){
			idb += razao * cos;
			jdb += razao * sin;
			i = (int) idb;
			j = (int) jdb;
		}
		i = (int) idb;
		j = (int) jdb;
		return new Point2D.Double(i + 100*cos, j + 100*sin);
	}

	public double getAngulo(int numCarros) {
		return anguloInicial;
	}

	public boolean estaNaGrama(int posx, int posy) {
		int cor = imagem.getRGB(posx, posy);
		int transp = (cor & 0xff000000) >> 24;
		//int red = (cor & 0x00ff0000) >> 16;
		//int green = (cor & 0x0000ff00) >> 8;
		//int blue = (cor & 0x000000ff);
		if(transp == 0)
			return true;
		return false;
	}

	public boolean estaNaBarreira(int posx, int posy) {

		int cor = imagem.getRGB(posx, posy);
		//int transp = (cor & 0xff000000) >> 24;
		int red = (cor & 0x00ff0000) >> 16;
		int green = (cor & 0x0000ff00) >> 8;
		int blue = (cor & 0x000000ff);
		if(red == 80 && green == 0 && blue == 0)
			return true;
		return false;
	}
	public boolean estaNaLargada(int posx, int posy){
		return polLargada.contains(posx, posy);
	}
	public int direcaoPassouLargada(double x, double y, double antx, double anty){
		if(trazLargada.ptSegDist(x, y) > frenteLargada.ptSegDist(x, y))
			return 1;
		return 0;
	}

	public BufferedImage getMiniMapa() {
		return miniMapa;
	}

	public boolean estaNaPoca(int x, int y) {
		int cor = imagem.getRGB(x, y);
		//int transp = (cor & 0xff000000) >> 24;
		int red = (cor & 0x00ff0000) >> 16;
		int green = (cor & 0x0000ff00) >> 8;
		int blue = (cor & 0x000000ff);
		if(red == 50 && green == 60 && blue == 180)
			return true;
		return false;
	}
}
