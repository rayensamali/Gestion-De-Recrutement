/*package Controller;

import Entity.Candidat;
import Service.CandidatRepository;
import Service.ServiceCandidat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class CandidatsController {

        @FXML private TableView<Candidat> table;
        @FXML private TableColumn<Candidat, String> colNom;
        @FXML private TableColumn<Candidat, String> colEmail;
        @FXML private TableColumn<Candidat, String> colTel;
        @FXML private TableColumn<Candidat, String> colLinkedin;
        @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;

        private final CandidatRepository service = new ServiceCandidat();
        private final ObservableList<Candidat> data = FXCollections.observableArrayList();

        @FXML
        public void initialize() {
            colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNomComplet()));
            colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
            colTel.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelephone()));
            colLinkedin.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLinkedinUrl()));

            table.setItems(data);
            refresh();
            table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateButtons());
            updateButtons();
        }

        private void updateButtons() {
            boolean sel = table.getSelectionModel().getSelectedItem() != null;
            btnEdit.setDisable(!sel);
            btnDelete.setDisable(!sel);
        }

        @FXML public void onRefresh() { refresh(); }

        private void refresh() {
            data.setAll(service.findAll());
        }

        @FXML
        public void onAdd() {
            openForm(null).ifPresent(newOne -> {
                Integer id = service.create(newOne);
                if (id != null) refresh();
            });
        }

        @FXML
        public void onEdit() {
            Candidat selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            openForm(selected).ifPresent(updated -> {
                updated.setId(selected.getId());
                if (service.update(updated)) refresh();
            });
        }

        @FXML
        public void onDelete() {
            Candidat selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            if (confirm("Supprimer le candidat ?", "Cette action est définitive.")) {
                if (service.deleteById(selected.getId())) refresh();
            }
        }

        private Optional<Candidat> openForm(Candidat existing) {
            Dialog<Candidat> dialog = new Dialog<>();
            dialog.setTitle(existing == null ? "Nouveau candidat" : "Modifier candidat");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            TextField tfNom = new TextField(existing == null ? "" : existing.getNomComplet());
            TextField tfEmail = new TextField(existing == null ? "" : existing.getEmail());
            TextField tfTel = new TextField(existing == null ? "" : existing.getTelephone());
            TextField tfLinkedin = new TextField(existing == null ? "" : existing.getLinkedinUrl());

            GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
            grid.addRow(0, new Label("Nom complet"), tfNom);
            grid.addRow(1, new Label("Email"), tfEmail);
            grid.addRow(2, new Label("Téléphone"), tfTel);
            grid.addRow(3, new Label("LinkedIn"), tfLinkedin);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt -> {
                if (bt == ButtonType.OK) {
                    if (tfNom.getText().isBlank() || tfEmail.getText().isBlank()) {
                        showError("Nom et Email sont obligatoires.");
                        return null;
                    }
                    Candidat c = new Candidat();
                    c.setNomComplet(tfNom.getText().trim());
                    c.setEmail(tfEmail.getText().trim());
                    c.setTelephone(tfTel.getText().trim());
                    c.setLinkedinUrl(tfLinkedin.getText().trim());
                    return c;
                }
                return null;
            });
            return dialog.showWait();
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

*/