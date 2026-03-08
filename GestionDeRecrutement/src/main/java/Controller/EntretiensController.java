package Controller;

import Entity.*;
import Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
public class EntretiensController {

        @FXML private TableView<Entretien> table;
        @FXML private TableColumn<Entretien, String> colCode;
        @FXML private TableColumn<Entretien, String> colCandidat;
        @FXML private TableColumn<Entretien, String> colType;
        @FXML private TableColumn<Entretien, String> colStatut;
        @FXML private TableColumn<Entretien, LocalDate> colDate;
        @FXML private TableColumn<Entretien, String> colDebut;
        @FXML private TableColumn<Entretien, String> colFin;
        @FXML private TableColumn<Entretien, Number> colDuree;
        @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;

        private final EntretienRepository service = new ServiceEntretien();
        private final CandidatureRepository candidatureService = new ServiceCandidature();
        private final CandidatRepository candidatService = new ServiceCandidat();
        private final RecruteurRepository recruteurService = new ServiceRecruteur();

        private final ObservableList<Entretien> data = FXCollections.observableArrayList();
        private Map<Integer, String> candidatNamesById;
        private List<Candidature> candidatures;
        private List<Recruteur> recruteurs;

        @FXML public void initialize(){
            colCode.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(Objects.toString(c.getValue().getCode(),"")));
            colCandidat.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(nameForCandidat(c.getValue().getCandidatId())));
            colType.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getType().name()));
            colStatut.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatut().name()));
            colDate.setCellValueFactory(c-> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDateEntretien()));
            colDebut.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getHeureDebut().toString()));
            colFin.setCellValueFactory(c-> new javafx.beans.property.SimpleStringProperty(c.getValue().getHeureFin().toString()));
            colDuree.setCellValueFactory(c-> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getDureeMinutes()));

            table.setItems(data);
            refresh();
            table.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->updateButtons());
            updateButtons();
        }

        private String nameForCandidat(Integer id){
            return id==null? "" : candidatNamesById.getOrDefault(id, "#"+id);
        }

        @FXML public void onRefresh(){ refresh(); }
        private void refresh(){
            candidatures = candidatureService.findAll();
            recruteurs = recruteurService.findAll();
            candidatNamesById = candidatService.findAll().stream().collect(Collectors.toMap(Candidat::getId, Candidat::getNomComplet));
            data.setAll(service.findAll());
        }
        private void updateButtons(){ boolean sel = table.getSelectionModel().getSelectedItem()!=null; btnEdit.setDisable(!sel); btnDelete.setDisable(!sel); }

        @FXML public void onAdd(){
            openForm(null).ifPresent(e->{
                try {
                    Integer id = service.create(e);
                    if (id != null) refresh();
                } catch (IllegalStateException ex){
                    showError(ex.getMessage());
                }
            });
        }
        @FXML public void onEdit(){
            Entretien e = table.getSelectionModel().getSelectedItem();
            if (e==null) return;
            openForm(e).ifPresent(updated->{
                updated.setId(e.getId());
                try {
                    if (service.update(updated)) refresh();
                } catch (IllegalStateException ex){
                    showError(ex.getMessage());
                }
            });
        }
        @FXML public void onDelete(){
            Entretien e = table.getSelectionModel().getSelectedItem();
            if (e==null) return;
            if (confirm("Supprimer l'entretien ?", "Action définitive")) {
                if (service.deleteById(e.getId())) refresh();
            }
        }

        private Optional<Entretien> openForm(Entretien existing){
            Dialog<Entretien> dialog = new Dialog<>();
            dialog.setTitle(existing==null? "Planifier entretien" : "Modifier entretien");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField tfCode = new TextField(existing==null? "" : Objects.toString(existing.getCode(),""));

            // Candidature (inclut le candidat)
            ComboBox<Candidature> cbCand = new ComboBox<>(FXCollections.observableArrayList(candidatures));
            cbCand.setCellFactory(lv-> new ListCell<>() {
                @Override protected void updateItem(Candidature c, boolean empty) {
                    super.updateItem(c, empty);
                    String label = (empty || c==null) ? "" :
                            "["+Objects.toString(c.getCode(),"")+" ] "+ nameForCandidat(c.getCandidatId());
                    setText(label.trim());
                }
            });
            cbCand.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(Candidature c, boolean empty) {
                    super.updateItem(c, empty);
                    String label = (empty || c==null) ? "" :
                            "["+Objects.toString(c.getCode(),"")+" ] "+ nameForCandidat(c.getCandidatId());
                    setText(label.trim());
                }
            });

            DatePicker dpDate = new DatePicker(existing==null? LocalDate.now() : existing.getDateEntretien());
            TextField tfHeureDeb = new TextField(existing==null? "09:00" : existing.getHeureDebut().toString()); // format HH:MM
            TextField tfHeureFin = new TextField(existing==null? "10:00" : existing.getHeureFin().toString());
            Spinner<Integer> spDuree = new Spinner<>(15, 480, existing==null? 60 : existing.getDureeMinutes(), 15);

            ComboBox<TypeEntretien> cbType = new ComboBox<>(FXCollections.observableArrayList(TypeEntretien.values()));
            ComboBox<StatutEntretien> cbStatut = new ComboBox<>(FXCollections.observableArrayList(StatutEntretien.values()));
            if (existing!=null){ cbType.setValue(existing.getType()); cbStatut.setValue(existing.getStatut()); }

            // Recruteurs (facultatifs)
            ComboBox<Recruteur> cbTech = new ComboBox<>(FXCollections.observableArrayList(recruteurs));
            cbTech.setPromptText("Tech (optionnel)");
            cbTech.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Recruteur r, boolean empty){ super.updateItem(r, empty); setText(empty||r==null? "" : r.getNomComplet());}});
            cbTech.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Recruteur r, boolean empty){ super.updateItem(r, empty); setText(empty||r==null? "" : r.getNomComplet());}});
            ComboBox<Recruteur> cbRh = new ComboBox<>(FXCollections.observableArrayList(recruteurs));
            cbRh.setPromptText("RH (optionnel)");
            cbRh.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Recruteur r, boolean empty){ super.updateItem(r, empty); setText(empty||r==null? "" : r.getNomComplet());}});
            cbRh.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Recruteur r, boolean empty){ super.updateItem(r, empty); setText(empty||r==null? "" : r.getNomComplet());}});
            ComboBox<Recruteur> cbMgr = new ComboBox<>(FXCollections.observableArrayList(recruteurs));
            cbMgr.setPromptText("Manager (optionnel)");
            cbMgr.setCellFactory(lv-> new ListCell<>(){ @Override protected void updateItem(Recruteur r, boolean empty){ super.updateItem(r, empty); setText(empty||r==null? "" : r.getNomComplet());}});
            cbMgr.setButtonCell(new ListCell<>(){ @Override protected void updateItem(Recruteur r, boolean empty){ super.updateItem(r, empty); setText(empty||r==null? "" : r.getNomComplet());}});

            if (existing!=null){
                cbCand.getSelectionModel().select(candidatures.stream().filter(c-> Objects.equals(c.getId(), existing.getCandidatureId())).findFirst().orElse(null));
                cbTech.getSelectionModel().select(recruteurs.stream().filter(r-> Objects.equals(r.getId(), existing.getRecruteurTechniqueId())).findFirst().orElse(null));
                cbRh.getSelectionModel().select(recruteurs.stream().filter(r-> Objects.equals(r.getId(), existing.getRecruteurRhId())).findFirst().orElse(null));
                cbMgr.getSelectionModel().select(recruteurs.stream().filter(r-> Objects.equals(r.getId(), existing.getRecruteurManagerId())).findFirst().orElse(null));
            }

            GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
            grid.addRow(0, new Label("Code"), tfCode);
            grid.addRow(1, new Label("Candidature"), cbCand);
            grid.addRow(2, new Label("Type"), cbType);
            grid.addRow(3, new Label("Statut"), cbStatut);
            grid.addRow(4, new Label("Date"), dpDate);
            grid.addRow(5, new Label("Heure début (HH:MM)"), tfHeureDeb);
            grid.addRow(6, new Label("Heure fin (HH:MM)"), tfHeureFin);
            grid.addRow(7, new Label("Durée (min)"), spDuree);
            grid.addRow(8, new Label("Recruteur technique"), cbTech);
            grid.addRow(9, new Label("Recruteur RH"), cbRh);
            grid.addRow(10, new Label("Recruteur Manager"), cbMgr);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt->{
                if (bt==ButtonType.OK){
                    if (cbCand.getValue()==null || cbType.getValue()==null || cbStatut.getValue()==null || dpDate.getValue()==null){
                        showError("Candidature, Type, Statut, Date sont obligatoires.");
                        return null;
                    }
                    LocalTime hDeb, hFin;
                    try {
                        hDeb = LocalTime.parse(tfHeureDeb.getText().trim());
                        hFin = LocalTime.parse(tfHeureFin.getText().trim());
                    } catch (Exception ex) {
                        showError("Heure au format HH:MM (ex. 09:30).");
                        return null;
                    }
                    Entretien e = new Entretien();
                    e.setCode(tfCode.getText().isBlank()? null : tfCode.getText().trim());
                    e.setCandidatureId(cbCand.getValue().getId());
                    e.setCandidatId(cbCand.getValue().getCandidatId());
                    e.setType(cbType.getValue());
                    e.setStatut(cbStatut.getValue());
                    e.setDateEntretien(dpDate.getValue());
                    e.setHeureDebut(hDeb);
                    e.setHeureFin(hFin);
                    e.setDureeMinutes(spDuree.getValue());
                    e.setRecruteurTechniqueId(cbTech.getValue()==null? null : cbTech.getValue().getId());
                    e.setRecruteurRhId(cbRh.getValue()==null? null : cbRh.getValue().getId());
                    e.setRecruteurManagerId(cbMgr.getValue()==null? null : cbMgr.getValue().getId());
                    return e;
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

