package org.bitbucket.creditauto.usermanager.facade;

import java.util.List;
import org.bitbucket.creditauto.entity.Externaldistributor;
import org.bitbucket.creditauto.entity.Powerofattorney;
import org.bitbucket.creditauto.entity.Urole;
import org.bitbucket.creditauto.entity.User;
import org.bitbucket.creditauto.wicket.SearchData.AllUsers;

public interface IUsermanager {
    List<Externaldistributor> getExternaldistributors();

    List<User> getUsers(AllUsers usersSearchCriteria);

    Long save(User user);

    List<Externaldistributor> getUserExternaldistributors(User user);

    List<Powerofattorney> getUserPowerofattorneys(User user);

    Long save(Powerofattorney pow);

    Long deleteUserPowerofattorney(User user, Powerofattorney pow);

    Long addUserExternaldistributor(User user, String extId);

    Long deleteUserExternaldistributor(User user, Externaldistributor ext);

    List<Urole> getAllRoles();

    Long addUserRole(User user, Long id);

    User getUserWithRoles(User user);

    Long deleteUserRole(User user, Long id);
}
