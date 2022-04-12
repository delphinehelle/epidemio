package enfip.epidemio.domaine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import comp.transaction.api.ContexteTransactionnel;

import enfip.epidemio.domaine.types.Region;

public class RegionDAO
{

    public static Boolean creer(ContexteTransactionnel ctx, Integer id, String nom) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Boolean res = false;
        try
        {
            st = ctx.get().obtenirStatement(false);
            rs = st.executeQuery("SELECT id_reg FROM region WHERE id_reg=" + id);
            if (rs.first() == false)
            {
                st.executeUpdate("INSERT INTO region " + "VALUES (" + id + ", '" + nom + "')");
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

    public static List<Region> lire(ContexteTransactionnel ctx) throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Region> reponses = null;

        try
        {
            ps = ctx.get().obtenirPreparedStatement("SELECT id_reg, nom_reg FROM region");
            rs = ps.executeQuery();
            reponses = new LinkedList<Region>();
            while (rs.next())
            {
                Region r = new Region(rs.getInt("id_reg"), rs.getString("nom_reg"));
                reponses.add(r);
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
            if (ps != null)
                ps.close();
        }
        return reponses;
    }
}
