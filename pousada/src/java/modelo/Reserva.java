package modelo;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Reserva implements java.io.Serializable {
    private int id = -1;
    private Chale chale;
    private Calendar dataInicio;
    private Calendar dataFim;
    private String status;
    
    private SimpleDateFormat formatData;
    
    public Reserva() {
        dataInicio = Calendar.getInstance();
        dataFim = Calendar.getInstance();
        formatData = new SimpleDateFormat("dd/MM/yyyy");
        dataInicio.setLenient(false);
        dataFim.setLenient(false);
        formatData.setLenient(false);
        status = "Reservado";
    }
    public Reserva(Chale chale, Calendar dataInicio, Calendar dataFim) {
        this();
        try {
            this.setChale(chale);
            this.setDataInicio(dataInicio);
            this.setDataFim(dataFim);
            this.checkDatas();
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Reserva(int id, Chale chale, Calendar dataInicio, Calendar dataFim) {
        this(chale, dataInicio, dataFim);
        try {
            this.setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Reserva(Chale chale, String dataInicio, String dataFim) {
        this();
        try {
            this.setChale(chale);
            this.setDataInicioString(dataInicio);
            this.setDataFimString(dataFim);
            this.checkDatas();
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Reserva(int id, Chale chale, String dataInicio, String dataFim) {
        this(chale, dataInicio, dataFim);
        try {
            this.setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public Chale getChale() {
        return chale;
    }
    public Calendar getDataInicio() {
        return dataInicio;
    }
    public Calendar getDataFim() {
        return dataFim;
    }
    public String getStatus() {
        return status;
    }
    public String getDataInicioString() {
        return formatData.format(dataInicio.getTime());
    }
    public String getDataFimString() {
        return formatData.format(dataFim.getTime());
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setChale(Chale chale) {
        this.chale = chale;
    }
    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
    }
    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
    }
    public void setStatus(String status) {
        if (status == null || status.isEmpty())
            throw new IllegalArgumentException("Status inválido.");
        this.status = status;
    }
    
    public void setDataInicioString(String dataInicio) {
        try {
            this.dataInicio.setTime(formatData.parse(dataInicio));
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Data de início inválida: " + dataInicio);
        }
    }
    public void setDataFimString(String dataFim) {
        try {
            this.dataFim.setTime(formatData.parse(dataFim));
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Data final inválida: " + dataFim);
        }
    }
    
    public void checkDatas() {
        if (diferencaEmDias() <= 0) {
            throw new IllegalArgumentException(
                "Datas inválidas: Início ["
                + getDataInicioString()
                + "] não é após o final ["
                + getDataFimString()
                + "]."
            );
        }
    }
    
    public long diferencaEmDias() {
        return ((dataFim.getTimeInMillis() - dataInicio.getTimeInMillis()) / (24*60*60*1000));
    }
}
