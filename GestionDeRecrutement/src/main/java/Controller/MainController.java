package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML private StackPane contentPane;
    @FXML private Button btnCandidats;
    @FXML private Button btnCandidatures;
    @FXML private Button btnEntretiens;
    @FXML private Button btnCompetences;
    @FXML private Button btnCompetencePoste;
    @FXML private Button btnPostes;
    @FXML private Button btnRecruteurs;

    private Button activeBtn;

    @FXML public void showCandidats()      { load("/candidats.fxml", btnCandidats); }
    @FXML public void showCandidatures()   { load("/candidatures.fxml", btnCandidatures); }
    @FXML public void showEntretiens()     { load("/entretiens.fxml", btnEntretiens); }
    @FXML public void showCompetences()    { load("/competences.fxml", btnCompetences); }
    @FXML public void showCompetencePoste(){ load("/competence_poste.fxml", btnCompetencePoste); }
    @FXML public void showPostes()         { load("/postes.fxml", btnPostes); }
    @FXML public void showRecruteurs()     { load("/recruteurs.fxml", btnRecruteurs); }

    private void load(String fxml, Button btn) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(node);
            // highlight active nav button
            if (activeBtn != null) activeBtn.getStyleClass().remove("nav-btn-active");
            btn.getStyleClass().add("nav-btn-active");
            activeBtn = btn;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
