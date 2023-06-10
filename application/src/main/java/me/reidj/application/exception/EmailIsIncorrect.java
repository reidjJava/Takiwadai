package me.reidj.application.exception;

import me.reidj.application.util.Utils;

public class EmailIsIncorrect implements Solid {

	private final String[] message = new String[]{
			"Пожалуйста, проверьте правильность введённой почты",
			"Ваш формат почты неверен!"
	};

	@Override
	public boolean check(String... strings) {
		return Utils.isRegularExpressionCheck(Utils.EMAIL_REGEX, strings[0]);
	}

	@Override
	public String[] getMessage() {
		return message;
	}
}
