package Interface;

import Client.MasterMindGame;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Acer
 */
public class Controller implements Initializable {
    
    @FXML private Button submitBtn;
    @FXML private MenuItem setAnswerMI;
    @FXML private Text resultMsg;
    
    private Circle[] answers;
    private Circle[] choices;
    private Circle[][] guesses;
    private Circle[][] clues;
    
    @FXML private Circle answer1, answer2, answer3, answer4;
    
    @FXML private Circle choice1, choice2, choice3, choice4, 
                            choice5, choice6;
    
    @FXML private Circle guess01, guess02, guess03, guess04,
                            guess11, guess12, guess13, guess14,
                            guess21, guess22, guess23, guess24,
                            guess31, guess32, guess33, guess34,
                            guess41, guess42, guess43, guess44,
                            guess51, guess52, guess53, guess54,
                            guess61, guess62, guess63, guess64,
                            guess71, guess72, guess73, guess74,
                            guess81, guess82, guess83, guess84,
                            guess91, guess92, guess93, guess94;
    
    @FXML private Circle clue01, clue02, clue03, clue04,
                            clue11, clue12, clue13, clue14,
                            clue21, clue22, clue23, clue24,
                            clue31, clue32, clue33, clue34,
                            clue41, clue42, clue43, clue44,
                            clue51, clue52, clue53, clue54,
                            clue61, clue62, clue63, clue64,
                            clue71, clue72, clue73, clue74,
                            clue81, clue82, clue83, clue84,
                            clue91, clue92, clue93, clue94;
    
    private MasterMindGame MMGame;
    private Circle currentChoice;
    
    private Stage setAnswerStage;
    private TextField newAnswerTf1;
    private TextField newAnswerTf2;
    private TextField newAnswerTf3;
    private TextField newAnswerTf4;
    private Text answerStageErrorMsg;
    @FXML
    private Text txtMMTitle;
    
    
    /**
     * Adicionar objetos Circle aos arrays correspondentes.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        answers = new Circle[] {
            answer1, answer2, answer3, answer4
        };
        
        choices = new Circle[] {
            choice1, choice2, choice3, choice4, 
            choice5, choice6
        };
        
        guesses = new Circle[][] {
            {guess01, guess02, guess03, guess04},
            {guess11, guess12, guess13, guess14},
            {guess21, guess22, guess23, guess24},
            {guess31, guess32, guess33, guess34},
            {guess41, guess42, guess43, guess44},
            {guess51, guess52, guess53, guess54},
            {guess61, guess62, guess63, guess64},
            {guess71, guess72, guess73, guess74},
            {guess81, guess82, guess83, guess84},
            {guess91, guess92, guess93, guess94}
        };
        
        clues = new Circle[][] {
            {clue01, clue02, clue03, clue04},
            {clue11, clue12, clue13, clue14},
            {clue21, clue22, clue23, clue24},
            {clue31, clue32, clue33, clue34},
            {clue41, clue42, clue43, clue44},
            {clue51, clue52, clue53, clue54},
            {clue61, clue62, clue63, clue64},
            {clue71, clue72, clue73, clue74},
            {clue81, clue82, clue83, clue84},
            {clue91, clue92, clue93, clue94}
        };
    }  
    
    /**
     * O MMModel fornece uma classe MasterMindGame com endereço IP e porta conectados a este controller
     * @param MMG 
     */
    public void setMMGame(MasterMindGame MMG) {
        this.MMGame = MMG;
    }
    
    /**
     * Inicia um novo jogo redefinindo tudo para o padrão. 
     * Se as tentativas estiver acima de 0 (o que significa que não é a primeira vez que eles jogam o jogo), 
     * use o método MasterMindGame para se redefinir também.
     */
    public void launchNewGame() {
        
        //redefinir mensagem de resultado e ativar o botão enviar
        resultMsg.setText("");
        submitBtn.setDisable(false);
        setAnswerMI.setDisable(false);
        //torna a cor selecionada em vermelho (cor padrão), a seleção é vista através de uma sombra
        if(currentChoice != null)
            currentChoice.setEffect(dimChoice());
        choices[0].setEffect(highlightChoice());
        currentChoice = choices[0];
        //torna a primeira linha clicável
        //esconde a resposta
        for(int i=0; i<4; i++) {
            guesses[0][i].setDisable(false);
            guesses[0][i].setFill(Color.web("#878282"));
            answers[i].setFill(Color.web("#878282"));
            clues[0][i].setFill(Color.web("#878282"));
        }
        //torna a linha 2 a 10 não clicável
        for(int i=1; i<10; i++) {
            for(int j=0; j<4; j++) {
                guesses[i][j].setFill(Color.web("#878282"));
                guesses[i][j].setDisable(true);
                clues[i][j].setFill(Color.web("#878282"));
            }
        }
        
        try{
            //se o utilizador iniciar um novo jogo
            if(MMGame.getAttempts() > 0)
                MMGame.playAgain();
        }
        catch(IOException e) {
            System.out.println("Erro: não foi possível iniciar um novo jogo.");
            System.exit(1);
        }
    }
    
