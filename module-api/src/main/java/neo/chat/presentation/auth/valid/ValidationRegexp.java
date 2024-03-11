package neo.chat.presentation.auth.valid;

public class ValidationRegexp {

    public static final String USERNAME = "^[a-zA-Z0-9.\\-_]{4,20}$";
    public static final String PASSWORD =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_\\-+=`~]).{8,30}$";

}
