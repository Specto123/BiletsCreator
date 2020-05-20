package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.omg.CORBA.INTERNAL;

import java.io.*;
import java.util.*;


public class Controller {

    @FXML
    private TextArea txtArea;
    @FXML
    private Window mainStage;
    @FXML
    private TextArea txtShapka;
    @FXML
    private TextArea txtPodpisi;
    @FXML
    private TextField kolBilety;
    @FXML
    private TextField kolVoprosy;
    private String questions[];
    private LinkedList<Integer> ends = new LinkedList<>();

    public void fileChoose() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Word Files", "*.docx"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            readFile1(selectedFile);
        }

    }
    @FXML
    void readShapka()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Word Files", "*.docx"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(selectedFile));
            XWPFWordExtractor extract = new XWPFWordExtractor(doc);
            txtShapka.setText(extract.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void readPodpisi()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Word Files", "*.docx"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(selectedFile));
            XWPFWordExtractor extract = new XWPFWordExtractor(doc);
            txtPodpisi.setText(extract.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readFile1(File file) {
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
            XWPFWordExtractor extract = new XWPFWordExtractor(doc);
            txtArea.setText(txtArea.getText() + extract.getText());
            questions = txtArea.getText().split("\n");
            ends.offer(questions.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeFile() throws IOException {
        if(txtPodpisi.getText().equals("") || txtArea.getText().equals("") || txtPodpisi.getText().equals("") || kolVoprosy.getText().equals("") || kolBilety.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Ошибка");
            alert.setContentText("Заполните все поля");
            alert.showAndWait();
        }
        else
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Word Files", "*.docx"));
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(new File(String.valueOf(selectedFile)));
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            int size=Integer.valueOf(kolBilety.getText());
            String [] shapka = txtShapka.getText().split("\n");
            String [] podpisi = txtPodpisi.getText().split("\n");
            for(int j=0;j<size;j++)
            {
                int z=0;
                for(String str:shapka)
                {
                    run.setText(str);
                    run.addBreak();
                }
                run.addBreak();
                run.addBreak();
                for(int i=0;i<ends.size();i++)
                {
                    String[] listOfQuestions;
                    if(i==0)
                    {
                        listOfQuestions = generateQuestions(0,ends.get(0));
                    }
                    else
                    {
                        listOfQuestions = generateQuestions(ends.get(i-1),ends.get(i));
                    }
                    for(String question:listOfQuestions)
                    {
                        z++;
                        run.setText(String.valueOf(z));
                        run.setText(") ");
                        run.setText(question);
                        run.addBreak();
                    }
                }
                run.addBreak();
                for(String str:podpisi)
                {
                    run.setText(str);
                    run.addBreak();
                }
                run.addBreak();
                run.addBreak();
            }
            document.write(out);
            out.close();
        }
    }
    private String[] generateQuestions(int start,int end)
    {
        int size = Integer.valueOf(kolVoprosy.getText());
        ArrayList<String> listOfQuestions = new ArrayList<>();
        for(int i=start;i<end;i++)
        {
            listOfQuestions.add(questions[i]);
        }
        Collections.shuffle(listOfQuestions);
        while(listOfQuestions.size()>=size+1)
        {
            listOfQuestions.remove(0);
        }
        String [] temp=new String[listOfQuestions.size()];
        temp=listOfQuestions.toArray(temp);
        return temp;
    }
    @FXML
    void clear()
    {
        kolVoprosy.setText("");
        kolBilety.setText("");
        txtArea.setText("");
        txtPodpisi.setText("");
        txtShapka.setText("");
        questions=new String[0];
        ends.clear();
    }
}
//TODO сообщения о создании билетов и сообщения если поля пустые