    /**
     * O método do evento de ação é chamado quando o utilizador escolhe uma nova cor. 
     * Define o circulo selecionado como novo círculo atual
     * Adiciona o efeito de sombra caso esteja selecionado
     * @param event 
     */
    @FXML
    private void onPickColor(MouseEvent event) {
        
        currentChoice.setEffect(dimChoice());
        
        currentChoice = (Circle)event.getSource();
        currentChoice.setEffect(highlightChoice());
    }
    
    /**
     * O método do evento de ação é chamado quando o utilizador coloca a cor selecionada no respetivo circulo da linha da jogada.
     * @param event 
     */
    @FXML
    private void onSetChoice(MouseEvent event) {
        
        Circle selectedCircle = (Circle)event.getSource();
        selectedCircle.setFill(currentChoice.getFill());
    }
    
    /**
     * O método de evento de ação é chamado quando o utilizador pressiona o botão enviar, 
     * que verifica se todos os círculos foram preenchidos. Caso contrário não faz nada; 
     * Envia um packet de 4 bytes correspondentes às cores escolhidas. 
     * Obtêm o feedback da resposta.
     * 
     * Se for verificado que o jogador venceu, usamos o método endGame e apresentamos a resposta
     * Caso contrário, verifica se o jogador esgotou as jogadas e perdeu ou se continua o jogo
     * @param event 
     */
    @FXML
    private void onSubmit(MouseEvent event) {
  
        boolean allSet = true;
        int currentRow = MMGame.getAttempts();
        
        //caso todos os circulos da linha não estiverem preenchidos define allset como false
        for(int i=0; i<4; i++)
            if(guesses[currentRow][i].getFill().equals(Color.web("#878282")))
                allSet = false;
        
        //se os circulos estiverem preenchidos
        if(allSet) {
            //armazena as cores em 4 bytes
            byte[] rowGuess = new byte[4];
            //desativa os circulos da linha da jogada como não clicáveis
            //obtem número de bytes por cor a cada 4 círculos (por exemplo, vermelho = 1)
            for(int i=0; i<4; i++) {
                guesses[currentRow][i].setDisable(true);
                rowGuess[i] = getNumByColor(guesses[currentRow][i].getFill());
            }
            
            try{
                //envia o packet da tentativa e recebe o feedback
                MMGame.sendGuess(rowGuess);     
                byte[] rowClues = MMGame.readReponse();
                
                //exibe o feedback
                //verifica se o utilizador ganhou
                boolean isWon = displayClues(rowClues, currentRow);
                
                //obtem a tentativa
                currentRow = MMGame.getAttempts();
                
                //se ganhar
                if(isWon)
                    endGame(true, rowGuess); //torna endGame verdadeiro e exibe a resposta
                else {
                    //se o utilizador esgotar as tentativas perde o jogo
                    if(currentRow == 10)
                        endGame(false, rowClues); //torna endGame falso e exibe a resposta
                    else {
                        //torna próxima linha de tentativa clicável
                        for(int i=0; i<4; i++) 
                            guesses[currentRow][i].setDisable(false);
                    }
                }
            }
            catch(IOException e) {
                System.out.println("Erro: não é possível enviar ou receber bytes");
                System.exit(1);
            }
        }
    }
    
    /**
     * Exibe o feedback.
     * Se todo o feedback tiver pinos pretos retorna o isWon verdadeiro
     * Caso contrário retorna o isWon falso
     * 
     * @param rowClues
     * @param currentRow
     * @return isWon
     */
    private boolean displayClues(byte[] rowClues, int currentRow) {
        
        boolean isWon = true;
        for(int i=0; i<4; i++) {
            
            switch (rowClues[i]) {
                case 2:
                    clues[currentRow][i].setFill(Color.BLACK);
                    break;
                case 1:
                    clues[currentRow][i].setFill(Color.WHITE);
                    isWon = false;
                    break;
                default:    //if == 0
                    isWon = false;
                    break;
            }
        }
        return isWon;
    }
    
