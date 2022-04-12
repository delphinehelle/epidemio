package enfip.epidemio.service.contrat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import enfip.epidemio.service.contrat.types.PathologieVO;
import enfip.epidemio.service.contrat.types.RegionVO;

public interface ServiceDictionnaireRemote extends Remote, ServiceDictionnaire
{

    List<RegionVO> lireRegion() throws RemoteException;

    List<PathologieVO> lirePathologie() throws RemoteException;

    Boolean ajouterRegion(Integer id, String nom) throws RemoteException;

    Boolean ajouterPathologie(Integer id, String nom) throws RemoteException;

}
