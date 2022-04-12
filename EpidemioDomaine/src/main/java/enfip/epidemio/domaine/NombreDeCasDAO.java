package enfip.epidemio.domaine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import comp.transaction.api.ContexteTransactionnel;
import enfip.epidemio.service.contrat.SequenceDonneesEpidemiomlogique;

public class NombreDeCasDAO
{

	private static final int TAILLE_LOT = 1000;

	public static void ajouter(ContexteTransactionnel ctx, List<SequenceDonneesEpidemiomlogique> listSeq) throws Exception
	{
		PreparedStatement ps = null;
		try
		{
			ps =
					ctx.get()
					.obtenirPreparedStatement(
							"INSERT INTO stat_nbrecas("
									+
									"annee, semaine, id_patho,"
									+ " v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, " +
									"v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)"
									+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			
			int lot = 0;
			for(SequenceDonneesEpidemiomlogique seq : listSeq)
			{
				lot++;
				ps.setInt(1, seq.getAnnee());
				ps.setInt(2, seq.getSemaine());
				ps.setInt(3, seq.getIdentifiantPathologie());
				int rang = 4;
				boolean valorise = true;
				for (Integer val : seq.getValeurs())
				{
					if (val != null && valorise == true)
					{
						ps.setInt(rang, val);
						valorise = (val != 0);
					}
					else
					{
						ps.setNull(rang, Types.INTEGER);
					}
					rang++;
				}

				ps.addBatch();
				if(lot == TAILLE_LOT)
				{
					ps.executeBatch();
					lot = 0;
				}
				
			}
			if(lot > 0) ps.executeBatch();
		}
		catch (Exception e)
		{
			ctx.get().setRollback(e.getMessage(), e);
			throw e;
		}
		finally
		{
			if (ps != null)
				ps.close();
		}
	}

	public static void supprimer(ContexteTransactionnel ctx, Integer annee, Integer semaine,
			Integer identifiantPathologie) throws Exception
	{
		Statement st = null;
		try
		{
			st = ctx.get().obtenirStatement(false);
			st.executeUpdate("DELETE FROM stat_nbrecas WHERE"
					+ " annee >= " + annee
					+ " AND semaine = " + semaine
					+ "AND id_patho = " + identifiantPathologie
					);
		}
		catch (Exception e)
		{
			ctx.get().setRollback(e.getMessage(), e);
			throw e;
		}
		finally
		{
			if (st != null)
				st.close();
		}

	}

	public static Integer supprimer(ContexteTransactionnel ctx) throws Exception
	{
		Statement st = null;
		try
		{
			st = ctx.get().obtenirStatement(false);
			return st.executeUpdate("DELETE FROM stat_nbrecas");
		}
		catch (Exception e)
		{
			ctx.get().setRollback(e.getMessage(), e);
			throw e;
		}
		finally
		{
			if (st != null)
				st.close();
		}
	}

	public static boolean exist(ContexteTransactionnel ctx, Integer annee, Integer semaine, Integer identifiantPathologie) throws Exception
	{
		Statement st = null;
		ResultSet rs = null;
		try
		{
			st = ctx.get().obtenirStatement(true);
			rs = st.executeQuery("select id_patho FROM stat_nbrecas WHERE"
					+ " annee = " + annee
					+ " AND semaine = " + semaine
					+ "AND id_patho = " + identifiantPathologie
					);
			return rs.first();
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
	}

}