    /**
     * O método é chamado quando o utilizador ganha ou alcança no máximo 10 tentativas.
     * Se o jogador acertou a resposta, envie as cores escolhidas como answerCodes, para exibir a resposta
     * Caso contrário, a resposta será o packet que contém a resposta caso alcance as 10 tentativas
     * 
     * AnswerCodes exibe a reposta
     * @param isWin
     * @param answerCodes 
     */
    private void endGame(boolean isWin, byte[] answerCodes) {
        
        //desativar o botão enviar
        submitBtn.setDisable(true);
        setAnswerMI.setDisable(true);
        //exibe o texto caso ganhe ou perca o jogo
        if(isWin) 
            resultMsg.setText("Ganhou, acertou a Resposta!");
        else 
            resultMsg.setText("Perdeu, tente mais uma vez!");
        
        //obtém as cores pelos números
        //preenche a resposta com as cores
        Color[] answerColors = getColorsByNums(answerCodes);
        for(int i=0; i<4; i++)
            answers[i].setFill(answerColors[i]);
    }
    
    
    /**
     * Redefinir e iniciar um novo jogo.
     * @param event 
     */
    @FXML
    private void newGame(ActionEvent event) {
        launchNewGame();
    }
    
    /**
     * Abre uma nova janela que exibirá os créditos do jogo.
     * @param event 
     */
    @FXML
    private void displayCredits(ActionEvent event) {
        
        Stage aboutStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        
        Label gameLbl = new Label("MasterMind");
        gameLbl.setFont(Font.font(null, FontWeight.BOLD, 14));
        Label authorLbl = new Label("Criado Por:");
        Label a1 = new Label("João Rodrigues");
        Label n1 = new Label("19...");
        Label a2 = new Label("Miguel Carvalho");
        Label n2 = new Label("19157");
        Label a3 = new Label("Tiago Marinho");
        Label n3 = new Label("19592");
        Label verLbl = new Label("Laboratório de Programação");
        Label pubDate = new Label("2019/2020");
        
        grid.setPadding(new Insets(25, 10, 25, 10));
        grid.add(gameLbl, 0, 0, 3, 1);
        grid.add(authorLbl, 0, 1);
        grid.add(a1, 1, 1);
        grid.add(n1, 2, 1);
        grid.add(a2, 1, 2);
        grid.add(n2, 2, 2);
        grid.add(a3, 1, 3);
        grid.add(n3, 2, 3);
        grid.add(verLbl, 0, 4, 3, 1);
        grid.add(pubDate, 0, 5, 3, 1);
        
        aboutStage.setTitle("Créditos");
        aboutStage.setResizable(false);
        aboutStage.setScene(new Scene(grid));
        aboutStage.show();
    }
    
    /**
     * Abre uma janela com as instrunções do jogo
     * @param event 
     */
    @FXML
    private void displayHelp(ActionEvent event) {
        
        Stage helpStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        
        Label instrLbl = new Label("Instruções");
        Text i1 = new Text("No jogo tens de acertar a sequência de 4 cores para ganhar!");
        Text i2 = new Text("1. Seleciona a cor que queres usar.");
        Text i3 = new Text("2. Clica no lugar onde queres colocar essa cor. Começa pela linha de baixo");
        Text i4 = new Text("3. Cliqua no botão para submeter a sequência.");
        Text i5 = new Text("4. Utiliza o feedback para tentar descobrir a resposta.");
        Text legend1 = new Text("Pino branco: cor certa, no lugar errado.");
        Text legend2 = new Text("Pino preto: cor certa, no lugar certo.");

        grid.setPadding(new Insets(25, 10, 25, 10));
        grid.add(instrLbl, 0, 0, 2, 1);
        grid.add(i1, 0, 1);
        grid.add(i2, 0, 2);
        grid.add(i3, 0, 3);
        grid.add(i4, 0, 4);
        grid.add(i5, 0, 5);
        grid.add(legend1, 0, 7);
        grid.add(legend2, 0, 8);
        
        helpStage.setTitle("Instruções");
        helpStage.setResizable(false);
        helpStage.setScene(new Scene(grid));
        helpStage.show();
    }
    
