package com.api;

/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/classes/com/api/Person.class */
public class Person {
    public String name;
    public String age;

    public String toString() {
        return "Person{name='" + this.name + "', age='" + this.age + "'}";
    }

    public Person(String name, String age) {
        this.name = "john";
        this.age = "11";
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}