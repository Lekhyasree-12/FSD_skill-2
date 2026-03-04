package com.inventory.hql;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;

public class HQLDemo {

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {

            // Task 3a
            sortProductsByPriceAscending(session);

            // Task 3b
            sortProductsByPriceDescending(session);

            // Task 4
            sortProductsByQuantityDescending(session);

            // Task 5
            getFirstThreeProducts(session);
            getNextThreeProducts(session);

            // Task 6
            countTotalProducts(session);
            countProductsInStock(session);
            countProductsByDescription(session);
            findMinMaxPrice(session);

            // Task 7
            groupProductsByDescription(session);

            // Task 8
            filterProductsByPriceRange(session, 20.0, 100.0);

            // Task 9
            findProductsStartingWith(session, "D");
            findProductsEndingWith(session, "p");
            findProductsContaining(session, "Desk");
            findProductsByNameLength(session, 5);

        } finally {
            session.close();
            factory.close();
        }
    }

    // Task 3a: Sort by price ASC
    public static void sortProductsByPriceAscending(Session session) {

        String hql = "FROM Product p ORDER BY p.price ASC";

        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\n=== Products Sorted by Price ASC ===");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // Task 3b: Sort by price DESC
    public static void sortProductsByPriceDescending(Session session) {

        String hql = "FROM Product p ORDER BY p.price DESC";

        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\n=== Products Sorted by Price DESC ===");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // Task 4: Sort by quantity
    public static void sortProductsByQuantityDescending(Session session) {

        String hql = "FROM Product p ORDER BY p.quantity DESC";

        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\n=== Products Sorted by Quantity ===");

        for (Product p : products) {
            System.out.println(p.getName() + " - Quantity: " + p.getQuantity());
        }
    }

    // Task 5a: First 3 products
    public static void getFirstThreeProducts(Session session) {

        String hql = "FROM Product p";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setFirstResult(0);
        query.setMaxResults(3);

        List<Product> products = query.list();

        System.out.println("\n=== First 3 Products ===");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // Task 5b: Next 3 products
    public static void getNextThreeProducts(Session session) {

        String hql = "FROM Product p";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setFirstResult(3);
        query.setMaxResults(3);

        List<Product> products = query.list();

        System.out.println("\n=== Next 3 Products ===");

        for (Product p : products) {
            System.out.println(p);
        }
    }

    // Task 6a: Count products
    public static void countTotalProducts(Session session) {

        String hql = "SELECT COUNT(p) FROM Product p";

        Query<Long> query = session.createQuery(hql, Long.class);

        Long count = query.uniqueResult();

        System.out.println("\nTotal Products: " + count);
    }

    // Task 6b: Count products where quantity > 0
    public static void countProductsInStock(Session session) {

        String hql = "SELECT COUNT(p) FROM Product p WHERE p.quantity > 0";

        Query<Long> query = session.createQuery(hql, Long.class);

        Long count = query.uniqueResult();

        System.out.println("\nProducts In Stock: " + count);
    }

    // Task 6c: Count products by description
    public static void countProductsByDescription(Session session) {

        String hql = "SELECT p.description, COUNT(p) FROM Product p GROUP BY p.description";

        Query<Object[]> query = session.createQuery(hql, Object[].class);

        List<Object[]> results = query.list();

        System.out.println("\n=== Products by Description ===");

        for (Object[] r : results) {

            String desc = (String) r[0];
            Long count = (Long) r[1];

            System.out.println(desc + " : " + count);
        }
    }

    // Task 6d: Find min and max price
    public static void findMinMaxPrice(Session session) {

        String hql = "SELECT MIN(p.price), MAX(p.price) FROM Product p";

        Query<Object[]> query = session.createQuery(hql, Object[].class);

        Object[] result = query.uniqueResult();

        Double min = (Double) result[0];
        Double max = (Double) result[1];

        System.out.println("\nMin Price: " + min);
        System.out.println("Max Price: " + max);
    }

    // Task 7: Group products by description
    public static void groupProductsByDescription(Session session) {

        String hql = "SELECT p.description, p.name, p.price FROM Product p ORDER BY p.description";

        Query<Object[]> query = session.createQuery(hql, Object[].class);

        List<Object[]> results = query.list();

        System.out.println("\n=== Grouped Products ===");

        String current = "";

        for (Object[] r : results) {

            String desc = (String) r[0];
            String name = (String) r[1];
            Double price = (Double) r[2];

            if (!desc.equals(current)) {

                System.out.println("\n" + desc + ":");
                current = desc;
            }

            System.out.println("  " + name + " - " + price);
        }
    }

    // Task 8: Filter by price range
    public static void filterProductsByPriceRange(Session session, double min, double max) {

        String hql = "FROM Product p WHERE p.price BETWEEN :min AND :max";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setParameter("min", min);
        query.setParameter("max", max);

        List<Product> products = query.list();

        System.out.println("\nProducts between " + min + " and " + max);

        for (Product p : products) {
            System.out.println(p.getName() + " - " + p.getPrice());
        }
    }

    // Task 9a: Names starting with prefix
    public static void findProductsStartingWith(Session session, String prefix) {

        String hql = "FROM Product p WHERE p.name LIKE :pattern";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setParameter("pattern", prefix + "%");

        List<Product> products = query.list();

        System.out.println("\nStarting with " + prefix);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

    // Task 9b: Names ending with suffix
    public static void findProductsEndingWith(Session session, String suffix) {

        String hql = "FROM Product p WHERE p.name LIKE :pattern";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setParameter("pattern", "%" + suffix);

        List<Product> products = query.list();

        System.out.println("\nEnding with " + suffix);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

    // Task 9c: Names containing substring
    public static void findProductsContaining(Session session, String substring) {

        String hql = "FROM Product p WHERE p.name LIKE :pattern";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setParameter("pattern", "%" + substring + "%");

        List<Product> products = query.list();

        System.out.println("\nContaining " + substring);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }

    // Task 9d: Exact name length
    public static void findProductsByNameLength(Session session, int length) {

        String hql = "FROM Product p WHERE LENGTH(p.name) = :len";

        Query<Product> query = session.createQuery(hql, Product.class);

        query.setParameter("len", length);

        List<Product> products = query.list();

        System.out.println("\nProducts with name length " + length);

        for (Product p : products) {
            System.out.println(p.getName());
        }
    }
}