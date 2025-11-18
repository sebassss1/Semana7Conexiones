public class Profesor {
    private int idProfesor = 0;
    private String nombre = "";
    private String apellido = "";
    private int idAsignatura = 0;
    private String fechaInicio = "";

    // Constructores
    public Profesor() {}

    public Profesor(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Getters y Setters
    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getIdAsignatura() { return idAsignatura; }
    public void setIdAsignatura(int idAsignatura) { this.idAsignatura = idAsignatura; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Profesor{id=" + idProfesor + ", nombre='" + nombre + "', apellido='" + apellido + "', asignatura=" + idAsignatura + "}";
    }
}