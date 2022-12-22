package diary;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class diaryCoverController {

    private String[] quote
            ={"True bravery is doing what is right even when it's not popular.",
            "The past cannot be changed. The future is yet in your power.",
            "Today, be the reason someone feels loved.",
            "Instead of complaining about your circumstances, get busy and create some new ones.",
            "Patience is not the ability to wait, but how you act while you're waiting.",
            "It takes some courage to stand up and speak; it takes even more courage to open your mind and listen."};

    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    static Date date=new Date();

    public Button btn0;
    public Text text0;
    public Text motiText;
    public Text time0;
    public PasswordField password;
    static Scene diaryScene;

    public void initialize(){
        motiText.setText(quote[(int) (Math.random() * quote.length)]);
        time0.setText(formatter.format(date));
    }
    public boolean checkPassword() {
    	String passwrd=password.getText();
    	if(!passwrd.equals("1234"))
    	{
    		Alert alert=new Alert(Alert.AlertType.WARNING);
              alert.setTitle("WARNING");
              alert.setHeaderText(null);
              alert.setContentText("Parolanız yanlış");
              ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
              alert.getButtonTypes().setAll(yesButton);
              alert.showAndWait();
              
              return false;
    	}
    	return true;
    }

    @FXML
    private void openDiary() throws Exception {
    	boolean result= checkPassword();
    	if(result == false) return; 
        Parent root = FXMLLoader.load(getClass().getResource("diary.fxml"));
        diaryScene= new Scene(root,800,502);
        diaryScene.getStylesheets().add(getClass().getResource("/extra/light_theme.css").toExternalForm());
        diaryScene.getRoot().requestFocus();
        Main.window.setScene(diaryScene);

    }

}
