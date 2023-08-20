package org.bitbucket.creditauto.wicket;

import java.util.List;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

import org.bitbucket.creditauto.entity.User;
import org.bitbucket.creditauto.wicket.SearchData;
import org.bitbucket.creditauto.LOG;

public class UserLoginValidator implements INullAcceptingValidator {
    private static final long serialVersionUID = 1L;
    private Long userId;

    /**
     * Constructor.
     * @param userId the user id
     */
    public UserLoginValidator(Long userId) {
        this.userId = userId;
    }

    public void validate(IValidatable v) {
        Object value = v.getValue();
        String checkLoginValue = (String) v.getValue();

        if (checkLoginValue == null) {
            return;
        }
        SearchData userSearch = new SearchData();
        userSearch.getAllUsers().setUserLogin(checkLoginValue);
        userSearch.getAllUsers().setPowerOfAttStart(null);
        userSearch.getAllUsers().setPowerOfAttFinish(null);
        List<User> users = new org.bitbucket.creditauto.usermanager.server.UsermanagerServerImpl().getUsers(userSearch.getAllUsers());
        int countUsers = 0;
        for (User user : users) {
            if (user.getId().equals(userId)) {
                continue;
            }
            countUsers += 1;
        }
        if (countUsers > 0) {
            v.error(new ValidationError().setMessage("Логин пользователя должен быть уникальным"));
        }
    }
}
