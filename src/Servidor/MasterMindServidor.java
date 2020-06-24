package Servidor;

import java.net.*; 
import java.io.*;

/**
 *  Esta classe fornece implementação do servidor MasterMind
 * 
 * @author Acer
 */
public class MasterMindServidor {
    
    private ServerSocket serverSock;
       
    public MasterMindServidor(int port)throws IOException
    {
        serverSock = new ServerSocket(port);
    }
    
    /** 
    *Este método executa o servidor e permite que ele escute a solicitação do cliente
    * 
    */
    public void run()throws IOException 
    {
        while(true)
        {
            Socket clSock =  serverSock.accept();                
            
            MasterMindSessao player = new MasterMindSessao(clSock);
            System.out.println("Atender o cliente no Endereço IP #: " + clSock.getInetAddress().getHostAddress());
            // abrir um novo thread e gerir a solicitação dos utilizadores
            Thread game = new Thread(player);
            game.start();           
            System.out.println("Manipular o cliente" + clSock.getInetAddress().getHostAddress());
        }
    }
    
    
}
