package me.reidj.application.exception;

import java.util.Arrays;

public class FieldIsEmpty implements Solid {

	private final String[] message = new String[]{
			"Пожалуйста, заполните все поля",
			"Поля не могут быть пустыми!"
	};

	@Override
	public boolean check(String... strings) {
		return Arrays.stream(strings).anyMatch(s -> s == null || s.isEmpty());
	}

	@Override
	public String[] getMessage() {
		return message;
	}

}