    /**
     * Sai do jogo
     * @param event 
     */
    @FXML
    private void exitGame(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Abre uma janela para definir a resposta
     * 
     * @param evt 
     */
    @FXML
    private void setAnswer(ActionEvent evt) {       
        setAnswerStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        Label lbl = new Label("Escolha a nova resposta");
        HBox hb = new HBox();
        newAnswerTf1 = new TextField("");
        newAnswerTf1.setPrefWidth(40);
        newAnswerTf2 = new TextField("");
        newAnswerTf2.setPrefWidth(40);
        newAnswerTf3 = new TextField("");
        newAnswerTf3.setPrefWidth(40);
        newAnswerTf4 = new TextField("");
        newAnswerTf4.setPrefWidth(40);
        hb.getChildren().addAll(newAnswerTf1, newAnswerTf2, 
                                newAnswerTf3, newAnswerTf4);
        hb.setSpacing(10);

        Button btn = new Button("Definir");
        btn.setOnAction(event -> validateNewAnswer());
        answerStageErrorMsg = new Text("");

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(lbl, 0, 0);
        grid.add(hb, 0, 1, 3, 1);
        grid.add(btn, 4, 1);
        grid.add(answerStageErrorMsg, 0, 3);

        setAnswerStage.setTitle("Nova Resposta");
        setAnswerStage.setResizable(true);
        setAnswerStage.setScene(new Scene(grid));
        setAnswerStage.show();
    }
    
    /**
     * Valida a resposta inserida pelo utilizador
     * Caso a resposta não seja válida imprime erros.
     */
    private void validateNewAnswer() {
        
        boolean isValid = true;
        byte[] answerPacket = new byte[4];
        String[] newAnswers = new String[4];
        //obtem entradas da string num array
        newAnswers[0] = newAnswerTf1.getText();
        newAnswers[1] = newAnswerTf2.getText();
        newAnswers[2] = newAnswerTf3.getText();
        newAnswers[3] = newAnswerTf4.getText();
        
        try{
            //verifica se todos os números estão entre 1 e 6
            for(int i=0; i < 4; i++) {
                int num = Integer.parseInt(newAnswers[i]);
                if((num > 0) && (num < 7))
                    answerPacket[i] = (byte)num;
                else
                    isValid = false;
            }
            //se todos os dígitos são válidos
            if(isValid) {
                //informa ao utilizador para inserir nova resposta
                MMGame.signalAnswerChange();
                MMGame.sendAnswerSet(answerPacket);
                setAnswerStage.hide();
            }
            else //se a nova resposta não for válida
                answerStageErrorMsg.setText("Insire números de 1 a 6.");
        }
        catch(NumberFormatException e) {
            answerStageErrorMsg.setText("Insere apenas números.");
        }
        catch(IOException e) {
            System.out.println("Não foi possível definir nova resposta");
            System.exit(1);
        }
    }
     
    
    
    /**
     * Retorna o DropShadow para destacar a cor selecionada
     * @return DropShadow
     */
    private DropShadow highlightChoice() {
        DropShadow dropShadow = new DropShadow(); 
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setColor(Color.BLACK);
        dropShadow.setHeight(1);
        dropShadow.setWidth(1);
        return new DropShadow();
    }
    
    /**
     * Retorna BoxBlur para escurecer a escolha da cor.
     * @return BoxBlur
     */
    private BoxBlur dimChoice() {
        return new BoxBlur(0.0, 0.0, 0);
    }
    
    /**
     * Retorna os números de 1 a 6 com base na cor fornecida. 
     * Retorna 0 se a cor não corresponder a nenhuma no jogo.
     * 
     * @param color
     * @return numbers 1 to 6
     */
    private byte getNumByColor(Paint color) {
        
        byte num = 0;
        
        if(color.equals(Color.RED))
            num = 1;
        else if(color.equals(Color.ORANGE))
            num = 2;
        else if(color.equals(Color.YELLOW))
            num = 3;
        else if(color.equals(Color.GREEN))
            num = 4;
        else if(color.equals(Color.PINK))
            num = 5;
        else if(color.equals(Color.BLUE))
            num = 6;
        
        return num;
    }
    
    /**
     * Através dos números definimos as cores correspondentes
     * @param answers
     * @return Color array
     */
    private Color[] getColorsByNums(byte[] answers) {
        
        Color[] c = new Color[4];
        
        for(int i=0; i<4; i++) {
            switch(answers[i]) {
                
                case 1: 
                    c[i] = Color.RED;
                    break;
                case 2: 
                    c[i] = Color.ORANGE;
                    break;
                case 3: 
                    c[i] = Color.YELLOW;
                    break;
                case 4: 
                    c[i] = Color.GREEN;
                    break;
                case 5: 
                    c[i] = Color.PINK;
                    break;
                case 6: 
                    c[i] = Color.BLUE;
                    break;
                default: 
                    c[i] = Color.web("#878282");
                    break;
            }
        }
        
        return c;
    }
    
//    enum Colors {
//        
//        RED, ORANGE, YELLOW, GREEN, PINK, BLUE
//    }
}
