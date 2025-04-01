package java8;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GroupByCollections {
static List<Employee> employees = buildEmp();

    private static List<Employee> buildEmp() {
       return List.of(
        new Employee("A",23,"1","M",12000.00),
        new Employee("B",33,"3","F",13000.00),
        new Employee("C",46,"2","F",22000.00),
        new Employee("D",22,"3","M",19000.00),
        new Employee("E",27,"1","M",22000.00),
        new Employee("F",29,"1","M",19000.00),
        new Employee("G",48,"3","M",17000.00)
        );
    }

    public static void main(String[] args) {
        getEmployeesDepartmentWiseByMaleEmplyeesAgeGreaterThan27OrFemaleEmployeesGreterThan23();
    }
    //get department wise employees who is age is above 27 for male and 23 for female
    private static void getEmployeesDepartmentWiseByMaleEmplyeesAgeGreaterThan27OrFemaleEmployeesGreterThan23() {
        Map<String,List<Employee>> temps = employees.stream().filter(emp-> emp.getGender().equals("M")
                &&  emp.getAge() >= 27 || (emp.getGender().equals("F"))
                        && emp.getAge()>= 23).collect(Collectors.groupingBy(Employee::getDepartMent));

        System.out.println(temps);
    }
    //    //get second highest salary employees from each department
    private static void getHighestSalariedEmployeesFromEachDepartment() {
        Map<String,List<Employee>> temps = employees.stream().filter(emp-> emp.getGender().equals("M")
                &&  emp.getAge() >= 27 || (emp.getGender().equals("F"))
                && emp.getAge()>= 23).collect(Collectors.groupingBy(Employee::getGender));

        System.out.println(temps);
    }
}
