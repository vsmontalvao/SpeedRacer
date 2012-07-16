import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
  
public class TocadorMusica extends Thread {  
   String musica[] = new String[100];   
   Player plr[] = new Player[100];
   boolean tocando = false;
   private int numMusicas = 0, atual;
   
   public TocadorMusica(String musica){
	   adicionarMusica(musica);  	 
   }
   public void adicionarMusica(String mus){
		musica[numMusicas++] = mus;
   }
   
   public void run() {  
	   if(plr != null){
		   tocando = true;
		   while(tocando){
			   try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			   for(int i=0;i<numMusicas; i++){
				   atual = i;
				   try {
					    try {
							plr[i] = new Player((InputStream)(new FileInputStream(musica[i])));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					    plr[i].play();
						plr[i].close();
				   } catch (JavaLayerException e) {
						e.printStackTrace();}
				   if(!tocando)
					   break;
			   }
		   }
	   }
   }
   public void terminarMusica(){
	   tocando = false;
	   plr[atual].close();
   }
	public void passarMusica() {
		plr[atual].close();		
	}
   
}  