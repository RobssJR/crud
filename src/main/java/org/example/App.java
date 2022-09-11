package org.example;

//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
import org.ui.frmPrincipal;

public class App
{
    //private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("endereco");
    //private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static void main( String[] args )
    {
        frmPrincipal principal = new frmPrincipal();
        principal.setVisible(true);
    }
}
