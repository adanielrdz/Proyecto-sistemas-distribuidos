package cliente.servidor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Servidor extends JFrame implements ActionListener
{
	//4560
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;
	private Socket s=null;
	private ServerSocket ss;
	
	//OBJETOS DEL JFRAME 
	private JPanel contentPane;
	private JTable tablaClientes;
	private JTable tablaDatos;
	private JTable tablaRank;
	private DefaultTableModel modeloDatos=new DefaultTableModel();//MODELO DE LA TABLA DATOS
	private DefaultTableModel modeloClientes=new DefaultTableModel();//MODELO DE LA TABLA CLIENTES
	private DefaultTableModel modeloRank=new DefaultTableModel();
	private JButton btnCerrar;
	private JButton btnEjecutar;
	private JTextField txtPort;
	public static void main(String[]args)
	{
		Servidor server=new Servidor();
		server.interfazServidor();
		
	}
	
	//INTERFAZ DEL SERVER
	protected void interfazServidor()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1216, 513);
		setVisible(true);
		setTitle("Servidor");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnCerrar = new JButton("Cerrar conexi\u00F3n");
		btnCerrar.setBounds(15, 389, 164, 35);
		btnCerrar.addActionListener(this);
		contentPane.add(btnCerrar);
		
		btnEjecutar = new JButton("Levantar conexion");
		btnEjecutar.setBounds(1015, 386, 164, 41);
		btnEjecutar.addActionListener(this);
		contentPane.add(btnEjecutar);
		
		tablaClientes = new JTable();
		tablaClientes.setModel(modeloClientes);
		contentPane.add(tablaClientes);
		
		tablaDatos = new JTable();
		tablaDatos.setModel(modeloDatos);
		contentPane.add(tablaDatos);
		
		tablaRank=new JTable();
		tablaRank.setModel(modeloRank);
		contentPane.add(tablaRank);
		
		JScrollPane scrollClientes = new JScrollPane(tablaClientes);
		scrollClientes .setBounds(15, 59, 164, 121);
		contentPane.add(scrollClientes );
		
		JScrollPane scrollDatos = new JScrollPane(tablaDatos);
		scrollDatos.setBounds(194, 59, 985, 121);
		contentPane.add(scrollDatos);

		JScrollPane scrollRank = new JScrollPane(tablaRank);
		scrollRank.setBounds(209, 232, 824, 121);
		contentPane.add(scrollRank);
		
		txtPort = new JTextField();
		txtPort.setBounds(80, 20, 146, 26);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(15, 23, 69, 20);
		contentPane.add(lblPuerto);
		
		JLabel lblRankeo = new JLabel("Rankeo");
		lblRankeo.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblRankeo.setBounds(552, 183, 98, 41);
		contentPane.add(lblRankeo);
		
		encabezadoTablas();
		
		
	}
	
	//METODO PARA LEVANTAR LA CONEXION ENTRE EL CLIENTE Y SERVIDOR 
	
	protected void ejecutarConexion(int port)
	{
		ois = null;
		oos = null;
		s = null;
        try {
			ss = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Thread hilo = new Thread(new Runnable() {
            @Override
           public void run() {
             while (true) 
                {
                  try 
                   {

                    System.out.println("Esperando conexión entrante en el puerto " + String.valueOf(port) + "...");
                    s = ss.accept();
                    System.out.println("Conexión establecida con: " + s.getInetAddress().getHostName() + "\n\n\n");
                    ois = new ObjectInputStream(s.getInputStream());
                	Datos data = (Datos)ois.readObject();
                	cargarDatos(data);    
                    } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
	
	//METODO PARA CARGAR EL MODELO DE LA TABLA Y QUE SEA DINAMICA
	protected void cargarDatos(Datos datos)
	{
		String modeloProcesador=datos.getModeloProcesador();
		String velocidadProcesador=datos.getVelocidadProcesador();
		String so=datos.getSo();
		String ram=datos.getRam();
		String disco=datos.getDisco();
		// DATOS DINAMICOS
		String cpuLibre=datos.getCpuLibre();
		String ramLibre=datos.getRamLibre();
		String discoLibre=datos.getDiscoLibre();
		String[] data= {modeloProcesador,velocidadProcesador,so,ram,disco,cpuLibre,ramLibre,discoLibre};
		modeloDatos.addRow(data);
		
		String nomCliente=datos.getUsuario();
		String[] dataCliente= {nomCliente};
		modeloClientes.addRow(dataCliente);
		
	}
	
	protected void encabezadoTablas()
	{
		modeloDatos.addColumn("Modelo del procesador");
		modeloDatos.addColumn("Velocidad del procesador");
		modeloDatos.addColumn("Sistema operativo");
		modeloDatos.addColumn("Memoria RAM");
		modeloDatos.addColumn("Disco duro");
		modeloDatos.addColumn("CPU libre");
		modeloDatos.addColumn("RAM libre");
		modeloDatos.addColumn("Disco duro libre");
		modeloClientes.addColumn("Clientes");
		modeloRank.addColumn("Posición");
		modeloRank.addColumn("Clientes");
		modeloRank.addColumn("Puntos");
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
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getSource()==btnCerrar)
		{
			cerrarConexion();
			JOptionPane.showMessageDialog(null, "Se ha cerrado la conexion");
		}
		if(e.getSource()==btnEjecutar)
		{
			
		        	ejecutarConexion(Integer.parseInt(txtPort.getText()));
		      
		       
		  
		    System.out.println("Fue con exito");
		}
	}
}
