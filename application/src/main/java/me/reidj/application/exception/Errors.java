package me.reidj.application.exception;

import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;
import me.reidj.application.App;

@AllArgsConstructor
public enum Errors implements Solid {

	EMAIL(new EmailIsIncorrect()),
	FIELD_EMPTY(new FieldIsEmpty()),
	PASSWORD(new PasswordIsNotEqual()),
	PASSWORD_IS_SHORT(new PasswordShort()),
	INCORRECT_SYMBOL(new SymbolIsIncorrect());

	private final Solid solid;

	@Override
	public boolean check(String... strings) {

		boolean bool = solid.check(strings);

		if (bool) {

			String[] message = solid.getMessage();

			App.getApp().getPrimaryStage().showAlert(
					Alert.AlertType.ERROR,
					message[0],
					message[1]
			);
		}

		return bool;
	}

	public boolean checkWithoutAlert(String... strings) {
		return solid.check(strings);
	}

	@Override
	public String[] getMessage() {
		return solid.getMessage();
	}
}
