package server.system;




import data.Worker;

import java.util.Collections;
import java.util.LinkedList;

public class CollectionManager {
    private final LinkedList<Worker> workerLinkedList = new LinkedList<>();


    public void add(Worker worker) {
        workerLinkedList.add(worker);
    }

    public void shuffle() {
        Collections.shuffle(workerLinkedList);
    }

    public void reorder() {
        Collections.reverse(workerLinkedList);
    }

    public void clearCollection() {
        workerLinkedList.clear();
    }

    public void removeId(int id) {
        for (Worker w : workerLinkedList) {
            if (w.getId() == id) {
                workerLinkedList.remove(w);
                return;
            }
        }
    }

    public void removeFirst() {
        workerLinkedList.removeFirst();
    }

    public int averageSalary() {
        int aveSal = 0;
        int counter = 0;
        for (Worker w : workerLinkedList) {
            aveSal += w.getSalary();
            counter += 1;
        }
        return aveSal / counter;
    }


    public LinkedList<Worker> getworkerLinkedList() {
        return workerLinkedList;
    }

    @Override
    public String toString() {
        return "Count of workers: " + workerLinkedList.size();
    }
}
