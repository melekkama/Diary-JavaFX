package diary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static diary.diaryCoverController.*;

public class diaryController {
    public CheckBox darkModeCheckBox;
    public TextArea contentBox;
    public Button loadbtn;
    public Button savebtn;
    public DatePicker datePick;
    public AnchorPane background;
    public TextArea titleBox;
    public MenuItem feedbackMenu;
    public MenuItem saveFileMenu;
    public MenuItem exitMenu;
    public ToggleGroup fontGroup;

    private List<DiaryPage> book;
    private String oldContent="";
    private String oldDate;
    static Scene scene;

    public void initialize(){
        //constraint title 
        titleBox.setTextFormatter( new TextFormatter<>(c -> {
            if(c.getControlNewText().length() > 30 ) return null;
            boolean error=false;
            char[] chars = c.getText().toCharArray();
            for (char aChar : chars)
                if (aChar == '\n' ) {
                    error = true;
                    break;
                }
            if (error) return null;
            else return c;
        }));
        //default settings
        formatter.applyPattern("dd/MM/yyyy");
        datePick.getEditor().setText(formatter.format(date));
        //arrayList to store each diaryPage and every words in dictionary
        book= new ArrayList<>();
        //read from txt file for existing diary pages and dictionary
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/diary/content.txt")));
            String line= reader.readLine();
            while (line != null){
                String date,title,content="";
                int lineCount;
                date = line;
                title = reader.readLine();
                lineCount = Integer.parseInt(reader.readLine());
                for(int i=0;i<lineCount;i++){
                    line = reader.readLine();
                    //noinspection StringConcatenationInLoop
                    content = content + line + System.lineSeparator();
                }
                book.add(new DiaryPage(date,title,content,lineCount));
                line = reader.readLine();
            }
            reader.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        //save new diary into txt file after user exit the application
        Main.window.setOnHiding(event -> saveFile());
    }

    @FXML
    private void loadDiary(){
    	Alert alert;
        String datePicked = datePick.getEditor().getText();
        //check if there is diary page to be loaded on that day
        boolean found=false;
        for (DiaryPage page : book) {
            if(page.getDate().equals(datePicked)){
                titleBox.setText(page.getTitle());
                contentBox.setText(page.getContent());
                found=true;
                break;
            }
        }
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Load Diary Status");
        alert.setHeaderText(null);
        if(found)
            alert.setContentText("Diary successfully loaded!");

        else{
            alert.setContentText("No diary content found on "+datePicked+" !");
            titleBox.setText("");
            contentBox.setText("");
        }
        oldContent=contentBox.getText();
        oldDate=datePick.getEditor().getText();
        alert.showAndWait();
        //make the spell check text box empty
    }

   
    public void saveDiary() {
        boolean found=false;
        String title = titleBox.getText();
        if (title.equals("")){
            title = "Untitled";
            titleBox.setText(title);
        }
        String content = contentBox.getText();
        if(content.equals("")){
            content="Empty Content";
            contentBox.setText(content);
        }
        content += System.lineSeparator();
        int lineCount=0;
        char[] chars = content.toCharArray();
        for (char c : chars)
            if (c == '\n')
                lineCount++;
        for (DiaryPage page : book)
            if (page.getDate().equals(datePick.getEditor().getText())) {
            	page.setDiary(datePick.getEditor().getText(), title, content, lineCount);
                found = true;
                break;
            }
        if(!found) book.add(0,new DiaryPage(datePick.getEditor().getText(), title, content, lineCount));
        oldContent=contentBox.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Diary Status");
        alert.setHeaderText(null);
        alert.setContentText("Diary successfully saved");
        alert.showAndWait();
    }

    public void goDark() {
        //swap between themes
        if(darkModeCheckBox.isSelected()){
            diaryScene.getStylesheets().clear();
            diaryScene.getStylesheets().add(getClass().getResource("/extra/dark_theme.css").toExternalForm());
        }
        else{
            diaryScene.getStylesheets().clear();
            diaryScene.getStylesheets().add(getClass().getResource("/extra/light_theme.css").toExternalForm());
        }
    }

    public void feedback() {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            //link to google form
            desktop.browse(new URI("https://linktr.ee/melekkm"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* public void changePassword() {
    	
    }*/

    @FXML
    private void changePassword() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("password.fxml"));
        scene= new Scene(root,800,502);
        scene.getStylesheets().add(getClass().getResource("/extra/light_theme.css").toExternalForm());
        scene.getRoot().requestFocus();
        Main.window.setScene(scene);

    }
    public void exit() { Main.window.hide(); }

    public void saveFile() {
        try {   //write to the same txt file
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/diary/content.txt")));
            for (DiaryPage aBook : book) {  //format output so it can be read properly next time
                writer.write(aBook.getDate() + System.lineSeparator());
                writer.write(aBook.getTitle() + System.lineSeparator());
                writer.write(aBook.getLineCount() + System.lineSeparator());
                writer.write(aBook.getContent());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeFont() {
        RadioMenuItem selectedRadioButton = (RadioMenuItem) fontGroup.getSelectedToggle();
        contentBox.setFont(Font.font(selectedRadioButton.getText()));
    }
}

class DiaryPage{
    //constructor
    DiaryPage(String date, String title, String content, int lineCount){
        setDiary(date,title,content,lineCount);
    }
    void setDiary(String date, String title, String content, int lineCount){
        this.date=date;
        this.title=title;
        this.content=content;
        this.lineCount=lineCount;
    }

    String getDate() {return this.date;}
    String getTitle() {return this.title;}
    String getContent() {return this.content;}
    int getLineCount() {return this.lineCount;}

    private String date;
    private String title;
    private String content;
    private int lineCount;
}