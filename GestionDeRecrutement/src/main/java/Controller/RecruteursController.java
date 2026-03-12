package Controller;

import Entity.Recruteur;
import Entity.Role;
import Service.RecruteurRepository;
import Service.ServiceRecruteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class RecruteursController {

    @FXML private TableView<Recruteur> table;
    @FXML private TableColumn<Recruteur, String> colNom;
    @FXML private TableColumn<Recruteur, String> colEmail;
    @FXML private TableColumn<Recruteur, String> colRole;
    @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;

    private final RecruteurRepository service = new ServiceRecruteur();
    private final ObservableList<Recruteur> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNomComplet()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colRole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole().name()));

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
        openForm(null).ifPresent(r -> {
            Integer id = service.create(r);
            if (id != null) refresh();
        });
    }

    @FXML
    public void onEdit() {
        Recruteur selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        openForm(selected).ifPresent(updated -> {
            updated.setId(selected.getId());
            if (service.update(updated)) refresh();
        });
    }

    @FXML
    public void onDelete() {
        Recruteur selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        if (confirm("Supprimer le recruteur ?", "Action définitive.")) {
            if (service.deleteById(selected.getId())) refresh();
        }
    }

    private Optional<Recruteur> openForm(Recruteur existing) {
        Dialog<Recruteur> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Nouveau recruteur" : "Modifier recruteur");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField tfNom   = new TextField(existing == null ? "" : existing.getNomComplet());
        TextField tfEmail = new TextField(existing == null ? "" : existing.getEmail());
        ComboBox<Role> cbRole = new ComboBox<>(FXCollections.observableArrayList(Role.values()));
        if (existing != null) cbRole.setValue(existing.getRole());

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Nom complet *"), tfNom);
        grid.addRow(1, new Label("Email *"),       tfEmail);
        grid.addRow(2, new Label("Rôle *"),        cbRole);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                if (tfNom.getText().isBlank() || tfEmail.getText().isBlank() || cbRole.getValue() == null) {
                    showError("Tous les champs sont obligatoires.");
                    return null;
                }
                Recruteur r = new Recruteur();
                r.setNomComplet(tfNom.getText().trim());
                r.setEmail(tfEmail.getText().trim());
                r.setRole(cbRole.getValue());
                return r;
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
