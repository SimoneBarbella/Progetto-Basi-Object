package com.unina.foodlab.Boundary.util.cells;

import com.unina.foodlab.Controller.GestoreRicette;
import com.unina.foodlab.Entity.Ricetta;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RicettaListCell extends ListCell<Ricetta> {

    private final Supplier<Integer> sessioneIdSupplier;
    private final Runnable onReload;
    private final Consumer<String> onError;
    private final Function<Ricetta, String> display;

    public RicettaListCell(
        Supplier<Integer> sessioneIdSupplier,
        Runnable onReload,
        Consumer<String> onError,
        Function<Ricetta, String> display
    ) {
        this.sessioneIdSupplier = sessioneIdSupplier;
        this.onReload = onReload;
        this.onError = onError;
        this.display = display;
    }

    @Override
    protected void updateItem(Ricetta item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        Label lbl = new Label(display.apply(item));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("x");
        removeBtn.getStyleClass().add("secondary-button");
        removeBtn.setFocusTraversable(false);
        removeBtn.setOnAction(e -> {
            Integer idSessione = sessioneIdSupplier.get();
            if (idSessione == null || item.getIdRicetta() == null) {
                return;
            }
            try {
                GestoreRicette.getInstance().disassociaRicettaSessione(idSessione, item.getIdRicetta());
                onReload.run();
            } catch (RuntimeException ex) {
                onError.accept(ex.getMessage());
            }
        });

        setText(null);
        setGraphic(new HBox(10, lbl, spacer, removeBtn));
    }
}
