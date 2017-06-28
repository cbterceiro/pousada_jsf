package modelo;

import modelo.excecoes.PagamentoOverflowException;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class Hospedagem implements java.io.Serializable {
    private int id = -1;
    private Chale chale;
    private Cliente cliente;
    private int qntdAcomp;
    private Calendar dataInicio;
    private Calendar dataFim;
    private String status;
    private ArrayList<ReciboServico> recibos;
    
    private Pagamento pagamento;
    
    private SimpleDateFormat formatData;
    
    public Hospedagem() {
        dataInicio = Calendar.getInstance();
        dataFim = Calendar.getInstance();
        formatData = new SimpleDateFormat("dd/MM/yyyy");
        recibos = new ArrayList<>();
        pagamento = new Pagamento();
        status = "Hospedado";
        
        dataInicio.setLenient(false);
        dataFim.setLenient(false);
        formatData.setLenient(false);
    }
    public Hospedagem(Chale chale, Cliente cliente, int qntdAcomp, Calendar dataInicio, Calendar dataFim) {
        this();
        try {
            this.setChale(chale);
            this.setCliente(cliente);
            this.setQntdAcomp(qntdAcomp);
            this.setDataInicio(dataInicio);
            this.setDataFim(dataFim);
            this.checkDatas();
            this.pagamento.setValorTotal(
                this.diferencaEmDias() * this.chale.getDiaria()
            );
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Hospedagem(Chale chale, Cliente cliente, int qntdAcomp, String dataInicio, String dataFim) {
        this();
        try {
            this.setChale(chale);
            this.setCliente(cliente);
            this.setQntdAcomp(qntdAcomp);
            this.setDataInicioString(dataInicio);
            this.setDataFimString(dataFim);
            this.checkDatas();
            this.pagamento.setValorTotal(
                this.diferencaEmDias() * this.chale.getDiaria()
            );
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
    public Cliente getCliente() {
        return cliente;
    }
    public int getQntdAcomp() {
        return qntdAcomp;
    }
    public Calendar getDataInicio() {
        return dataInicio;
    }
    public Calendar getDataFim() {
        return dataFim;
    }
    public ArrayList<ReciboServico> getRecibos() {
        return recibos;
    }
    public Pagamento getPagamento() {
        return pagamento;
    }
    public double getValorTotal() {
        return pagamento.getValorTotal();
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
    public String getValorTotalString() {
        return pagamento.getValorTotalString();
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setChale(Chale chale) {
        this.chale = chale;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setQntdAcomp(int qntdAcomp) {
        if (qntdAcomp < 0)
            throw new IllegalArgumentException("Quantidade de acompanhantes inválida.");
        this.qntdAcomp = qntdAcomp;
    }
    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
        this.dataInicio.set(Calendar.HOUR, 0);
        this.dataInicio.set(Calendar.MINUTE, 0);
        this.dataInicio.set(Calendar.SECOND, 0);
        this.dataInicio.set(Calendar.MILLISECOND, 0);
    }
    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
        this.dataFim.set(Calendar.HOUR, 0);
        this.dataFim.set(Calendar.MINUTE, 0);
        this.dataFim.set(Calendar.SECOND, 0);
        this.dataFim.set(Calendar.MILLISECOND, 0);
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
     
    public void addRecibo(Servico servico, int qntd) {
        this.recibos.add(new ReciboServico(Calendar.getInstance(), Calendar.getInstance(), servico, qntd));
        this.pagamento.setValorTotal(
            this.pagamento.getValorTotal()
            + (servico.getValor() * qntd)
        );
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
        
    public void addParcelaPagamento(TipoPagamento tipo, double valor) throws PagamentoOverflowException {
        this.pagamento.addParcelaPagamento(tipo, valor);
    }
    
    public boolean estaPago() {
        return pagamento.estaPago();
    }
    
    public double calcValorTotal() {
        double valor = diferencaEmDias() * chale.getDiaria();
        for (ReciboServico r : recibos) {
            valor += r.getValorTotal();
        }
        this.pagamento.setValorTotal(valor);
        return valor;
    }
    
    public void resetPagamento() {
        this.pagamento.removeAllParcelas();
        this.pagamento.setValorAtual(0);
    }
    
    @Override
    public String toString() {
        return "Hospedagem " + id;
    }
    
    public String info() {
        String s = "";
        
        s += "Hospedagem " + id + "\n\n";
        
        s += "Status: " + status + "\n";
        s += chale + ", " + chale.getDiariaString() + " a diária\n";
        s += "Cliente: " + cliente + "\n";
        s += "Valor Total: " + getValorTotalString() + "\n";
        s += "De " + getDataInicioString() + " até " + getDataFimString() + "\n\n";
        
        s += "Serviços:\n";
        if (recibos.isEmpty())
            s += "    Nenhum.\n";
        else
            for (ReciboServico rs : recibos)
                s += "    " + rs.getServico().getNome() + ", " + rs.getQntd() + " vezes [" + rs.getValorTotalString() + "]\n";        
        
        /* Comentado por motivos de não sei
        s += "Pagamento:\n";
        if (pagamento.getParcelas().isEmpty())
            s += "    Ainda não efetuado.\n";
        else
            for (ParcelaPagamento p : pagamento.getParcelas())
                s += "    " + p.getTipoPagamento().getDescricao() + ", " + p.getValorString() + "\n";
        */
         
        return s;
    } 
}
