package test;

import config.AppConfig;
import config.DynamicDaoConfig;
import dao.IDao;
import metier.IMetier;
import metier.MetierImpl;
import metier.MetierMultiple;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OcpSelectionTest {

    @Test
    public void testDaoSelectionFromPropertiesDefault() {
        // Test de sélection dynamique via app.properties (dao.target=dao2 -> 150.0 * 2.0 = 300.0)
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);
        IMetier metier = context.getBean(IMetier.class);

        assertNotNull(metier);
        assertEquals(300.0, metier.calcul(), 0.001);

        context.close();
    }

    @Test
    public void testOcpPrincipleWithMockDao() {
        // Preuve du principe OCP : MetierImpl n'a pas besoin d'être modifié pour changer d'implémentation
        IDao mockDao = () -> 50.0;
        MetierImpl metier = new MetierImpl(mockDao);

        assertEquals(100.0, metier.calcul(), 0.001);
    }

    @Test
    public void testMetierMultipleAggregation() {
        // Test de l'extension : injection de la collection de tous les DAOs
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);
        MetierMultiple metierMultiple = context.getBean(MetierMultiple.class);

        assertNotNull(metierMultiple);
        assertNotNull(metierMultiple.getDaos());
        assertNotNull(metierMultiple.getDaoMap());

        double total = metierMultiple.calcul();
        // Somme des DAOs scannés : 100(dao) + 150(dao2) + 180(daoFile) + 220(daoApi) + 150(dynamicDao) = 800.0
        assertEquals(800.0, total, 0.001);

        context.close();
    }
}
