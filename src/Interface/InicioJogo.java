package Interface;

import Client.MasterMindGame;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Inicia o Jogo
 * 
 * @author Acer
 */
public class InicioJogo extends Application {
    
    private MasterMindGame MMGame;
    private TextField ipTf;
    private TextField portTf;
    private Label errorMsg;
    private Stage gameStage;
    private Stage connectStage;
    
    /**
     * Inicia a janela de conexão para obter os dados para conectar ao servidor
     * O botão Conectarchama o método onConnect para verificar se o endereço IP e a porta fornecidos são válidos.
     * 
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        //cria 2 janelas stage
        gameStage = primaryStage;
        connectStage = new Stage();
        
        //criar containers e controlos
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        Label txt = new Label("Conecção Mastermind");
        Label ipLbl = new Label("Endereço IP");
        ipTf = new TextField("");
        Label portLbl = new Label("Porta:");
        portTf = new TextField("");
        
        Button connectBtn = new Button("Conectar");
        connectBtn.setOnAction(event -> onConnect());
        
        errorMsg = new Label("");
        errorMsg.setTextFill(Color.RED);
        
        //colocar os controlos na grid deifinindo qual a coluna e a linha
        grid.setPadding(new Insets(25, 25, 10, 25));
        grid.add(txt, 0, 0);
        grid.add(ipLbl, 0, 1);
        grid.add(ipTf, 1, 1);
        grid.add(portLbl, 0, 2);
        grid.add(portTf, 1, 2);
        grid.add(connectBtn, 1, 4);
        grid.add(errorMsg, 1, 5);
        
        //define definições da stage, adiciona a grid, e mostra
        connectStage.setTitle("Conecção Mastermind");
        connectStage.setResizable(false);
        connectStage.setScene(new Scene(grid));
        connectStage.show();
    }
    
    /**
     * Verifica se os campos inseridos da conexão são válidos
     * Oculta a janela de conexão e inicia a janela do jogo
     */
    private void onConnect() {
        
        MMGame = new MasterMindGame();
        try{
            String ipAddress = ipTf.getText();
            int portNum = -1;
            
            try {  portNum = Integer.parseInt(portTf.getText()); }
            catch(NumberFormatException e) {
                errorMsg.setText("Porta apenas contém números.");
            }
            MMGame.connect(ipAddress, portNum);
            

            connectStage.hide();
            System.out.println("Conectado ao Endereço IP: " + ipAddress);
            initGame();
        }
        catch(IOException e) {
            errorMsg.setText("Não foi possível conectar ao servidor.");
        }
    }
    
    /**
     * Obtem o ficheiro fxml para ser instanciada a cena
     * Obtem o controller do fxml
     * Incia a janela do jogo e o jogo
     * 
     * @param ip
     * @param port 
     */
    private void initGame() {
        
        try {
            //open game window with fxml
            URL path = Paths.get("./src/fxml/JanelaJogo.fxml").toUri().toURL();
            
            //set fxml path in loader
            //give builder factory for suitable instance constructing
            FXMLLoader gameLoader = new FXMLLoader();
            gameLoader.setLocation(path);
            gameLoader.setBuilderFactory(new JavaFXBuilderFactory());
            //load into scene
            Scene gameScene = new Scene(gameLoader.load());
            //get controller from fxmlloader to use Controller class
            Controller controller = gameLoader.getController();
            controller.setMMGame(MMGame);
            controller.launchNewGame();
            
            //set settings on stage, add scene, and show
            gameStage.setTitle("Mastermind");
            gameStage.setScene(gameScene);
            gameStage.setResizable(false);
            gameStage.show();
        }
        catch(IOException e) {
            System.out.println("Erro: falha no carregamento do jogo.\n"+e.getMessage());
            System.exit(1);
        }
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
