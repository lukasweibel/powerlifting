package ch.lukasweibel.powerlifting;

import java.util.Date;

public class Powerlifter {

    private String name;
    private String sex;
    private String equipment;
    private Double age;
    private Double bodyweightKg;
    private Double best3SquatKg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Double getBodyweightKg() {
        return bodyweightKg;
    }

    public void setBodyweightKg(Double bodyweightKg) {
        this.bodyweightKg = bodyweightKg;
    }

    public Double getBest3SquatKg() {
        return best3SquatKg;
    }

    public void setBest3SquatKg(Double best3SquatKg) {
        this.best3SquatKg = best3SquatKg;
    }

}