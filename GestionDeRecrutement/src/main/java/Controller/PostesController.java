package Controller;

import Entity.*;
import Service.PosteRepository;
import Service.ServicePoste;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;
public class PostesController {

        @FXML private TableView<Poste> table;
        @FXML private TableColumn<Poste, String> colIntitule;
        @FXML private TableColumn<Poste, String> colDepartement;
        @FXML private TableColumn<Poste, String> colGrade;
        @FXML private TableColumn<Poste, String> colMode;
        @FXML private TableColumn<Poste, String> colContrat;
        @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;

        private final PosteRepository service = new ServicePoste();
        private final ObservableList<Poste> data = FXCollections.observableArrayList();

        @FXML public void initialize(){
            colIntitule.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getIntitule()));
            colDepartement.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getDepartement()));
            colGrade.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getGrade().name()));
            colMode.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getModeTravail().name()));
            colContrat.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getTypeContrat().name()));
            table.setItems(data);
            refresh();
            table.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->updateButtons());
            updateButtons();
        }

        @FXML public void onRefresh(){ refresh(); }
        private void refresh(){ data.setAll(service.findAll()); }
        private void updateButtons(){ boolean sel = table.getSelectionModel().getSelectedItem()!=null; btnEdit.setDisable(!sel); btnDelete.setDisable(!sel); }

        @FXML public void onAdd(){
            openForm(null).ifPresent(p->{
                Integer id = service.create(p);
                if (id != null) refresh();
            });
        }
        @FXML public void onEdit(){
            Poste p = table.getSelectionModel().getSelectedItem();
            if (p==null) return;
            openForm(p).ifPresent(updated->{
                updated.setId(p.getId());
                if (service.update(updated)) refresh();
            });
        }
        @FXML public void onDelete(){
            Poste p = table.getSelectionModel().getSelectedItem();
            if (p==null) return;
            if (confirm("Supprimer le poste ?", "Action définitive")) {
                if (service.deleteById(p.getId())) refresh();
            }
        }

        private Optional<Poste> openForm(Poste existing){
            Dialog<Poste> dialog = new Dialog<>();
            dialog.setTitle(existing==null? "Nouveau poste" : "Modifier poste");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField tfIntitule = new TextField(existing==null? "" : existing.getIntitule());
            TextField tfDep = new TextField(existing==null? "" : existing.getDepartement());
            ComboBox<GradePoste> cbGrade = new ComboBox<>(FXCollections.observableArrayList(GradePoste.values()));
            ComboBox<ModeTravail> cbMode = new ComboBox<>(FXCollections.observableArrayList(ModeTravail.values()));
            ComboBox<TypeContrat> cbContrat = new ComboBox<>(FXCollections.observableArrayList(TypeContrat.values()));
            if (existing!=null){ cbGrade.setValue(existing.getGrade()); cbMode.setValue(existing.getModeTravail()); cbContrat.setValue(existing.getTypeContrat()); }

            GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
            grid.addRow(0, new Label("Intitulé"), tfIntitule);
            grid.addRow(1, new Label("Département"), tfDep);
            grid.addRow(2, new Label("Grade"), cbGrade);
            grid.addRow(3, new Label("Mode de travail"), cbMode);
            grid.addRow(4, new Label("Type de contrat"), cbContrat);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt->{
                if (bt==ButtonType.OK){
                    if (tfIntitule.getText().isBlank() || tfDep.getText().isBlank() || cbGrade.getValue()==null || cbMode.getValue()==null || cbContrat.getValue()==null){
                        showError("Tous les champs sont obligatoires.");
                        return null;
                    }
                    Poste p = new Poste();
                    p.setIntitule(tfIntitule.getText().trim());
                    p.setDepartement(tfDep.getText().trim());
                    p.setGrade(cbGrade.getValue());
                    p.setModeTravail(cbMode.getValue());
                    p.setTypeContrat(cbContrat.getValue());
                    return p;
                }
                return null;
            });
            return dialog.showAndWait();
        }

        private boolean confirm(String header, String content) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
            a.setHeaderText(header);
            return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
        }
        private void showError(String msg) {
            Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            a.setHeaderText("Erreur");
            a.showAndWait();
        }
    }
