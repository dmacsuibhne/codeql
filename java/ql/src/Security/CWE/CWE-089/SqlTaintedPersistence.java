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
            entityManager.close();
            response.getWriter().print("rows=" + n);
        } catch (Exception e) {
            response.getWriter().print("query reached");
        }
    }
}
