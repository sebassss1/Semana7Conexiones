public class Asignatura {
    private int idAsignatura = 0;
    private String nombreAsignatura = "";
    private String aula = "";
    private boolean obligatoria = false;
    private int idProfesor = 0;

    // Constructores
    public Asignatura() {}

    public Asignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public Asignatura(String nombreAsignatura, String aula, boolean obligatoria) {
        this.nombreAsignatura = nombreAsignatura;
        this.aula = aula;
        this.obligatoria = obligatoria;
    }

    // Getters y Setters
    public int getIdAsignatura() { return idAsignatura; }
    public void setIdAsignatura(int idAsignatura) { this.idAsignatura = idAsignatura; }

    public String getNombreAsignatura() { return nombreAsignatura; }
    public void setNombreAsignatura(String nombreAsignatura) { this.nombreAsignatura = nombreAsignatura; }

    public String getNombre() { return nombreAsignatura; } // Para compatibilidad

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public boolean isObligatoria() { return obligatoria; }
    public void setObligatoria(boolean obligatoria) { this.obligatoria = obligatoria; }

    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }

    @Override
    public String toString() {
        return "Asignatura{id=" + idAsignatura + ", nombre='" + nombreAsignatura + "', aula='" + aula + "', obligatoria=" + obligatoria + "}";
    }
}