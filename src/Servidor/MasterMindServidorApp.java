package Servidor;

import java.io.*; 
import java.util.*; 

/**
 *
 *  Esta classe inicia o servidor
 * 
 * @author Acer
 */
public class MasterMindServidorApp {

    public static void main(String[] args) {
        
        int port = 50000;
        try{
            
            MasterMindServidor server = new MasterMindServidor(port);
            server.run();
        }
        catch(IOException e)
        {
            System.out.println("O servidor não pode executar " + e.getMessage());
        }

    }   
}
