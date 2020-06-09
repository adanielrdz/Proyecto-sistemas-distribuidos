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
	Datos datos;
	
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
	protected void enviarDatos(String ip, int port) throws IOException
	{
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
	
	//METODO QUE EJECUTA LA CONEXION 
	protected void ejecutarConexion()
	{
		
		
		
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
