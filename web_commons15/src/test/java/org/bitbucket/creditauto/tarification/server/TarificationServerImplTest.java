package org.bitbucket.creditauto.tarification.server;

import org.bitbucket.creditauto.tarification.facade.CreditTypeInputParam;
import org.junit.Test;

/**.
 */
public class TarificationServerImplTest {
    private TarificationServerImpl server = new TarificationServerImpl();
    @Test
    public void getCreditTypes() {
        CreditTypeInputParam param = new CreditTypeInputParam(null, null, null, null, null);
        server.getCreditTypes(param);
    }
}
