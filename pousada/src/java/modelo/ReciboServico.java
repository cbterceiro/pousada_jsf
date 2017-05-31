package modelo;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ReciboServico implements java.io.Serializable {
    private int id = -1;
    private Calendar data;
    private Calendar horario;
    private Servico servico;
    private int qntd;
    
    private SimpleDateFormat formatData;
    private SimpleDateFormat formatHora;
    
    public ReciboServico() {
        data = Calendar.getInstance();
        horario = Calendar.getInstance();
        formatData = new SimpleDateFormat("dd/MM/yyyy");
        formatHora = new SimpleDateFormat("HH:mm:ss");
        data.setLenient(false);
        horario.setLenient(false);
        formatData.setLenient(false);
        formatHora.setLenient(false);
    }
    public ReciboServico(Calendar data, Calendar horario, Servico servico, int qntd) {
        this();
        try {
            this.setData(data);
            this.setHorario(horario);
            this.setServico(servico);
            this.setQntd(qntd);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public ReciboServico(int id, Calendar data, Calendar horario, Servico servico, int qntd) {
        this(data, horario, servico, qntd);
        try {
            this.setId(id);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public ReciboServico(String data, String horario, Servico servico, int qntd) {
        this();
        try {
            this.setDataString(data);
            this.setHorarioString(horario);
            this.setServico(servico);
            this.setQntd(qntd);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public ReciboServico(int id, String data, String horario, Servico servico, int qntd) {
        this(data, horario, servico, qntd);
        try {
            this.setId(id);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public Calendar getData() {
        return data;
    }
    public Calendar getHorario() {
        return horario;
    }
    public Servico getServico() {
        return servico;
    }
    public int getQntd() {
        return qntd;
    }
    public double getValorTotal() {
        return qntd * servico.getValor();
    }
    
    public String getDataString() {
        return formatData.format(data.getTime());
    }
    public String getHorarioString() {
        return formatHora.format(horario.getTime());
    }
    public String getValorTotalString() {
        return java.text.NumberFormat.getCurrencyInstance().format(qntd * servico.getValor());
    }
    
    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setData(Calendar data) {
        this.data = data;
        this.data.set(Calendar.HOUR, 0);
        this.data.set(Calendar.MINUTE, 0);
        this.data.set(Calendar.SECOND, 0);
        this.data.set(Calendar.MILLISECOND, 0);
    }
    public void setHorario(Calendar horario) {
        this.horario = horario;
        this.horario.set(Calendar.DATE, 0);
        this.horario.set(Calendar.MONTH, 0);
        this.horario.set(Calendar.YEAR, 0);
    } 
    public void setServico(Servico servico) {
        this.servico = servico;
    }
    public void setQntd(int qntd) {
        if (qntd < 0)
            throw new IllegalArgumentException("Quantidade inválida.");
        this.qntd = qntd;
    }
    
    public void setDataString(String data) {
        try {
            this.data.setTime(formatData.parse(data));
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Data inválida: " + data);
        }
    }
    public void setHorarioString(String horario) {
        try {
            this.horario.setTime(formatHora.parse(horario));
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("Horário inválido: " + horario);
        }
    }
}
