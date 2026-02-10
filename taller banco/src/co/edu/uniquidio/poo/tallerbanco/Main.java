package co.edu.uniquidio.poo.tallerbanco;

import co.edu.uniquidio.poo.tallerbanco.model.*;

public class Main {

    public static void main(String[] args) {

        Banco banco = new Banco("Banco UQ", "Armenia");


        Usuario juan = new Usuario("1001", "Juan Perez", "3001111111", "juan@gmail.com");
        Usuario ana = new Usuario("1002", "Ana Lopez", "3002222222", "ana@gmail.com");
        Usuario carlos = new Usuario("1003", "Carlos Ruiz", "3003333333", "carlos@gmail.com");


        Cuenta ahorro = new Cuenta_Ahorros(
                "0001",
                500000,
                "01/01/2025",
                juan
        );

        Cuenta corriente = new Cuenta_corriente(
                "0002",
                50000,
                "01/01/2025",
                ana
        );


        Cuenta_nomina nomina = new Cuenta_nomina(
                "0003",
                100000,
                "01/01/2025",
                carlos
        );


        banco.agregarcuenta(ahorro);
        banco.agregarcuenta(corriente);
        banco.agregarcuenta(nomina);


        System.out.println("\n===== PRUEBA CUENTA AHORROS =====");
        ahorro.retirar(450000); // NO debe permitir
        ahorro.Depositar(200000);
        ((Cuenta_Ahorros) ahorro).generarInteresMensual();
        ahorro.mostrarInformacion();



        System.out.println("\n===== PRUEBA CUENTA CORRIENTE =====");
        corriente.retirar(400000);
        ((Cuenta_corriente) corriente).cobrarComisionMensual();
        ((Cuenta_corriente) corriente).aplicarInteresSobregiro();
        corriente.mostrarInformacion();


        System.out.println("\n===== PRUEBA CUENTA NÓMINA =====");

        for (int mes = 1; mes <= 3; mes++) {
            System.out.println("Mes " + mes);
            nomina.procesarMes(); // no recibe salario → comisión
        }

        banco.cambiarCuentaNominaACorriente();



        System.out.println("\n===== ESTADO FINAL DEL BANCO =====");

        for (Cuenta c : banco.getListcuenta()) {
            c.mostrarInformacion();
            System.out.println("--------------------");
        }
    }
}
