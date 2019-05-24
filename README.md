# PSP05
Enunciado.
Ejercicio 1.

Modifica el ejemplo del servidor HTTP (Proyecto java ServerHTTP, apartado 5.1 de los contenidos) para que incluya la cabecera Date.
```Java
  private String getDateValue(){
      DateFormat df = new SimpleDateFormat("EEE,d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
      df.setTimeZone(TimeZone.getTimeZone("GTM"));
      return df.format(new Date());
  }
```
*Creo el método para mostrar la fecha y hora*  
```Java
printWriter.println("Date: " + getDateValue());
```
*Muestro la cabecera*  

Ejercicio 2.

Modifica el ejemplo del servidor HTTP (Proyecto java ServerHTTP, apartado 5.1 de los contenidos) para que implemente multihilo, y pueda gestionar la concurrencia de manera eficiente.

```Java
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PaquetePrincipal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author duche
 */
public class Main {
    private static ServerSocket socServidor;
    public static void main(String[] args) {
        
        Socket socCliente;
        HiloDespachador hilo;
        try {
                socServidor = new ServerSocket(8066);
            while (true) {
                //acepta una petición, y le asigna un socket cliente para la respuesta
                socCliente = socServidor.accept();
                //crea un nuevo hilo para despacharla por el socketCliente que le asignó
                System.out.println("Atendiendo al cliente ");
                hilo = new HiloDespachador(socCliente);
                hilo.start();
            }
        } catch (IOException ex) {
        }
    }   
}
```
*Creo la clase principal con el método main que se encargará de asignar un hilo a cada cliente*  
```Java
package PaquetePrincipal;

import java.io.BufferedReader;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * *****************************************************************************
 * Servidor HTTP que atiende peticiones de tipo 'GET' recibidas por el puerto 
 * 8066
 *
 * NOTA: para probar este código, comprueba primero de que no tienes ningún otro
 * servicio por el puerto 8066 (por ejemplo, con el comando 'netstat' si estás
 * utilizando Windows)
 *
 * @author IMCG
 */
class HiloDespachador extends Thread{

  /**
   * **************************************************************************
   * procedimiento principal que asigna a cada petición entrante un socket 
   * cliente, por donde se enviará la respuesta una vez procesada 
   *
   * @param args the command line arguments
   */
  private Socket socketCliente;
  public HiloDespachador(Socket socketCliente) {
    this.socketCliente = socketCliente;
  }



  /**
   *****************************************************************************
   * procesa la petición recibida
   *
   * @throws IOException
   */
  @Override
  public void run() {
      InputStreamReader inSR = null;
      try {
          //variables locales
          String peticion;
          String html;
          //Flujo de entrada
          inSR = new InputStreamReader(
                  socketCliente.getInputStream());
          //espacio en memoria para la entrada de peticiones
          BufferedReader bufLeer = new BufferedReader(inSR);
          //objeto de java.io que entre otras características, permite escribir
          //'línea a línea' en un flujo de salida
          PrintWriter printWriter = new PrintWriter(
                  socketCliente.getOutputStream(), true);
          //mensaje petición cliente
          peticion = bufLeer.readLine();
          //para compactar la petición y facilitar así su análisis, suprimimos todos
          //los espacios en blanco que contenga
          peticion = peticion.replaceAll(" ", "");
          
          //si realmente se trata de una petición 'GET' (que es la única que vamos a
          //implementar en nuestro Servidor)
          if (peticion.startsWith("GET")) {
              //extrae la subcadena entre 'GET' y 'HTTP/1.1'
              peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));
              
              //si corresponde a la página de inicio
              if (peticion.length() == 0 || peticion.equals("/")) {
                  //sirve la página
                  html = Paginas.html_index;
                  printWriter.println(Mensajes.lineaInicial_OK);
                  printWriter.println(Paginas.primeraCabecera);
                  printWriter.println("Content-Length: " + html.length() + 1);
                  printWriter.println("Date: " + getDateValue());
                  printWriter.println("\n");
                  printWriter.println(html);
              } //si corresponde a la página del Quijote
              else if (peticion.equals("/quijote")) {
                  //sirve la página
                  html = Paginas.html_quijote;
                  printWriter.println(Mensajes.lineaInicial_OK);
                  printWriter.println(Paginas.primeraCabecera);
                  printWriter.println("Content-Length: " + html.length() + 1);
                  printWriter.println("Date: " + getDateValue());
                  printWriter.println("\n");
                  printWriter.println(html);
              } //en cualquier otro caso
              else {
                  //sirve la página
                  html = Paginas.html_noEncontrado;
                  printWriter.println(Mensajes.lineaInicial_NotFound);
                  printWriter.println(Paginas.primeraCabecera);
                  printWriter.println("Content-Length: " + html.length() + 1);
                  printWriter.println("Date: " + getDateValue());
                  printWriter.println("\n");
                  printWriter.println(html);
              }
              
          } } catch (IOException ex) {
          Logger.getLogger(HiloDespachador.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
          try {
              socketCliente.close();
              System.out.println("Cliente atendido");
          } catch (IOException ex) {
              Logger.getLogger(HiloDespachador.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
  }

  /**
   * **************************************************************************
   * muestra un mensaje en la Salida que confirma el arranque, y da algunas
   * indicaciones posteriores
   */
  private void imprimeDisponible() {

    System.out.println("El Servidor WEB se está ejecutando y permanece a la "
            + "escucha por el puerto 8066.\nEscribe en la barra de direcciones "
            + "de tu explorador preferido:\n\nhttp://localhost:8066\npara "
            + "solicitar la página de bienvenida\n\nhttp://localhost:8066/"
            + "quijote\n para solicitar una página del Quijote,\n\nhttp://"
            + "localhost:8066/q\n para simular un error");
  }
  private String getDateValue(){
      DateFormat df = new SimpleDateFormat("EEE,d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
      df.setTimeZone(TimeZone.getTimeZone("GTM"));
      return df.format(new Date());
  }


}
```
*Heredo de la clase Thread e implemento el método run en la clase principal*  
