package lol;

import java.util.*;

/******* Class definition for ListNode *******/
class ListNode <E> {
  /* data attributes */
  private E element;
  private ListNode <E> next;

  /* constructors */
  public ListNode(E element) {
    this(element, null);
  }

  public ListNode(E element, ListNode <E> next) {
    this.element = element;
    this.next = next;
  }

  public ListNode <E> getNext() {
    return this.next;
  }

  public E getElement() {
    return this.element;
  }

  public void setNext(ListNode <E> next) {
    this.next = next;
  }
}

/******* Class definition for MySortedTaskList *******/
class MySortedTaskList <E extends Comparable <E>> {

  // Data attributes
  private ListNode <E> head = null;
  private int numNodes = 0;

  // Return true if list is empty; otherwise return false.
  public boolean isEmpty() {
    return (numNodes == 0);  // or return (head == null);
  }

  // Return number of nodes in list.
  public int size() {
    return numNodes;
  }

  // Return value in the first node.
  public E getFirst() throws NoSuchElementException {
    if (head == null)
      throw new NoSuchElementException("List is empty!");
    else
      return head.getElement();
  }

  // Return true if list contains item, otherwise return false.
  public boolean contains(E item) {
    for (ListNode <E> curr = head; curr != null; curr = curr.getNext())
      if (curr.getElement().equals(item))
        return true;

      return false;
    }

  // Add item to front of list.
    public void addFirst(E item) {
      head = new ListNode <E> (item, head);
      numNodes++;
    }

  // Remove first node of list.
    public E removeFirst() throws NoSuchElementException {
      if (head == null)
        throw new NoSuchElementException("Can't remove from an empty list!");
      else {
        ListNode <E> first = head;
        head = head.getNext();
        numNodes--;
        return first.getElement();
      }
    }

  // Return string representation of list.
    public String toString() {
      String str = "[";
      ListNode <E> ln = head;

      if (head == null)
        return ("[]");

      str += ln.getElement();

      for (int i = 1; i < numNodes; i++) {
        ln = ln.getNext();
        str += ", " + ln.getElement();
      }
      str += "]";
      return str;
    }

  // Add item to the list, maintaining the order.
    public void addOrdered(E item) {

      ListNode <E> current;

    // if list is empty to begin with or item is 'smaller' than head use addFirst()
      if (head == null || item.compareTo(head.getElement()) < 0)
        this.addFirst(item);

    // otherwise, go down the list and locate the node before the point of insertion
      else {
        current = head;

        while (current.getNext() != null && current.getNext().getElement().compareTo(item) < 0) {
          current = current.getNext();
        }

        ListNode <E> newNode = new ListNode <E> (item);
        newNode.setNext(current.getNext());
        current.setNext(newNode);

        numNodes++;
      }
    }
}
