package estres.factorial;

import java.math.BigInteger;

public class program {
	public static void estresar() {
		factorial hilo1 = new factorial(1,125000);
		factorial hilo2 = new factorial(125001,250000);
		factorial hilo3 = new factorial(250001,375000);
		factorial hilo4 = new factorial(375001,500000);
		
		hilo1.start();
		hilo2.start();
		hilo3.start();
		hilo4.start();
	}
}
