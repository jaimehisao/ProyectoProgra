package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PendientesController {
	
	

	Callback<TableColumn<Pendiente, Boolean>, TableCell<Pendiente, Boolean>> booleanCellFactory = 
            new Callback<TableColumn<Pendiente, Boolean>, TableCell<Pendiente, Boolean>>() {
            @Override
                public TableCell<Pendiente, Boolean> call(TableColumn<Pendiente, Boolean> p) {
                    return new BooleanCell();
            }
        };
       // active.setCellValueFactory(new PropertyValueFactory<TableData,Boolean>("active"));
//        active.setCellFactory(booleanCellFactory);

	class Pendiente {
		
		public Pendiente() {
			
		}
	}

	class BooleanCell extends TableCell<Pendiente, Boolean> {
		private CheckBox checkBox;

		public BooleanCell() {
			checkBox = new CheckBox();
			checkBox.setDisable(true);
			checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (isEditing())
						commitEdit(newValue == null ? false : newValue);
				}
			});
			this.setGraphic(checkBox);
			this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			this.setEditable(true);
		}

		@Override
		public void startEdit() {
			super.startEdit();
			if (isEmpty()) {
				return;
			}
			checkBox.setDisable(false);
			checkBox.requestFocus();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			checkBox.setDisable(true);
		}

		public void commitEdit(Boolean value) {
			super.commitEdit(value);
			checkBox.setDisable(true);
		}

		@Override
		public void updateItem(Boolean item, boolean empty) {
			super.updateItem(item, empty);
			if (!isEmpty()) {
				checkBox.setSelected(item);
			}
		}
	}

}
