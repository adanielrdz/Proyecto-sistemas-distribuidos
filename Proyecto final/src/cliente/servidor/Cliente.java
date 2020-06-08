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

import javax.swing.JFrame;

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
	
	public static void main(String[]args) throws SigarException, IOException
	{
		Cliente c=new Cliente();
		c.obtenerDatos();
		c.enviarDatos();
		
	}
	//INTERFAZ DEL CLIENTE 
	protected void interfazCliente()
	{
		
	}
	
	//METODO QUE OBTIENE LOS DATOS
	protected Datos obtenerDatos() throws SigarException, UnknownHostException
	{
		//OBETNER HOSTNAME 
	//	InetAddress addr = InetAddress.getByName("25.0.122.89");
		String usuario ="";
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
	protected void enviarDatos() throws IOException
	{
		System.out.println("Se entró a: enviar datos");
		try{
			//INSTANCIO EL SOCKET CON LA IP Y PUERTO
			s=new Socket("localhost",4090);
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
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	
}
