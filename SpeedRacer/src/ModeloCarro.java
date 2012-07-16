import java.awt.Image;
import java.awt.image.BufferedImage;


public class ModeloCarro{
	Image[] icon = new Image[100];
	BufferedImage[] imagem = new BufferedImage[100];
	String modelo;
	String[] cor = new String[100];
	int numCores = 0;
	static int iconsize = 450;
	double aceleracao = 600, stamina = 100, health = 100, vmaxRecomendada = 1500, vLimite = vmaxRecomendada + 300;
	
	
	public ModeloCarro(String m){
		modelo = m;
	}
	public void setComponents(double ac, double stam, double health){
		aceleracao = ac;
		stamina = stam;
		this.health = health;
	}
	public void addCor(String c, BufferedImage i){
		cor[numCores] = c;
		imagem[numCores] =  i;
		//int width = i.getWidth(), height = i.getHeight();
		//double razao = iconsize / width;
		//if(width < height)
		//	razao = iconsize / height;
		icon[numCores++] = i.getScaledInstance(250, 400, 0);
	}
	public String getModelo(){
		return modelo;
	}
	public String getCor(int i){
		return cor[i];
	}
	public Image getIcon(int i) {
		return icon[i];
	}
	public Carro getNewInstance(Jogo j, int i, int num){
		return new Carro(j, modelo, cor[i], aceleracao, vmaxRecomendada, stamina, health, imagem[i], num);
	}
	public int getNumCores() {
		return numCores;
	}
}
