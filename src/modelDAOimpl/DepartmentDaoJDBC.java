import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn){ //injecao de dependencia
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
         
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "insert into department "
                + "(Name) "
                + "values"
                + "(?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys(); //retorna uma 'lista' de id's, no caso, com apenas um elemento
                if(rs.next()){
                    int id = rs.getInt(1); //pega o id gerado na pos 1
                    obj.setId(id);  //atualiza o id em relacao ao banco
                }
                DB.closeResultSet(rs);
            }

        } catch (Exception e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatment(st);
        }
    }

    @Override
    public void update(Department obj) {
        
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "update department "
                + "set Id = ?, Name = ? "
                + "Where Id = ?"
            );
            st.setInt(1, obj.getId());
            st.setString(2, obj.getName());
            st.setInt(3, obj.getId());
            st.executeUpdate();

        } catch (Exception e) {
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
            st = conn.prepareStatement(
                "delete from department where Id = ?"
            );

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
    public Department findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            
            st = conn.prepareStatement(
                "select * from department "
                + "where Id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();
            Department dep = new Department();

            if(rs.next()){
                dep.setId(rs.getInt("Id"));
                dep.setName(rs.getString("Name"));
            }

            return dep;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally{
            DB.closeStatment(st);
        }
    }

    @Override
    public List<Department> findAll() {

        List<Department> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            
            st = conn.prepareStatement("select * from department order by Name");
            rs = st.executeQuery();

            while(rs.next()){
                Department dep = new Department();

                dep.setId(rs.getInt("Id"));
                dep.setName(rs.getString("Name"));

                list.add(dep);
            }   

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally{
            DB.closeResultSet(rs);
            DB.closeStatment(st);
        }
    
        
    }
    
    
}