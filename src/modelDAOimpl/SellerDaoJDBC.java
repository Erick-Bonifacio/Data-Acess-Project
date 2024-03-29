import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){ //injecao de dependencia
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                "insert into seller "
                + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                + "values "
                + "(?, ?, ?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys(); //retorna uma 'lista' de id's, no caso, com apenas um elemento
                if(rs.next()){
                    int id = rs.getInt(1); //pega o id gerado na pos 1
                    obj.setId(id);  //atualiza o id em relacao ao banco
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("Unexpected error! No rows affected");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                "update seller "
                + "set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                + "where Id = ?"
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();
            
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("delete from seller where Id = ? ");

            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
           throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "select seller.*, department.Name as DepName "
                + "from seller inner join department "
                + "on seller.DepartmentId = department.Id "
                + "where seller.Id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();

            //instanciar a tabela que vem de ResultSet em classes
            if(rs.next()){ //rs aponta para 0 primeiro
                //instancia departamento
                Department dep = instanciateDepartment(rs);

                //instancia seller
                Seller seller = instanciateSeller(rs, dep);

                return seller;
            }
            return null;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instanciateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dep); //associa department
        return seller;
    }

    private Department instanciateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;    
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "select seller.*, department.Name as DepName "
                + "from seller inner join department "
                + "on seller.DepartmentId = department.Id "
                + "order by Name"
            );
    
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            //instanciar a tabela que vem de ResultSet em classes
            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId")); //se existir dep nao entra no if
                if(dep == null){
                    dep = instanciateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instanciateSeller(rs, dep);
                list.add(seller);
            }
            return (List<Seller>) list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
            //DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department dp) {
        
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "select seller.*, department.Name as DepName "
                + "from seller inner join department "
                + "on seller.DepartmentId = department.Id "
                + "where DepartmentId = ? "
                + "order by Name"
            );
            st.setInt(1, dp.getId());
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            //instanciar a tabela que vem de ResultSet em classes
            while(rs.next()){ //rs aponta para 0 primeiro
                
                Department dep = map.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instanciateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instanciateSeller(rs, dep);
                list.add(seller);
            }
            return (List<Seller>) list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
            //DB.closeResultSet(rs);
        }
    }
    
    
}