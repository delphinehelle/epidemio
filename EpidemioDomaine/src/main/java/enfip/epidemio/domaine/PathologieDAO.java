package enfip.epidemio.domaine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import comp.transaction.api.ContexteTransactionnel;

import enfip.epidemio.domaine.types.Pathologie;

public class PathologieDAO
{

    public static Boolean creer(ContexteTransactionnel ctx, Integer id, String nom) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Boolean res = false;
        try
        {
            st = ctx.get().obtenirStatement(false);
            rs = st.executeQuery("SELECT id_patho FROM pathologie WHERE id_patho=" + id);
            if (rs.first() == false)
            {
                st.executeUpdate("INSERT INTO pathologie " + "VALUES (" + id + ", '" + nom + "')");
                res = true;
            }
        }
        catch (Exception e)
        {
            ctx.get().setRollback(e.getMessage(), e);
            throw e;
        }
        finally
        {
            if (rs != null)
                rs.close();
            if (st != null)
                st.close();
        }

        return res;
    }

    public static List<Pathologie> lire(ContexteTransactionnel ctx) throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Pathologie> reponses = null;
        try
        {
            ps = ctx.get().obtenirPreparedStatement("SELECT id_patho, nom_patho FROM pathologie");
            rs = ps.executeQuery();
            reponses = new LinkedList<Pathologie>();
            while (rs.next())
            {
                Pathologie p = new Pathologie(rs.getInt("id_patho"), rs.getString("nom_patho"));
                reponses.add(p);
            }
        }
        catch (SQLException e)
        {
            ctx.get().setRollback(e.getMessage(), e);
            throw e;
        }
        finally
        {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
        return reponses;
    }
}
