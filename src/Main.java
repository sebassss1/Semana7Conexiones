//Sebastian Mertic
import java.sql.*;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) {
        //Se que no es una buena practica dejarlo a la vista, pero me ha estado dando problemos cuando lo metia en un
        //fichero separado
        String url = "jdbc:postgresql://ad-postgres.cweidvmebcxq.us-east-1.rds.amazonaws.com:5432/hogwarts";
        String user = "postgres";
        String password = "acceso_adatos";

        System.out.println("Iniciando Conexión\n");

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            HogwartsDAO dao = new HogwartsDAO(connection);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // EJERCICIO 1: TRANSACCIÓN
            System.out.println("=".repeat(60));
            System.out.println("EJERCICIO 1: TRANSACCIÓN - CREAR PROFESOR Y ASIGNATURA");
            System.out.println("=".repeat(60));

            // Mostrar estado inicial
            dao.mostrarTodosLosProfesores();

            // Caso de prueba exitoso
            System.out.println("\n--- CASO DE PRUEBA EXITOSO ---");
            Profesor profesor1 = new Profesor("Aibus", "Dumbledore");
            Asignatura asignatura1 = new Asignatura("Arte Muggle", "Aula 7B", false);
            boolean resultado1 = dao.crearProfesorYAsignatura(asignatura1, profesor1);

            if (resultado1) {
                dao.verificarProfesorExistente("Aibus", "Dumbledore");
            }

            // Caso de prueba fallido (profesor que ya existe)
            System.out.println("\n--- CASO DE PRUEBA FALLIDO ---");
            Profesor profesor2 = new Profesor("Minerva", "McGonagall");
            Asignatura asignatura2 = new Asignatura("Defensa Contra las Artes Oscuras", "Aula 3C", true);
            boolean resultado2 = dao.crearProfesorYAsignatura(asignatura2, profesor2);

            System.out.println("\nRESULTADOS EJERCICIO 1:");
            System.out.println("Caso exitoso: " + (resultado1 ? "ÉXITO" : "FALLÓ"));
            System.out.println("Caso fallido: " + (resultado2 ? "ÉXITO" : "FALLÓ (esperado)"));

            // Mostrar estado final
            dao.mostrarTodosLosProfesores();

            // EJERCICIO 2: FUNCIÓN
            System.out.println("\n" + "=".repeat(60));
            System.out.println("EJERCICIO 2: FUNCIÓN - MATRICULAR ESTUDIANTE");
            System.out.println("=".repeat(60));

            Estudiante estudiante1 = new Estudiante(
                    "Selena", "Shade", 4, sdf.parse("2007-05-23")
            );
            dao.funcionMatricularEstudiante(estudiante1);

            // EJERCICIO 3: PROCEDIMIENTO
            System.out.println("\n" + "=".repeat(60));
            System.out.println("EJERCICIO 3: PROCEDIMIENTO - MATRICULAR ESTUDIANTE");
            System.out.println("=".repeat(60));

            Estudiante estudiante2 = new Estudiante(
                    "Theo", "Blackthorn", 3, sdf.parse("2008-10-11")
            );
            dao.procedimientoMatricularEstudiante(estudiante2);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}