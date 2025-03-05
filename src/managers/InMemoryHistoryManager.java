package managers;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> listOfSeenTasks = new ArrayList<>();
    Node first;
    Node last;
    private Map<Integer, Node> idToNode = new HashMap<>();

    public void removeNode(int id) {
        Node node = idToNode.get(id);
        idToNode.remove(id);
        if (node == null) {
            return;
        }
        Node prev = node.previous;
        Node next = node.next;
        // связи между след и пред нодами
        if (prev != null) {
            prev.next = next;
        } else {
            first = next;
        }

        if (next != null) {
            next.previous = prev;
        } else {
            last = prev;
        }
    }

    @Override
    public void addToSeenTasks(Task seenTask) {
        if (idToNode.containsKey(seenTask.getId())) {
            Node seenNode = idToNode.get(seenTask.getId());
            removeNode(seenTask.getId());
           }
        if (first == null) {
            first = new Node(null, null, seenTask);
            //System.out.println("ID: " + seenTask.getId());
            idToNode.put(seenTask.getId(), first);
            return;
        }
        if (last == null) {
            last = new Node(null, first, seenTask);
            first.next = last;
            idToNode.put(seenTask.getId(), last);
            return;
        }
        Node newLast = new Node(null, last, seenTask); //новая нода
        last.next = newLast; // Присваиваем бывшей последней ноде новый след. элемент
        last = newLast; //заменяем последнюю ноду новой последней нодой
        idToNode.put(seenTask.getId(), newLast); // сохрняем новую ноду

    }

    public void updateListOfSeenTasks() {
        for (Node node : idToNode.values()) {
            listOfSeenTasks.add(node.task);
        }
    }

    @Override
    public ArrayList<Task> getSeenTasks() {
        updateListOfSeenTasks();
        return listOfSeenTasks;
    }

    public static class Node {
        private Node next;
        private Node previous;
        private Task task;

        public Node() {

        }

        public Node(Node next, Node previous, Task task) {
            this.next = next;
            this.previous = previous;
            this.task = task;
        }
    }

}
