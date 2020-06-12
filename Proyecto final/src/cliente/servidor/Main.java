package cliente.servidor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
//***///
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame implements ActionListener
{
	///ESCRITO BY DANIEL
	private JPanel contentPane;
	private Socket s, s2;
	private ServerSocket ss, ss2;
	private DataInputStream entrada;	
	private static Servidor server = null;
	JButton btnEmpezar;
	static Main main;
	private String[] direcciones = {
			"25.0.122.89",
			"25.24.184.239",
			"25.18.90.103",
			"25.11.6.101",
			"25.12.252.241"
	};
	
	public static void main(String[]args) throws ClassNotFoundException, IOException, InterruptedException
	{
		Cliente c=new Cliente();
		c.interfazCliente().setVisible(true);	
		main=new Main();
		main.interfaz().setVisible(true);
		server = new Servidor();
		main.recibirSenal();
		
	}
		
	public void recibirSenal() throws IOException, ClassNotFoundException, InterruptedException {
		entrada = null;
		s = null;
		ss=null;
        try {
			ss = new ServerSocket(4897);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Thread hilo = new Thread(new Runnable() {
        	@Override
        	public void run() {
        		while (true) 
        		{
                  try 
                  {
                	  // se ha recibido la alerta de que es el nuevo servidor (esta maquina)
                    s = ss.accept();
                    entrada = new DataInputStream(s.getInputStream());
                    String ip = entrada.readUTF();
                    // si esta maquina ya es el servidor, no hace nada
                    // si no, instancia el servidor y lo pone visible
                    

                  	System.out.println("Main -> Tu eres el que tiene el mejor rank");
                  	
                  	if(!server.seVeVentana()) {
                  		System.out.println("Main -> Iniciando servidor...");
                  		JOptionPane.showMessageDialog(null, "Tu eres el que tiene el mejor rank");
                      	server.verVentana(true);
                  	}
                  } catch (IOException  e) {
                        System.err.println("Error -> " + e.getMessage());
                  }
                
                    } 
        	}
        	
        });
        hilo.start();
		
      }
      
	
	
	private JFrame interfaz()
	{
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		btnEmpezar = new JButton("Empezar!!!");
		btnEmpezar.addActionListener(this);
		contentPane.add(btnEmpezar, BorderLayout.CENTER);
		return this;
	}
	

	
	//////
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource()==btnEmpezar)
		{
			server=new Servidor();
			main.interfaz().setVisible(false);
		}
		
	}
	
	

}