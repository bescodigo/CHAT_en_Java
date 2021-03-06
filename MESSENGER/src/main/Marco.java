package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import paqueteDatos.PaqueteDatos;
import sockets.Enchufe;

public class Marco extends JFrame implements Runnable{
	
	public Marco() {
		
		this.setBounds(111, 111, 666, 666);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Messenger");
		
		Lamina lamina = new Lamina();
		this.add(lamina);
		
		this.setVisible(true);
		
		
		Enchufe conexion = new Enchufe();
		conexion.avisarOnline();
		
		
		Thread hilo = new Thread(this);
		hilo.start();
		
	}
	
	
	public class Lamina extends JPanel{
		
		public Lamina() {
			
			String usuario = JOptionPane.showInputDialog("Nombre:");
			nick = new JLabel();
			nick.setText(usuario);
			
			area = new JTextArea(20,40);
			campo = new JTextField(40);
			comboBox = new JComboBox<String>();
			btnEnviar = new JButton("Enviar");
			
			enviarMensaje eventoEnviar = new enviarMensaje();
			
			btnEnviar.addActionListener(eventoEnviar);
			
			this.add(nick);
			this.add(area);
			this.add(campo);
			this.add(comboBox);
			this.add(btnEnviar);
			
			
			
		}
		
		
		public class enviarMensaje implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
					
					try {
						
						Socket conexionEnvio = new Socket("192.168.42.228", 9999);
						
						area.append(campo.getText());
						
						PaqueteDatos datosEnvio = new PaqueteDatos();
						datosEnvio.setIp(comboBox.getSelectedItem().toString());
						datosEnvio.setNick(nick.getText());
						datosEnvio.setMensaje(campo.getText());
						
						
						ObjectOutputStream flujoSalida = new ObjectOutputStream(conexionEnvio.getOutputStream());
						
						flujoSalida.writeObject(datosEnvio);
						
						flujoSalida.close();
						conexionEnvio.close();
						
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				
				
			}
			
		}


		
	}
	
	JComboBox<String> comboBox;
	JTextField campo;
	JTextArea area;
	JButton btnEnviar;
	JLabel nick;
	
	
	
	@Override
	public void run() {
		
		try {
			
			ServerSocket serverCliente = new ServerSocket(9090);
			
			Socket conexionServer;
			PaqueteDatos datosRecibidos;
			
			while(true) {
				
				conexionServer = serverCliente.accept();
				
				ObjectInputStream flujoEntrada = new ObjectInputStream(conexionServer.getInputStream());
				
				datosRecibidos = (PaqueteDatos) flujoEntrada.readObject();
				
				
				
				
				if(!datosRecibidos.getMensaje().equals("online")) {
					area.append("\n" + datosRecibidos.getNick() + ": "+ datosRecibidos.getMensaje());
					area.append("\n ----------------------");
				}else {
					
					ArrayList<String> IpsConectados = new ArrayList<String>();
					
					IpsConectados = datosRecibidos.getIps();
					
					comboBox.removeAllItems();
					
					for(String z: IpsConectados){
						comboBox.addItem(z);
					}
					
//					areaTextos.append("\n"+ recibido.getIps());
				}
				
				
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
}


