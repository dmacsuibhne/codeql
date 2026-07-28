package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-089 JPQL injection. Source: "category". Sink: entityManager.createQuery(query1). */
public class CWE_089_SqlTaintedPersistence extends HttpServlet {
    private static EntityManagerFactory emf;
    private static synchronized EntityManagerFactory emf() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("vulnpu");
        }
        return emf;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String category = request.getParameter("category");
        try {
            EntityManager entityManager = emf().createEntityManager();
            // BAD: the category might have JPQL special characters in it
            String query1 = "SELECT p FROM Product p WHERE p.category LIKE '"
                    + category + "' ORDER BY p.price";
            Query q = entityManager.createQuery(query1);
            int n = q.getResultList().size();

            // GOOD: use a named parameter and set its value
            String query2 = "SELECT p FROM Product p WHERE p.category LIKE :category ORDER BY p.price";
            Query q2 = entityManager.createQuery(query2);
            q2.setParameter("category", category);

            // GOOD: use a positional parameter and set its value
            String query3 = "SELECT p FROM Product p WHERE p.category LIKE ?1 ORDER BY p.price";
            Query q3 = entityManager.createQuery(query3);
            q3.setParameter(1, category);

            // GOOD: use a named query with a named parameter and set its value
            Query namedQuery1 = entityManager.createNamedQuery("lookupByCategory");
            namedQuery1.setParameter("category", category);

            // GOOD: use a named query with a positional parameter and set its value
            Query namedQuery2 = entityManager.createNamedQuery("lookupByCategory");
            namedQuery2.setParameter(1, category);

            entityManager.close();
            response.getWriter().print("rows=" + n);
        } catch (Exception e) {
            response.getWriter().print("query reached");
        }
    }
}
