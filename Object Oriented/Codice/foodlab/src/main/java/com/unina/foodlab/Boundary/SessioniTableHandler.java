package com.unina.foodlab.Boundary;

import com.unina.foodlab.Boundary.util.FormatiDataOra;
import com.unina.foodlab.Boundary.util.cells.QuantitaTotaleButtonCell;
import com.unina.foodlab.Controller.GestoreRicette;
import com.unina.foodlab.Controller.GestoreSessioni;
import com.unina.foodlab.Entity.Corso;
import com.unina.foodlab.Entity.Ricetta;
import com.unina.foodlab.Entity.Sessione;
import com.unina.foodlab.Entity.SessionePresenza;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class SessioniTableHandler {

    private final TableView<SessionePresenza> sessioniTable;
    private final TableColumn<SessionePresenza, String> dataCol;
    private final TableColumn<SessionePresenza, Integer> aderentiCol;
    private final TableColumn<SessionePresenza, String> ricetteCol;
    private final TableColumn<SessionePresenza, String> quantitaTotaleCol;

    private final Map<Integer, String> ricettePerSessione = new HashMap<>();
    private final Map<Integer, String> quantitaTotalePerSessione = new HashMap<>();

    private final Consumer<SessionePresenza> onApriListaSpesa;

    SessioniTableHandler(
        TableView<SessionePresenza> sessioniTable,
        TableColumn<SessionePresenza, String> dataCol,
        TableColumn<SessionePresenza, Integer> aderentiCol,
        TableColumn<SessionePresenza, String> ricetteCol,
        TableColumn<SessionePresenza, String> quantitaTotaleCol,
        Consumer<SessionePresenza> onApriListaSpesa
    ) {
        this.sessioniTable = sessioniTable;
        this.dataCol = dataCol;
        this.aderentiCol = aderentiCol;
        this.ricetteCol = ricetteCol;
        this.quantitaTotaleCol = quantitaTotaleCol;
        this.onApriListaSpesa = onApriListaSpesa;
    }

    void initialize(Consumer<SessionePresenza> onSelectionChanged) {
        setupTableColumns();
        setupSelectionListeners(onSelectionChanged);
    }

    SessionePresenza caricaEApplica(Corso corso, SessionePresenza currentlySelected) {
        if (corso == null) return null;

        Integer previousSelectedSessionId = currentlySelected != null ? currentlySelected.getIdSessione() : null;

        List<Sessione> sessioni = GestoreSessioni.getInstanza().getSessioniByCorso(corso);
        List<SessionePresenza> pratiche = sessioni.stream()
            .filter(s -> s instanceof SessionePresenza)
            .map(s -> (SessionePresenza) s)
            .collect(Collectors.toList());

        aggiornaDatiDerivati(pratiche);

        if (sessioniTable != null) {
            sessioniTable.setItems(FXCollections.observableArrayList(pratiche));
            ripristinaSelezione(pratiche, previousSelectedSessionId);
            sessioniTable.refresh();
            return sessioniTable.getSelectionModel().getSelectedItem();
        }

        return currentlySelected;
    }

    private void setupTableColumns() {
        if (dataCol != null) {
            dataCol.setCellValueFactory(c -> {
                LocalDateTime d = c.getValue().getOraInizio();
                String val = d != null ? d.format(FormatiDataOra.YYYY_MM_DD_HH_MM) : "";
                return new SimpleStringProperty(val);
            });
        }
        if (aderentiCol != null) {
            aderentiCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getNumAderenti()));
        }
        if (ricetteCol != null) {
            ricetteCol.setCellValueFactory(c -> {
                Integer id = c.getValue().getIdSessione();
                String val = (id != null) ? ricettePerSessione.getOrDefault(id, "") : "";
                return new SimpleStringProperty(val);
            });
        }
        if (quantitaTotaleCol != null) {
            quantitaTotaleCol.setCellValueFactory(c -> {
                Integer id = c.getValue().getIdSessione();
                String val = (id != null) ? quantitaTotalePerSessione.getOrDefault(id, "") : "";
                return new SimpleStringProperty(val);
            });
            quantitaTotaleCol.setCellFactory(col -> new QuantitaTotaleButtonCell(onApriListaSpesa));
        }
    }

    private void setupSelectionListeners(Consumer<SessionePresenza> onSelectionChanged) {
        if (sessioniTable == null || onSelectionChanged == null) return;

        sessioniTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> onSelectionChanged.accept(newVal));
        sessioniTable.setOnMouseClicked(e -> onSelectionChanged.accept(sessioniTable.getSelectionModel().getSelectedItem()));
    }

    private void ripristinaSelezione(List<SessionePresenza> pratiche, Integer previousSelectedSessionId) {
        if (sessioniTable == null) return;
        if (pratiche == null || pratiche.isEmpty()) return;

        boolean restored = false;
        if (previousSelectedSessionId != null) {
            for (SessionePresenza s : pratiche) {
                if (s != null && s.getIdSessione() != null && s.getIdSessione().equals(previousSelectedSessionId)) {
                    sessioniTable.getSelectionModel().select(s);
                    restored = true;
                    break;
                }
            }
        }
        if (!restored) {
            sessioniTable.getSelectionModel().selectFirst();
        }
    }

    private void aggiornaDatiDerivati(List<SessionePresenza> sessioni) {
        ricettePerSessione.clear();
        quantitaTotalePerSessione.clear();
        if (sessioni == null) return;

        for (SessionePresenza s : sessioni) {
            Integer id = s.getIdSessione();
            if (id == null) continue;

            List<Ricetta> ricette = GestoreRicette.getInstanza().getRicettePerSessione(id);
            String nomi = ricette.stream()
                .map(Ricetta::getNome)
                .filter(n -> n != null && !n.isBlank())
                .collect(Collectors.joining(", "));
            ricettePerSessione.put(id, nomi);

            java.math.BigDecimal tot = GestoreSessioni.getInstanza().getQuantitaTotaleBySessioneId(id);
            String totStr = tot != null ? tot.stripTrailingZeros().toPlainString() : "0";
            quantitaTotalePerSessione.put(id, totStr);
        }
    }
}
