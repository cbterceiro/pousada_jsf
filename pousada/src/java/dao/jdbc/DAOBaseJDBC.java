package dao.jdbc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import dao.*;

public class DAOBaseJDBC implements DAOBase {
    private static final String CAMINHO_PROPERTIES = "properties/JDBCProperties.properties";
    private static Connection conn;

    private AdministradorDAO administradorDao;
    private ChaleDAO         chaleDao;
    private ClienteDAO       clienteDao;
    private EquipamentoDAO   equipamentoDao;
    private HospedagemDAO    hospedagemDao;
    private ReservaDAO       reservaDao;
    private ServicoDAO       servicoDao;
    private TipoPagamentoDAO tipoPagamentoDao;
    
    private int numTransacoes;

    public DAOBaseJDBC() {
        Properties props;
        FileInputStream stream;

        try {
            props = new Properties();
            stream = new FileInputStream(CAMINHO_PROPERTIES);
            props.load(stream);

            String driver = props.getProperty("nome_driver");
            String url    = props.getProperty("bd_url");
            String login  = props.getProperty("bd_login");
            String senha  = props.getProperty("bd_senha");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, login, senha);

            administradorDao = new AdministradorDAOJDBC(this);
            chaleDao         = new ChaleDAOJDBC(this);
            clienteDao       = new ClienteDAOJDBC(this);
            equipamentoDao   = new EquipamentoDAOJDBC(this);
            hospedagemDao    = new HospedagemDAOJDBC(this);
            reservaDao       = new ReservaDAOJDBC(this);
            servicoDao       = new ServicoDAOJDBC(this);
            tipoPagamentoDao = new TipoPagamentoDAOJDBC(this);
            
            numTransacoes = 0;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Arquivo properties não encontrado!");
            System.exit(1);
        }
        catch (IOException ex) {
            System.out.println("Falha ao carregar arquivo properties!");
            System.exit(1);
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Falha ao carregar driver do banco de dados!");
            System.exit(1);
        }
        catch (SQLException ex) {
            System.out.println("Falha ao tentar se conectar ao banco de dados!");
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    @Override
    public AdministradorDAO getAdministradorDAO() {
        return administradorDao;
    }

    @Override
    public ChaleDAO getChaleDAO() {
        return chaleDao;
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return clienteDao;
    }

    @Override
    public EquipamentoDAO getEquipamentoDAO() {
        return equipamentoDao;
    }

    @Override
    public HospedagemDAO getHospedagemDAO() {
        return hospedagemDao;
    }

    @Override
    public ReservaDAO getReservaDAO() {
        return reservaDao;
    }

    @Override
    public ServicoDAO getServicoDAO() {
        return servicoDao;
    }

    @Override
    public TipoPagamentoDAO getTipoPagamentoDAO() {
        return tipoPagamentoDao;
    }

    @Override
    public void iniciarTransacao() throws DAOException {
        numTransacoes++;
        if (numTransacoes == 1) {
            try {
                conn.setAutoCommit(false);
            }
            catch (SQLException ex) {
                System.out.println("Erro no banco de dados.");
                System.out.println(ex);
                throw new DAOException(ex);
            }
        }
    }

    @Override
    public void finalizarTransacao() throws DAOException {
        numTransacoes--;
        if (numTransacoes == 0) {
            try {
                conn.commit();
                conn.setAutoCommit(true);
            }
            catch (SQLException ex) {
                System.out.println("Erro no banco de dados.");
                System.out.println(ex);
                throw new DAOException(ex);
            }
        }
        else if (numTransacoes < 0)
            numTransacoes = 0;
    }

    @Override
    public void descartarTransacao() throws DAOException {
        numTransacoes = 0;
        try {
            conn.rollback();
        }
        catch (SQLException ex) {
            System.out.println("Erro no banco de dados.");
            System.out.println(ex);
            throw new DAOException(ex);
        }
    }
}
