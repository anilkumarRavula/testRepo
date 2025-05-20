package interview;

import java.util.*;
import java.util.stream.Collectors;
/**
 *  NOTE: If you need an IDE, you can use this:
 https://www.jdoodle.com/online-java-compiler

 Question:

 Write a Java 8+ program that processes a list of Employee objects to perform the following operations using Java Streams:

 Group the employees by their department.
 For each group, calculate the average salary.
 Filter out groups where the average salary is less than $50,000.
 Sort the remaining groups by the average salary in ascending order.
 Collect the results into a Map where the key is the department name and the value is the average salary.
 Print the department name and average salary for each entry in the final map.


 Sample data:

 Employee Name   Department  Salary
 "John"          "HR"    60000
 "Jane"          "IT"    75000
 "Doe"           "HR"    50000
 "Smith"     "Finance"   45000
 "Emily"         "IT"    80000
 "Anna"      "Finance"   55000


 */

public class EmployeeStreamExample {
    public static void main(String[] args) {
      /**  "John"          "HR"    10000
        "Jane"          "IT"    75000
        "Doe"           "HR"    50000
        "Smith"     "Finance"   45000
        "Emily"         "IT"    80000
        "Anna"      "Finance"   55000*/

        // define Sample data here //
        List<Employee> employee = new ArrayList<>();
        employee.add(new Employee("John" ,         "HR" ,   10000));
        employee.add(new Employee("Jane" ,         "IT" ,   75000));
        employee.add(new Employee("Doe" ,         "HR" ,   50000));
        employee.add(new Employee("Smith" ,         "Finance" ,   45000));
        employee.add(new Employee("Emily" ,         "IT" ,   80000));
        employee.add(new Employee("Anna" ,         "Finance" ,   55000));


        // Processing employee data here //
        //dep and avs salary
       Map<String,Double>  employess = employee.stream().collect(Collectors.groupingBy(Employee::getDepartment,
               Collectors.averagingDouble(Employee::getSalary)));
        System.out.println(employess);

        LinkedHashMap<String, Double> collect = employess.entrySet().stream().filter(entry -> entry.getValue() >= 30000)
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        (ent1, ent2) -> ent2.putAll(ent1));

        System.out.println(collect);
        //filter each department and sort


        //print final list



    }
}

