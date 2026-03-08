package Controller;

import Entity.*;
import Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CandidaturesController {

        @FXML private TableView<Candidature> table;
        @FXML private TableColumn<Candidature, String> colCode;
        @FXML private TableColumn<Candidature, String> colCandidat;
        @FXML private TableColumn<Candidature, String> colPoste;
        @FXML private TableColumn<Candidature, String> colPhase;
        @FXML private TableColumn<Candidature, String> colDecision;
        @FXML private TableColumn<Candidature, LocalDate> colDateCand;
        @FXML private TableColumn<Candidature, LocalDate> colDateDecision;
        @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;

        private final CandidatureRepository service = new ServiceCandidature();
        private final CandidatRepository candidatService = new ServiceCandidat();
        private final PosteRepository posteService = new ServicePoste();

        private final ObservableList<Candidature> data = FXCollections.observableArrayList();
        private Map<Integer, String> candidatNamesById;
        private Map<Integer, String> posteNamesById;

        @FXML public void initialize(){
            colCode.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(Objects.toString(c.getValue().getCode(),"")));
            colCandidat.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(nameForCandidat(c.getValue().getCandidatId())));
            colPoste.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(nameForPoste(c.getValue().getPosteId())));
            colPhase.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhase().name()));
            colDecision.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(
                    c.getValue().getDecisionFinale()==null? "" : c.getValue().getDecisionFinale().name()));
            colDateCand.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDateCandidature()));
            colDateDecision.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDateDecision()));

            table.setItems(data);
            refresh();
            table.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->updateButtons());
            updateButtons();
        }

        private String nameForCandidat(Integer id){
            return id==null? "" : candidatNamesById.getOrDefault(id, "#"+id);
        }
        private String nameForPoste(Integer id){
            return id==null? "" : posteNamesById.getOrDefault(id, "#"+id);
        }

        @FXML public void onRefresh(){ refresh(); }
        private void refresh(){
            candidatNamesById = candidatService.findAll().stream().collect(Collectors.toMap(Candidat::getId, Candidat::getNomComplet));
            posteNamesById = posteService.findAll().stream().collect(Collectors.toMap(Poste::getId, Poste::getIntitule));
            data.setAll(service.findAll());
        }
        private void updateButtons(){ boolean sel = table.getSelectionModel().getSelectedItem()!=null; btnEdit.setDisable(!sel); btnDelete.setDisable(!sel); }

        @FXML public void onAdd(){
            openForm(null).ifPresent(c->{
                Integer id = service.create(c);
                if (id != null) refresh();
            });
        }
        @FXML public void onEdit(){
            Candidature c = table.getSelectionModel().getSelectedItem();
            if (c==null) return;
            openForm(c).ifPresent(updated->{
                updated.setId(c.getId());
                if (service.update(updated)) refresh();
            });
        }
        @FXML public void onDelete(){
            Candidature c = table.getSelectionModel().getSelectedItem();
            if (c==null) return;
            if (confirm("Supprimer la candidature ?", "Action définitive")) {
                if (service.deleteById(c.getId())) refresh();
            }
        }

        private Optional<Candidature> openForm(Candidature existing){
            Dialog<Candidature> dialog = new Dialog<>();
            dialog.setTitle(existing==null? "Nouvelle candidature" : "Modifier candidature");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField tfCode = new TextField(existing==null? "" : Objects.toString(existing.getCode(),""));

            ComboBox<Candidat> cbCandidat = new ComboBox<>(FXCollections.observableArrayList(candidatService.findAll()));
            cbCandidat.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Candidat c, boolean empty){ super.updateItem(c, empty); setText(empty||c==null? "" : c.getNomComplet());}});
            cbCandidat.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Candidat c, boolean empty){ super.updateItem(c, empty); setText(empty||c==null? "" : c.getNomComplet());}});

            ComboBox<Poste> cbPoste = new ComboBox<>(FXCollections.observableArrayList(posteService.findAll()));
            cbPoste.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Poste p, boolean empty){ super.updateItem(p, empty); setText(empty||p==null? "" : p.getIntitule());}});
            cbPoste.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Poste p, boolean empty){ super.updateItem(p, empty); setText(empty||p==null? "" : p.getIntitule());}});

            DatePicker dpDateCand = new DatePicker(existing==null? LocalDate.now() : existing.getDateCandidature());
            ComboBox<PhaseRecrutement> cbPhase = new ComboBox<>(FXCollections.observableArrayList(PhaseRecrutement.values()));
            ComboBox<ResultatScreening> cbScreen = new ComboBox<>(FXCollections.observableArrayList(ResultatScreening.values()));
            cbScreen.getItems().add(0, null); // autoriser vide
            ComboBox<DecisionFinale> cbDecision = new ComboBox<>(FXCollections.observableArrayList(DecisionFinale.values()));
            cbDecision.getItems().add(0, null);
            DatePicker dpDateDecision = new DatePicker(existing==null? null : existing.getDateDecision());

            if (existing!=null){
                // pré-sélection depuis ids
                cbCandidat.getSelectionModel().select(cbCandidat.getItems().stream().filter(c-> Objects.equals(c.getId(), existing.getCandidatId())).findFirst().orElse(null));
                cbPoste.getSelectionModel().select(cbPoste.getItems().stream().filter(p-> Objects.equals(p.getId(), existing.getPosteId())).findFirst().orElse(null));
                cbPhase.setValue(existing.getPhase());
                cbScreen.setValue(existing.getResultatScreening());
                cbDecision.setValue(existing.getDecisionFinale());
            }

            GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
            grid.addRow(0, new Label("Code"), tfCode);
            grid.addRow(1, new Label("Candidat"), cbCandidat);
            grid.addRow(2, new Label("Poste"), cbPoste);
            grid.addRow(3, new Label("Date candidature"), dpDateCand);
            grid.addRow(4, new Label("Phase"), cbPhase);
            grid.addRow(5, new Label("Résultat screening"), cbScreen);
            grid.addRow(6, new Label("Décision finale"), cbDecision);
            grid.addRow(7, new Label("Date décision"), dpDateDecision);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt->{
                if (bt==ButtonType.OK){
                    if (cbCandidat.getValue()==null || cbPoste.getValue()==null || cbPhase.getValue()==null || dpDateCand.getValue()==null){
                        showError("Candidat, Poste, Phase et Date sont obligatoires.");
                        return null;
                    }
                    Candidature c = new Candidature();
                    c.setCode(tfCode.getText().isBlank()? null : tfCode.getText().trim());
                    c.setCandidatId(cbCandidat.getValue().getId());
                    c.setPosteId(cbPoste.getValue().getId());
                    c.setDateCandidature(dpDateCand.getValue());
                    c.setPhase(cbPhase.getValue());
                    c.setResultatScreening(cbScreen.getValue());
                    c.setDecisionFinale(cbDecision.getValue());
                    c.setDateDecision(dpDateDecision.getValue());
                    return c;
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

