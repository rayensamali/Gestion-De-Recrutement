package Controller;

import Entity.CategorieCompetence;
import Entity.Competence;
import Entity.Niveau;
import Service.CompetenceRepository;
import Service.ServiceCompetence;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompetencesController {

    @FXML private TableView<Competence> table;
    @FXML private TableColumn<Competence, Number> colId;
    @FXML private TableColumn<Competence, String> colNom;
    @FXML private TableColumn<Competence, String> colCategorie;
    @FXML private TableColumn<Competence, String> colNiveau;

    @FXML private Button btnAdd, btnEdit, btnDelete, btnRefresh;
    @FXML private TextField tfSearch;
    @FXML private ComboBox<CategorieCompetence> cbFilterCategorie;

    private final CompetenceRepository service = new ServiceCompetence();
    private final ObservableList<Competence> allData  = FXCollections.observableArrayList();
    private final ObservableList<Competence> viewData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        colCategorie.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategorie().name()));
        colNiveau.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNiveau().name()));

        table.setItems(viewData);

        btnEdit.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        btnDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));

        cbFilterCategorie.setItems(FXCollections.observableArrayList(CategorieCompetence.values()));
        cbFilterCategorie.getItems().add(0, null);
        cbFilterCategorie.setPromptText("Toutes catégories");

        tfSearch.textProperty().addListener((obs, old, val) -> applyFilter());
        cbFilterCategorie.valueProperty().addListener((obs, old, val) -> applyFilter());

        // Double-clic pour éditer
        table.setRowFactory(tv -> {
            TableRow<Competence> row = new TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (evt.getClickCount() == 2 && !row.isEmpty()) onEdit();
            });
            return row;
        });

        refresh();
    }

    @FXML public void onRefresh() { refresh(); }

    private void refresh() {
        allData.setAll(service.findAll());
        applyFilter();
    }

    private void applyFilter() {
        String search = tfSearch == null ? "" : tfSearch.getText().trim().toLowerCase();
        CategorieCompetence cat = cbFilterCategorie == null ? null : cbFilterCategorie.getValue();

        List<Competence> filtered = allData.stream()
                .filter(c -> search.isEmpty() || c.getNom().toLowerCase().contains(search))
                .filter(c -> cat == null || c.getCategorie() == cat)
                .collect(Collectors.toList());
        viewData.setAll(filtered);
    }

    @FXML
    public void onAdd() {
        openForm(null).ifPresent(c -> {
            Integer id = service.create(c);
            if (id != null) refresh();
        });
    }

    @FXML
    public void onEdit() {
        Competence selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        openForm(selected).ifPresent(updated -> {
            updated.setId(selected.getId());
            if (service.update(updated)) refresh();
        });
    }

    @FXML
    public void onDelete() {
        Competence selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        if (confirm("Supprimer la compétence ?", "Action définitive.")) {
            if (service.deleteById(selected.getId())) refresh();
        }
    }

    private Optional<Competence> openForm(Competence existing) {
        Dialog<Competence> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Nouvelle compétence" : "Modifier compétence");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField tfNom = new TextField(existing == null ? "" : existing.getNom());
        ComboBox<CategorieCompetence> cbCat = new ComboBox<>(FXCollections.observableArrayList(CategorieCompetence.values()));
        ComboBox<Niveau> cbNiv = new ComboBox<>(FXCollections.observableArrayList(Niveau.values()));
        if (existing != null) { cbCat.setValue(existing.getCategorie()); cbNiv.setValue(existing.getNiveau()); }

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Nom *"),      tfNom);
        grid.addRow(1, new Label("Catégorie *"), cbCat);
        grid.addRow(2, new Label("Niveau *"),    cbNiv);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                if (tfNom.getText().isBlank() || cbCat.getValue() == null || cbNiv.getValue() == null) {
                    showError("Tous les champs sont obligatoires.");
                    return null;
                }
                Competence c = new Competence();
                c.setNom(tfNom.getText().trim());
                c.setCategorie(cbCat.getValue());
                c.setNiveau(cbNiv.getValue());
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
