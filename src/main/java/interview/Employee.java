package interview;

/** lseg
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

   class Employee {
        private String name;
        private String department;
        private double salary;

        public Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public String getDepartment() {
            return department;
        }

        public double getSalary() {
            return salary;
        }



   }

