import java.util.ArrayList;
import java.util.Date;
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

        System.out.println();
        System.out.println("=== TEST 3: seller find all ===");
        sellerList = sellerDao.findAll();
        for (Seller obj : sellerList) System.out.println(obj);

        System.out.println();
        System.out.println("=== TEST 4: seller insert ===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.00, new Department(2, null));
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " +newSeller.getId());
    }
}