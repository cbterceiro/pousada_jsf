package modelo;

import java.util.ArrayList;

public class Chale implements java.io.Serializable {
    private int id = -1;
    private int numero;
    private double diaria;
    private boolean estaOcupado;
    private ArrayList<Equipamento> equipamentos;
    
    public Chale() {
        equipamentos = new ArrayList<>();
        estaOcupado = false;
    }
    public Chale(int numero, double diaria) {
        this();
        try {
            setNumero(numero);
            setDiaria(diaria);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Chale(int id, int numero, double diaria) {
        this(numero, diaria);
        try {
            setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Chale(int numero, double diaria, ArrayList<Equipamento> equipamentos) {
        this(numero, diaria);
        try {
            setEquipamentos(equipamentos);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
    public Chale(int id, int numero, double diaria, ArrayList<Equipamento> equipamentos) {
        this(numero, diaria, equipamentos);
        try {
            setId(id);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public int getNumero() {
        return numero;
    }
    public double getDiaria() {
        return diaria;
    }
    public boolean getEstaOcupado() {
        return estaOcupado;
    }
    public ArrayList<Equipamento> getEquipamentos() {
        return equipamentos;
    }
    
    public String getDiariaString() {
        return java.text.NumberFormat.getCurrencyInstance().format(diaria);
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }
    public void setNumero(int numero) {
        if (numero <= 0)
            throw new IllegalArgumentException("Número de chalé inválido.");
        this.numero = numero;
    }
    public void setDiaria(double diaria) {
        if (diaria <= 0)
            throw new IllegalArgumentException("Valor da diária inválido.");
        this.diaria = diaria;
    }
    public void setEstaOcupado(boolean estaOcupado) {
        this.estaOcupado = estaOcupado;
    }
    public void setEquipamentos(ArrayList<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }
    
    public void addEquipamento(Equipamento e) {
        this.equipamentos.add(e);
    }
    public void removeAllEquipamentos() {
        this.equipamentos = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return "Chalé " + numero;
    }
    
    public String info() {
        String s = "";
        
        s += "Número: " + numero + "\n";
        s += "Valor da diária: " + getDiariaString() + "\n";
        s += "Equipamentos:" + "\n";
        
        if (equipamentos.isEmpty())
            s += "    Nenhum.\n";
        else
            for (Equipamento e : equipamentos)
                s += "    " + e.toString() + "\n";
        
        return s;
    }
}
