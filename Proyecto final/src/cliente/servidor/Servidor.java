package cliente.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class Servidor extends JFrame implements ActionListener
{
	private int port;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket s;
	private ServerSocket ss;

	public static void main(String[]args)
	{
		
	}
	
	//INTERFAZ DEL SERVER
	protected void interfazServidor()
	{
		
	}
	
	//METODO PARA LEVANTAR LA CONEXION ENTRE EL CLIENTE Y SERVIDOR 
	protected void levantarConexion(int port)
	{
		
	}
	//METODO PARA RECIBIR LOS DATOS
	protected void recibirDatos(Datos datos)
	{
		
	}
	
	
	//METODO PARA EL ALGORITMO DE RANKEO
	protected int algoritmoRankeo(Datos datos)
	{
		int rank=0;
		return rank; 
	}

	///METODO PARA CERRAR LA CONEXION 
	protected void cerrarConexion()
	{
		
	}

	
	//METODO PARA LOS ACCIONES DE LOS BOTONOES DE LA INTERFAZ
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
