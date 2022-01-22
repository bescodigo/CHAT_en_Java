package sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import paqueteDatos.PaqueteDatos;

public class Enchufe {

	
	public void avisarOnline() {
		
		try {
			Socket enchufe = new Socket("192.168.42.228", 9999);
			PaqueteDatos datos = new PaqueteDatos();
			
			datos.setMensaje("online");
			
			ObjectOutputStream flujoSalida = new ObjectOutputStream(enchufe.getOutputStream());
			
			flujoSalida.writeObject(datos);
			
			flujoSalida.close();
			enchufe.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
		
	}
	
	
}
