package cliente.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class Servidor extends JFrame implements ActionListener
{
	private static int port=4090;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket s=null;
	private ServerSocket ss;

	public static void main(String[]args)
	{
		Servidor server=new Servidor();
		server.levantarConexion(port);
		server.ejecutarConexion();
		
	}
	
	//INTERFAZ DEL SERVER
	protected void interfazServidor()
	{
		
	}
	
	//METODO PARA LEVANTAR LA CONEXION ENTRE EL CLIENTE Y SERVIDOR 
	protected void levantarConexion(int port)
	{
		try 
		{
        	ss = new ServerSocket(port);
        	System.out.println("Esperando conexion del puerto: "+port);
        	s = ss.accept();
        //	System.out.println("Conexión establecida con: " + s.getInetAddress().getHostName() + "\n\n\n");
		} catch (Exception e) 
		{
        	System.out.println("Error en levantarConexion(): " + e.getMessage());
        	System.exit(0);
		}
		
	}
	//METODO PARA RECIBIR LOS DATOS
	protected void recibirDatos() throws IOException, ClassNotFoundException
	{
		ois = new ObjectInputStream(s.getInputStream());
		oos = new ObjectOutputStream(s.getOutputStream());
		Datos data = (Datos)ois.readObject();
		System.out.println("Leyendo el sistema operativo.....");
		System.out.println(data.getSo());
	}
	
	protected void ejecutarConexion()
	{
		Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        levantarConexion(port);
                        try {
							recibirDatos();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("No se encontro la clase");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
                    } finally {
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
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
		try {
            ois.close();
            oos.close();
            s.close();
        } catch (IOException e) {
        } finally {
            System.out.println("Conversación finalizada....");
            System.exit(0);

        }
	}

	
	//METODO PARA LOS ACCIONES DE LOS BOTONOES DE LA INTERFAZ
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
