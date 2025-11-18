import java.util.Date;

public class Estudiante {
    private int idEstudiante = 0;
    private String nombre = "";
    private String apellido = "";
    private int idCasa = 0;
    private int anyoCurso = 0;
    private Date fechaNacimiento;

    // Constructores
    public Estudiante() {}

    public Estudiante(String nombre, String apellido, int anyoCurso, Date fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.anyoCurso = anyoCurso;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters y Setters
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public int getIdCasa() { return idCasa; }
    public void setIdCasa(int idCasa) { this.idCasa = idCasa; }

    public int getAnyoCurso() { return anyoCurso; }
    public void setAnyoCurso(int anyoCurso) { this.anyoCurso = anyoCurso; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Estudiante{id=" + idEstudiante + ", nombre='" + nombre + "', apellido='" + apellido + "', curso=" + anyoCurso + ", casa=" + idCasa + "}";
    }
}