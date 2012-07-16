import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;


public class Jogo {
	Controladora c;
	Mapa mapa;
	private Carro[] carro = new Carro[100];
	private long tempoRegressivo;
	private int numCarros = 0;
	private boolean pausado = false;
	private boolean regressivo = true;
	private boolean jogoterminou = false;
	private long tempoMaximo = -1;
	private int numMaximoVoltas = -1;
	private int numCarrosTerminaram = 0;
	private long tempo;
	
	TocadorMusica tocador = new TocadorMusica("sound/carryon.mp3");
	
	private Jogador[] jogador;
	private int numJogadores;
	
	public Jogo(Controladora controller, Mapa m) {
		this.c = controller;
		mapa = m;
		tocador.adicionarMusica("sound/iwantout.mp3");
		tocador.adicionarMusica("sound/nothingtosay.mp3");    
		tocador.adicionarMusica("sound/intothestorm.mp3"); 
		tocador.adicionarMusica("sound/eagleflyfee.mp3");
		//Utilizar pasta sound_complete caso descomente as linhas abaixo
		//tocador.adicionarMusica("sound_complete/linesInTheSand.mp3");
		//tocador.adicionarMusica("sound_complete/theScarecrow.mp3");
		//tocador.adicionarMusica("sound_complete/takeTheTime.mp3");
		//tocador.adicionarMusica("sound_complete/spreadyourfire.mp3");
		//tocador.adicionarMusica("sound_complete/waitingsilence.mp3");
		//tocador.adicionarMusica("sound_complete/thetempleofhate.mp3");
		//tocador.adicionarMusica("sound_complete/mirrormirror.mp3");
		//tocador.adicionarMusica("sound_complete/timestandsstill.mp3");
		//tocador.adicionarMusica("sound_complete/thorn.mp3");
		//tocador.adicionarMusica("sound_complete/rebellionindreamland.mp3");
		//tocador.adicionarMusica("sound_complete/lastbeforethestorm.mp3");
		//tocador.adicionarMusica("sound_complete/sendmeasign.mp3");
		//tocador.adicionarMusica("sound_complete/spaceeater.mp3");
		//tocador.adicionarMusica("sound_complete/thesilence.mp3");
		//tocador.adicionarMusica("sound_complete/thetempleofhate.mp3");
		tocador.start();
		new ContadorRegressivo();
	}
	public void start(){
		for(int i=0; i<numCarros; i++)
			carro[i].start();
	}
	private class ContadorRegressivo implements Runnable{
		Thread thread;
		public ContadorRegressivo() {
			thread = new Thread(this);
	        thread.start();
		}
		@Override
		public void run() {
			long t1, t2;
			Thread me = Thread.currentThread();		
			tempoRegressivo = 0;
		    while (thread == me && regressivo) 
		    	if(!pausado){
					t1 = Calendar.getInstance().getTimeInMillis();
			        try {
			            Thread.sleep(1);
			        } catch (InterruptedException e) {break;}
			        t2 = Calendar.getInstance().getTimeInMillis();
			        tempoRegressivo += t2-t1;
		    	}
		    thread = null;
		    new ContadorTotal();
		}
	}
	private class ContadorTotal implements Runnable{
		Thread thread;
		public ContadorTotal() {
			thread = new Thread(this);
	        thread.start();
		}
		@Override
		public void run() {
			long t1, t2;
			Thread me = Thread.currentThread();		
			tempo = 0;
		    while (thread == me && !jogoterminou) 
		    	if(!pausado){
					t1 = Calendar.getInstance().getTimeInMillis();
			        try {
			            Thread.sleep(1);
			        } catch (InterruptedException e) {break;}
			        t2 = Calendar.getInstance().getTimeInMillis();
			        tempo += t2-t1;
			        verificarTermino();
		    	}
		    thread = null;
		    c.jogoTerminou();
		}
	}
	
