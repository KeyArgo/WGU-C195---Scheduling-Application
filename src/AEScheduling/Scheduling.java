
package AEScheduling;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBLink;

/**
 *
 * @author Daniel LaForce
 */
public class Scheduling extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr"));//set default language to french, comment out when not testing
        
        //Open connection to the database
        DBLink.startConnection();
        launch(args);
        
        //Close connection to the database
        DBLink.closeConnection();

    }

}
