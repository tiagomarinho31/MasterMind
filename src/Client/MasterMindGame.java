package Client;



import java.net.*;
import java.io.*;
import OperacoesServidor.OperacoesServidor;
/**
 *
 * Esta classe define as funcionalidades necessária para executar a comunicação com um servidor para criar um jogo 
 * 
 * @author Acer
 */
public class MasterMindGame {
 
    private OperacoesServidor network; 
    private int attempts;
    
    public MasterMindGame(){}
    
    
    /**
     * Estabelece uma conexão entre o cliente e o servidor
     * 
     * @param ipAddress                 
     * @param port                      
     *         
     */
    public void connect (String ipAddress, int port)
            throws UnknownHostException, IOException
    {
        Socket serverSock = new Socket(ipAddress, port);
        network = new OperacoesServidor(serverSock);
    }
    
    /**
     * Envia uma tentativa para o servidor.
     * 
     * @param guess
     */
    public void sendGuess(byte[] guess)throws IOException
    {
        attempts++; 
        network.writeMessage(guess);
    }
  
    /**
     * Visualiza as cores da tentativa
     * 
     */
    public byte[] readReponse()throws IOException
    {
        return network.readMessage();
    }
    
    
    /**
     * Utilizador deseja jogar de novo.
     * Envia uma mensagem para o servidor para permitir reiniciar o jogo
     * 
     */
    public void playAgain()throws IOException
    {
        byte[] packet = new byte[4]; 
        
        attempts = 0;
        network.writeMessage(packet);
                
    }
    
    /**
     * Utilizador deseja fechar o jogo.
     * Envia uma mensagem para o servidor para permitir fechar o jogo
     * 
     */
    public void quitGame()throws IOException
    {
        //Packet 000-1 - end game/session
        byte[] packet = new byte[4];
        packet[packet.length - 1] = -1;
        network.writeMessage(packet);
        
    }

    /**
     * Obtem as tentativas do jogo
     * 
     * @return attemps
     */
    public int getAttempts(){
        return attempts; 
    }
    
    /**
     * Utilizador deseja inserir um conjuto de tentativas
     * 
     */
    public void signalAnswerChange()throws IOException
    {
    	//informa o servidor que quer inserir respostas
    	byte[] packet = new byte[4];
    	packet[packet.length - 1] = -2;
    	network.writeMessage(packet);	
    }
    
    /**
     * Este método envia a resposta para o servidor
     * 
     * @param answer   
     */
    public void sendAnswerSet(byte[] answer)throws IOException
    {
    	//validate 
    	network.writeMessage(answer);
    	
    }
    
}
