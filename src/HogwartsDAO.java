import java.sql.*;
import java.text.SimpleDateFormat;

public class HogwartsDAO {
    private Connection connection;

    public HogwartsDAO(Connection connection) {
        this.connection = connection;
    }

    // EJERCICIO 1: TRANSACCIÓN - CREAR PROFESOR Y ASIGNATURA
    public boolean crearProfesorYAsignatura(Asignatura asignatura, Profesor profesor) {
        System.out.println("Iniciando transacción para crear profesor y asignatura...");
        System.out.println("Profesor: " + profesor.getNombreCompleto());
        System.out.println("Asignatura: " + asignatura.getNombreAsignatura());

        PreparedStatement stmtProfesor = null;
        PreparedStatement stmtAsignatura = null;
        PreparedStatement stmtUpdateProfesor = null;

        try {
            connection.setAutoCommit(false);

            // 1. Insertar el profesor (con nombre y apellido)
            String sqlProfesor = "INSERT INTO Profesor (nombre, apellido, fecha_inicio) VALUES (?, ?, ?)";
            stmtProfesor = connection.prepareStatement(sqlProfesor, Statement.RETURN_GENERATED_KEYS);
            stmtProfesor.setString(1, profesor.getNombre());
            stmtProfesor.setString(2, profesor.getApellido());
            stmtProfesor.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Fecha actual

            int filasAfectadas = stmtProfesor.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("Falló la inserción del profesor");
            }

            // Obtener ID generado del profesor
            ResultSet rsProfesor = stmtProfesor.getGeneratedKeys();
            int idProfesor = 0;
            if (rsProfesor.next()) {
                idProfesor = rsProfesor.getInt(1);
                System.out.println("Profesor insertado con ID: " + idProfesor);
            }

            // 2. Insertar la asignatura
            String sqlAsignatura = "INSERT INTO Asignatura (nombre_asignatura, aula, obligatoria, id_profesor) VALUES (?, ?, ?, ?)";
            stmtAsignatura = connection.prepareStatement(sqlAsignatura, Statement.RETURN_GENERATED_KEYS);
            stmtAsignatura.setString(1, asignatura.getNombreAsignatura());
            stmtAsignatura.setString(2, asignatura.getAula());
            stmtAsignatura.setBoolean(3, asignatura.isObligatoria());
            stmtAsignatura.setInt(4, idProfesor);

            filasAfectadas = stmtAsignatura.executeUpdate();

            if (filasAfectadas == 0) {
                throw new SQLException("Falló la inserción de la asignatura");
            }

            // Obtener ID generado de la asignatura
            ResultSet rsAsignatura = stmtAsignatura.getGeneratedKeys();
            int idAsignatura = 0;
            if (rsAsignatura.next()) {
                idAsignatura = rsAsignatura.getInt(1);
                System.out.println("Asignatura insertada con ID: " + idAsignatura);
            }

            // 3. Actualizar profesor con referencia a la asignatura
            String sqlUpdateProfesor = "UPDATE Profesor SET id_asignatura = ? WHERE id_profesor = ?";
            stmtUpdateProfesor = connection.prepareStatement(sqlUpdateProfesor);
            stmtUpdateProfesor.setInt(1, idAsignatura);
            stmtUpdateProfesor.setInt(2, idProfesor);
            stmtUpdateProfesor.executeUpdate();

            // Confirmar transacción
            connection.commit();
            System.out.println("Transacción completada exitosamente!");

            // Actualizar los objetos con los IDs generados
            profesor.setIdProfesor(idProfesor);
            profesor.setIdAsignatura(idAsignatura);
            asignatura.setIdAsignatura(idAsignatura);
            asignatura.setIdProfesor(idProfesor);

            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Error en transacción: " + e.getMessage());
                System.out.println("Transacción revertida");
            } catch (SQLException rollbackEx) {
                System.out.println("Error al revertir transacción: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                if (stmtProfesor != null) stmtProfesor.close();
                if (stmtAsignatura != null) stmtAsignatura.close();
                if (stmtUpdateProfesor != null) stmtUpdateProfesor.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    // EJERCICIO 2: FUNCIÓN - MATRICULAR ESTUDIANTE
    public void funcionMatricularEstudiante(Estudiante estudiante) {
        String sql = "SELECT * FROM matricular_estudiante(?, ?, ?, ?)";

        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getApellido());

            // Convertir java.util.Date a java.sql.Date
            java.sql.Date fechaSql = new java.sql.Date(estudiante.getFechaNacimiento().getTime());
            stmt.setDate(3, fechaSql);

            stmt.setInt(4, estudiante.getAnyoCurso());

            ResultSet rs = stmt.executeQuery();

            System.out.println("=== INFORMACIÓN DEL ESTUDIANTE MATRICULADO ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("out_id_estudiante"));
                System.out.println("Nombre: " + rs.getString("nombre") + " " + rs.getString("apellido"));
                System.out.println("Casa: " + rs.getString("nombre_casa"));
                System.out.println("Mascota: " + rs.getString("mascota"));
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar función: " + e.getMessage());
        }
    }

    // EJERCICIO 3: PROCEDIMIENTO - MATRICULAR ESTUDIANTE
    public void procedimientoMatricularEstudiante(Estudiante estudiante) {
        String sql = "CALL crear_estudiante(?, ?, ?, ?)";

        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getApellido());

            java.sql.Date fechaSql = new java.sql.Date(estudiante.getFechaNacimiento().getTime());
            stmt.setDate(3, fechaSql);

            stmt.setInt(4, estudiante.getAnyoCurso());

            stmt.execute();
            System.out.println("Procedimiento ejecutado exitosamente para: " + estudiante.getNombreCompleto());

            // Verificar la inserción
            verificarEstudiante(estudiante.getNombre(), estudiante.getApellido());

        } catch (SQLException e) {
            System.out.println("Error al ejecutar procedimiento: " + e.getMessage());
        }
    }

    // Métodos auxiliares
    public void mostrarTodosLosProfesores() {
        String sql = "SELECT p.id_profesor, p.nombre, p.apellido, a.nombre_asignatura " +
                "FROM Profesor p LEFT JOIN Asignatura a ON p.id_asignatura = a.id_asignatura " +
                "ORDER BY p.id_profesor";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("\nLISTA DE PROFESORES:");
            System.out.println("ID | Nombre | Asignatura");
            System.out.println("---|--------|------------");
            while (rs.next()) {
                System.out.println(rs.getInt("id_profesor") + " | " +
                        rs.getString("nombre") + " " + rs.getString("apellido") + " | " +
                        rs.getString("nombre_asignatura"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener profesores: " + e.getMessage());
        }
    }

    public void verificarProfesorExistente(String nombre, String apellido) {
        String sql = "SELECT COUNT(*) as count FROM Profesor WHERE nombre = ? AND apellido = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("El profesor '" + nombre + " " + apellido + "' existe " + count + " veces en la BD");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar profesor: " + e.getMessage());
        }
    }

    private void verificarEstudiante(String nombre, String apellido) {
        String sql = """
            SELECT e.id_estudiante, e.nombre, e.apellido, c.nombre_casa, 
                   m.nombre_mascota, m.especie, STRING_AGG(a.nombre_asignatura, ', ') as asignaturas
            FROM Estudiante e
            LEFT JOIN Casa c ON e.id_casa = c.id_casa
            LEFT JOIN Mascota m ON m.id_estudiante = e.id_estudiante
            LEFT JOIN Estudiante_Asignatura ea ON ea.id_estudiante = e.id_estudiante
            LEFT JOIN Asignatura a ON ea.id_asignatura = a.id_asignatura
            WHERE e.nombre = ? AND e.apellido = ?
            GROUP BY e.id_estudiante, e.nombre, e.apellido, c.nombre_casa, m.nombre_mascota, m.especie
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            ResultSet rs = stmt.executeQuery();

            System.out.println("=== VERIFICACIÓN DEL ESTUDIANTE ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_estudiante"));
                System.out.println("Nombre: " + rs.getString("nombre") + " " + rs.getString("apellido"));
                System.out.println("Casa: " + rs.getString("nombre_casa"));
                System.out.println("Mascota: " + rs.getString("nombre_mascota") + " (" + rs.getString("especie") + ")");
                System.out.println("Asignaturas: " + rs.getString("asignaturas"));
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar estudiante: " + e.getMessage());
        }
    }
}