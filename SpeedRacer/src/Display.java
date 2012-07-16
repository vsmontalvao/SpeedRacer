import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class Display extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controladora c;
	private Thread thread;
	protected Menu menu;
	protected Jogo jogo;
	Label l1;
	BufferedImage logo;
	private int foco = 0, foco2 = 1;
	private int modoVisual = 0;
	private AffineTransform affineTransform = new AffineTransform();
	protected Estatisticas estatisticas; 
	
	public Display(Controladora c){
		this.c = c;

		menu = c.getMenu();
		start();
	}
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	                RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(
	            RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
	    if(c.getTela().equalsIgnoreCase("menu"))
	    	imprimirMenu(g2);    
	    else if(c.getTela().equalsIgnoreCase("jogo"))
	    	imprimirJogo(g2);  
	    else if(c.getTela().equalsIgnoreCase("estatisticas"))
	    	imprimirEstatisticas(g2);
	}
	public void imprimirEstatisticas(Graphics2D g){
		estatisticas = c.getEstatisticas();
		logo = c.getLogo();
		
		g.drawImage(estatisticas.getFundo(), 0, 0, null);
		
	    int linha = 40;
	    int coluna = c.getWidth()/2;
	    int extra = 80;
	    g.drawImage(logo, coluna - logo.getWidth()/2, linha, null);   
	    linha += logo.getHeight() + 70;
	    
    	g.setFont(new Font("Sans", Font.BOLD, 54));
    	FontMetrics metrics = g.getFontMetrics();
    	int fontHeight = metrics.getHeight();
		g.setPaint(new GradientPaint(0, linha - fontHeight, new Color(100,100,100), c.getWidth()/2,
			linha - fontHeight, new Color(230,230,230), true));  
	    g.drawString(estatisticas.getTitulo(), coluna - metrics.stringWidth(estatisticas.getTitulo())/2,
    			linha);
	    
    	linha+=fontHeight + 50;
    	int linhaInicial = linha;
    	
    	g.setFont(new Font("Sans", Font.BOLD, 32));
    	metrics = g.getFontMetrics();
    	fontHeight = metrics.getHeight();
    	g.setColor(new Color(200, 220, 230));
    	
	    for(int i=0; i<estatisticas.getNumElementos(); i++){
	    	String texto = estatisticas.getElemento(i);
	    	if(estatisticas.getElemento(i).equalsIgnoreCase("sair")||
	    			estatisticas.getElemento(i).equalsIgnoreCase("novo jogo")){
	    		int col = c.getWidth()/4;
	    		if(estatisticas.getElemento(i).equalsIgnoreCase("novo jogo"))
	    			col = c.getWidth() * 3/4;
	    		
	    		int lbaixo= c.getHeight() - fontHeight - 60;
	    		if(i == estatisticas.getLocalAtual()){
		    		g.setPaint(new GradientPaint(col - metrics.stringWidth(texto)/2
		    				- extra, c.getHeight() - 20, new Color(95, 169, 215,0), col,
		    				c.getHeight() - 20, new Color(95, 169, 215), true)); 
		    	    g.fillOval(col - metrics.stringWidth(texto)/2 - extra, 
		    	    		lbaixo, metrics.stringWidth(texto) + 2*extra, fontHeight*3/2);
		    	    g.setColor(Color.WHITE);
		    	}
	    		g.drawString(texto, col - metrics.stringWidth(texto)/2, lbaixo + fontHeight);
	    	}
	    	else{
	    		if(estatisticas.isMultiplayer()){
	    			if(i == 0)
	    				coluna = c.getWidth()/4;
	    			if(i == estatisticas.getNumElementosPlayer()){
	    				linha = linhaInicial;
	    				coluna = c.getWidth() * 3/4;
	    			}
	    		}
	    		g.setColor(Color.WHITE);
	    		g.drawString(texto, coluna - metrics.stringWidth(texto)/2, linha);
	    		linha += fontHeight + 5;
	    	}
	    }
	}
	public void imprimirMenu(Graphics2D g){
		menu = c.getMenu();
		logo = c.getLogo();
		String titulo = menu.getTitulo();
		
		g.drawImage(menu.getFundo(), 0, 0, null);
		
	    int linha = 40;
	    int coluna = c.getWidth()/2;
	    int extra = 80;
	    g.drawImage(logo, coluna - logo.getWidth()/2, linha, null);   
	    linha += logo.getHeight() + 70;
	    
    	g.setFont(new Font("Sans", Font.BOLD, 54));
    	FontMetrics metrics = g.getFontMetrics();
    	int fontHeight = metrics.getHeight();
		g.setPaint(new GradientPaint(0, linha - fontHeight, new Color(100,100,100), c.getWidth()/2,
			linha - fontHeight, new Color(230,230,230), true));  
	    if(!titulo.equalsIgnoreCase("Menu Principal"))
	    	g.drawString(titulo, coluna - metrics.stringWidth(titulo)/2,
    			linha);
	    
    	
    	int linhaimagem = linha + 90;
    	if(titulo.equalsIgnoreCase("Mapa") || titulo.contains("Carro")){
    		Image imagemMenu = menu.getImagem();
    		linha -= 40;
    		coluna = c.getWidth()*3/4;
    		if(titulo.equalsIgnoreCase("Mapa")){
	    		g.setPaint ( menu.getFundoIcon() );
	    		g.fillRect ( c.getWidth()*13/40 - imagemMenu.getWidth(null)/2, linhaimagem - fontHeight,
	    				imagemMenu.getWidth(null), imagemMenu.getHeight(null) );
    		}
    		g.drawImage(imagemMenu, c.getWidth()*13/40 - imagemMenu.getWidth(null)/2, 
    			linhaimagem - fontHeight, imagemMenu.getWidth(null), imagemMenu.getHeight(null), null);
    		extra /= 2;
    	}
    	linha+=fontHeight + 50;
    	
	    for(int i=0; i<menu.getNumElementos(); i++){ 
	    	if(menu.getTitulo().equalsIgnoreCase("Controles")){
	    		coluna = 60;
	    		if(!menu.getElemento(i).equalsIgnoreCase("voltar")){
		    		g.setFont(new Font("Sans", Font.BOLD, 22));
		        	metrics = g.getFontMetrics();
		        	fontHeight = metrics.getHeight();
		        	g.setColor(Color.WHITE);
		        	if(i == 0){
		        		linha -= 60;
		        		g.drawString("MultiPlayer", 890, linha);
		        		g.drawString("Joystick         SinglePlayer                        "+
		        		"          Jogador1                        Jogador2 ", 190, (linha+=fontHeight*1.5));
		        		linha+=fontHeight*2;
		        	}
	    		}
	    		else{
	    			linha += fontHeight;
	    			coluna = c.getWidth()/2 - metrics.stringWidth(menu.getElemento(i))/2;
	    			g.setFont(new Font("Sans", Font.BOLD, 36));
		        	metrics = g.getFontMetrics();
		        	fontHeight = metrics.getHeight();
		        	g.setColor(new Color(200, 220, 230));
	    		}
	    		if(i == menu.getLocalAtual()){
		    		g.setPaint(new GradientPaint(coluna - extra, linha - fontHeight, new Color(95, 169, 215,0), 
		    				coluna + metrics.stringWidth(menu.getElemento(i))/2,
		    				linha - fontHeight, new Color(95, 169, 215), true)); 
		    	    g.fillOval(coluna - extra, linha - fontHeight, 
		    	    		metrics.stringWidth(menu.getElemento(i)) + 2 * extra, fontHeight*3/2);
		    	    g.setColor(Color.WHITE);
    			}
	    		g.drawString(menu.getElemento(i), coluna, linha);
	    		linha += fontHeight*1.5;
	    	}
	    	else{
	    		g.setFont(new Font("Sans", Font.BOLD, 36));
	        	metrics = g.getFontMetrics();
	        	fontHeight = metrics.getHeight();
	        	g.setColor(new Color(200, 220, 230));
	    		
		    	if(menu.getElemento(i).equalsIgnoreCase("voltar")){
		    		linha += 30;
		    		if(menu.getTitulo().equalsIgnoreCase("carro"))
		    			linha += 50;
		    	}
		    	if(i == menu.getLocalAtual()){
		    		g.setPaint(new GradientPaint(coluna - metrics.stringWidth(menu.getElemento(i))/2
		    				- extra, linha - fontHeight, new Color(95, 169, 215,0), coluna,
		    				linha - fontHeight, new Color(95, 169, 215), true)); 
		    	    g.fillOval(coluna - metrics.stringWidth(menu.getElemento(i))/2 - extra, 
		    	    		linha - fontHeight, metrics.stringWidth(menu.getElemento(i)) + 2 * extra, fontHeight*3/2);
		    	    if(menu.getTitulo().contains("Carro") && c.isMultiplayer()){
		    	    	g.setFont(new Font("Sans", Font.BOLD, 14));
			    	    g.setColor(Color.GREEN);
			    	    g.drawString("Jogador"+Integer.toString(c.getControle()), coluna + metrics.stringWidth(menu.getElemento(i))/2 + extra,
				    			linha);
			    	    g.setFont(new Font("Sans", Font.BOLD, 36));
		    	    }
		    	    g.setColor(Color.WHITE);
		    	}
		    	g.drawString(menu.getElemento(i), coluna - metrics.stringWidth(menu.getElemento(i))/2, linha);
		    	if(i == menu.getLocalAtual())
		    		g.setColor(new Color(200, 220, 230));
		    	linha += fontHeight*1.5;
		    	if(menu.getElemento(i).equalsIgnoreCase("Próximo") || menu.getElemento(i).equalsIgnoreCase("iniciar jogo")){
		    		if(menu.getTitulo().equalsIgnoreCase("carro"))
		    			linha += 50;
		    		linha += 30;
		    	}
	    	}
	    }
	}
	public void imprimirJogo(Graphics2D g){
		if(!jogo.isMultiplayer())
			imprimirXY(g, 0, 0, c.getWidth());
		else{
			imprimirXY(g, foco, 0, c.getWidth()/2);

			g.setPaint(new GradientPaint(0, 0, new Color(50, 120, 215), 0, c.getWidth()/2, new Color(95, 169, 215), true));
			g.fill(new Rectangle2D.Double(c.getWidth()/2-10, 0, c.getWidth()/2+10, c.getHeight()));
			
			imprimirXY(g, foco2, c.getWidth()/2, c.getWidth()/2);
		}
		g.setFont(new Font("Sans", Font.BOLD, 80));
    	String pause = "Parado";

    	FontMetrics metrics = g.getFontMetrics();
    	//int fontHeight = metrics.getHeight();
    	if(jogo.estaRegressivo() && metrics != null){
    		g.drawString(jogo.getRegressivo(), c.getWidth()/2 - g.getFontMetrics().stringWidth(jogo.getRegressivo())/2,
	    			c.getHeight()/2 + g.getFontMetrics().getHeight()/2);
		}
		if(jogo.estaPausado()){
	    	g.drawString(pause, c.getWidth()/2 - g.getFontMetrics().stringWidth(pause)/2,
	    			c.getHeight()/2 + g.getFontMetrics().getHeight()/2);
    	}
		
	}
	private void imprimirXY(Graphics2D g, int f, int x, int width) {
		
		g.setPaint ( jogo.getFundo() );
		g.fillRect ( x, 0, width, c.getHeight() );
		
		
		int posxf = jogo.getPosXCarro(f),
				posyf = jogo.getPosYCarro(f),
				imx = posxf - width/2, 
				imy = posyf - c.getHeight()/2 - Carro.getCarroHeight();
		//boolean bordaDir = false, bordaEsq = false, bordaCima = false, bordaBaixo = false;
		if(imx < 0){
			imx = 0;
		//	bordaEsq = true;
		}
		if(imx > jogo.getWidth() - width){
			imx = jogo.getWidth() - width;
		//	bordaDir = true;
		}
		if(imy < 0){
			imy = 0;
		//	bordaCima = true;
		}
		if(imy > jogo.getHeight() - c.getHeight()){
			imy = jogo.getHeight() - c.getHeight();
		//	bordaBaixo = true;
		}
		affineTransform.rotate(jogo.getDirecaoCarro(f), posxf + Carro.getCarroWidth()/2,
				posyf + Carro.getCarroHeight());
		
		g.drawImage(jogo.getPista().getSubimage(imx, imy, width, c.getHeight()), x, 0, null);
		//g.drawImage(jogo.getPista().getSubimage(imx, imy, width, c.getHeight()), affineTransform, null);
		AffineTransform af = new AffineTransform();
		for(int i=0; i < jogo.getNumCarros(); i++){
			int px, py;
			if(i == f){
				px = posxf;
				py = posyf;
			}
			else{
				px = jogo.getPosXCarro(i);
				py = jogo.getPosYCarro(i);
			}
			
	    	if(px - imx + Carro.getCarroWidth() > 0 
	    		&& px - imx < width
	    		&& py - imy  + Carro.getCarroHeight()> 0 
	    		&& py - imy < c.getHeight())
	    	{
	    		int posicaoImagemCarroX = x + px - imx - Carro.getCarroWidth()/2;
	    		int posicaoImagemCarroY = py - imy - Carro.getCarroHeight()/2;
	    		af = new AffineTransform();
	    		af.translate(x + px - imx - Carro.getCarroWidth()/2, 
    	    			py - imy - Carro.getCarroHeight()/2);
	    		af.rotate(jogo.getDirecaoCarro(i) + Math.toRadians(90.0),
	    				Carro.getCarroWidth()/2, Carro.getCarroHeight()/2);
	    		if (jogo.getPeriodo().equalsIgnoreCase("noite")){
		    		g.setColor(new Color(1,1,0,0.50f));
		    		// depois aumentar o arco e tornar opicional o farol
		    		g.fillArc(posicaoImagemCarroX - 75	 , posicaoImagemCarroY - 65, 
		    				200, 200, (int) (-30 - Math.toDegrees(jogo.getDirecaoCarro(i))), 60);
	    		}
	    		g.drawImage(jogo.getImagemCarro(i), af, null);
	    		
	    	}
	    }	
		
		g.setFont(new Font("Sans", Font.BOLD, 20));
    	FontMetrics metrics = g.getFontMetrics();
    	int fontHeight = metrics.getHeight();
    	g.setColor(Color.WHITE);
    	
    	int linha = 20, coluna = x + 40;
    	//g.drawString(Integer.toString((int) (jogo.getDirecaoCarro(f) * 180/Math.PI)%360)+'°', x + 40, 60 + fontHeight);
    	g.drawString(jogo.getNome(f), coluna + 60, (linha+=fontHeight));
    	g.drawString("Volta: " + Integer.toString(jogo.getNumTempoVoltasCarro(f)), coluna, (linha+=fontHeight));
    	g.drawString("Tempo: " + Double.toString(((double)(jogo.getTempo()))/1000f), coluna, (linha+=fontHeight));
    	g.drawString("Média: " + jogo.getTempoMedioCarro(f), coluna, (linha+=fontHeight));
    	g.drawString("Melhor Tempo: " + jogo.getTempoMelhorVoltaCarro(f), coluna, (linha+=fontHeight));
    	g.drawString("Última Volta: " + jogo.getTempoUltimaVolta(f), coluna, (linha+=fontHeight));
    	g.drawString("Tempo Volta: " + Double.toString(jogo.getTempoVoltaCarro(f)), coluna, (linha+=fontHeight));
    	g.drawString("Velocidade: " + Integer.toString(jogo.getVelCarro(f)/5) + " km/h", coluna, (linha+=fontHeight));
    	
    	linha += 5 + fontHeight;
    	String stamina = "Nitro: " + jogo.getStamina(f);
    	g.drawString(stamina, coluna + 100 - g.getFontMetrics().stringWidth(stamina)/2, linha);
    	g.setPaint(new GradientPaint(coluna, (linha+=10) + 30, new Color(150,150,150), coluna, linha, new Color(220,220,220)));
    	g.fillRoundRect(coluna, linha, 200, 30, 10, 30);
    	g.setPaint(new GradientPaint(coluna, linha + 30, new Color(70,110,200), coluna, linha, new Color(180,210,255)));
    	g.fillRoundRect(coluna, linha, (int) (jogo.getStamina(f)*2), 30, 10, 30);
    	
		linha = 20;
		BufferedImage mMapa  =  jogo.getMiniMapa();
		int w = mMapa.getWidth(), h = mMapa.getHeight();
		if(mMapa != null){
	    	g.setPaint ( new Color(255, 255, 255, 20) );
			g.fillRect ( x + width - w - 50, linha, w, h);
	    	g.drawImage(mMapa, x + width - w - 50, linha, w, h, null);
	    	g.setPaint ( new Color(190,240,200) );
	    	g.fillOval(x + width - w - 50 + jogo.getPosXCarro(0)*w/jogo.getPista().getWidth(), 
	    			linha + jogo.getPosYCarro(0)*h/jogo.getPista().getHeight(), 5, 5);
	    	if(jogo.isMultiplayer()){
		    	g.setPaint ( new Color(240,250,60) );
		    	g.fillOval(x + width - w - 50 + jogo.getPosXCarro(1)*w/jogo.getPista().getWidth(), 
		    			linha + jogo.getPosYCarro(1)*h/jogo.getPista().getHeight(), 5, 5);
	    	}
		}
		g.setPaint ( Color.WHITE );
	}
	public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) { break; }
        }
        thread = null;
    }
    public void start() {
        thread = new Thread(this);
        thread.start();
    }
	public void setJogo(Jogo jogo) {
		this.jogo = jogo;		
		if(jogo.isMultiplayer())
			mudaModoVisual();
	}
	public void setFoco(int i){
		foco = i;		
		foco2 = 1-i;
	}
	public void mudaModoVisual() {
		modoVisual = modoVisual+1 %2;
	}
	public int getFoco() {
		return foco;
	}
	public int getFoco2() {
		return foco2;
	}
}