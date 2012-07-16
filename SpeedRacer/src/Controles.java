import java.awt.event.KeyEvent;


public class Controles {
	private int[] teclado = new int[Tecla.values().length];
	private int[] joystick = new int[Tecla.values().length];
	
	public Controles(int iPlayer, int numPlayers){
		switch (iPlayer){
			case 0:
				int[] controles1 = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, 
					KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_ENTER,
					KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_X, KeyEvent.VK_Z,
					KeyEvent.VK_P};
				if(numPlayers == 2){
					controles1[Tecla.ACELERA.ordinal()] = KeyEvent.VK_W;
					controles1[Tecla.FREA.ordinal()] = KeyEvent.VK_S;
					controles1[Tecla.ESQUERDA.ordinal()] = KeyEvent.VK_A;
					controles1[Tecla.DIREITA.ordinal()] = KeyEvent.VK_D;
				}
				teclado = controles1;
				break;
			case 1:
				int[] controles2 = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, 
						KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_ALT_GRAPH,
						KeyEvent.VK_SLASH, KeyEvent.VK_ALT, KeyEvent.VK_M, KeyEvent.VK_ALT_GRAPH, KeyEvent.VK_P};
				teclado = controles2;
				break;
		}
	}
	public String getTecla(Tecla t){
		for(int i=0; i<teclado.length; i++)
			if(t == Tecla.values()[i])
				//return KeyEvent.getKeyModifiersText(teclado[i]);
				return KeyEvent.getKeyText(teclado[i]);
		return null;
	}
	public Tecla tipoTecla(int keycode){
		for(int i=0; i<Tecla.values().length; i++){
			if(teclado[i] == keycode)
				return Tecla.values()[i];
		}
		return null;
	}
	public Tecla tipoBotao(int keycode){
		for(int i=0; i<Tecla.values().length; i++){
			if(joystick[i] == keycode)
				return Tecla.values()[i];
		}
		return null;
	}
	public boolean isHome(int keycode){
		if(KeyEvent.VK_HOME == keycode)
			return true;
		return false;
	}
	public boolean isPageUp(int keycode){
		if(KeyEvent.VK_PAGE_UP == keycode)
			return true;
		return false;
	}
	public boolean isPageDown(int keycode){
		if(KeyEvent.VK_PAGE_DOWN== keycode)
			return true;
		return false;
	}
	public boolean isEnd(int keycode){
		if(KeyEvent.VK_END == keycode)
			return true;
		return false;
	}
}