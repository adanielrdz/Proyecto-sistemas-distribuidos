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
	private int port=4560;
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;
	private Socket s=null;
	private ServerSocket ss;

	public static void main(String[]args)
	{
		Servidor server=new Servidor();
		server.ejecutarConexion();
		
	}
	
	//INTERFAZ DEL SERVER
	protected void interfazServidor()
	{
		
	}
	
	//METODO PARA LEVANTAR LA CONEXION ENTRE EL CLIENTE Y SERVIDOR 
	
	protected void ejecutarConexion()
	{
		ois = null;
		oos = null;
		s = null;
			
		Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) 
                {
                    try 
                    {
                    	ss = new ServerSocket(port);
                    	System.out.println("Esperando conexión entrante en el puerto " + String.valueOf(port) + "...");
                    	s = ss.accept();
                    	System.out.println("Conexión establecida con: " + s.getInetAddress().getHostName() + "\n\n\n");
                    	ois = new ObjectInputStream(s.getInputStream());
                	    Datos data = (Datos)ois.readObject();
                		System.out.println("Leyendo el sistema operativo.....");
                		System.out.println(data.getSo());
                        
                    } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
            System.out.println("Conexion cerrada....");
            System.exit(0);

        }
	}

	
	//METODO PARA LOS ACCIONES DE LOS BOTONOES DE LA INTERFAZ
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
