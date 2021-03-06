package main;

import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import paqueteDatos.PaqueteDatos;

public class MarcoServer extends JFrame implements Runnable{
	
	public MarcoServer() {
		
		LaminaServer lamina = new LaminaServer();
		
		
		this.setBounds(111,111,666,666);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.add(lamina);		
		
		this.setVisible(true);
		this.setTitle("ServerMessenger");
		
		Thread hilo = new Thread(this);
		
		hilo.start();
		
	}
	
	
	public class LaminaServer extends JPanel {
		
		public LaminaServer() {
			
			this.setVisible(true);
			
			area = new JTextArea(20, 40);
			
			this.add(area);
			
		}
		
		
	}


	@Override
	public void run() {
		try {
			
			ServerSocket conexionRecibida = new ServerSocket(9999);
			
			String ip, nick, mensaje;
			
			PaqueteDatos datosRecibidos;
			
			ArrayList<String> listIps = new ArrayList<String>();
			
			while(true) {
				
				Socket miSocket = conexionRecibida.accept();
				
				ObjectInputStream flujoEntrada = new ObjectInputStream(miSocket.getInputStream());
				
				datosRecibidos =(PaqueteDatos) flujoEntrada.readObject();
				
				nick = datosRecibidos.getNick();
				ip = datosRecibidos.getIp();
				mensaje = datosRecibidos.getMensaje();
				

				if(!mensaje.equals("online")) {
					
					area.append("\n"+ nick + ":"+ mensaje + " para "+ip);
					
				
					Socket enviaDestinatario = new Socket(ip, 9090);
				
					ObjectOutputStream reenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
				
					reenvio.writeObject(datosRecibidos);
				
					
					reenvio.close();
				
					enviaDestinatario.close();
				
					miSocket.close();
					
				}else {

//					------------------DETECTA ONLINE------------------------------
					InetAddress clienteConectado = miSocket.getInetAddress();
					String ipRemota = clienteConectado.getHostAddress();
					
					System.out.println(ipRemota);
					
					listIps.add(ipRemota);
					
					datosRecibidos.setIps(listIps);
					
					for(String z: listIps){
						Socket enviaDestinatario = new Socket(z, 9090);
						
						ObjectOutputStream reenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
						
						reenvio.writeObject(datosRecibidos);
						
						reenvio.close();
						
						enviaDestinatario.close();
						
						miSocket.close();
					}
					
//					--------------------------------------------------------------
					
				}
				
			}
		
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	
	
	JTextArea area;
	
}
	

