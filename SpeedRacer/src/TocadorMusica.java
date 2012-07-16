import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream; 
  
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;  
  
public class TocadorMusica extends Thread {  
   
   
   String musica[] = new String[100];   
   Player plr[] = new Player[100];
   InputStream in[] = new InputStream[100];
   boolean tocando = false;
   private int numMusicas = 0, atual;
   
   public TocadorMusica(String musica){
	   adicionarMusica(musica);  	 
   }
   public void adicionarMusica(String mus){
	   try {
		in[numMusicas] = (InputStream)(new FileInputStream(mus));
		plr[numMusicas] = new Player(in[numMusicas]);
		musica[numMusicas++] = mus;
	} catch (FileNotFoundException | JavaLayerException e) {
		e.printStackTrace();
	}  
   }
   
   public void run() {  
	   if(plr != null){
		   tocando = true;
		   int i=0; //Problema para repetir músicas
		   while(tocando){
			   for(;i<numMusicas; i++){
				   atual = i;
				   try {
						plr[i].play();
				   } catch (JavaLayerException e) {
						e.printStackTrace();}
				   if(!tocando)
					   break;
			   }
			   if(i == numMusicas)
				   tocando = false;
		   }
		   
		   
	   }
   }  
   
   /*
   public String getMusica() {  
      return musica;  
   }  
  
   public void setMusicaSelecionada(String musica) {  
      this.musica = musica;  
   }  
   
   public void mudarMusica(){
	  terminarMusica();
	  
	  try{
		  in = (InputStream)(new FileInputStream(musica));
		  plr = new Player(in);  
		  plr.play();
	  } catch(Exception e){
		  System.out.println("### Exception do PlayMusic ### "+e.getClass().getName()); 
	  }
	    
   }
   */
   public void terminarMusica(){
	   tocando = false;
	   plr[atual].close();
   }
	public void passarMusica() {
		plr[atual].close();
	}
   
}  