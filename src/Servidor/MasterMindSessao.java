package Servidor;

import OperacoesServidor.OperacoesServidor;
import java.net.*;
import java.io.*; 
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Esta classe define a funcionalidade de uma sessão de jogo.
 *  Fornece as ações necessárias para jogar 
 * 
 * @author Acer
 */
public class MasterMindSessao implements Runnable {
    
    private byte[] answer; 
    private OperacoesServidor op_serv; 
    private byte[] clues; 
    private Socket sock;
    
    public MasterMindSessao(Socket sock)
    {
        op_serv = new OperacoesServidor(sock);     
        this.sock = sock; 
    }
    
    /**
     * Este método é chamado quando a thread é iniciada, ele manipula todas as solicitações feitas pelo utilizador
     */
    @Override
    public void run()
    {
        try {
            startGame();
        } catch (IOException ex) {           
            System.out.println("EXCEPTION THROWN " + ex.getMessage());
        }
        System.out.println("Cliente finalizado");
    }
    
    
    
    /**
     * Este método inicia um jogo
     * 
     */
    public void startGame()throws IOException
    {
        boolean play = true;
        boolean win = false;  

        while(play)  
        {            
            generateAnswer();           
            
            int attempts = 0;
            while(!win && attempts < 10)// 1 jogo, 10 tentativas
            {
               byte[] guess = op_serv.readMessage();

               //verifique se o jogador deseja sair
               switch(guess[guess.length - 1])
               {
                   case 0:
                       System.out.println("Cliente " + sock.getInetAddress().getHostAddress() + 
                               " deseja reiniciar o jogo");
                       generateAnswer();
                       attempts = 0; 
                       break;
                   
                   case -1: //utilizador deseja sair
                        play = false;
                        attempts = 10; 
                        break;
                   
                   case -2: //utilizador deseja criar uma nova resposta
                        System.out.println("Cliente " + sock.getInetAddress().getHostAddress() + " deseja dar uma nova resposta");
                        setAnswer();
                        break;
                   
                   default:
                       win = generateClue(guess);     
                       //a tentativa não correspondeu, enviar resposta
                       if(!win && attempts == 9)
                       {
                           //ler a mensagem e enviar a resposta
                           
                           op_serv.writeMessage(answer);
                       }
                       else
                           op_serv.writeMessage(clues);                     
                       attempts++;
                       break;                                     
               }
               System.out.println("Tentativas: " + attempts);
            }
            
            //verifica se o utilizador cheogu ao fim com 10 tentativas ou saiu
            if(play)
            {
                System.out.println("Tentativas concluídas com:" + attempts);
                
                //ganhou ou esgotou tentativas          
                byte[] response = op_serv.readMessage();
            
                // Packets 
                // 0 0 0 0 - jogar de novo
                // 0 0 0-1 - sair do jogo          
                if(response[response.length - 1] == 0)
                {
                    attempts = 0; 
                    win = false;
                    System.out.println("Joga outro jogo");
                }
                else
                    play = false;               
            }           
        }
        
        System.out.println("Sessão finalizada");
    }
    
    /*
     * Este método gera a resposta
     */
    private void generateAnswer()
    {
        byte[] answer = new byte[4];
        
        for(int i = 0; i < answer.length; i++)
            answer[i] = (byte)((Math.random() * 6) + 1);
        
        System.out.println("Resposta para o jogo atual:" 
                + java.util.Arrays.toString(answer));
        
        this.answer = answer; 
    }
    
    /*
     * Este método envia ao utilizador o feddback e informa se ganhou a tentativa ganhou o jogo ou não
     * @return          if the owner has found the answer 
     */
    private boolean generateClue(byte[] guess)throws IOException
    {
        byte[] cluePacket = new byte[4];
        
        System.out.println("Cliente " + sock.getInetAddress().getHostAddress() + 
                ": Adivinhou" + Arrays.toString(guess));
        
        byte[] answerCopy = copyAnswer();
       
        int arrayIndex = 0; 
        int matchPos =0; 
        
        
        for(int i = 0; i < guess.length; i++)
        {
            if (answerCopy[i] == guess[i]){
                cluePacket[arrayIndex] = 2;
                arrayIndex++;
                answerCopy[i] = -7;
                guess[i] = -10;
                matchPos++; 
            }
        }
        
        if(arrayIndex != 4)  
        {
            for(int i=0; i < answerCopy.length; i++)
            {     
                for(int j = 0; j < guess.length; j++)
                if(answerCopy[i] == guess[j])
                {
                    answerCopy[i] = -7;
                    guess[j] = -10;
                    if(arrayIndex < cluePacket.length){
                        cluePacket[arrayIndex] = 1;
                        arrayIndex++;          
                    }
                }
            }   
        } 
        System.out.println("Feedback: " + Arrays.toString(cluePacket) +
                           "Resposta: " + Arrays.toString(answer));
        
        this.clues = cluePacket;
        
        return matchPos == 4; 
    }
    
    /*
     *  Este método cria cópia das respostas
     */
    private byte[] copyAnswer()
    {
        byte[] copy = new byte[answer.length];
        for(int i=0; i<answer.length; i++)
            copy[i] = answer[i];
        return copy; 
    }
    
    /*
     *  Este método define a resposta
     */
    private void setAnswer()throws IOException
    {            
        this.answer = op_serv.readMessage();
        
        System.out.println("Resposta para o jogo atual: " 
                + java.util.Arrays.toString(answer));
    }
    

}
