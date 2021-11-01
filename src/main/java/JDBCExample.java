import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import sql.Person;

import java.util.List;

public class JDBCExample {

    private static SessionFactory factory;

    public static Person addPerson(String firstName, String lastName, int age){
        Session session = factory.openSession();
        Transaction tx = null;
        Person person = null;

        try {
            tx = session.beginTransaction();
            person = new Person(firstName, lastName, age);
            session.save(person);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return person;
    }

    public static void showAllPersons(){
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            List<Person> persons = session.createQuery("FROM Person").list();
            for (Person p : persons) {
                System.out.print("ID: " + p.getId());
                System.out.print(" First Name: " + p.getFirstName());
                System.out.print(" Last Name: " + p.getLastName());
                System.out.println("  Age: " + p.getAge());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void deletePerson(Person deletedPerson){
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.delete(deletedPerson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void updatePerson(Person updatedPerson){
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.update(updatedPerson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // You wrote the code to connect to a database with Hibernate
            factory = new Configuration().configure().buildSessionFactory();

            // You wrote the code to insert new records in the persons table
            Person person1 = addPerson("Paul", "Paulsen", 42 );
            Person person2 = addPerson("Cody", "Codeson", 55);

            // You wrote the code to retrieve all records from the persons table
            System.out.println("After adding Persons:");
            showAllPersons();

            // You wrote the code to update the lastname of one record in the persons table
            person1.setLastName("Choppy");
            updatePerson(person1);

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("After update Person Lastname:");
            showAllPersons();

            // You wrote the code to delete two record from the persons table
            deletePerson(person1);
            deletePerson(person2);
            person1 = null;
            person2 = null;

            // You wrote the code to retrieve all records from the persons table
            System.out.println();
            System.out.println("After deletion:");
            showAllPersons();

        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
