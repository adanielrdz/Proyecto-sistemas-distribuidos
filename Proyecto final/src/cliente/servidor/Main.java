package cliente.servidor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame implements ActionListener
{
	private JPanel contentPane;
	private Socket s;
	private ServerSocket ss;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;	
	private Socket socket2;
	private Servidor server = null;
	JButton btnEmpezar;
	private String[] direcciones = {
			"25.0.122.89",
			"25.24.184.239",
			"25.18.90.103",
			"25.11.6.101",
			"25.12.252.241"
	};
	
	
	
	public static void main(String[]args)
	{
	//	Cliente c=new Cliente();
	//	c.interfazCliente().setVisible(true);
		
		Main m=new Main();
		m.interfaz();
		Cliente c=new Cliente();
		c.interfazCliente().setVisible(true);
		
	}
	
	/*
	 * Espera recibir la señal de que es el nuevo servidor
	 */
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
        	
        	  // se ha recibido la alerta de que es el nuevo servidor (esta maquina)
            s = ss.accept();
            
            ois = new ObjectInputStream(s.getInputStream());
            String ip = (String)ois.readObject();
            
            // si esta maquina ya es el servidor, no hace nada
            // si no, instancia el servidor y lo pone visible
            if(server == null) {
            	server = new Servidor();
            	server.interfazServidor().setVisible(true);
            	
            }
            
            // le aviso a todos quien es el nuevo servidor
            enviarDifusionIp(ip);
        	
            } catch (Exception e) {
            	
            }
        }
		
		
	}
	private void interfaz()
	{
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setVisible(true);
		btnEmpezar = new JButton("Empezar!!!");
		btnEmpezar.addActionListener(this);
		contentPane.add(btnEmpezar, BorderLayout.CENTER);
	}
	/* Este metodo envia a todos la nueva direccion */
	private void enviarDifusionIp(String nuevaIpServidor) {
		
		// enviar 5 veces la direccion (son 5 hosts)
		for(int i=0; i<5; i++) {
			try{
				s=new Socket(direcciones[i],4466);
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(nuevaIpServidor);
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}
	}
	
	
	/*
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
	*/
	
	//////
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource()==btnEmpezar)
		{
			Servidor server=new Servidor();
			server.interfazServidor().setVisible(true);
		}
		
	}
	
	

}
