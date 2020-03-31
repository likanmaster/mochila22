
package mochila2;
import java.util.Random;
/**
 *
 * @author jose licancura
 * 
 */
public class Mochila2 {
   static int tammochila = 300;
   static int[] peso = { 20, 15, 13, 80, 82, 87, 90, 115, 118, 120};
   static int[] valor = { 135 , 139, 184, 192, 201, 210, 214, 221, 229, 240};
   static int objetoscantidad = 10;
   static float tau= (float) 1.6;
   static int[] mochila = { 0, 0, 0, 0, 0, 0 ,0, 0, 0, 0};
   static int[] mejormochila;
   static int mejorvalor;
   static int pesofinal;
   public static Random rnd;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        int semilla = 5;
        int iteraciones = 1000;
        rnd = new Random(semilla);
        
       
           
       //llenamos la mochila de objetos
        for (int i =0;i< objetoscantidad; i++) {
            if(tammochila> pesochilamochila()+peso[i])
                mochila[i] = 1;              
        }
         //obtenemos el peso de la mochila inicial
         
        int pesomochila = 0;
        for (int i = 0; i < objetoscantidad; i++) {
            pesomochila += mochila[i]*peso[i];
        }
        //System.out.println("peso "+pesomochila);
        mejormochila = new int[tammochila];
       System.arraycopy(mochila, 0, mejormochila, 0, objetoscantidad);
        
        //obtenemos el valor de la mochila
        int valorMochila = 0;
        for (int i = 0; i < objetoscantidad; i++) {
            valorMochila += mochila[i]*valor[i];
        }
        mejorvalor = valorMochila;
        pesofinal = pesomochila;
        System.out.println("Mochila inicial");
        for (int i = 0; i < objetoscantidad; i++) {
            System.out.print(mochila[i]+" ");
        }
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("valor: "+mejorvalor+"   peso: "+pesofinal);
        System.out.println(" ");
        
        
        
         //Generando un ventor de probabilidades
        float[] P = new float[objetoscantidad];
        for (int i = 1; i <= objetoscantidad; i++) {
            P[i-1] = (float) Math.pow(i, -tau);
        }
        //aqui comienza las iteraciones
        int itera=0;
        for (int i = 0; i < iteraciones; i++) {
            
            int objetomochila = 0;
            int aux = 0;
            for (int j = 0; j < objetoscantidad; j++) {
                objetomochila += mochila[j];
            }
            //guardamos el objeto y su fittnes
            int[][] fitnesslocal = new int[objetomochila][2]; 
            for (int j = 0; j < objetoscantidad; j++) {
                if(mochila[j]!=0){
                    fitnesslocal[aux][0] = j;
                    fitnesslocal[aux][1] = (valor[j]-peso[j])*-1;
                    aux++;
                }
            }
            //ordenamiento de peor a mejor
            int temp1;
            int temp2;
            for (int j = 0; j < objetomochila-1 ; j++) {
                for (int k = j+1; k < objetomochila; k++) {
                    if (fitnesslocal[j][1]>fitnesslocal[k][1]) {
                        temp1 = fitnesslocal[j][0];
                        temp2 = fitnesslocal[j][1];
                        fitnesslocal[j][0] = fitnesslocal[k][0];
                        fitnesslocal[j][1] = fitnesslocal[k][1];
                        fitnesslocal[k][0] = temp1;
                        fitnesslocal[k][1] = temp2;
                    }
                }
            }
            //vemos los objetos que estan fuera de la mochila
            int[] fueraMochila = new int[objetoscantidad - objetomochila];
            int aux2 = 0;
            for (int j = 0; j < fueraMochila.length; j++) {
                if(mochila[j]==0){
                    fueraMochila[aux2] = j;
                    aux2++;
                }                    
            }
            //reemplazamos uno de afuera por uno de dentro de la mochila
            int entra = rnd.nextInt(fueraMochila.length);
            int quitar = sacarobjeto(objetomochila, fitnesslocal, P);
            //cambiamos el peor por uno random
            mochila[fitnesslocal[quitar][0]] = 0;
            mochila[fueraMochila[entra]] = 1;
            //verificamos que no supere la carga de la mochila. de superarla lo sacamos
            if(pesomochila > tammochila){
                mochila[fitnesslocal[quitar][0]] = 1;
                mochila[fueraMochila[entra]] = 0;
            }//verificamos si hay espacio para uno mas
            else{
                for (int j = objetoscantidad-1; j >= 0; j--) {
                    if(mochila[j]==0){
                        if(peso[j]+pesochilamochila()<= tammochila)
                            mochila[j] = 1;
                    }
                }
                //si es mejor actualizamos
                if(valorchilamochila()>mejorvalor){
                    System.arraycopy(mochila, 0, mejormochila, 0, objetoscantidad);
                    mejorvalor = valorchilamochila();
                    pesofinal = pesochilamochila();
                }
            }    
            itera=itera+1;
        }
        System.out.println("mochila Final");
        for (int i = 0; i < objetoscantidad; i++) {
            System.out.print(mejormochila[i]+" ");
        }
        System.out.println("");
        System.out.println("valor: "+mejorvalor+"   peso: "+pesofinal);
        System.out.println("iteracion "+itera);
    }
     public static int valorchilamochila(){
        int valormochila = 0;
        for (int i = 0; i < objetoscantidad; i++) {
            valormochila += mochila[i]*valor[i];
        }
        return valormochila;
    }
      public static int sacarobjeto(int objmochila, int[][] fitnesslocal, float[] P){
        int peorobj = 0;
        float aleatorio = rnd.nextFloat();
        float[] ruleta = new float[objmochila];
        float total = 0;
        for (int i = 0; i < objmochila; i++) {
            total += P[i];
        }
        ruleta[0] = P[0]/total;
        for (int i = 1; i < objmochila; i++) {
            ruleta[i] = ruleta[i-1]+(P[i]/total); 
        }
        for (int i = 0; i < objmochila; i++) {
            if(i != 0 && aleatorio > ruleta[i-1] && ruleta[i] >= aleatorio){
                peorobj = i;
            }
            else {
                if(i==0 && aleatorio < ruleta[i]){
                    peorobj = i;
                }
            }           
        }
        return peorobj;
    }
      public static int pesochilamochila(){
        int pesomochila = 0;
        for (int i = 0; i < objetoscantidad; i++) {
            pesomochila += mochila[i]*peso[i];
        }
        return pesomochila;
    }
}

