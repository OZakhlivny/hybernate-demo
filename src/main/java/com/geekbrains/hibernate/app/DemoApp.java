package com.geekbrains.hibernate.app;

import com.geekbrains.hibernate.app.data.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class DemoApp {
    public static void main(String[] args) {
        PrepareData.doPrepareData();

        SessionFactory factory = new Configuration().configure("configs/hibernate.cfg.xml").buildSessionFactory();
        Session session = null;
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        try {
            while (!exit) {
                System.out.println("Input command:\n/customers,\n/products\n" +
                        "/customer_products <customer_id>,\n" +
                        "/product_customers <product_id>,\n" +
                        "/customer_products_detail <customer_id>,\n" +
                        "/delete_customer <customer_id>,\n" +
                        "/delete_product <product_id>,\n" +
                        "/exit):");
                String command = scanner.nextLine();
                String[] commandParts = command.split(" ");
                switch (commandParts[0]) {
                    case "/customers": {
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        List<Customer> customers = session.createQuery("SELECT c FROM Customer c").getResultList();
                        System.out.println(customers);
                        session.getTransaction().commit();
                        break;
                    }
                    case "/products": {
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        List<Product> products = session.createQuery("SELECT p FROM Product p").getResultList();
                        System.out.println(products);
                        session.getTransaction().commit();
                        break;
                    }

                    case "/customer_products": {
                        if (commandParts.length > 1) {
                            session = factory.getCurrentSession();
                            session.beginTransaction();
                            Customer customer = (Customer) session.createQuery(
                                    String.format("SELECT c FROM Customer c WHERE c.id=%d", Long.parseLong(commandParts[1]))).getSingleResult();
                            List<Order> orders = customer.getOrders();
                            HashSet<Product> customerProducts = new HashSet<>();
                            for (Iterator<Order> i = orders.iterator(); i.hasNext(); ) {
                                List<OrderProducts> orderProducts = i.next().getOrderProducts();
                                for (Iterator<OrderProducts> j = orderProducts.iterator(); j.hasNext(); ) {
                                    customerProducts.add(j.next().getProduct());
                                }
                            }
                            System.out.println(customerProducts);
                            session.getTransaction().commit();
                        } else System.out.println("You need to input <customer_id> parameter!");
                        break;
                    }

                    case "/product_customers": {
                        if (commandParts.length > 1) {
                            session = factory.getCurrentSession();
                            session.beginTransaction();
                            Product product = (Product) session.createQuery(
                                    String.format("SELECT p FROM Product p WHERE p.id=%d", Long.parseLong(commandParts[1]))).getSingleResult();
                            List<OrderProducts> orderProducts = product.getOrderProducts();
                            HashSet<Customer> productCustomers = new HashSet<>();
                            for (Iterator<OrderProducts> i = orderProducts.iterator(); i.hasNext(); ) {
                                productCustomers.add(i.next().getOrder().getCustomer());
                            }
                            System.out.println(productCustomers);
                            session.getTransaction().commit();
                        } else System.out.println("You need to input <product_id> parameter!");
                        break;
                    }

                    case "/customer_products_detail": {
                        if (commandParts.length > 1) {
                            session = factory.getCurrentSession();
                            session.beginTransaction();
                            Customer customer = (Customer) session.createQuery(
                                    String.format("SELECT c FROM Customer c WHERE c.id=%d", Long.parseLong(commandParts[1]))).getSingleResult();
                            List<Order> orders = customer.getOrders();
                            List<PriceHistory> productList = new ArrayList<>();
                            for (Iterator<Order> i = orders.iterator(); i.hasNext(); ) {
                                Order order = i.next();
                                List<OrderProducts> orderProducts = order.getOrderProducts();
                                for (Iterator<OrderProducts> j = orderProducts.iterator(); j.hasNext(); ) {
                                    OrderProducts products = j.next();
                                    productList.add(new PriceHistory(products.getProduct().getName(), order.getSaleTime(), products.getCost()));
                                }
                            }
                            Collections.sort(productList);
                            System.out.println("Sale history:" + productList);
                            session.getTransaction().commit();
                        } else System.out.println("You need to input <customer_id> parameter!");
                        break;
                    }

                    case "/delete_customer": {
                        if (commandParts.length > 1) {
                            session = factory.getCurrentSession();
                            session.beginTransaction();
                            Customer customer = (Customer) session.createQuery(
                                    String.format("SELECT c FROM Customer c WHERE c.id=%d", Long.parseLong(commandParts[1]))).getSingleResult();
                            session.delete(customer);
                            System.out.println("Customer was deleted.");
                            session.getTransaction().commit();
                        } else System.out.println("You need to input <customer_id> parameter!");
                        break;
                    }

                    case "/delete_product": {
                        if (commandParts.length > 1) {
                            session = factory.getCurrentSession();
                            session.beginTransaction();
                            Product product = (Product) session.createQuery(
                                    String.format("SELECT p FROM Product p WHERE p.id=%d", Long.parseLong(commandParts[1]))).getSingleResult();
                            session.delete(product);
                            System.out.println("Product was deleted.");
                            session.getTransaction().commit();
                        } else System.out.println("You need to input <product_id> parameter!");
                        break;
                    }

                    case "/exit": {
                        exit = true;
                        break;
                    }
                    default: {
                        System.out.println("Unknown command!");
                    }
                }
            }
        } finally {
            factory.close();
            if(session != null) session.close();
        }

    }
}
