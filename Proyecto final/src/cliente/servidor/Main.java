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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame implements ActionListener
{
	///ESCRITO BY DANIEL
	private JPanel contentPane;
	private Socket s, s2;
	private ServerSocket ss, ss2;
	private ObjectInputStream entrada;	
	private Servidor server = null;
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
		main.interfaz().setVisible(true);;
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
		while (true) 
		{
          try 
          {
        	  // se ha recibido la alerta de que es el nuevo servidor (esta maquina)
            s = ss.accept();
            entrada = new ObjectInputStream(s.getInputStream());
            String ip = (String)entrada.readObject();
            // si esta maquina ya es el servidor, no hace nada
            // si no, instancia el servidor y lo pone visible
            String iplocal=obtenerIPLocal();
          	if(iplocal.equals(ip))
          	{
          		System.out.println("Main -> Tu eres el que tiene el mejor rank");
          		if(server == null) 
          		{
          			System.out.println("Main -> Iniciando servidor...");
                	server = new Servidor();
                	server.interfazServidor().setVisible(true);
                }
          	}
      
            } catch (SocketException e) {
                System.err.println("Error -> " + e.getMessage());
            }
        
            // le aviso a todos quien es el nuevo servidor
            //enviarDifusionIp(ip);
            } 
      }
      
	//OBTIENE DIRECCION IP
	protected String obtenerIPLocal()
	{
		String direccionIP="";
		  try {
	            Enumeration<NetworkInterface> interfaces = 
	                    NetworkInterface.getNetworkInterfaces();
	            
	            while(interfaces.hasMoreElements()){
	                
	                NetworkInterface interfaz = interfaces.nextElement();
	                Enumeration<InetAddress> direcciones = interfaz.getInetAddresses();
	                
	                while(direcciones.hasMoreElements()){
	                    
	                    InetAddress direccion = direcciones.nextElement();
	                    
	                    if (direccion instanceof Inet4Address
	                            && !direccion.isLoopbackAddress())
	                    {
	                    	if(direccion.toString().contains("/25"))
	                     	{
	                     		direccionIP=direccion.toString();
	                     	}
	                    }
	                       
	                    }
	                }
	            
	        } catch (SocketException e) {
	            System.err.println("Error -> " + e.getMessage());
	        }
		return direccionIP;
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
	
	/*
	/* Este metodo envia a todos la nueva direccion 
	private void enviarDifusionIp(String nuevaIpServidor) {
		// enviar 5 veces la direccion (son 5 hosts)
		for(int i=0; i<5; i++) {
			try{
				s2 = new Socket(direcciones[i],4060);
				oos2 = new ObjectOutputStream(s2.getOutputStream());
				oos2.writeObject(nuevaIpServidor);
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}
	}
	*/
	
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
			main.interfaz().setVisible(false);
		}
		
	}
	
	

}