/*package Controller;

import Entity.CategorieCompetence;
import Entity.Competence;
import Entity.Niveau;
import Service.CompetenceRepository;
import Service.ServiceCompetence;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CompetencesController {

    // Table & colonnes
    @FXML private TableView<Competence> table;
    @FXML private TableColumn<Competence, Number> colId;
    @FXML private TableColumn<Competence, String> colNom;
    @FXML private TableColumn<Competence, String> colCategorie;
    @FXML private TableColumn<Competence, String> colNiveau;

    // Barre d’actions
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;

    // Recherche / filtre
    @FXML private TextField tfSearch; // fx:id dans FXML
    @FXML private ComboBox<CategorieCompetence> cbFilterCategorie; // fx:id dans FXML

    // Service & données
    private final CompetenceRepository service = new ServiceCompetence();
    private final ObservableList<Competence> backingData = FXCollections.observableArrayList(); // données brutes DB
    private final ObservableList<Competence> viewData = FXCollections.observableArrayList();    // données filtrées

    @FXML
    public void initialize() {
        // Mapping des colonnes
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colNom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNom()));
        colCategorie.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategorie().name()));
        colNiveau.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNiveau().name()));

        table.setItems(viewData);

        // Désactiver les boutons si aucune ligne sélectionnée
        btnEdit.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        btnDelete.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));

        // Préparer combo de filtre catégorie
        cbFilterCategorie.setItems(FXCollections.observableArrayList(CategorieCompetence.values()));
        cbFilterCategorie.getItems().add(0, null); // option "toutes catégories"
        cbFilterCategorie.setPromptText("Catégorie (toutes)");

        // Listeners recherche/filtre
        tfSearch.textProperty().addListener((obs, old, val) -> applyFilter());
        cbFilterCategorie.valueProperty().addListener((obs, old, val) -> applyFilter());

        // Entrée dans la barre de recherche
        tfSearch.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                applyFilter();
            }
        });

        // Double-clic pour éditer
        table.setRowFactory(tv -> {
            TableRow<Competence> row = newRow<>();
            row.setOnMouseClicked(evt -> {
                if (evt.getClickCount() == 2 && !row.isEmpty()) {
                    onEdit();
                }
            });
            return row;
        });

        refresh();
    }

*/