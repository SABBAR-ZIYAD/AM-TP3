package config;

import dao.DaoApi;
import dao.IDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Stratégie B : Créer un pointeur/alias dynamique via @Bean et @Primary.
 * Activée sous le profil "alias-strategy" pour éviter les conflits de @Primary multiples.
 */
@Configuration
@Profile("alias-strategy")
public class DaoAliasConfig {

    @Bean
    @Primary
    public IDao daoAlias() {
        // Pointe vers l'implémentation API
        return new DaoApi();
    }
}
