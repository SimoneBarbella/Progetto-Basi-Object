package com.unina.foodlab.Boundary.util.cells;

import com.unina.foodlab.Entity.SessionePresenza;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.function.Consumer;

public class QuantitaTotaleButtonCell extends TableCell<SessionePresenza, String> {

    private final Button btn = new Button();
    private final Consumer<SessionePresenza> onOpen;

    public QuantitaTotaleButtonCell(Consumer<SessionePresenza> onOpen) {
        this.onOpen = onOpen;
        btn.getStyleClass().add("secondary-button");
        btn.setText("Quantità totale");
        btn.setOnAction(e -> {
            if (getTableView() == null || getTableView().getItems() == null) return;
            if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) return;
            SessionePresenza sessione = getTableView().getItems().get(getIndex());
            if (sessione != null) {
                this.onOpen.accept(sessione);
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || getIndex() < 0) {
            setGraphic(null);
            return;
        }
        if (getTableView() == null || getTableView().getItems() == null || getIndex() >= getTableView().getItems().size()) {
            setGraphic(null);
            return;
        }

        SessionePresenza sessione = getTableView().getItems().get(getIndex());
        btn.setDisable(sessione == null || sessione.getNumAderenti() <= 0);
        setGraphic(btn);
    }
}
