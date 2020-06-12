package cliente.servidor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

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

public class Servidor extends JFrame implements ActionListener {
	/* by danie */
	// 4560
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private DataOutputStream salida = null;
	private Socket s = null, s2 = null;
	private ServerSocket ss, ss2 = null;
	// OBJETOS DEL JFRAME
	private JFrame frameServidor;
	private JPanel contentPane;
	private JTable tablaDatos;
	private JTable tablaRank;
	private DefaultTableModel modeloDatos = new DefaultTableModel();// MODELO DE LA TABLA DATOS
	private DefaultTableModel modeloRank = new DefaultTableModel();
	private JButton btnCerrar;
	private JButton btnEjecutar;
	private JTextField txtPort;
	private JFrame interfaz;

	// puntos de cada cliente
	private int danielPts = 0;
	private int cesarPts = 0;
	private int erikPts = 0;
	private int josePts = 0;
	private int ivanPts = 0;

	private boolean soyElServidor = true;
	private boolean serverIniciado = false;

	private String[] direcciones = { "25.0.122.89",
			"25.24.184.239",
			"25.18.90.103",
			"25.11.6.101",
			"25.12.252.241"
	};
	
	private String daniel = "/25.0.122.89";
	private String cesar = "/25.24.184.239";
	private String erik = "/25.18.90.103";
	private String jose = "/25.11.6.101";
	private String ivan = "/25.12.252.241";

	private String[] nombres = { "Daniel", "Cesar", "Erik", "Jose", "Ivan" };

	int[] puntuaciones = { danielPts, cesarPts, erikPts, josePts, ivanPts };

	public int[] getPuntuaciones() {
		return puntuaciones;
	}

	HashMap<Integer, String> puntuacionesNombres = new HashMap<Integer, String>();
	HashMap<Integer, String> puntuacionesIp = new HashMap<Integer, String>();

	public HashMap<Integer, String> getPuntuacionesNombres() {
		return puntuacionesNombres;
	}

	public Servidor() {
		interfaz = interfazServidor();
		interfaz.setVisible(false);
		ejecutarConexion(Integer.parseInt(txtPort.getText()));
	}

	public void verVentana(boolean valor) {
		interfaz.setVisible(valor);
	}

	public boolean seVeVentana() {
		return interfaz.isVisible();
	}

