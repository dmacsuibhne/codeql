package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-833: inconsistent lock ordering (deadlock risk). Sink: nested synchronized blocks in opposite orders. */
public class CWE_833_LockOrderInconsistency extends HttpServlet {
    private int primaryAccountBalance = 100;
    private final Object primaryLock = new Object();
    private int savingsAccountBalance = 100;
    private final Object savingsLock = new Object();
    public boolean transferToSavings(int amount) {
        synchronized (primaryLock) {
            synchronized (savingsLock) {
                if (amount > 0 && primaryAccountBalance >= amount) {
                    primaryAccountBalance -= amount;
                    savingsAccountBalance += amount;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean transferToPrimary(int amount) {
        // AVOID: lock order differs from transferToSavings and may deadlock
        synchronized (savingsLock) {
            synchronized (primaryLock) {
                if (amount > 0 && savingsAccountBalance >= amount) {
                    savingsAccountBalance -= amount;
                    primaryAccountBalance += amount;
                    return true;
                }
            }
        }
        return false;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        transferToSavings(10);
        transferToPrimary(10);
        response.getWriter().print("transfers done");
    }
}
