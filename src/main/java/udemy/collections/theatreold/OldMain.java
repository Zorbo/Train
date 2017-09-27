package udemy.collections.theatreold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OldMain {

    public static void main(String[] args) {

        OldTheatre theatre = new OldTheatre("Nemzeti", 8, 12);
        //  theatre.getSeats();
        if (theatre.reserveSeat("H11")) {
            System.out.println("Please pay");
        } else {
            System.out.println("Sorry, seat is taken");
        }

        if (theatre.reserveSeat("H11")) {
            System.out.println("Please pay");
        } else {
            System.out.println("Sorry, seat is taken");
        }

        List<OldTheatre.Seat> seatCopy = new ArrayList<>(theatre.seats);
        // printList(seatCopy);

        seatCopy.get(1).reserve();
        if (theatre.reserveSeat("A02")) {
            System.out.println("Please pay for A02");
        } else {
            System.out.println("Seat already reserved");
        }

        Collections.shuffle(seatCopy);
     /*   System.out.println("Printing seatCopy");
        printList(seatCopy);
        System.out.println("Printing theatre.seats");
        printList(theatre.seats);
*/

        OldTheatre.Seat minSeat = Collections.min(seatCopy);
        OldTheatre.Seat maxSeat = Collections.max(seatCopy);
        System.out.println("Min seat number " + minSeat.getSeatNumber());
        System.out.println("Max seat number " + maxSeat.getSeatNumber());

        sortList(seatCopy);
        System.out.println("Printing sorted seatCopy");
        printList(seatCopy);
    }

    public static void printList(List<OldTheatre.Seat> list) {
        for (OldTheatre.Seat seat : list) {
            System.out.println(" " + seat.getSeatNumber());
        }
        System.out.println();
        System.out.println("==================================================");
    }

    public static void sortList(List<OldTheatre.Seat> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).compareTo(list.get(j)) > 0) {
                    Collections.swap(list, i, j);
                }
            }
        }
    }
}
