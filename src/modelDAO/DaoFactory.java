public class DaoFactory {
    
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(); //retorna a implementa;'ao a partir da interface'
    }
}