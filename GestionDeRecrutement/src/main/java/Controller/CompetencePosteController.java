package Controller;

import Entity.Competence;
import Entity.CompetencePoste;
import Entity.Niveau;
import Entity.Poste;
import Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;
public class CompetencePosteController {

        @FXML private TableView<CompetencePoste> table;
        @FXML private TableColumn<CompetencePoste, Number> colId;
        @FXML private TableColumn<CompetencePoste, Number> colCompetenceId;
        @FXML private TableColumn<CompetencePoste, Number> colPosteId;
        @FXML private TableColumn<CompetencePoste, String> colNiveau;
        @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;

        private final CompetencePosteRepository service = new ServiceCompetencePoste();
        private final CompetenceRepository competenceService = new ServiceCompetence();
        private final PosteRepository posteService = new ServicePoste();

        private final ObservableList<CompetencePoste> data = FXCollections.observableArrayList();
        private List<Competence> competences;
        private List<Poste> postes;

        @FXML public void initialize(){
            colId.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
            colCompetenceId.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getCompetenceId()));
            colPosteId.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPosteId()));
            colNiveau.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getNiveau().name()));

            table.setItems(data);
            refresh();
            table.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->updateButtons());
            updateButtons();
        }

        @FXML public void onRefresh(){ refresh(); }
        private void refresh(){
            competences = competenceService.findAll();
            postes = posteService.findAll();
            data.setAll(service.findAll());
        }
        private void updateButtons(){ boolean sel = table.getSelectionModel().getSelectedItem()!=null; btnEdit.setDisable(!sel); btnDelete.setDisable(!sel); }

        @FXML public void onAdd(){
            openForm(null).ifPresent(cp->{
                Integer id = service.create(cp);
                if (id != null) refresh();
            });
        }
        @FXML public void onEdit(){
            CompetencePoste cp = table.getSelectionModel().getSelectedItem();
            if (cp==null) return;
            openForm(cp).ifPresent(updated->{
                updated.setId(cp.getId());
                if (service.update(updated)) refresh();
            });
        }
        @FXML public void onDelete(){
            CompetencePoste cp = table.getSelectionModel().getSelectedItem();
            if (cp==null) return;
            if (confirm("Supprimer l'association ?", "Action définitive")) {
                if (service.deleteById(cp.getId())) refresh();
            }
        }

        private Optional<CompetencePoste> openForm(CompetencePoste existing){
            Dialog<CompetencePoste> dialog = new Dialog<>();
            dialog.setTitle(existing==null? "Associer compétence ↔ poste" : "Modifier association");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            ComboBox<Competence> cbComp = new ComboBox<>(FXCollections.observableArrayList(competences));
            cbComp.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Competence c, boolean empty){ super.updateItem(c, empty); setText(empty || c==null? "" : c.getNom());}});
            cbComp.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Competence c, boolean empty){ super.updateItem(c, empty); setText(empty || c==null? "" : c.getNom());}});
            ComboBox<Poste> cbPoste = new ComboBox<>(FXCollections.observableArrayList(postes));
            cbPoste.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Poste p, boolean empty){ super.updateItem(p, empty); setText(empty || p==null? "" : p.getIntitule());}});
            cbPoste.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Poste p, boolean empty){ super.updateItem(p, empty); setText(empty || p==null? "" : p.getIntitule());}});
            ComboBox<Niveau> cbNiv = new ComboBox<>(FXCollections.observableArrayList(Niveau.values()));

            if (existing!=null){
                // pré-sélection via id
                cbComp.setValue(competences.stream().filter(c-> c.getId().equals(existing.getCompetenceId())).findFirst().orElse(null));
                cbPoste.setValue(postes.stream().filter(p-> p.getId().equals(existing.getPosteId())).findFirst().orElse(null));
                cbNiv.setValue(existing.getNiveau());
            }

            GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
            grid.addRow(0, new Label("Compétence"), cbComp);
            grid.addRow(1, new Label("Poste"), cbPoste);
            grid.addRow(2, new Label("Niveau requis"), cbNiv);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt->{
                if (bt==ButtonType.OK){
                    if (cbComp.getValue()==null || cbPoste.getValue()==null || cbNiv.getValue()==null){
                        showError("Tous les champs sont obligatoires.");
                        return null;
                    }
                    CompetencePoste cp = new CompetencePoste();
                    cp.setCompetenceId(cbComp.getValue().getId());
                    cp.setPosteId(cbPoste.getValue().getId());
                    cp.setNiveau(cbNiv.getValue());
                    return cp;
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
