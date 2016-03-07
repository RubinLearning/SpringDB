package exceptions;

import org.springframework.dao.DataAccessException;

public class MyException extends DataAccessException {
    public MyException(String msg) {
        super(msg);
    }
}
