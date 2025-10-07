package ejercicio3;

public class PrinterThread extends Thread {
    private MessagePrinter printer;
    private String message;

    public PrinterThread(MessagePrinter printer, String message, String name) {
        super(name);
        this.printer = printer;
        this.message = message;
    }

    @Override
    public void run() {
        printer.printMessage(message);
    }
}
