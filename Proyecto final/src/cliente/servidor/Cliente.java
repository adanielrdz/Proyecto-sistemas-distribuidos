package cliente.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class Cliente extends JFrame implements ActionListener
{
	Datos datos;
	ArrayList<String> direcciones;
	//OBJETOS DE NETWOEKING
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket s = null;
	//OBJETOS DE JFRANE
	private JLabel txtPuerto, txtIPusuario;
	private JPanel contentPane;
	private JFrame frameInterfaz;
	private JTextField txtIPdestino;
	private JTextField txtRAM;
	private JTextField txtProcesador;
	private JTextField txtSO;
	private JTextField txtDD;
	private JTextField txtDDlibre;
	private JTextField txtRAMlibre;
	private JTextField txtCPUlibre;
	private JTextField txtVelProcesador;
	private JTextField txtUsuario;
	private JButton btnCargar ,btnParar;
	private JButton btnEmpezar;
	private Boolean activado=false;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String ipLocal="25.24.184.239";	//IP propia de Hamachi
	private String cliente = "Daniel";			//Nombre propio
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String daniel = "25.0.122.89";
	private String cesar = "25.24.184.239";
	private String erik = "25.18.90.103";
	private String jose = "25.11.6.101";
	private String ivan = "25.12.252.241";
	private ServerSocket ss;
	private boolean flagCargar;
	/*
	public static void main(String[]args) throws SigarException, IOException
	{
		//Cliente c=new Cliente();
		//c.interfazCliente();
	}
	*/
	//INTERFAZ DEL CLIENTE 
	public JTextField getTxtIp() {
		return txtIPdestino;
	}
	protected JFrame interfazCliente()
	{
		setTitle("SDP: Cliente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 332);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 11, 476, 277);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panelDatosEstaticos = new JPanel();
		panelDatosEstaticos.setBounds(5, 35, 466, 130);
		panel_2.add(panelDatosEstaticos);
		panelDatosEstaticos.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDatosEstaticos.setLayout(null);
		
		JLabel jlRAM = new JLabel("RAM:");
		jlRAM.setBounds(10, 42, 90, 14);
		panelDatosEstaticos.add(jlRAM);
		
		JLabel jlProcesador = new JLabel("Procesador:");
		jlProcesador.setBounds(10, 73, 90, 14);
		panelDatosEstaticos.add(jlProcesador);
		
		JLabel jlSO = new JLabel("Sistema operativo:");
		jlSO.setBounds(10, 11, 110, 14);
		panelDatosEstaticos.add(jlSO);
		
		JLabel jlDD = new JLabel("Disco duro:");
		jlDD.setBounds(10, 104, 90, 14);
		panelDatosEstaticos.add(jlDD);
		
		txtRAM = new JTextField();
		txtRAM.setEditable(false);
		txtRAM.setBounds(127, 39, 86, 20);
		panelDatosEstaticos.add(txtRAM);
		txtRAM.setColumns(10);
		
		txtProcesador = new JTextField();
		txtProcesador.setEditable(false);
		txtProcesador.setBounds(127, 70, 120, 20);
		panelDatosEstaticos.add(txtProcesador);
		txtProcesador.setColumns(10);
		
		txtSO = new JTextField();
		txtSO.setEditable(false);
		txtSO.setBounds(127, 8, 120, 20);
		panelDatosEstaticos.add(txtSO);
		txtSO.setColumns(10);
		
		txtDD = new JTextField();
		txtDD.setEditable(false);
		txtDD.setBounds(127, 101, 86, 20);
		panelDatosEstaticos.add(txtDD);
		txtDD.setColumns(10);
		
		txtVelProcesador = new JTextField();
		txtVelProcesador.setEditable(false);
		txtVelProcesador.setBounds(390, 70, 66, 20);
		panelDatosEstaticos.add(txtVelProcesador);
		txtVelProcesador.setColumns(10);
		
		JLabel jlVelProcesador = new JLabel("Velocidad procesador:");
		jlVelProcesador.setBounds(250, 73, 130, 14);
		panelDatosEstaticos.add(jlVelProcesador);
		
		btnCargar = new JButton("Cargar datos");
		btnCargar.setBounds(287, 97, 110, 23);
		btnCargar.addActionListener(this);
		panelDatosEstaticos.add(btnCargar);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(64, 11, 150, 20);
		panel_2.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel jlNombreUsuario = new JLabel("Usuario:");
		jlNombreUsuario.setBounds(10, 14, 60, 14);
		panel_2.add(jlNombreUsuario);
		
		JLabel jlIPusuario = new JLabel("IP usuario:");
		jlIPusuario.setBounds(284, 14, 60, 14);
		panel_2.add(jlIPusuario);
		
		JPanel panelDatosDinamicos = new JPanel();
		panelDatosDinamicos.setBounds(5, 167, 246, 105);
		panel_2.add(panelDatosDinamicos);
		panelDatosDinamicos.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDatosDinamicos.setLayout(null);
		
		JLabel jlRAMlibre = new JLabel("RAM libre:");
		jlRAMlibre.setBounds(10, 45, 80, 14);
		panelDatosDinamicos.add(jlRAMlibre);
		
		JLabel jlCPUlibre = new JLabel("CPU libre:");
		jlCPUlibre.setBounds(10, 14, 80, 14);
		panelDatosDinamicos.add(jlCPUlibre);
		
		JLabel jlDDlibre = new JLabel("Disco duro libre:");
		jlDDlibre.setBounds(10, 76, 92, 14);
		panelDatosDinamicos.add(jlDDlibre);
		
		txtDDlibre = new JTextField();
		txtDDlibre.setEditable(false);
		txtDDlibre.setBounds(112, 73, 124, 20);
		panelDatosDinamicos.add(txtDDlibre);
		txtDDlibre.setColumns(10);
		
		txtRAMlibre = new JTextField();
		txtRAMlibre.setEditable(false);
		txtRAMlibre.setBounds(112, 42, 86, 20);
		panelDatosDinamicos.add(txtRAMlibre);
		txtRAMlibre.setColumns(10);
		;
		txtCPUlibre = new JTextField();
		txtCPUlibre.setEditable(false);
		txtCPUlibre.setBounds(112, 11, 124, 20);
		panelDatosDinamicos.add(txtCPUlibre);
		txtCPUlibre.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(261, 176, 210, 58);
		panel_2.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel jlIPdestino = new JLabel("IP destino:");
		jlIPdestino.setBounds(24, 11, 60, 14);
		panel_1.add(jlIPdestino);
		
		JLabel jlPuerto = new JLabel("Puerto:");
		jlPuerto.setBounds(24, 33, 46, 14);
		panel_1.add(jlPuerto);
		
		txtIPdestino = new JTextField();
		txtIPdestino.setBounds(94, 11, 106, 14);
		panel_1.add(txtIPdestino);
		
		txtPuerto = new JLabel("4560");
		txtPuerto.setBounds(94, 33, 46, 14);
		panel_1.add(txtPuerto);
		
		btnParar = new JButton("Parar");
		btnParar.setBounds(271, 243, 89, 23);
		btnParar.addActionListener(this);
		panel_2.add(btnParar);
		
		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setBounds(370, 243, 89, 23);
		btnEmpezar.addActionListener(this);
		panel_2.add(btnEmpezar);
		
		txtIPusuario = new JLabel(ipLocal);
		txtIPusuario.setBounds(352, 14, 103, 14);
		panel_2.add(txtIPusuario);
		//
		//setVisible(true);
		return this;
		
	}
	
	//METODO QUE OBTIENE LOS DATOS
	protected Datos obtenerDatos() throws SigarException, UnknownHostException
	{
		//INSTANCIAS DE OBJETOS DE LA LIBRERIA SIGAR
		NumberFormat df = new DecimalFormat("#0.00");
		NumberFormat velocidad = new DecimalFormat("#0.000");
		Sigar sigar=new Sigar();
		Mem mem=sigar.getMem();
		CpuInfo cpu[]=sigar.getCpuInfoList();
		CpuInfo info=cpu[0];
		CpuPerc cpuPorcentaje=sigar.getCpuPerc();
		File drive = new File("C:\\");
		//VALORES ESTATICOS
		String modeloProcesador=info.getModel();
		String velocidadProcesador=velocidad.format((double)info.getMhz()/1000);
		String so=System.getProperty("os.name");
		String ram=(mem.getRam()/1000) + "";
		double discoTotal=drive.getTotalSpace()/1073741824;
		String disco= String.valueOf(discoTotal);
		//VALORES DINAMICOS
		String cpuLibre= df.format((100-cpuPorcentaje.getCombined()*100));//(100-cpuPorcentaje.getCombined()*100) + "";
		double division = (((mem.getRam()-(mem.getActualUsed()/(1024*1024)))/1000)*100)/Double.parseDouble(ram);
		String ramLibre= df.format(division);
		String discoLibre= df.format(((drive.getFreeSpace()/1073741824)*100)/discoTotal);//(((drive.getFreeSpace()/1073741824)*100)/discoTotal) + "";
	//	String puntos=String.valueOf(algoritmoRankeo(datos));
		//AGREGAMOS LOS DATOS AL CONSTRUCTOR
		datos = new Datos(cliente, modeloProcesador,velocidadProcesador,so,ram,disco,cpuLibre,ramLibre,discoLibre);
		return datos;
	}
	
	//METODO QUE ENVIA LOS DATOS POR EL SOCKET
	protected void enviarDatos(String ip, int port) throws IOException, SigarException
	{
		obtenerDatos();
		System.out.println("Se entr� a: enviar datos");
		try{
			System.out.println("Antes de cargar el socket");
			//INSTANCIO EL SOCKET CON LA IP Y PUERTO
			s=new Socket(ip,port);
			System.out.println("socket cargado");
			oos = new ObjectOutputStream(s.getOutputStream());
			////ENVIO DE DATOS AL SERVIDOR
			System.out.println("Cargando...");
			oos.writeObject(datos);
			System.out.println("Cargado...");
			System.out.println("Datos enviados");
		}catch(Exception ex){
			//ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}
	//METODO QUE CIERRA LA CONEXION 
	protected void cerrarConexion() throws IOException
	{
		if(oos != null)oos.close();
		if(s!=null)s.close();
		System.out.println("Conexion cerrada");
	}

	//METODO RECIBIR PUNTU
	protected void recibirDatos()
	{
		ois = null;
		s = null;
        try {
			ss = new ServerSocket(4066);
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
                	Thread.sleep(3000);
                    s = ss.accept();
                    ois = new ObjectInputStream(s.getInputStream());
                    
                    // leo la ip enviada desde la difusion del nuevo servidor
                    // y la asigno al text field para que se tome de ahí cuando se cree
                    // un nuevo socket.
                	txtIPdestino.setText((String)ois.readObject());
                   
                	
                    } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
                }
            }
        });
		
        hilo.start();
	}

	//METODO PARA LAS ACCIONES DE LOS BOTONES
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==btnEmpezar)
		{
			Thread hilo=new Thread(new Runnable() {

				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					activado=true;
					while(activado)
					{
						try {
							enviarDatos(txtIPdestino.getText(),Integer.parseInt(txtPuerto.getText()));
						} catch (IOException e1) {
							e1.printStackTrace();
							System.out.println("IOException: " + e1.getMessage());
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
							System.out.println("NumberFormatException: " + e1.getMessage());
						} catch (SigarException e1) {
							e1.printStackTrace();
							System.out.println("SigarException: " + e1.getMessage());
						}
					}
				}
		
			});
			 hilo.start();
		}
		
		if(e.getSource()==btnCargar)
		{
			try {
				obtenerDatos();
			} catch (UnknownHostException | SigarException e1) {
				e1.printStackTrace();
				System.out.println("Exception: " + e1.getMessage());
			}
			txtSO.setText(datos.getSo().toString());
			txtRAM.setText(datos.getRam().toString() + " GB");
			txtProcesador.setText(datos.getModeloProcesador().toString());
			txtVelProcesador.setText(datos.getVelocidadProcesador().toString() + " GHz");
			txtDD.setText(datos.getDisco().toString() + " GB");
			txtCPUlibre.setText(datos.getCpuLibre().toString() + " %");
			txtRAMlibre.setText(datos.getRamLibre().toString() + " %");
			txtDDlibre.setText(datos.getDiscoLibre().toString() + " %");
		}
		if(e.getSource()==btnParar) {
			activado=false;
			Thread hilo=new Thread(new Runnable() 
			{
				public void run() 
				{
					try 
					{
						cerrarConexion();
					}catch(Exception e1) {
					System.out.println("Exception: " + e1.getMessage());
					}		
				}
				
			});
		}
		
	}
	
}