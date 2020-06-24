package OperacoesServidor;

/**
 *  Esta classe manipula todas as operações de leitura e operações de gravação
 * 
 * @author Acer
 */
import java.net.*; 
import java.io.*;

public class OperacoesServidor {
    
    private Socket sock; 
    
    public OperacoesServidor(Socket sock)
    {
        this.sock = sock; 
    }
    
    /**
     * Lê os packets
     * 
     * @return messageBuffer
     */
    public byte[] readMessage()throws IOException
    {     
        InputStream in = sock.getInputStream();

      // Tamanho do buffer
      byte[] messageBuffer = new byte[4];
      
      int totalBytesRcvd = 0;						
      int bytesRcvd;
      
      while (totalBytesRcvd < messageBuffer.length)
      {
        if ((bytesRcvd = in.read(messageBuffer, totalBytesRcvd,
                        messageBuffer.length - totalBytesRcvd)) == -1)
            throw new SocketException("Conexão fechada prematuramente");
            
        totalBytesRcvd += bytesRcvd;
      }
           
       return messageBuffer;              
    }
    
    /** 
     * Escreve a mensagem do packet
     * 
     * @param packet
     */
    public void writeMessage(byte[] packet)throws IOException
    {
        OutputStream out = sock.getOutputStream();       
        out.write(packet);
    }
    
}
