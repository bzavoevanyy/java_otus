package homework;


import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    TreeMap<Customer, String> customerStringMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {

        var customerStringEntry = customerStringMap.firstEntry();
        if (customerStringEntry != null) {
            return Map.entry(new Customer(customerStringEntry.getKey()), customerStringEntry.getValue());
        } else return null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var customerStringEntry = customerStringMap.higherEntry(customer);
        if (customerStringEntry != null) {
            return Map.entry(new Customer(customerStringEntry.getKey()), customerStringEntry.getValue());
        } else return null;
    }

    public void add(Customer customer, String data) {
        customerStringMap.put(customer, data);
    }
}
