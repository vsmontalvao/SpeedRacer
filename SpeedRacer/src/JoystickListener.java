import net.java.games.input.Component;
import net.java.games.input.Controller;


public class JoystickListener implements Runnable{
	private int num;
	private Controller controller; 
	private Controladora c; 
	private Thread thread;
	private Component[] botao;
	float[] valores, novosvalores;
	
	public JoystickListener(Controladora c, Controller controller, int num){
		this.c = c;
		this.controller = controller;
		this.num = num;
		botao = controller.getComponents();
	}
	public void start(){
		thread = new Thread(this);
        thread.start();
	}
	@Override
	public void run() {
		Thread me = Thread.currentThread();
		valores = new float[botao.length];
		novosvalores = new float[botao.length];
		
		controller.poll();
    	for(int i=0;i<botao.length;i++){
    		valores[i] = botao[i].getPollData();
    		//System.out.println(i+" - "+botao[i].getName());
    	}
	    while (thread == me) {
	    	try {
	              Thread.sleep(1);
	           } catch (InterruptedException e) {
	              e.printStackTrace();
	           }	
	    	controller.poll();
	    	for(int i=0;i<botao.length;i++){
	    		novosvalores[i] = botao[i].getPollData();
	    		if(novosvalores[i] != valores[i]){
	    			//System.out.print(botao[i].getName());
		    		//System.out.print(novosvalores[i]+" ");
		    		gerarEvento(i);
		    		valores[i] = novosvalores[i];
	    		}
	    	}
	    }
	    thread = null;
	}
	private void gerarEvento(int i) {
		int numJogador = num;
		if(!c.isMultiplayer())
			numJogador = 0;
		if(botao[i].isAnalog()) {//1f, 0f ou -1f
			if(novosvalores[i] == 0f){
				if(Botao.values()[i] == Botao.x && valores[i] == 1f)
					c.acaoSoltou(Tecla.DIREITA, numJogador);
				else if(Botao.values()[i] == Botao.x && valores[i] == -1f)
					c.acaoSoltou(Tecla.ESQUERDA, numJogador);
				else if(Botao.values()[i] == Botao.y && valores[i] == 1f)
					c.acaoSoltou(Tecla.FREA, numJogador);
				else
					c.acaoSoltou(Tecla.ACELERA, numJogador);
			}	
			else if (novosvalores[i] == 1f){
				if(Botao.values()[i] == Botao.x)
					c.acaoPressionou(Tecla.DIREITA, numJogador);
				else if(Botao.values()[i] == Botao.y){
					if(c.getTela().equalsIgnoreCase("jogo")){
					//	c.acaoPressionou(Tecla.MUSICA, numJogador);
					}else
						c.acaoPressionou(Tecla.FREA, numJogador);
				}
			}
			else{
				if(Botao.values()[i] == Botao.x)
					c.acaoPressionou(Tecla.ESQUERDA, numJogador);
				else if(Botao.values()[i] == Botao.y){
					if(c.getTela().equalsIgnoreCase("jogo")){
						//c.acaoPressionou(Tecla.MUSICA, numJogador);
					}else
						c.acaoPressionou(Tecla.ACELERA, numJogador);
				}
			}
         } else { //On or off
            if(novosvalores[i] == 1.0f) {
            	if(Botao.values()[i] == Botao.START)
            		c.acaoPressionou(Tecla.PAUSE, numJogador);
            	else if(Botao.values()[i] == Botao.SELECT)
            		c.acaoPressionou(Tecla.SAIR, numJogador);
            	else if(Botao.values()[i] == Botao.B3)
            		c.acaoPressionou(Tecla.CONTINUAR, numJogador);
            	else if(Botao.values()[i] == Botao.B4)
            		c.acaoPressionou(Tecla.VOLTAR, numJogador);
            	else if(Botao.values()[i] == Botao.R1)
            		c.acaoPressionou(Tecla.ACELERA, numJogador);
            	else if(Botao.values()[i] == Botao.R2)
            		c.acaoPressionou(Tecla.FREA, numJogador);
            	if(Botao.values()[i] == Botao.L1 || Botao.values()[i] == Botao.L2)
            		c.acaoPressionou(Tecla.NITRO, numJogador);
            	if(Botao.values()[i] == Botao.B3)
            		c.acaoPressionou(Tecla.BRECAR, numJogador);
            	if(Botao.values()[i] == Botao.B4)
            		c.acaoPressionou(Tecla.BUZINA, numJogador);
            	if(Botao.values()[i] == Botao.B2)
            		c.acaoPressionou(Tecla.MUSICA, numJogador);
            	else{}
            	//System.out.println("On");
            } else {
            	if(Botao.values()[i] == Botao.START)
            		c.acaoSoltou(Tecla.PAUSE, numJogador);
            	else if(Botao.values()[i] == Botao.SELECT)
            		c.acaoSoltou(Tecla.SAIR, numJogador);
            	else if(Botao.values()[i] == Botao.B3)
            		c.acaoSoltou(Tecla.CONTINUAR, numJogador);
            	else if(Botao.values()[i] == Botao.B4)
            		c.acaoSoltou(Tecla.VOLTAR, numJogador);
            	else if(Botao.values()[i] == Botao.R1)
            		c.acaoSoltou(Tecla.ACELERA, numJogador);
            	else if(Botao.values()[i] == Botao.R2)
            		c.acaoSoltou(Tecla.FREA, numJogador);
            	if(Botao.values()[i] == Botao.L1 || Botao.values()[i] == Botao.L2)
            		c.acaoSoltou(Tecla.NITRO, numJogador);
            	if(Botao.values()[i] == Botao.B3)
            		c.acaoSoltou(Tecla.BRECAR, numJogador);
            	else{}
            	//System.out.println("Off");
            }
         }
	}
	public int getNumber() {
		return num;
	}
}