package com.unina.foodlab.Boundary.util.cells;

import com.unina.foodlab.Entity.IngredienteQuantita;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.function.Consumer;

public class IngredienteQuantitaListCell extends ListCell<IngredienteQuantita> {

    private final Consumer<IngredienteQuantita> onRemove;

    public IngredienteQuantitaListCell(Consumer<IngredienteQuantita> onRemove) {
        this.onRemove = onRemove;
    }

    @Override
    protected void updateItem(IngredienteQuantita item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        String unita = item.getUnita() != null ? item.getUnita() : "";
        Label lbl = new Label(item.getNome() + " - " + item.getQuantita() + (unita.isEmpty() ? "" : (" " + unita)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("x");
        removeBtn.getStyleClass().add("secondary-button");
        removeBtn.setFocusTraversable(false);
        removeBtn.setOnAction(e -> onRemove.accept(item));

        setText(null);
        setGraphic(new HBox(10, lbl, spacer, removeBtn));
    }
}
