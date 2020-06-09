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
		int pts=0;
		// serie de cpu intel y AMD (mejorable)
		if(datos.getModeloProcesador().contains("i7")) {
			pts+=1000;
		} else if(datos.getModeloProcesador().contains("i5")) {
			pts+=800;
		} else if(datos.getModeloProcesador().contains("i3")) {
			pts+=600;
		} else if(datos.getModeloProcesador().contains("Pentium")) {
			pts+=400;
		} else if(datos.getModeloProcesador().contains("Celeron")) {
			pts+=200;
		} else if(datos.getModeloProcesador().contains("A8")) {
			pts+=300;
		}
		
		// velocidad de cpu
		int cpufreq = Integer.parseInt(datos.getVelocidadProcesador());
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
		int mem = Integer.parseInt(datos.getRam());
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
		double cpuLibre =Double.parseDouble(datos.getCpuLibre());
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
		double ramLibre =Double.parseDouble(datos.getRamLibre());
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
		double discoLibre = Double.parseDouble(datos.getDiscoLibre());
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
		
		/*
		//TABLA Y MODEO DE RANKEO
		String puntos=String.valueOf(algoritmoRankeo(datos));
		String[] dataRank= {puntos};
		modeloRank.addRow(dataRank);
		*/
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
