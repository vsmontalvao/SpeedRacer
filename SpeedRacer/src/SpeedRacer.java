import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class SpeedRacer extends JFrame{
		/**
	 * 
	 */
	Controladora c;
	
	private static final long serialVersionUID = 1L;
		public Display display;

		public SpeedRacer(){
			super("SpeedRacer");
			c = Controladora.instance;
			
			display = c.getDisplay();
			
			adicionarElementos();
			
			getContentPane().add(display, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setPreferredSize(c.getDimension());
			pack();
			setLocationRelativeTo(null);
			setExtendedState(JFrame.MAXIMIZED_BOTH); 
			setVisible(true);
			KeyHandler k = new KeyHandler();
			addKeyListener(k);	
			
			Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
			boolean stick1  = false;
			boolean stick2  = false;
		    for(int i=0;i<controllers.length && !stick2; i++) {
		    	if(controllers[i].getType()==Controller.Type.STICK) {
		    		if(!stick1){
		    			c.addJoystick(new JoystickListener(c, controllers[i], i));
		    			stick1 = true;
		    		}
		    		else{
		    			c.addJoystick(new JoystickListener(c, controllers[i], i));
		    			stick2 = true;
		    		}
		    	}
		    }		    
		}

		private void adicionarElementos() {			
			c.adicionarCarro("Esportivo", "Cinza", "img/renault.png");
			c.adicionarCarro("Esportivo", "Vermelho", "img/carro1.png");
			c.adicionarCarro("Popular", "Cinza", "img/carro2.png");
			c.adicionarCarro("Corrida", "Mini Cooper George Harrison", "img/carro3.png");
			c.adicionarCarro("Míssil", "Preto", "img/carro4.png");
			c.adicionarCarro("Corrida", "Azul e Branco", "img/carro5-2011-Ford-Fiesta-RS-WRC-Top-View-590x337.png"); 
			c.adicionarCarro("Popular", "Branco", "img/carro6.png");		 
			c.adicionarCarro("Popular", "Azul", "img/carro7.png"); 
			c.adicionarCarro("Popular", "Prateado", "img/carro8.png");
			c.adicionarCarro("PickUp", "Branco", "img/carro9.png");
			c.adicionarCarro("Fórmula", "Azul", "img/carro10.png");
			c.adicionarCarro("Trator", "Laranja", "img/carro11.png");
			c.adicionarCarro("Popular", "Amarelo", "img/carro12.png");
			c.adicionarCarro("Corrida", "Amarelo", "img/carro13.png");
			c.adicionarCarro("Corrida", "Vermelho", "img/carro14.png");
			c.adicionarCarro("Corrida", "Prateado", "img/carro15.png");
			c.adicionarCarro("Corrida", "Vermelho Escuro", "img/carro16.png");
			c.adicionarCarro("Corrida", "Branco", "img/carro17.png"); 
			c.adicionarCarro("Corrida", "Amarelo e Branco", "img/carro18.png");
			c.adicionarCarro("Popular", "Surf", "img/carro19.png"); 
			c.adicionarCarro("Ônibus", "Cinza", "img/carro20.png"); 
			
			
			c.adicionarMapa("Pista K", "img/pistak.png", "img/pistak_500.png", "img/pistak_200.png");
			int[] inicios1 = {2203, 330, 2040, 480};
			c.setMapaInicios("Pista K", 0, inicios1 );
			c.adicionarMapa("Circular", "img/pista_circular.png", "img/pista_circular_450.png", "img/pista_circular_200.png");
			int[] inicios2 = {5120, 4550, 4940, 4530};
			c.setMapaInicios("Circular", -60, inicios2 );
			c.adicionarMapa("Linha", "img/pista_linha.png", "img/pista_linha_450.png", "img/pista_linha_200.png");
			int[] inicios3 = {3000, 5730, 3160, 5730};
			c.setMapaInicios("Linha", -90, inicios3 );		
			c.adicionarMapa("Molhada", "img/pista_molhada.png", "img/pista_molhada_450.png", "img/pista_molhada_200.png");
			int[] inicios4 = {3100, 6390, 3100, 6540};
			c.setMapaInicios("Molhada", 0, inicios4 );	
		}
		
		private class KeyHandler extends KeyAdapter {			
			public void keyPressed(KeyEvent e) {
				c.keyPressed(e.getKeyCode());
			} 
			public void keyReleased(KeyEvent e) {	
				c.keyReleased(e.getKeyCode());
			}
		}
}