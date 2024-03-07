public class DaoFactory {
    
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection()); //retorna a implementacao a partir da interface
    }
}