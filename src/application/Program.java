public class Program {
    public static void main(String[] args){

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller find by id ====");
        Seller seller = sellerDao.findById(3);
        
        System.out.println(seller);
    }
}