
public class Jogador {
	Carro carro;
	Controles controles;
	int num;
	String nome;
	
	public Jogador(Controles c, int num){
		controles = c;
		this.num = num;
		nome = "Jogador" + num;
	}
	public void setCarro(Carro c){
		carro = c;
	}
	public Tecla tipoTecla(int keycode){
		return controles.tipoTecla(keycode);
	}
	public Carro getCarro() {
		return carro;
	}
	public String getNome() {
		return nome;
	}
}
