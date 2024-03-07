import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args){

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller find by id ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println();
        System.out.println("=== TEST 2: seller find by department ===");
        List<Seller> sellerList = sellerDao.findByDepartment(new Department(2, null));
        for (Seller obj : sellerList) System.out.println(obj);
    }
}