package cliente.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;

public class Main extends JFrame implements ActionListener
{

	private Socket s;
	private ServerSocket ss;
	private ObjectInputStream ois;
	
	private ObjectOutputStream oos;
	
	private Socket socket2;
	
	public static void main(String[]args)
	{
		Cliente c=new Cliente();
		c.interfazCliente().setVisible(true);
		
		Servidor server=new Servidor();
		server.interfazServidor().setVisible(true);
		
		Main m = new Main();
		m.recibirSenal();
		
		
	}
	
	public void recibirSenal() {
		ois = null;
		s = null;
        try {
			ss = new ServerSocket(4066);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String ipMasAlto;
		while (true) 
        {
          try 
           {
        	
            s = ss.accept();
            
           
            ois = new ObjectInputStream(s.getInputStream());
            ipMasAlto = (String)ois.readObject();
        	enviarSenal(ipMasAlto, 4066);
            } catch (Exception e) {
            	
            }
        }
		
		
	}
	
	private void enviarSenal(String ip, int port) {
		try{
			System.out.println("Antes de cargar el socket");
			//INSTANCIO EL SOCKET CON LA IP Y PUERTO
			s=new Socket(ip,port);
			System.out.println("socket cargado");
			oos = new ObjectOutputStream(s.getOutputStream());
			////ENVIO DE DATOS AL SERVIDOR
			
			oos.writeObject("eres el server");
			
			System.out.println("Cargado...");
			System.out.println("Datos enviados");
		}catch(Exception ex){
			//ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}
	
	//////
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		
		
	}
	
	

}
