package com.curso.reactive.curso_leccion1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class CursoReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursoReactiveApplication.class, args);
        /* Desde aquí se Ejecuta el taller #1*/
        List<Persona> personas = Arrays.asList(
                new Persona("Juan", 25, "Masculino"),
                new Persona("María", 30, "Femenino"),
                new Persona("Pedro", 40, "Masculino"),
                new Persona("Ana", 20, "Femenino")
        );
        //1 Filtrar personas mayores de 25 años
        List<Persona> mayores = personas.stream().filter(persona -> persona.getEdad()>25).collect(Collectors.toList());
         System.out.println(mayores);
        //2 Obtener una lista con los nombres de las personas.
        List<String> nombres = personas.stream().map(persona -> persona.getNombre()).collect(Collectors.toList());
        System.out.println(nombres);
        //3 Obtener la suma de las edades de todas las personas.
        int sumaEdades = personas.stream()
                .mapToInt(Persona::getEdad)
                .sum();
        System.out.println(sumaEdades);
        //4 Contar la cantidad de personas de cada género.
        Map<String, Long> cantidadPorGenero = personas.stream()
                .collect(Collectors.groupingBy(Persona::getGenero, Collectors.counting()));
        System.out.println(cantidadPorGenero);

        //5 Calcular el promedio de edades de las personas.
        double promedioEdades = personas.stream()
                .mapToInt(Persona::getEdad)
                .average()
                .orElse(0.0);
        System.out.println(promedioEdades);
        //6 Encontrar la persona de mayor edad.

        Persona personaMayorEdad = personas.stream()
                .max(Comparator.comparingInt(Persona::getEdad))
                .orElse(null);
        System.out.println(personaMayorEdad);

        //-----------------------------------------------------------------------------------------------------------//

        /* Desde aquí se Ejecuta el taller #2*/
        BankAccount account = new BankAccount();
		account.addTransaction(new Transaction(100, "deposit", "2024-05-13"));
		account.addTransaction(new Transaction(50, "withdrawal", "2024-05-14"));
		account.addTransaction(new Transaction(200, "deposit", "2024-05-15"));
		account.addTransaction(new Transaction(150, "deposit", "2024-05-16"));
		account.addTransaction(new Transaction(75, "withdrawal", "2024-05-17"));

		// Prueba las funcionalidades aquí
		// Puedes comentar estas líneas y pedir a los estudiantes que las descomenten una vez que hayan completado los TODOs

        account.getTotalBalance().ifPresent(balance -> System.out.println("Saldo total: " + balance));
        account.getTotalDeposits().ifPresent(total -> System.out.println("Total de depósitos: " + total));
        account.getLargestWithdrawal().ifPresent(transaction -> System.out.println("Retiro de mayor monto: " + transaction.getAmount()));
        account.getTransactionsOnDate("2024-05-13").ifPresent(transactions -> transactions.forEach(transaction -> System.out.println(transaction.getAmount())));
        account.getAverageTransactionAmount().ifPresent(average -> System.out.println("Promedio de montos: " + average));
        account.getTransactionsWithAmountGreaterThan(100).ifPresent(transactions -> transactions.forEach(transaction -> System.out.println(transaction.getAmount())));
        BankAccount targetAccount = new BankAccount();
        BankAccount.transfer(account, targetAccount, 50);
        targetAccount.getTotalBalance().ifPresent(balance -> System.out.println("Saldo total de la cuenta destino: " + balance));
        account.getTotalWithdrawals().ifPresent(total -> System.out.println("Total de retiros: " + total));
        account.getTransactionsSummary().forEach((type, sum) -> System.out.println("Tipo: " + type + ", Total: " + sum));
	}

}