	public void adicionarCarro(Carro c){		
		carro[numCarros] = c;
		if(mapa.getNumInicios() > numCarros){
			carro[numCarros].setX(mapa.getInicioX(numCarros));
			carro[numCarros].setY(mapa.getInicioY(numCarros));
			carro[numCarros].setDirecao(mapa.getAngulo(numCarros));
		}
		numCarros++;
	}
	public int getNumCarros(){
		return numCarros;
	}
	public Image getImagemCarro(int i){
		return carro[i].getImagem();
	}
	public int getPosXCarro(int i){
		return (int) carro[i].getX();
	}
	public int getPosYCarro(int i){
		return (int) carro[i].getY();
	}
	public int getVelCarro(int i){
		return (int) carro[i].getVel();
	}
	public Paint getFundo() {
		return mapa.getFundo();
	}
	public BufferedImage getPista() {
		return mapa.getImagem();
	}
/*	public boolean estaNaGrama(){
		if(mapa.getImagem().g)
	}
*/
	public void Acelerar(int numCarro) {
		if(!pausado)
			carro[numCarro].setCima(true);
	}
	public void Frear(int numCarro) {
		if(!pausado)
			carro[numCarro].setBaixo(true);
	}
	public void Esquerda(int numCarro) {
		if(!pausado)
			carro[numCarro].setEsquerda(true);
	}
	public void Direita(int numCarro) {
		if(!pausado)
			carro[numCarro].setDireita(true);		
	}
	public void soltouCima(int numCarro) {
		if(!pausado)
			carro[numCarro].setCima(false);
	}
	public void soltouBaixo(int numCarro) {
		if(!pausado)
			carro[numCarro].setBaixo(false);
	}
	public void soltouDireita(int numCarro) {
		if(!pausado)
			carro[numCarro].setDireita(false);
	}
	public void soltouEsquerda(int numCarro) {
		if(!pausado)
			carro[numCarro].setEsquerda(false);
	}
	public String getPeriodo() {
		return mapa.getPeriodo();
	}
	public int getWidth(){
		return mapa.getWidth();
	}
	public int getHeight(){
		return mapa.getHeight();
	}
	public boolean estaNaGrama(double posx, double posy) {
		return mapa.estaNaGrama((int) posx, (int) posy);
	}
	public boolean estaNaBarreira(double posx, double posy) {
		return mapa.estaNaBarreira((int) posx, (int) posy);
	}
	public void continuar() {
		pausado = false;
		for(int i = 0; i<numCarros; i++){
			carro[i].continuar();
		}
	}
	public void pausar() {
		pausado = true;
		for(int i = 0; i<numCarros; i++){
			carro[i].pausar();
		}
	}
	public boolean estaPausado() {
		return pausado ;
	}
	public double getDirecaoCarro(int f) {
		return carro[f].getDirecao();
	}
	public TocadorMusica getTocador(){
		return tocador;
	}
	public Mapa getMapa(){
		return mapa;
	}
	public int getVoltaCarro(int f) {
		return carro[f].getNumVolta();
	}
	public double getTempoVoltaCarro(int f) {
		return carro[f].getTempoVolta();
	}
	public String getTempoMelhorVoltaCarro(int f) {
		double r = carro[f].getTempoMelhorVolta();
		if(r == 0f)
			return "---";
		return Double.toString(r);
	}
	public synchronized boolean estaRegressivo() {
		return regressivo;
	}
	public String getRegressivo() {
		if(tempoRegressivo < 3000){
			return Long.toString(3 - (tempoRegressivo)/1000);
		}
		else if (tempoRegressivo < 3500){
			return "GO!";
		}
		else{
			regressivo = false;
			return null;
		}
	}
	public boolean isMultiplayer(){
		return (numCarros == 2);
	}
	public void Brecar(Integer iJogador) {
		carro[iJogador].setBreque(true);
	}
	public void soltouBrecar(Integer iJogador) {
		carro[iJogador].setBreque(false);
	}
	public void tocarBuzina() {
		new TocadorMusica("sound/buzina.mp3").start();
	}
	public synchronized boolean estaSobreCarro(int i, Point2D p) {
		return carro[i].estaNoCarro(p);
	}
	public boolean jogoTerminou(){
		return jogoterminou;
	}
	public synchronized void verificarTermino(){
		if(!jogoterminou){
			if(tempoMaximo != -1)
				if(tempo > tempoMaximo){
					jogoterminou = true;	
					c.jogoTerminou();
				}
			if(numMaximoVoltas != -1){
				for(int i=0; i<numCarros; i++)
					if(!carro[i].terminou() && carro[i].getNumVolta() > numMaximoVoltas){
						carro[i].atualizarTerminou();
						numCarrosTerminaram++;
						if(numCarrosTerminaram == numCarros){
							jogoterminou = true;
							c.jogoTerminou();
						}
					}
			}
		}
	}
	public void atualizarTempo(long tempo){
		this.tempo = tempo;
	}
	public long getTempo(){
		return tempo;
	}
	public String getTempoMedioCarro(int f) {
		double r = carro[f].getTempoMedio();
		if(r == 0f)
			return "---";
		return Double.toString(r);
	}
	public String getTempoUltimaVolta(int f) {
		double r = carro[f].getTempoUltimaVolta();
		if(r == 0f)
			return "---";
		return Double.toString(r);
	}
	public String getNome(int i) {
		return c.getNome(i);
	}
	public void adicionarJogador(Jogador j) {
		jogador[numJogadores] = j;
		numJogadores++;
	}
	public int getNumJogadores(){
		return numJogadores;
	}
	public void setTempoLimite(double t) {
		tempoMaximo = (long) (t * 1000);
	}
	public void setLimiteVoltas(int limiteVoltas) {
		numMaximoVoltas = limiteVoltas;
	}
	public int getNumTempoVoltasCarro(int f) {
		return carro[f].getNumTempoVoltas();
	}
	public int getNumBatidas(int i) {
		return carro[i].getNumBatidas();
	}
	public void passarMusica(){
		tocador.passarMusica();
	}
	public void Nitro(Integer iJogador) {
		if(!pausado)
			carro[iJogador].setNitro(true);		
	}
	public void soltouNitro(Integer iJogador) {
		if(!pausado)
			carro[iJogador].setNitro(false);		
	}
	public BufferedImage getMiniMapa() {
		return mapa.getMiniMapa();
	}
	public int getStamina(int i){
		return (int) carro[i].getStamina();
	}
	public boolean estaNaPoca(double x, double y) {
		return mapa.estaNaPoca((int) x, (int) y);
	}
}
