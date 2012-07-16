import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import org.apache.batik.ext.awt.geom.Polygon2D;


public class Carro  implements Runnable {
	BufferedImage imagemoriginal, imagem;
	String modelo;
	String cor;
	double aceleracao, velx = 0, vely = 0, posx = 0, posy = 0;
	double stamina;
	double health;
	static int width = 50, height = 80;
	Thread thread;
	boolean direita = false, esquerda = false, cima = false, baixo = false, breque = false, nitro = false;
	Jogo jogo;
	private boolean pausado;
	private double vmax;
	private double vel;
	private double direcao = 0;
	private int numVolta = 0;
	private double antx;
	private double anty;
	private long[] tempoVolta = new long[100];
	private int numTempoVoltas = 0;
	private double melhorVolta = 0;
	private Polygon2D polCarro;
	Point2D[] bordas = new Point2D[4];
	//private int num; // Utilizar para identificar colisão entre carros
	private boolean terminou = false;
	private long tempoMedio = 0;
	private int numbatidas = 0;
	
	public Carro(Jogo j, String m, String c, double a, double vmax, double s, double h, BufferedImage i, int num){
		//this.num = num;
		jogo = j;
		modelo = m;
		cor = c;
		aceleracao = a;
		this.vmax = vmax;
		stamina = s;
		health = h;
		imagemoriginal = i;
		imagem =  Controladora.toBufferedImage(i.getScaledInstance(width, height, 0));
	}
	public void start(){
		thread = new Thread(this);
        thread.start();
	}
	private void novoTempoVolta(){
		if(numVolta == numTempoVoltas + 1){
			tempoVolta[numTempoVoltas] = jogo.getTempo();
			for(int i=0; i<numTempoVoltas; i++){
				if(tempoVolta[numTempoVoltas] - tempoVolta[numTempoVoltas-1] < melhorVolta){
					melhorVolta = tempoVolta[numTempoVoltas] - tempoVolta[numTempoVoltas-1];
					stamina += 50;
				}
			}
			if(numTempoVoltas == 1)
				melhorVolta = tempoVolta[1] - tempoVolta[0];
			if(numTempoVoltas > 0)
				tempoMedio = tempoVolta[numTempoVoltas]/numTempoVoltas;
			numTempoVoltas++;
			stamina += 10;
			if(stamina > 100)
				stamina = 100;
		}
		jogo.verificarTermino();
	}
	public void setCima(boolean b){
		cima = b;
	}
	public void setBreque(boolean b) {			
		 breque = b;								 
	}
	public void setBaixo(boolean b){
		baixo = b;
	}
	public void setEsquerda(boolean b){
		esquerda = b;
	}
	public void setDireita(boolean b){
		direita = b;
	}
	
	public Image getImagem(){
		return imagem;
	}
	public Image getIcon(){
		return imagem.getScaledInstance(380, 380, 0);
	}
	public String getModelo(){
		return modelo;
	}
	public String getCor(){
		return cor;
	}
	public double getAceleracao(){
		return aceleracao;
	}
	public double getStamina(){
		return stamina;
	}
	public double getHealth(){
		return health;
	}

	public static int getCarroWidth() {
		return width;
	}
	public static int getCarroHeight() {
		return height;
	}
	public int getX() {
		return (int) posx;
	}
	public int getY() {
		return (int) posy;
	}

