package cliente.servidor;

import java.io.Serializable;

public class Datos implements Serializable
{
	private String usuario;
	private String modeloProcesador;
	private String velocidadProcesador;
	private String so;
	private String ram;
	private String disco;
	// DATOS DINAMICOS
	private String cpuLibre;
	private String ramLibre;
	private String discoLibre;
	//PUNTOS 
	private String puntos;
	public Datos(String usuario, String modeloProcesador, String velocidadProcesador, String so, String ram,
			String disco, String cpuLibre, String ramLibre, String discoLibre) {
		super();
		this.usuario = usuario;
		this.modeloProcesador = modeloProcesador;
		this.velocidadProcesador = velocidadProcesador;
		this.so = so;
		this.ram = ram;
		this.disco = disco;
		this.cpuLibre = cpuLibre;
		this.ramLibre = ramLibre;
		this.discoLibre = discoLibre;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getModeloProcesador() {
		return modeloProcesador;
	}

	public void setModeloProcesador(String modeloProcesador) {
		this.modeloProcesador = modeloProcesador;
	}

	public String getVelocidadProcesador() {
		return velocidadProcesador;
	}

	public void setVelocidadProcesador(String velocidadProcesador) {
		this.velocidadProcesador = velocidadProcesador;
	}

	public String getSo() {
		return so;
	}

	public void setSo(String so) {
		this.so = so;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getDisco() {
		return disco;
	}

	public void setDisco(String disco) {
		this.disco = disco;
	}

	public String getCpuLibre() {
		return cpuLibre;
	}

	public void setCpuLibre(String cpuLibre) {
		this.cpuLibre = cpuLibre;
	}

	public String getRamLibre() {
		return ramLibre;
	}

	public void setRamLibre(String ramLibre) {
		this.ramLibre = ramLibre;
	}

	public String getDiscoLibre() {
		return discoLibre;
	}

	public void setDiscoLibre(String discoLibre) {
		this.discoLibre = discoLibre;
	}

	@Override
	public String toString() {
		return "Datos [usuario=" + usuario + ", modeloProcesador=" + modeloProcesador + ", velocidadProcesador="
				+ velocidadProcesador + ", so=" + so + ", ram=" + ram + ", disco=" + disco + ", cpuLibre=" + cpuLibre
				+ ", ramLibre=" + ramLibre + ", discoLibre=" + discoLibre + ", puntos=" + puntos + "]";
	}
	
	
}
