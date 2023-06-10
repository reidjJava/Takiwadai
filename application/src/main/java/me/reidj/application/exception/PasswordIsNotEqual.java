package me.reidj.application.exception;

public class PasswordIsNotEqual implements Solid {

	private final String[] message = new String[]{
			"Пожалуйста, проверьте правильность введённого пароля!",
			"Ваши пароли не совпадают!"
	};

	@Override
	public boolean check(String... strings) {
		return !strings[0].equals(strings[1]);
	}

	@Override
	public String[] getMessage() {
		return message;
	}

}
