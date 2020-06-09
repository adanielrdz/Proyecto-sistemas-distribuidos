package cliente.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class Cliente extends JFrame implements ActionListener
{
	 static //OBJETOS
	Datos datos;
	ArrayList<String> direcciones;
	//OBJETOS DE NETWOEKING
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket s=null;
	//OBJETOS DE JFRAME

	//OBJETOS DE JFRANE
	private JPanel contentPane;
	private JTextField txtIP;
	private JTextField txtPuerto;
	private JButton btnConectar;
	private JButton btnEnviar;
	private JButton btnObtener;
	private JLabel lblDatos;
	private JLabel lblPuerto;
	private JLabel lblIp;
	public static void main(String[]args) throws SigarException, IOException
	{
		Cliente c=new Cliente();
		c.interfazCliente();
		
	}
	//INTERFAZ DEL CLIENTE 
	protected void interfazCliente()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 592, 347);
		setTitle("Cliente");
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//INSTANCIAR OBJETOS DE JLABEL
		lblIp = new JLabel("IP del servidor");
		lblIp.setBounds(15, 37, 115, 20);
		contentPane.add(lblIp);
		
		lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(318, 37, 69, 20);
		contentPane.add(lblPuerto);
		
		lblDatos = new JLabel("Informacion del sistema");
		lblDatos.setBounds(15, 86, 533, 100);
		contentPane.add(lblDatos);

		//INSTANCIAR OBJETOS DE JTEXTFIELD
		txtIP = new JTextField();
		txtIP.setBounds(145, 34, 146, 26);
		contentPane.add(txtIP);
		txtIP.setColumns(10);

		txtPuerto = new JTextField();
		txtPuerto.setBounds(402, 34, 146, 26);
		contentPane.add(txtPuerto);
		txtPuerto.setColumns(10);
		
		//INSTANCIAR OBJETOS JBUTTON 
		btnConectar = new JButton("Conectar");
		btnConectar.setBounds(26, 246, 115, 29);
		btnConectar.addActionListener(this);
		contentPane.add(btnConectar);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(414, 246, 115, 29);
		btnEnviar.addActionListener(this);
		contentPane.add(btnEnviar);

		btnObtener = new JButton("Obtener Informacion");
		btnObtener.setBounds(187, 203, 196, 29);
		btnObtener.addActionListener(this);
		contentPane.add(btnObtener);
		
	}
	
	//METODO QUE OBTIENE LOS DATOS
	protected Datos obtenerDatos() throws SigarException, UnknownHostException
	{
		//OBETNER HOSTNAME 
		InetAddress addr = InetAddress.getByName("25.0.122.89");
		String usuario ="Daniel";
		//INSTANCIAS DE OBJETOS DE LA LIBRERIA SIGAR
		Sigar sigar=new Sigar();
		Mem mem=sigar.getMem();
		CpuInfo cpu[]=sigar.getCpuInfoList();
		CpuInfo info=cpu[0];
		CpuPerc cpuPorcentaje=sigar.getCpuPerc();
		File drive = new File("C:\\");
		//VALORES ESTATICOS
		String modeloProcesador=info.getModel();
		String velocidadProcesador=info.getMhz()+"";
		String so=System.getProperty("os.name");
		String ram=(mem.getRam()/1000) + "";
		double discoTotal=drive.getTotalSpace()/1073741824;
		String disco=String.valueOf(discoTotal);
		//VALORES DINAMICOS
		String cpuLibre= (100-cpuPorcentaje.getCombined()*100) + "";
		double division = (((mem.getRam()-(mem.getActualUsed()/(1024*1024)))/1000)*100)/Double.parseDouble(ram);
		String ramLibre= division + "";
		String discoLibre=(((drive.getFreeSpace()/1073741824)*100)/discoTotal) + "";
		//AGREGAMOS LOS DATOS AL CONSTRUCTOR
		datos = new Datos(usuario, modeloProcesador,velocidadProcesador,so,ram,disco,cpuLibre,ramLibre,discoLibre);
		return datos;
	}
	//METODO QUE ENVIA LOS DATOS POR EL SOCKET
	protected void enviarDatos(String ip, int port) throws IOException, SigarException
	{
		obtenerDatos();
		System.out.println("Se entró a: enviar datos");
		try{
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
		finally{
			if(oos != null)oos.close();
			if(s!=null)s.close();
			System.out.println("Conexion cerrada");
		}
	}
	//METODO QUE CIERRA LA CONEXION 
	
	
	protected void cerrarConexion()
	{
		
	}
	
	//METODO QUE ALAMACENA TODAS LAS DIRECCIONES IP	
	protected ArrayList<String> direccionesIP()
	{
		direcciones=new ArrayList<String>();
		String daniel="25.0.122.89";
		direcciones.add(daniel);
		return direcciones;
	}
	
	//ALGORITMO DE RANKEO
	protected int algoritmoRankeo(Datos dato)
	{
		int pts=0;
		// serie de cpu intel y AMD (mejorable)
		if(dato.getModeloProcesador().contains("i7")) {
			pts+=1000;
		} else if(dato.getModeloProcesador().contains("i5")) {
			pts+=800;
		} else if(dato.getModeloProcesador().contains("i3")) {
			pts+=600;
		} else if(dato.getModeloProcesador().contains("Pentium")) {
			pts+=400;
		} else if(dato.getModeloProcesador().contains("Celeron")) {
			pts+=200;
		} else if(dato.getModeloProcesador().contains("A8")) {
			pts+=300;
		}
		
		// velocidad de cpu
		int cpufreq = Integer.parseInt(dato.getVelocidadProcesador());
		if(cpufreq >= 3200) {
			pts+=1100;
		}else if(cpufreq < 3200 && cpufreq >= 2400) {
			pts+=900;
		} else if(cpufreq < 2400 && cpufreq >= 1900) {
			pts+=700;
		} else if(cpufreq < 1900 && cpufreq >= 1400) {
			pts=500;
		}else {
			pts+=200;
		}
		
		// RAM total
		int mem = Integer.parseInt(dato.getRam());
		// 12 GB o mas
		if(mem >= 12288) {
			pts+=700;
		}
		// Entre 8GB y 12 GB (menor a 12GB)
		else if(mem < 12288 && mem >= 8192) {
			pts+=500;
		} 
		// Entre 4GB y 8GB (menor a 8GB)
		else if(mem < 8192 && mem >= 4096) {
			pts+=250;
		}
		else {
			pts+=100;
		}
		
		//CPU libre %
		double cpuLibre =Double.parseDouble(dato.getCpuLibre());
		if(cpuLibre >= 90) {
			pts+=1500;
		} else if(cpuLibre < 90 && cpuLibre >= 80) {
			pts+=1350;
		} else if(cpuLibre < 80 && cpuLibre >= 70) {
			pts+=1050;
		} else if(cpuLibre < 70 && cpuLibre >= 60) {
			pts+=700;
		} else if(cpuLibre < 60 && cpuLibre >= 50) {
			pts+=550;
		} else {
			pts+=300;
		}
		
		// RAM libre %
		double ramLibre =Double.parseDouble(dato.getRamLibre());
		if(ramLibre >= 90) {
			pts+=1400;
		} else if(ramLibre < 90 && ramLibre >= 80) {
			pts+=1250;
		} else if(ramLibre < 80 && ramLibre >= 70) {
			pts+=950;
		} else if(ramLibre < 70 && ramLibre >= 60) {
			pts+=700;
		} else if(ramLibre < 60 && ramLibre >= 50) {
			pts+=550;
		} else {
			pts+=300;
		}
		
		// disco libre %
		double discoLibre = Double.parseDouble(dato.getDiscoLibre());
		if(discoLibre >= 90) {
			pts+=1400;
		} else if(discoLibre < 90 && discoLibre >= 80) {
			pts+=1250;
		} else if(discoLibre < 80 && discoLibre >= 70) {
			pts+=950;
		} else if(discoLibre < 70 && discoLibre >= 60) {
			pts+=700;
		} else if(discoLibre < 60 && discoLibre >= 50) {
			pts+=550;
		} else {
			pts+=300;
		}
		
		return pts;
	}
	
	//METODO PARA LAS ACCIONES DE LOS BOTONES
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==btnEnviar)
		{
			try {
				enviarDatos(txtIP.getText(),Integer.parseInt(txtPuerto.getText()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SigarException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(e.getSource()==btnObtener)
		{
			try {
				obtenerDatos();
			} catch (UnknownHostException | SigarException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			lblDatos.setText(datos.toString());
			
		}
		
	}
	
	
}
