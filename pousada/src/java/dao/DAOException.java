package dao;

import java.sql.SQLException;

public class DAOException extends Exception {
    public DAOException() {
        super();
    }
    public DAOException(String message) {
        super(message);
    }
    public DAOException(SQLException ex) {
        super("Código " + ex.getErrorCode() + ".\n" + ex);
    }
}
