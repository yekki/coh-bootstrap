package me.yekki.coh.bootstrap.structures.dataobjects;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class Person implements PortableObject {
    private String givenName;
    private String surName;
    private int age;
    private String eMail;
    private String phone;
    private String address;

    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        givenName = pofReader.readString(1);
        surName = pofReader.readString(2);
        age = pofReader.readInt(3);
        eMail = pofReader.readString(4);
        phone = pofReader.readString(5);
        address = pofReader.readString(6);
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(1, givenName);
        pofWriter.writeString(2, surName);
        pofWriter.writeInt(3, age);
        pofWriter.writeString(4, eMail);
        pofWriter.writeString(5, phone);
        pofWriter.writeString(6, address);
    }

    public static class Builder {

        private String givenName = "";
        private String surName = "";
        private int age = 0;
        private String eMail = "";
        private String phone = "";
        private String address = "";

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder surName(String surName) {
            this.surName = surName;
            return this;
        }

        public Builder age(int val) {
            age = val;
            return this;
        }

        public Builder email(String val) {
            eMail = val;
            return this;
        }

        public Builder phoneNumber(String val) {
            phone = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    public Person() {
        super();
    }

    public Person(Builder builder) {
        givenName = builder.givenName;
        surName = builder.surName;
        age = builder.age;
        eMail = builder.eMail;
        phone = builder.phone;
        address = builder.address;

    }

    public String getGivenName() {
        return givenName;
    }

    public String getSurName() {
        return surName;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return eMail;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void print() {
        System.out.println(
                "\nName: " + givenName + " " + surName + "\n" +
                        "Age: " + age + "\n" +
                        "eMail: " + eMail + "\n" +
                        "Phone: " + phone + "\n" +
                        "Address: " + address + "\n"
        );
    }

    public String printCustom(Function<Person, String> f) {
        return f.apply(this);
    }


    public void printWesternName() {

        System.out.println("\nName: " + this.getGivenName() + " " + this.getSurName() + "\n" +
                "Age: " + this.getAge() + "\n" +
                "EMail: " + this.getEmail() + "\n" +
                "Phone: " + this.getPhone() + "\n" +
                "Address: " + this.getAddress());
    }


    public void printEasternName() {

        System.out.println("\nName: " + this.getSurName() + " " + this.getGivenName() + "\n" +
                "Age: " + this.getAge() + "\n" +
                "EMail: " + this.getEmail() + "\n" +
                "Phone: " + this.getPhone() + "\n" +
                "Address: " + this.getAddress());
    }


    @Override
    public String toString() {
        return "Name: " + givenName + " " + surName + "\n" + "Age: " + age + "\n" + "eMail: " + eMail + "\n";
    }

    public static List<Person> createShortList() {
        List<Person> people = new ArrayList<>();

        people.add(
                new Builder()
                        .givenName("Bob")
                        .surName("Baker")
                        .age(21)
                        .email("bob.baker@example.com")
                        .phoneNumber("201-121-4678")
                        .address("44 4th St, Smallville, KS 12333")
                        .build()
        );

        people.add(
                new Builder()
                        .givenName("Jane")
                        .surName("Doe")
                        .age(25)
                        .email("jane.doe@example.com")
                        .phoneNumber("202-123-4678")
                        .address("33 3rd St, Smallville, KS 12333")
                        .build()
        );

        people.add(
                new Builder()
                        .givenName("John")
                        .surName("Doe")
                        .age(25)
                        .email("john.doe@example.com")
                        .phoneNumber("202-123-4678")
                        .address("33 3rd St, Smallville, KS 12333")
                        .build()
        );

        people.add(
                new Builder()
                        .givenName("James")
                        .surName("Johnson")
                        .age(45)
                        .email("james.johnson@example.com")
                        .phoneNumber("333-456-1233")
                        .address("201 2nd St, New York, NY 12111")
                        .build()
        );

        people.add(
                new Builder()
                        .givenName("Joe")
                        .surName("Bailey")
                        .age(67)
                        .email("joebob.bailey@example.com")
                        .phoneNumber("112-111-1111")
                        .address("111 1st St, Town, CA 11111")
                        .build()
        );

        people.add(
                new Builder()
                        .givenName("Phil")
                        .surName("Smith")
                        .age(55)
                        .email("phil.smith@examp;e.com")
                        .phoneNumber("222-33-1234")
                        .address("22 2nd St, New Park, CO 222333")
                        .build()
        );

        people.add(
                new Builder()
                        .givenName("Betty")
                        .surName("Jones")
                        .age(85)
                        .email("betty.jones@example.com")
                        .phoneNumber("211-33-1234")
                        .address("22 4th St, New Park, CO 222333")
                        .build()
        );


        return people;
    }

}