	@Override
	public void run() {
		long t1, t2;
		
		Thread me = Thread.currentThread();		
		Mapa m = jogo.getMapa();
		boolean estaNaLargada = m.estaNaLargada((int) posx, (int) posy);
	    while (thread == me) {
	    	t1 = Calendar.getInstance().getTimeInMillis();
	        try {
	            Thread.sleep(1);
	        } catch (InterruptedException e) {break;}
	        t2 = Calendar.getInstance().getTimeInMillis();
	        
	    	if(!pausado && Controladora.instance.isModoJogo() && !jogo.estaRegressivo()){
		        if(!jogo.estaNaPoca(posx, posy)){
		        	atualizarDirecao(t2, t1);
			        atualizarVelocidade(t2, t1);
		        }
	        	
	        	int numGrama = 0, numBarreira = 0;
	        	
	        	Point2D[] bordas = calcularBordas(posx, posy, direcao);
	        	
	    		for(int i=0; i<4; i++){
	    			if(jogo.estaNaGrama(bordas[i].getX(), bordas[i].getY()))
	    				numGrama++;
	    			if(jogo.estaNaBarreira(bordas[i].getX(), bordas[i].getY()))
	    				numBarreira++;
	    			//System.out.println("Carro"+num+": "+bordas[i].getX()+" , "+ bordas[i].getY());
	    			//System.out.println(jogo.getNumCarros());
	    		}
	    		//System.out.println();
	    		
	    		double vmaxlocal = vmax * (1 - numGrama * 0.15);
	    		
	    		if(nitro && stamina > 0){
	    			stamina -= (double)(t2-t1) * 20f/1000f;
	    			if(stamina < 0)
	    				stamina = 0;
	    			vmaxlocal *= 1.5;
	    		}
	    		
	    		
	    		polCarro = new Polygon2D();
	    		for(int i=0; i<4; i++)
	    			polCarro.addPoint(bordas[i]);
	        	
	        	if (numBarreira != 0){
	        		
	        		
	        		int nbordas = 1, cont = 0;
	        		double razao, cos = Math.cos(direcao), sin = Math.sin(direcao);
	        		if(Math.abs(cos) > Math.abs(sin))
	        			razao = 1/Math.abs(cos);
	        		else
	        			razao = 1/Math.abs(sin);
	        		
	        		while(nbordas > 0 && cont<50){
		        		nbordas = 0;
		        		Point2D[] baux = calcularBordas(posx, posy, direcao);
		        		
		        		cos = Math.cos(direcao); sin = Math.sin(direcao);
		        		for(int i=0; i<4; i++)
			    			if(jogo.estaNaBarreira(baux[i].getX(), baux[i].getY()))
			    				nbordas++;
		        		if(numBarreira > 0){
		        			posx -= razao * cos * vel/Math.abs(vel);
		        			posy -= razao * sin * vel/Math.abs(vel);
		        		}
		        		if(cont%5 == 2){
		        			int sinal = 1;
		        			if(esquerda)
		        				sinal = -1;
		        			double diraux = direcao; int nbordasaux = 1;
		        			int contaux = 0;
		        			while(nbordasaux >0 && contaux < 10){
		        				nbordasaux = 0;
		        				bordas = calcularBordas(posx, posy, diraux);
			        			for(int i=0; i<4; i++)
					    			if(jogo.estaNaBarreira(bordas[i].getX(), bordas[i].getY()))
					    				nbordasaux++;
			        			if(nbordasaux > 0)
			        				diraux += sinal * Math.toRadians(20);
			        			contaux++;
		        			}
		        			direcao -= Math.toRadians(10);
		        			posx += 4*razao * cos * vel/Math.abs(vel);
		        			posy += 4*razao * sin * vel/Math.abs(vel);
		        		}
		        		cont++;
	        		}
	        		nbordas = 0;
	        		for(int i=0; i<4; i++)
		    			if(jogo.estaNaBarreira(posx, posy))
		    				nbordas++;
	        		if(nbordas == 0 && vel != 0)
	        			vel *= -0.6;
	        		else
	        			vel *= -1;
	        		numbatidas++;
	        		
	        	}

	        	//detectarColisao();
	        	
	        	velx = vel*Math.cos(direcao);	    
	        	vely = vel*Math.sin(direcao);
	        	if(Math.abs(vel) > vmaxlocal){
	        		velx *= vmaxlocal/Math.abs(vel);
	        		vely *= vmaxlocal/Math.abs(vel);
	        		vel = (vel/Math.abs(vel))*vmaxlocal;
	        	}
	        	antx = posx;
	        	anty = posy;
	        	
	        	posx += velx * (t2-t1)/1000;
	    		posy += vely * (t2-t1)/1000;
	    		
		        if(!m.estaNaLargada((int) (posx), (int) (posy)) && estaNaLargada){
		        	numVolta += m.direcaoPassouLargada(posx, posy, antx, anty);
		        	novoTempoVolta();
		        }
		        if(m.estaNaLargada((int) (posx), (int) (posy)) && !estaNaLargada){
		        	numVolta -= m.direcaoPassouLargada(posx, posy, antx, anty);
		        }
		        estaNaLargada = m.estaNaLargada((int) (posx), (int) (posy));
	    		// TODO: Atualizar velocidade máxima com o tempo em que ele está nesse estado
	    		
		        double red = 200, raz = 0.8;
		        
		        if(posx < 0 || posx > jogo.getWidth() - imagem.getWidth()){
		        	posx -= velx * (t2-t1)/1000;
		        	velx = 0;
		        	if(vely > red || vely < -red)
		        		vely -= raz*(vely - red) ;
		        }
		        if(posy < 0 || posy > jogo.getHeight() - imagem.getHeight()){
		        	posy -= vely * (t2-t1)/1000;
		        	if(velx > red || velx < -red)
		        		velx -= raz*(velx - red) ;
		        	vely = 0;		        	
		        }
	    	}
	        
	    }
	    thread = null;
	}
	private void atualizarVelocidade(long t2, long t1) {
		double aceleracaoLocal = aceleracao;
        if(nitro && stamina > 0)		        	
        	aceleracaoLocal *= 1.5;
    	if(cima && ! baixo && !breque)	        			
    		vel += aceleracaoLocal * (t2-t1)/1000;				
    	else if(! cima && baixo && !breque)					
    		vel -= aceleracaoLocal * (t2-t1)/1000;				
    	else if(breque){				
    		if (vel>=0){									
    			vel -= (2)*aceleracaoLocal * (t2-t1)/1000;		 
    		}											
    		else{									 
    			vel += (2)*aceleracaoLocal * (t2-t1)/1000;		 
    		}												 
    	}													 
    	else if(! cima && !baixo && !breque){
    		if (vel != 0){			
    			int sinal = -1;
    			if(vel<0)
    				sinal = 1;
    			vel += sinal * aceleracaoLocal * (t2-t1)/1000 / 5;		
    		}												
    	}
	}
	private void atualizarDirecao(long t2, long t1) {
		if(direita && ! esquerda){
    		if (Math.abs(vel) > 600)
    			direcao += Math.toRadians(0.10);
    		else if(Math.abs(vel) > 300 && Math.abs(vel) < 600)
    			direcao += Math.toRadians(0.20);
    		else if(Math.abs(vel) > 10)
    			direcao += Math.toRadians(0.25);
    	}
    	else if(! direita && esquerda){
    		if (Math.abs(vel) > 600)
    			direcao += Math.toRadians(-0.10);
    		else if(Math.abs(vel) > 300 && Math.abs(vel) < 600)
    			direcao += Math.toRadians(-0.20);
    		else if(Math.abs(vel) > 10)
    			direcao += Math.toRadians(-0.25);
    	}
        direcao %= 2*Math.PI;
	}
	/*
	private void detectarColisao() {
		if(jogo.isMultiplayer()){
        	int numSobreCarro = 0;
        	for(int i=0; i<4; i++)
        		//if(jogo.estaSobreCarro(1-num, bordas[i].getX(), bordas[i].getY()))
        		if(jogo.estaSobreCarro(1-num, bordas[i]))
        			numSobreCarro++;
        	if(numSobreCarro > 0){
        		vel *= -1;
        	}
    	} 
	}
	*/
	private Point2D[] calcularBordas(double posx, double posy, double direcao) {
		double raio = Math.sqrt(width*width + height*height)/2;
    	double tg = Math.atan(height/width);
    	Point2D[] result = new Point2D[4];
    	result[0] = new Point2D.Double(posx + raio*Math.cos(-direcao + tg), posy + raio*Math.sin(-direcao + tg));
    	result[1] = new Point2D.Double(posx + raio*Math.cos(-direcao - tg), posy + raio*Math.sin(-direcao - tg));
    	result[2] = new Point2D.Double(posx - raio*Math.cos(-direcao + tg), posy - raio*Math.sin(-direcao + tg));
    	result[3] = new Point2D.Double(posx - raio*Math.cos(-direcao - tg), posy - raio*Math.sin(-direcao - tg));
		return result;
	}
	public void continuar() {
		pausado = false;
	}
	public void pausar() {
		pausado = true;
	}
	public double getDirecao() {
		return direcao;
	}
	public void setX(int x) {
		posx = x;
	}
	public void setY(int y) {
		posy = y;
	}
	public void setDirecao(double angulo) {
		direcao = angulo;
	}
	public double getVel() {
		return vel;
	}
	public int getNumVolta(){
		return numVolta;
	}
	public int getNumTempoVoltas(){
		return numTempoVoltas;
	}
	public double getTempoVolta(){
		if(numTempoVoltas > 0)
			return ((double)(jogo.getTempo() - tempoVolta[numTempoVoltas-1]))/1000f;
		return 0;
	}
	public double getTempoMelhorVolta() {
		return ((double)(melhorVolta))/1000f;
	}
	public double getTempoMedio(){
		return ((double)(tempoMedio))/1000f;
	}
	public boolean estaNoCarro(Point2D p){
		return polCarro.contains(p);
	}
	public boolean terminou() {
		return terminou;
	}
	public void atualizarTerminou() {
		terminou = true;
	}
	public double getTempoUltimaVolta() {
		if(numTempoVoltas > 1)
			return ((double)(tempoVolta[numTempoVoltas-1] - tempoVolta[numTempoVoltas-2]))/1000f;
		else return 0f;
	}
	public int getNumBatidas(){
		return numbatidas;
	}
	public void setNitro(boolean b) {
		nitro = b;
	}
}
