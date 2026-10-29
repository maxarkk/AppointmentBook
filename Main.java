public class Main {
    public static void main(String[] args) {
        AppointmentBook book = new AppointmentBook();

        System.out.println(book.findFreeBlock(2, 15));
        System.out.println(book.findFreeBlock(2, 9));
        System.out.println(book.findFreeBlock(2, 20));

        System.out.println(book.makeAppointment(2, 4, 22)); 
        System.out.println(book.makeAppointment(3, 4, 3));
        System.out.println(book.makeAppointment(2, 4, 30));

    }
}