	// INTERFAZ DEL SERVER
	protected JFrame interfazServidor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1216, 513);
		setTitle("Servidor");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnCerrar = new JButton("Cerrar conexi\u00F3n");
		btnCerrar.setBounds(15, 389, 164, 35);
		btnCerrar.addActionListener(this);
		contentPane.add(btnCerrar);


		tablaDatos = new JTable();
		tablaDatos.setModel(modeloDatos);
		contentPane.add(tablaDatos);

		tablaRank = new JTable();
		tablaRank.setModel(modeloRank);
		contentPane.add(tablaRank);

		JScrollPane scrollDatos = new JScrollPane(tablaDatos);
		scrollDatos.setBounds(102, 59, 985, 121);
		contentPane.add(scrollDatos);

		JScrollPane scrollRank = new JScrollPane(tablaRank);
		scrollRank.setBounds(209, 232, 824, 121);
		contentPane.add(scrollRank);

		txtPort = new JTextField("4560");
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

		// setVisible(true);

		encabezadoTablas();

		return this;
	}

	// METODO PARA LEVANTAR LA CONEXION ENTRE EL CLIENTE Y SERVIDOR
	protected void ejecutarConexion(int port) {
		ois = null;

		s = null;
		ss = null;
		try {
			ss = new ServerSocket(port);
			System.out.println("////Puerto de servidor abierto////");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				while (soyElServidor) {
					try {

						s = ss.accept();
						System.out.println("Servidor -> Conexion establecida con: " + s.getInetAddress() + "\n");
						ois = new ObjectInputStream(s.getInputStream());
						Datos data = (Datos) ois.readObject();
						cargarDatos(data, s.getInetAddress().toString());
						System.out.println("Servidor -> Se cargaron los datos");
					} catch (ClassNotFoundException | IOException | InterruptedException e) {
						e.printStackTrace();
						System.out.println("Servidor -> !Error: " + e.getMessage());
					} finally {
						if (s != null) {
							try {
								s.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (ois != null) {
							try {
								ois.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		hilo.start();
	}

	// METODO PARA EL ALGORITMO DE RANKEO
	protected int algoritmoRankeo(Datos datos) {
		int pts = 0;
		// serie de cpu intel y AMD (mejorable)
		if (datos.getModeloProcesador().contains("i7")) {
			pts += 600;
		} else if (datos.getModeloProcesador().contains("i5")) {
			pts += 500;
		} else if (datos.getModeloProcesador().contains("i3")) {
			pts += 400;
		} else if (datos.getModeloProcesador().contains("Pentium")) {
			pts += 300;
		} else if (datos.getModeloProcesador().contains("Celeron")) {
			pts += 100;
		} else if (datos.getModeloProcesador().contains("A8")) {
			pts += 200;
		}

		// velocidad de cpu
		double cpufreq = Double.parseDouble(datos.getVelocidadProcesador());
		if (cpufreq >= 3200) {
			pts += 500;
		} else if (cpufreq < 3200 && cpufreq >= 2400) {
			pts += 400;
		} else if (cpufreq < 2400 && cpufreq >= 1900) {
			pts += 300;
		} else if (cpufreq < 1900 && cpufreq >= 1400) {
			pts = 200;
		} else {
			pts += 100;
		}

		// RAM total
		int mem = Integer.parseInt(datos.getRam());
		// 12 GB o mas
		if (mem >= 12288) {
			pts += 700;
		}
		// Entre 8GB y 12 GB (menor a 12GB)
		else if (mem < 12288 && mem >= 8192) {
			pts += 500;
		}
		// Entre 4GB y 8GB (menor a 8GB)
		else if (mem < 8192 && mem >= 4096) {
			pts += 250;
		} else {
			pts += 100;
		}

		// CPU libre %
		double cpuLibre = Double.parseDouble(datos.getCpuLibre());
		if (cpuLibre >= 90) {
			pts += 1500;
		} else if (cpuLibre < 90 && cpuLibre >= 80) {
			pts += 1350;
		} else if (cpuLibre < 80 && cpuLibre >= 70) {
			pts += 1050;
		} else if (cpuLibre < 70 && cpuLibre >= 60) {
			pts += 700;
		} else if (cpuLibre < 60 && cpuLibre >= 50) {
			pts += 550;
		} else {
			pts += 300;
		}

		// RAM libre %
		double ramLibre = Double.parseDouble(datos.getRamLibre());
		if (ramLibre >= 90) {
			pts += 1400;
		} else if (ramLibre < 90 && ramLibre >= 80) {
			pts += 1250;
		} else if (ramLibre < 80 && ramLibre >= 70) {
			pts += 950;
		} else if (ramLibre < 70 && ramLibre >= 60) {
			pts += 700;
		} else if (ramLibre < 60 && ramLibre >= 50) {
			pts += 550;
		} else {
			pts += 300;
		}

		// disco libre %
		double discoLibre = Double.parseDouble(datos.getDiscoLibre());
		if (discoLibre >= 90) {
			pts += 500;
		} else if (discoLibre < 90 && discoLibre >= 80) {
			pts += 450;
		} else if (discoLibre < 80 && discoLibre >= 70) {
			pts += 400;
		} else if (discoLibre < 70 && discoLibre >= 60) {
			pts += 350;
		} else if (discoLibre < 60 && discoLibre >= 50) {
			pts += 300;
		} else {
			pts += 250;
		}

		return pts;
	}

	// METODO PARA CARGAR EL MODELO DE LA TABLA Y QUE SEA DINAMICA
	protected void cargarDatos(Datos _datos, String ip) throws InterruptedException {
		Datos datos = _datos;
		// Si el nombre de cliente ya est� registrado, no lo vuelva a agregar
		// en caso de que ya est� registrado, que sobrescriba los valores recibidos del
		// cliente

		String nomCliente = datos.getUsuario(); // Obtiene el usuario

		String modeloProcesador = datos.getModeloProcesador();
		String velocidadProcesador = datos.getVelocidadProcesador();
		String so = datos.getSo();
		String ram = datos.getRam();
		String disco = datos.getDisco();
		// DATOS DINAMICOS
		String cpuLibre = datos.getCpuLibre();
		String ramLibre = datos.getRamLibre();
		String discoLibre = datos.getDiscoLibre();
		String[] data = { nomCliente, modeloProcesador, velocidadProcesador + " GHz", so, ram + " GB", disco + " GB",
				cpuLibre + " %", ramLibre + " %", discoLibre + " %" };
		// modeloDatos.addRow(data);

		if (ip.equals(daniel)) {
			for (int i = 0; i < 9; i++) {
				modeloDatos.setValueAt(data[i], 0, i);
			}
			danielPts = algoritmoRankeo(datos);
		} else if (ip.equals(cesar)) {
			for (int i = 0; i < 9; i++) {
				modeloDatos.setValueAt(data[i], 1, i);
			}
			cesarPts = algoritmoRankeo(datos);
		} else if (ip.equals(erik)) {
			for (int i = 0; i < 9; i++) {
				modeloDatos.setValueAt(data[i], 2, i);
			}
			erikPts = algoritmoRankeo(datos);
		} else if (ip.equals(jose)) {
			for (int i = 0; i < 9; i++) {
				modeloDatos.setValueAt(data[i], 3, i);
			}
			josePts = algoritmoRankeo(datos);
		} else if (ip.equals(ivan)) {
			for (int i = 0; i < 9; i++) {
				modeloDatos.setValueAt(data[i], 4, i);
			}
			ivanPts = algoritmoRankeo(datos);
		}

		puntuaciones[0] = danielPts;
		puntuaciones[1] = cesarPts;
		puntuaciones[2] = erikPts;
		puntuaciones[3] = josePts;
		puntuaciones[4] = ivanPts;

		for (int i = 0; i < 5; i++) {
			puntuacionesNombres.put(puntuaciones[i], nombres[i]);
		}

		puntuacionesIp.put(danielPts, "25.0.122.89");
		puntuacionesIp.put(cesarPts, "25.24.184.239");
		puntuacionesIp.put(erikPts, "25.18.90.103");
		puntuacionesIp.put(josePts, "25.11.6.101");
		puntuacionesIp.put(ivanPts, "25.12.252.241");

		Arrays.sort(puntuaciones);
		// ordenamos las puntuaciones

		for (int i = 0; i < 5; i++) {
			modeloRank.setValueAt(puntuaciones[4 - i], i, 2);
			modeloRank.setValueAt(i + 1, i, 0);
			modeloRank.setValueAt(puntuacionesNombres.get(puntuaciones[4 - i]), i, 1);
		}

		try {
			// Thread.sleep(2500);
			enviarAlerta(puntuacionesIp.get(puntuaciones[4]));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Servidor -> !Error al enviar alerta!: " + e.getMessage());
		}
	}

	// socket para enviar alerta solo al server
	Socket s3 = null;
	DataOutputStream oos3 = null;

	protected void enviarAlerta(String IpMejorRank) throws UnknownHostException, IOException {
		System.out.println("Servidor -> Convertir en servidor la ip: " + IpMejorRank);

		/*****envio señal al nuevo servidor para que se active*****/
		try {
			s3 = new Socket(IpMejorRank, 4897);
			System.out.println("Avisando al servidor...");
			oos3 = new DataOutputStream(s3.getOutputStream());
			oos3.writeUTF(IpMejorRank);
			System.out.println("Servidor -> Se le envio la alerta al servidor: " + IpMejorRank);
			if (!IpMejorRank.equals(obtenerIPLocal())) {
				interfaz.setVisible(false);
			} else if (!interfaz.isVisible()) {
				interfaz.setVisible(true);
			}
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("Servidor -> Error al avisar al servidor: " + e.getMessage());
		}

		System.out.println("Servidor -> Difundiendo quien es el servidor...");

		/****** les envia la senal a todos los clientes ******/
		for (int i = 0; i < direcciones.length; i++) {
			try {
				s2 = new Socket(direcciones[i], 4892);
				System.out.println("Servidor -> nuevo socket cargado para la ip: " + direcciones[i]);
				salida = new DataOutputStream(s2.getOutputStream());
				salida.writeUTF(IpMejorRank);
				System.out.println(
						"Servidor -> Difusion (nuevo server es " + IpMejorRank + "): Se envio a " + direcciones[i]);
			} catch (Exception e) {
				e.getStackTrace();
				System.out.println("Servidor -> !Error en enviar alerta a " + direcciones[i] + "! : " + e.getMessage());
			}
		}
	}

	protected void encabezadoTablas() {
		// Agregar encabezado de las tablas
		modeloDatos.addColumn("Clientes");
		modeloDatos.addColumn("Modelo del procesador");
		modeloDatos.addColumn("Velocidad del procesador");
		modeloDatos.addColumn("Sistema operativo");
		modeloDatos.addColumn("Memoria RAM");
		modeloDatos.addColumn("Disco duro");
		modeloDatos.addColumn("CPU libre");
		modeloDatos.addColumn("RAM libre");
		modeloDatos.addColumn("Disco duro libre");
		modeloRank.addColumn("Posicion");
		modeloRank.addColumn("Clientes");
		modeloRank.addColumn("Puntos");

		// crear filas vacias
		String[] datosVacios = { "", "", "", "", "", "", "", "", "" };
		modeloDatos.addRow(datosVacios);
		modeloDatos.addRow(datosVacios);
		modeloDatos.addRow(datosVacios);
		modeloDatos.addRow(datosVacios);
		modeloDatos.addRow(datosVacios);
		String[] datosVaciosRank = { "", "", "" };
		modeloRank.addRow(datosVaciosRank);
		modeloRank.addRow(datosVaciosRank);
		modeloRank.addRow(datosVaciosRank);
		modeloRank.addRow(datosVaciosRank);
		modeloRank.addRow(datosVaciosRank);

	}

	/// METODO PARA CERRAR LA CONEXION
	protected void cerrarConexion() {
		soyElServidor = false;
		try {
			ois.close();
			oos.close();
			s.close();
			System.out.println("////Puerto de servidor cerrado///");
		} catch (IOException e) {
			e.getStackTrace();
			System.out.println("!Error: " + e.getMessage());
		} finally {
			interfaz.setVisible(false);
		}
	}

	// METODO PARA LOS ACCIONES DE LOS BOTONES DE LA INTERFAZ
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCerrar) {
			cerrarConexion();
			JOptionPane.showMessageDialog(null, "Se ha cerrado la conexion");
		}
		if (e.getSource() == btnEjecutar) {
			if (!txtPort.getText().isEmpty() && !serverIniciado) {
				ejecutarConexion(Integer.parseInt(txtPort.getText()));
				serverIniciado = true;
				txtPort.setEditable(false);

			} else {
				JOptionPane.showMessageDialog(null, "Ingrese un puerto para levantar conexion");
			}

		}
	}

	// OBTIENE DIRECCION IP
	protected String obtenerIPLocal() {
		String direccionIP = "";
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

			while (interfaces.hasMoreElements()) {

				NetworkInterface interfaz = interfaces.nextElement();
				Enumeration<InetAddress> direcciones = interfaz.getInetAddresses();

				while (direcciones.hasMoreElements()) {

					InetAddress direccion = direcciones.nextElement();

					if (direccion instanceof Inet4Address && !direccion.isLoopbackAddress()) {
						if (direccion.toString().contains("/25")) {
							direccionIP = direccion.toString();
						}
					}

				}
			}

		} catch (SocketException e) {
			System.err.println("Error -> " + e.getMessage());
		}
		return direccionIP.replace("/", "");
	}
}