package cliente.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;

public class Main extends JFrame implements ActionListener
{

	public static void main(String[]args)
	{
		Cliente c=new Cliente();
		c.interfazCliente().setVisible(true);
		Servidor server=new Servidor();
		server.interfazServidor().setVisible(true);
		
		
	}
	
	//////
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		
		
	}

}
