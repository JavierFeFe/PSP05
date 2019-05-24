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
