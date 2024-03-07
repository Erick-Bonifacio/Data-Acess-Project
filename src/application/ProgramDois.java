import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ProgramDois {
    
    public static void main(String[] args){

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== TEST 1: department find by id ===");
        Department department = departmentDao.findById(3);
        System.out.println(department);

        System.out.println();
        System.out.println("=== TEST 2: department find all ===");
        List<Department> list = new ArrayList<>();
        list = departmentDao.findAll();
        for (Department obj : list) System.out.println(obj);

        System.out.println();
        System.out.println("=== TEST 3: department insert ===");
        Department newDep = new Department(null, "Music");
        departmentDao.insert(newDep);
        System.out.println("Inserted! New id = " + newDep.getId());

        System.out.println();
        System.out.println("=== TEST 4: department update ===");
        department = departmentDao.findById(1);
        department.setName("Cars");
        departmentDao.update(department);
        System.out.println("Update completed!");

        System.out.println();
        System.out.println("=== TEST 5: department delete ===");
        System.out.println("Enter ID for delete test");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed");
        sc.close();

    }
}