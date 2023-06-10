package me.reidj.application.exception;

import me.reidj.application.util.Utils;

import java.util.Arrays;

public class SymbolIsIncorrect implements Solid {

	private final String[] message = new String[]{
			"Пожалуйста, проверьте правильность введённых данных",
			"Имя, фамилия и отчество могут содержать только русские символы!"
	};

	@Override
	public boolean check(String... strings) {
		return Arrays.stream(strings).anyMatch(s -> Utils.isRegularExpressionCheck(Utils.RUSSIAN_SYMBOL_REGEX, s));
	}

	@Override
	public String[] getMessage() {
		return message;
	}

}
